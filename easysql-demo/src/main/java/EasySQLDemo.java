import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.util.TimeDateUtils;
import cc.carm.lib.easysql.api.util.UUIDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EasySQLDemo {

	public void createTable(SQLManager sqlManager) {
		// 同步创建表
		sqlManager.createTable("users")
				.addColumn("id", "INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY")
				.addColumn("uuid", "VARCHAR(32) NOT NULL UNIQUE KEY")
				.addColumn("username", "VARCHAR(16) NOT NULL UNIQUE KEY")
				.addColumn("age", "INT(3) NOT NULL DEFAULT 1")
				.addColumn("email", "VARCHAR(32)")
				.addColumn("phone", "VARCHAR(16)")
				.addColumn("registerTime", "DATETIME NOT NULL")
				.build().execute(null /* 不处理错误 */);
	}

	public void sqlQuery(SQLManager sqlManager) {
		// 同步SQL查询
		try (SQLQuery query = sqlManager.createQuery()
				.inTable("users") // 在users表中查询
				.selectColumns("id", "name") // 选中 id 与 name列
				.addCondition("age", ">", 18) // 限定 age 要大于5
				.addCondition("email", null) // 限定查询 email 字段为空
				.addNotNullCondition("phone") // 限定 phone 字段不为空
				.addTimeCondition("registerTime", // 时间字段
						System.currentTimeMillis() - 100000, //限制开始时间
						-1//不限制结束时间
				).build().execute()) {
			ResultSet resultSet = query.getResultSet();
			//do something

		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		UUID userUUID = sqlManager.createQuery()
				.inTable("users") // 在users表中查询
				.selectColumns("uuid")
				.addCondition("id", 5) // 限定 id 为 5
				.setLimit(1) // 只取出一个数据
				.build().execute(query -> {
					//可以直接进行数据处理
					ResultSet result = query.getResultSet();
					try {
						if (result != null && result.next()) {
							return UUIDUtil.toUUID(result.getString("uuid"));
						}
					} catch (SQLException ignored) {
					}
					return null;
				}, (exception, action) -> {
					// 处理异常，不想处理直接填null
				});
	}

	public void sqlQueryAsync(SQLManager sqlManager) {
		// 异步SQL查询
		sqlManager.createQuery()
				.inTable("users") // 在users表中查询
				.addCondition("id", 5) // 限定 id 为 5
				.setLimit(1) // 只取出一个数据
				.build().executeAsync(success -> {
					ResultSet resultSet = success.getResultSet();
					try {
						if (resultSet != null && resultSet.next()) {
							String username = resultSet.getString("username");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}, (exception, action) -> {
					//do something
					long createTIme = action.getCreateTime();
					String shortID = action.getShortID();
					String sqlContent = action.getSQLContent();
				});
	}

	public void sqlInsert(SQLManager sqlManager) {
		// 同步SQL插入 （不使用try-catch的情况下，返回的数值可能为空。）
		Integer id = sqlManager.createInsert("users")
				.setColumnNames("username", "phone", "email", "registerTime")
				.setParams("CarmJos", "18888888888", "carm@carm.cc", TimeDateUtils.getCurrentTime())
				.setKeyIndex(1) // 设定自增主键的index，将会在后续返回自增主键
				.execute((exception, action) -> {
					// 处理异常
					System.out.println(action);

				});
	}

}