package cc.carm.lib.easysql.testrunner.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.enums.NumberType;
import cc.carm.lib.easysql.testrunner.EasySQLTest;

import java.sql.SQLException;

public class TableAlterTest extends EasySQLTest {

	@Override
	public void onTest(SQLManager sqlManager) throws SQLException {

		print("	修改 test_user_table");
		sqlManager.alterTable("test_user_table")
				.addColumn("test", "VARCHAR(16) NOT NULL")
				.execute();

		sqlManager.alterTable("test_user_table")
				.addColumn("test2", "VARCHAR(16) NOT NULL", "username")
				.execute();


		print("	修改 test_user_info");
		sqlManager.alterTable("test_user_info")
				.addAutoIncrementColumn("num", NumberType.BIGINT, false, true)
				.execute();

		sqlManager.alterTable("test_user_info")
				.addColumn("a", "VARCHAR(16) NOT NULL", "")
				.execute();

	}

}
