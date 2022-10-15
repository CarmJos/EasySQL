package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;
import cc.carm.lib.easysql.api.SQLManager;

import java.sql.SQLException;

public class QueryAsyncTest extends TestHandler {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {

        sqlManager.createQuery()
                .inTable("test_user_table")
                .orderBy("id", false)
                .setLimit(1)
                .build().executeAsync(query -> {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (!query.getResultSet().next()) {
                        System.out.println("id (ps): NULL");
                    } else {
                        System.out.println("id (ps): " + query.getResultSet().getInt("id"));
                    }
                });

    }
}
