package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.api.action.SQLUpdateBatchAction;
import cc.carm.lib.easysql.api.action.query.SQLQuery;
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
	 * @throws SQLException 见 {@link DataSource#getConnection()}
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
	 * 该方法使用 Statement 实现，请注意SQL注入风险！
	 *
	 * @param sql SQL语句内容
	 * @return 更新的行数
	 * @see SQLUpdateAction
	 */
	@Nullable Integer executeSQL(String sql);

	/**
	 * 执行一条不需要返回结果的预处理SQL更改(UPDATE、REPLACE、DELETE)
	 *
	 * @param sql    SQL语句内容
	 * @param params SQL语句中 ? 的对应参数
	 * @return 更新的行数
	 * @see PreparedSQLUpdateAction
	 */
	@Nullable Integer executeSQL(String sql, Object[] params);

	/**
	 * 执行多条不需要返回结果的SQL更改(UPDATE、REPLACE、DELETE)
	 *
	 * @param sql         SQL语句内容
	 * @param paramsBatch SQL语句中对应?的参数组
	 * @return 对应参数返回的行数
	 * @see PreparedSQLUpdateBatchAction
	 */
	@Nullable List<Integer> executeSQLBatch(String sql, Iterable<Object[]> paramsBatch);


	/**
	 * 执行多条不需要返回结果的SQL。
	 * 该方法使用 Statement 实现，请注意SQL注入风险！
	 *
	 * @param sql     SQL语句内容
	 * @param moreSQL 更多SQL语句内容
	 * @return 对应参数返回的行数
	 * @see SQLUpdateBatchAction
	 */
	@Nullable List<Integer> executeSQLBatch(@NotNull String sql, String... moreSQL);

	/**
	 * 执行多条不需要返回结果的SQL。
	 *
	 * @param sqlBatch SQL语句内容
	 * @return 对应参数返回的行数
	 */
	@Nullable List<Integer> executeSQLBatch(@NotNull Iterable<String> sqlBatch);

	/**
	 * 在库中创建一个表
	 *
	 * @param tableName 表名
	 * @return {@link TableCreateBuilder}
	 */
	TableCreateBuilder createTable(@NotNull String tableName);

	/**
	 * 新建一个查询
	 *
	 * @return {@link QueryBuilder}
	 */
	QueryBuilder createQuery();

	/**
	 * 创建一条插入操作
	 *
	 * @param tableName 目标表名
	 * @return {@link InsertBuilder}
	 */
	InsertBuilder<PreparedSQLUpdateAction> createInsert(@NotNull String tableName);

	/**
	 * 创建支持多组数据的插入操作
	 *
	 * @param tableName 目标表名
	 * @return {@link InsertBuilder}
	 */
	InsertBuilder<PreparedSQLUpdateBatchAction> createInsertBatch(@NotNull String tableName);

	/**
	 * 创建一条替换操作
	 *
	 * @param tableName 目标表名
	 * @return {@link ReplaceBuilder}
	 */
	ReplaceBuilder<PreparedSQLUpdateAction> createReplace(@NotNull String tableName);

	/**
	 * 创建支持多组数据的替换操作
	 *
	 * @param tableName 目标表名
	 * @return {@link ReplaceBuilder}
	 */
	ReplaceBuilder<PreparedSQLUpdateBatchAction> createReplaceBatch(@NotNull String tableName);

	/**
	 * 创建更新操作
	 *
	 * @param tableName 目标表名
	 * @return {@link UpdateBuilder}
	 */
	UpdateBuilder createUpdate(@NotNull String tableName);

	/**
	 * 创建删除操作
	 *
	 * @param tableName 目标表名
	 * @return {@link DeleteBuilder}
	 */
	DeleteBuilder createDelete(@NotNull String tableName);

}
