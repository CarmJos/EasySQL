import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.enums.ForeignKeyRule;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import cc.carm.lib.easysql.api.util.TimeDateUtils;
import cc.carm.lib.easysql.api.util.UUIDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EasySQLDemo {

	public void createTable(SQLManager sqlManager) {
		// 同步创建表
		sqlManager.createTable("users")
//				.addColumn("id", "INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY")
				.addAutoIncrementColumn("id", NumberType.INT, true, true)
				.addColumn("uuid", "VARCHAR(32) NOT NULL UNIQUE KEY")
				.addColumn("username", "VARCHAR(16) NOT NULL")
				.addColumn("age", "TINYINT NOT NULL DEFAULT 1")
				.addColumn("email", "VARCHAR(32)")
				.addColumn("phone", "VARCHAR(16)")
				.addColumn("registerTime", "DATETIME NOT NULL")
//				.addColumn("INDEX `phone`") // 原始方法添加索引
				.setIndex(IndexType.UNIQUE_KEY, "username") // 添加唯一索引
				.setIndex(IndexType.INDEX, "contact", "email", "phone") //添加联合索引 (示例)
				.build().execute(null /* 不处理错误 */);

		sqlManager.createTable("user_ipaddr")
				.addAutoIncrementColumn("id", NumberType.INT, true, true)
				.addColumn("uuid", "VARCHAR(32) NOT NULL")
				.addColumn("ip", "VARCHAR(16)")
				.addColumn("time", "DATETIME NOT NULL")
				.addForeignKey("uuid", null,
						"users", "uuid",
						ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
				)
				.build().execute(null /* 不处理错误 */);
	}

	public void alertTable(SQLManager sqlManager) {
		// 同步更新表
		sqlManager.alterTable("users")
				.modifyColumn("age", "TINYINT NOT NULL DEFAULT 0")
				.execute(null /* 不处理错误 */);
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
					return result.next() ? UUIDUtil.toUUID(result.getString("uuid")) : null;
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
					if (resultSet != null && resultSet.next()) {
						String username = resultSet.getString("username");
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
					System.out.println("#" + action.getShortID() + " -> " + action.getSQLContent());
					exception.printStackTrace();
				});

		try {
			Integer userID = sqlManager.createInsert("users")
					.setColumnNames("username", "phone", "email", "registerTime")
					.setParams("CarmJos", "18888888888", "carm@carm.cc", TimeDateUtils.getCurrentTime())
					.setKeyIndex(1) // 设定自增主键的index，将会在后续返回自增主键
					.execute();

			System.out.println("新用户的ID为 " + userID);

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

}