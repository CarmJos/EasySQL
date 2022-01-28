package cc.carm.lib.easysql.testrunner.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.testrunner.EasySQLTest;

import java.sql.SQLException;

public class QueryFunctionTest extends EasySQLTest {


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

		Integer id_2 = sqlManager.createQuery().withSQL("SELECT id FROM test_user_table ORDER BY id DESC LIMIT 1")
				.executeFunction(query -> {
					if (!query.getResultSet().next()) return -1;
					else return query.getResultSet().getInt("id");
				});

		System.out.println("id (s): " + id_2);

	}
}
