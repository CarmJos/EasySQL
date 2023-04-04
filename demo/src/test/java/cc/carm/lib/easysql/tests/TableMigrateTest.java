package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.MigrateResult;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 2022/11/28<br>
 * EasySQL<br>
 *
 * @author huanmeng_qwq
 */
public class TableMigrateTest extends TestHandler {
    @Override
    public void onTest(SQLManager sqlManager) throws Exception {
        print("	创建 test_migrate_table");
        sqlManager.createTable("test_migrate_table")
                .addAutoIncrementColumn("id")
                .addColumn("uuid", "VARCHAR(36) NOT NULL", "用户UUID")
                .addColumn("username", "VARCHAR(16) NOT NULL", "用户名")
                .addColumn("age", "TINYINT DEFAULT 0 NOT NULL", "年龄")

                .setIndex("uuid", IndexType.UNIQUE_KEY)
                .build().execute();

        print("	向 test_migrate_table 写入数据");
        sqlManager.createInsert("test_migrate_table")
                .setColumnNames("uuid", "username", "age")
                .setParams("1234567890", "qwq", 18)
                .execute();

        print("	迁移 test_migrate_table 至 test_transfer_table");
        MigrateResult migrate = sqlManager.createMigrate()
                .tableName("test_migrate_table", "test_transfer_table", null)
                .autoIncrementColumn("id", "dbId")
                .column("uuid", "uniqueId", "VARCHAR(40) NOT NULL")
                .column("username", "name", "VARCHAR(20) NOT NULL")
                .column("age", "age", "TINYINT DEFAULT 0 NOT NULL")
                .migrate();

        if (migrate.error() != null) {
            migrate.error().printStackTrace();
        }

        Set<String> set = sqlManager.fetchTableMetadata("test_transfer_table").listColumns().join()
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        print("    迁移后的表结构: " + set);
        boolean containsAll = set.containsAll(Stream.of
                ("dbId", "uniqueId", "name", "age").map(String::toLowerCase).collect(Collectors.toSet()));
        print("    迁移结果: " + (migrate.success() ? "true" : migrate.error()) + " , 是否包含所有字段: " + containsAll);
        try (SQLQuery query = sqlManager.createQuery().inTable("test_transfer_table").build().execute()) {
            while (query.getResultSet().next()) {
                print("    迁移后的数据: " +
                        query.getResultSet().getInt("dbId") + " , " +
                        query.getResultSet().getString("uniqueId") + " , " +
                        query.getResultSet().getString("name") + " , " +
                        query.getResultSet().getInt("age")
                );
            }
        }
    }
}
