package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;
import cc.carm.lib.easysql.api.SQLManager;

import java.sql.SQLException;

public class QueryFunctionTest extends TestHandler {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {

        Integer id_1 = sqlManager.createQuery()
                .inTable("test_user_table")
                .orderBy("id", false)
                .setLimit(1)
                .build().executeFunction(query -> {
                    if (!query.getResultSet().next()) return -1;
                    else return query.getResultSet().getInt("id");
                });

        System.out.println("id (ps): " + id_1);

    }
}
