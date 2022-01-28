package cc.carm.lib.easysql.testrunner.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.testrunner.EasySQLTest;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryNotCloseTest extends EasySQLTest {

	@Override
	public void onTest(SQLManager sqlManager) throws SQLException {
		SQLQuery query = sqlManager.createQuery()
				.inTable("test_user_table")
				.orderBy("id", false)
				.setLimit(5)
				.build().execute();

		ResultSet resultSet = query.getResultSet();

		while (resultSet.next()) {

			System.out.println("id: " + resultSet.getInt("id") + " username: " + resultSet.getString("username"));

		}


	}
}
