package cc.carm.lib.easysql.tester.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.enums.ForeignKeyRule;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.tester.EasySQLTest;

import java.sql.SQLException;

public class TableCreateTest extends EasySQLTest {

    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {

        print("	创建 test_user_table");
        sqlManager.createTable("test_user_table")
                .addAutoIncrementColumn("id")
                .addColumn("uuid", "VARCHAR(36) NOT NULL", "用户UUID")
                .addColumn("username", "VARCHAR(16) NOT NULL", "用户名")
                .addColumn("age", "TINYINT DEFAULT 0 NOT NULL", "年龄")

                .setIndex("uuid", IndexType.UNIQUE_KEY)
                .build().execute();

        print("	创建 test_user_info");
        sqlManager.createTable("test_user_info")
                .addColumn("uid", "INT UNSIGNED NOT NULL")
                .addColumn("info", "TEXT", "相关信息")
                .addForeignKey("uid", "user",
                        "test_user_table", "id",
                        ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
                )
                .setIndex(IndexType.FULLTEXT_INDEX, "sign", "info")
                .build().execute();

    }

}
