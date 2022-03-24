package cc.carm.lib.easysql.tester.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.tester.EasySQLTest;

import java.sql.SQLException;

public class TableRenameTest extends EasySQLTest {
    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {
        print("	重命名 test_user_table");
        sqlManager.alterTable("test_user_table")
                .renameTo("test_user_table2")
                .execute();
    }
}
