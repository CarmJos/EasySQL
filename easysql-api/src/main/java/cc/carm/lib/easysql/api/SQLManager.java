package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import cc.carm.lib.easysql.api.builder.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public interface SQLManager {

	Logger getLogger();

	boolean isDebugMode();

	void setDebugMode(boolean enable);

	/**
	 * 得到连接池源
	 *
	 * @return DataSource
	 */
	@NotNull DataSource getDataSource();

	/**
	 * 得到一个数据库连接实例
	 *
	 * @return Connection
	 */
	@NotNull Connection getConnection() throws SQLException;

	/**
	 * 得到正使用的查询。
	 *
	 * @return 查询列表
	 */
	@NotNull Map<UUID, SQLQuery> getActiveQuery();

	/**
	 * 执行一条不需要返回结果的SQL语句(多用于UPDATE、REPLACE、DELETE方法)
	 *
	 * @param sql SQL语句内容
	 * @return 更新的行数
	 */
	@Nullable Integer executeSQL(String sql);

	/**
	 * 执行一条不需要返回结果的SQL更改(UPDATE、REPLACE、DELETE)
	 *
	 * @param sql SQL语句内容
	 * @return 更新的行数
	 */
	@Nullable Integer executeSQL(String sql, Object[] params);

	/**
	 * 执行多条不需要返回结果的SQL更改(UPDATE、REPLACE、DELETE)
	 *
	 * @param sql SQL语句内容
	 * @return 对应参数返回的行数
	 */
	@Nullable List<Integer> executeSQLBatch(String sql, Iterable<Object[]> paramsBatch);


	/**
	 * 执行多条不需要返回结果的SQL。
	 *
	 * @param sql SQL语句内容
	 * @return 对应参数返回的行数
	 */
	@Nullable List<Integer> executeSQLBatch(@NotNull String sql, String... moreSQL);

	@Nullable List<Integer> executeSQLBatch(@NotNull Iterable<String> sqlBatch);

	TableCreateBuilder createTable(@NotNull String tableName);

	QueryBuilder createQuery();

	InsertBuilder<PreparedSQLUpdateBatchAction> createInsertBatch(@NotNull String tableName);

	InsertBuilder<PreparedSQLUpdateAction> createInsert(@NotNull String tableName);

	ReplaceBuilder<PreparedSQLUpdateBatchAction> createReplaceBatch(@NotNull String tableName);

	ReplaceBuilder<PreparedSQLUpdateAction> createReplace(@NotNull String tableName);

	UpdateBuilder createUpdate(@NotNull String tableName);

	DeleteBuilder createDelete(@NotNull String tableName);

}
