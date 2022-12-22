package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;

import java.sql.SQLException;

public class QueryFunctionTest extends TestHandler {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {

        Integer id_1 = sqlManager.createQuery()
                .fromTable("test_user_table")
                .orderBy("id", false)
                .limit(1)
                .build().executeFunction(query -> {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (!query.getResultSet().next()) return -1;
                    else return query.getResultSet().getInt("id");
                });

        System.out.println("id (ps): " + id_1);

    }
}
