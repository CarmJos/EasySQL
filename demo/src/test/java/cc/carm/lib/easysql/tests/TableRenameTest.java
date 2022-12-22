package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;

import java.sql.SQLException;

public class TableRenameTest extends TestHandler {
    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {
        print("	重命名 test_user_table");
        sqlManager.alterTable("test_user_table")
                .renameTo("test_user_table2")
                .execute();
    }
}
