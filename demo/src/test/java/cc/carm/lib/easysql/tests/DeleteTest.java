package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;

import java.sql.SQLException;

public class DeleteTest extends TestHandler {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {


        System.out.println("change(s): " + sqlManager.createDelete("test_user_table")
                .addCondition("id", ">", 5)
                .addNotNullCondition("username")
                .build().execute());

    }
}
