package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import cc.carm.lib.easysql.api.builder.*;
import cc.carm.lib.easysql.api.function.SQLHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

/**
 * SQLTable 基于 {@link TableCreateBuilder} 构建表，用于快速创建与该表相关的操作。
 * <ul>
 *  <li>1. 调用 {@link SQLTable#of(String, String[])} 方法创建一个 SQLTable 对象;</li>
 *  <li>2. 在应用初始化阶段调用 {@link SQLTable#create(SQLManager)} 方法初始化 SQLTable 对象;</li>
 *  <li>3. 获取已创建的{@link SQLTable} 实例，直接调用对应方法进行关于表的相关操作。</li>
 * </ul>
 *
 * @author CarmJos
 * @since 0.3.10
 */
public abstract class SQLTable {

    public static @NotNull SQLTable of(@NotNull String tableName, @Nullable SQLHandler<TableCreateBuilder> table) {
        return new SQLTable(tableName) {
            @Override
            public boolean create(SQLManager sqlManager) throws SQLException {
                if (this.manager == null) this.manager = sqlManager;
                TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
                if (table != null) table.accept(tableBuilder);
                return tableBuilder.build().executeFunction(l -> l > 0, false);
            }
        };
    }

    public static @NotNull SQLTable of(@NotNull String tableName, @NotNull String[] columns) {
        return of(tableName, columns, null);
    }

    public static @NotNull SQLTable of(@NotNull String tableName,
                                       @NotNull String[] columns, @Nullable String tableSettings) {
        return of(tableName, builder -> {
            builder.setColumns(columns);
            if (tableSettings != null) builder.setTableSettings(tableSettings);
        });
    }

    private final @NotNull String tableName;

    protected SQLManager manager;

    /**
     * 请调用 {@link SQLTable} 下的静态方法进行对象的初始化。
     *
     * @param tableName 该表的名称
     */
    private SQLTable(@NotNull String tableName) {
        this.tableName = tableName;
    }

    public @NotNull String getTableName() {
        return tableName;
    }

    /**
     * 使用指定 SQLManager 进行本示例的初始化。
     *
     * @param sqlManager {@link SQLManager}
     * @return 本表是否为首次创建
     * @throws SQLException 出现任何错误时抛出
     */
    public abstract boolean create(SQLManager sqlManager) throws SQLException;

    public @NotNull TableQueryBuilder createQuery(@NotNull SQLManager sqlManager) {
        return sqlManager.createQuery().inTable(getTableName());
    }

    public @NotNull TableQueryBuilder createQuery() {
        return createQuery(this.manager);
    }

    public @NotNull DeleteBuilder createDelete() {
        return createDelete(this.manager);
    }

    public @NotNull DeleteBuilder createDelete(@NotNull SQLManager sqlManager) {
        return sqlManager.createDelete(getTableName());
    }

    public @NotNull UpdateBuilder createUpdate() {
        return createUpdate(this.manager);
    }

    public @NotNull UpdateBuilder createUpdate(@NotNull SQLManager sqlManager) {
        return sqlManager.createUpdate(getTableName());
    }


    public @NotNull InsertBuilder<PreparedSQLUpdateAction> createInsert() {
        return createInsert(this.manager);
    }

    public @NotNull InsertBuilder<PreparedSQLUpdateAction> createInsert(@NotNull SQLManager sqlManager) {
        return sqlManager.createInsert(getTableName());
    }


    public @NotNull InsertBuilder<PreparedSQLUpdateBatchAction> createInsertBatch() {
        return createInsertBatch(this.manager);
    }

    public @NotNull InsertBuilder<PreparedSQLUpdateBatchAction> createInsertBatch(@NotNull SQLManager sqlManager) {
        return sqlManager.createInsertBatch(getTableName());
    }


    public @NotNull ReplaceBuilder<PreparedSQLUpdateAction> createReplace() {
        return createReplace(this.manager);
    }

    public @NotNull ReplaceBuilder<PreparedSQLUpdateAction> createReplace(@NotNull SQLManager sqlManager) {
        return sqlManager.createReplace(getTableName());
    }


    public @NotNull ReplaceBuilder<PreparedSQLUpdateBatchAction> createReplaceBatch() {
        return createReplaceBatch(this.manager);
    }

    public @NotNull ReplaceBuilder<PreparedSQLUpdateBatchAction> createReplaceBatch(@NotNull SQLManager sqlManager) {
        return sqlManager.createReplaceBatch(getTableName());
    }

}
