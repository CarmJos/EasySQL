package cc.carm.lib.easysql.migrate;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.MigrateResult;
import cc.carm.lib.easysql.api.migrate.SQLMigrate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2022/11/28<br>
 * EasySQL<br>
 *
 * @author huanmeng_qwq
 */
public class SQLMigrateImpl implements SQLMigrate {
    private final SQLManager sqlManager;

    private final Map<String, MigrateData> columnMap;

    private String oldTableName;
    private MigrateData newTableData;

    public SQLMigrateImpl(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
        this.columnMap = new LinkedHashMap<>();
    }

    @Override
    public SQLMigrate tableName(String oldTableName, String newTableName, String newTableSettings) {
        if (oldTableName == null) {
            throw new IllegalArgumentException("oldTableName can not be null");
        }
        this.oldTableName = oldTableName;
        this.newTableData = new MigrateData(newTableName, newTableSettings, null);
        return this;
    }

    @Override
    public SQLMigrate column(String oldColumnName, String newColumnName, String newColumnSettings, IndexType indexType) {
        if (oldColumnName == null) {
            throw new IllegalArgumentException("oldColumnName can not be null");
        }
        columnMap.put(oldColumnName, new MigrateData(newColumnName, newColumnSettings, indexType));
        return this;
    }

    @Override
    public SQLMigrate autoIncrementColumn(String oldColumnName, String newColumnName) {
        if (oldColumnName == null) {
            throw new IllegalArgumentException("oldColumnName can not be null");
        }
        columnMap.put(oldColumnName, new AutoIncrementMigrateData(newColumnName));
        return this;
    }

    @Override
    public MigrateResult migrate() throws SQLException {
        if (oldTableName == null) {
            return MigrateResult.from(MigrateResult.OLD_TABLE_NOT_EXIST, new IllegalArgumentException("oldTableName can not be null"));
        }
        if (newTableData == null) {
            return MigrateResult.from(MigrateResult.NEW_TABLE_EXIST, new IllegalArgumentException("new table name can not be null"));
        }
        if (columnMap.isEmpty()) {
            return MigrateResult.from(MigrateResult.NEW_COLUMN_EMPTY, new IllegalArgumentException("new column can not be empty"));
        }
        try {
            // check table
            Set<String> columns = sqlManager.fetchTableMetadata(oldTableName).listColumns().join();
            for (String column : columns) {
                if (columnMap.keySet().stream().noneMatch(k -> k.toLowerCase(Locale.ROOT).equals(column.toLowerCase(Locale.ROOT)))) {
                    return MigrateResult.from(MigrateResult.COLUMN_NOT_SET, new IllegalArgumentException("column " + column + " not set"));
                }
            }
            // create new table
            TableCreateBuilder table = sqlManager.createTable(newTableData.name());
            if (newTableData.settings() != null) {
                table.setTableSettings(newTableData.settings());
            }
            // add columns
            for (Map.Entry<String, MigrateData> entry : columnMap.entrySet()) {
                MigrateData migrateData = entry.getValue();
                if (migrateData.name() == null) {
                    // ignore
                    continue;
                }
                if (migrateData instanceof AutoIncrementMigrateData) {
                    table.addAutoIncrementColumn(migrateData.name());
                    continue;
                }
                table.addColumn(migrateData.name(), migrateData.settings());
                if (migrateData.indexType() != null) {
                    table.setIndex(migrateData.name(), migrateData.indexType());
                }
            }
            table.build().execute();

            // insert data
            try (SQLQuery query = sqlManager.createQuery().inTable(oldTableName).build().execute()) {
                insert(query.getResultSet());
            }
        } catch (SQLException e) {
            return new MigrateResult(false, e);
        }
        return MigrateResult.SUCCESS;
    }

    private void insert(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Map<String, String> columnNames = tableColumnNames();
            PreparedSQLUpdateAction<Integer> updateAction = sqlManager.createInsert(newTableData.name()).setColumnNames(columnNames.values().toArray(new String[0]));
            Object[] values = new Object[columnNames.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : columnNames.entrySet()) {
                values[i] = resultSet.getObject(entry.getKey());
                ++i;
            }
            updateAction.setParams(values);
            updateAction.execute();
        }
    }

    private Map<String, String> tableColumnNames() {
        Map<String, String> map = new ConcurrentHashMap<>();
        for (Map.Entry<String, MigrateData> entry : columnMap.entrySet()) {
            MigrateData migrateData = entry.getValue();
            if (migrateData.name() == null) {
                // ignore
                continue;
            }
            if (migrateData instanceof AutoIncrementMigrateData) {
                continue;
            }
            map.put(entry.getKey(), migrateData.name());
        }
        return map;
    }
}
