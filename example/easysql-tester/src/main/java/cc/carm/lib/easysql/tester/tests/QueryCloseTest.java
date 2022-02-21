package cc.carm.lib.easysql.tester.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.tester.EasySQLTest;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryCloseTest extends EasySQLTest {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {

        try (SQLQuery query = sqlManager.createQuery()
                .inTable("test_user_table")
                .orderBy("id", false)
                .setLimit(5)
                .build().execute()) {
            ResultSet resultSet = query.getResultSet();

            while (resultSet.next()) {

                System.out.printf(
                        "id: %d username: %s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("username")
                );

            }
        }

    }
}
