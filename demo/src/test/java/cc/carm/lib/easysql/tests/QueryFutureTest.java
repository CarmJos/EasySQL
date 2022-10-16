package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;
import cc.carm.lib.easysql.api.SQLManager;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class QueryFutureTest extends TestHandler {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException, ExecutionException, InterruptedException {


        Future<Integer> future = sqlManager.createQuery()
                .fromTable("test_user_table")
                .orderBy("id", false)
                .limit(1)
                .build().executeFuture((query) -> {
                    if (!query.getResultSet().next()) {
                        return -1;
                    } else {
                        return query.getResultSet().getInt("id");
                    }
                });

        int id = future.get();
        System.out.println("id(future): " + id);

    }
}
