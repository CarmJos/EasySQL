package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.TestHandler;

import java.sql.SQLException;

public class DeleteTest extends TestHandler {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {

        Integer changes = sqlManager.createDelete("test_user_table")
                .addCondition("id", ">", 5)
                .addNotNullCondition("username")
                .build().execute();

        System.out.println("change(s): " + changes);

    }
}
