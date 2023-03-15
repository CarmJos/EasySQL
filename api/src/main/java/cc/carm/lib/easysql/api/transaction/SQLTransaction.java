package cc.carm.lib.easysql.api.transaction;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.PreparedSQLQueryAction;
import cc.carm.lib.easysql.api.action.query.SQLQueryAction;
import cc.carm.lib.easysql.api.action.update.PreparedSQLBatchUpdateAction;
import cc.carm.lib.easysql.api.action.update.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.update.SQLBatchUpdateAction;
import cc.carm.lib.easysql.api.action.update.SQLUpdateAction;
import cc.carm.lib.easysql.api.builder.*;
import cc.carm.lib.easysql.api.enums.IsolationLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SQLTransaction extends AutoCloseable {

    @NotNull SQLManager getManager();

    /**
     * 得到本次事务的隔离级别
     *
     * @return {@link IsolationLevel} 隔离级别
     */
    @NotNull IsolationLevel getIsolationLevel();

    /**
     * 提交已有操作
     */
    void commit();

    /**
     * 设定一个记录点
     *
     * @param name 记录点名称
     * @return {@link SQLSavepoint} 事务记录点
     */
    @NotNull SQLSavepoint savepoint(@NotNull String name);

    /**
     * 回退全部操作
     */
    default void rollback() {
        rollback(null);
    }

    /**
     * 回退操作到某个记录点或回退整个事务操作。
     *
     * @param savepoint 记录点，若记录点为NULL则回退整个事务的操作。
     */
    void rollback(@Nullable SQLSavepoint savepoint);

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
     * @see PreparedSQLBatchUpdateAction
     */
    @Nullable List<Integer> executeSQLBatch(String sql, Iterable<Object[]> paramsBatch);

    /**
     * 执行多条不需要返回结果的SQL。
     * 该方法使用 Statement 实现，请注意SQL注入风险！
     *
     * @param sql     SQL语句内容
     * @param moreSQL 更多SQL语句内容
     * @return 对应参数返回的行数
     * @see SQLBatchUpdateAction
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
     * 新建一个查询。
     *
     * @return {@link QueryBuilder}
     */
    @NotNull QueryBuilder<SQLQueryAction.Base, PreparedSQLQueryAction.Base> createQuery();

    /**
     * 创建一条插入操作。
     *
     * @param tableName 目标表名
     * @return {@link InsertBuilder}
     */
    @NotNull InsertBuilder<PreparedSQLUpdateAction.Base<Integer>> insertInto(@NotNull String tableName);

    /**
     * 创建支持多组数据的插入操作。
     *
     * @param tableName 目标表名
     * @return {@link InsertBuilder}
     */
    @NotNull InsertBuilder<PreparedSQLBatchUpdateAction.Base<Integer>> insertBatchInto(@NotNull String tableName);

    /**
     * 创建一条替换操作。
     *
     * @param tableName 目标表名
     * @return {@link ReplaceBuilder}
     */
    @NotNull ReplaceBuilder<PreparedSQLUpdateAction.Base<Integer>> replaceInto(@NotNull String tableName);

    /**
     * 创建支持多组数据的替换操作。
     *
     * @param tableName 目标表名
     * @return {@link ReplaceBuilder}
     */
    @NotNull ReplaceBuilder<PreparedSQLBatchUpdateAction.Base<Integer>> replaceBatchInto(@NotNull String tableName);

    /**
     * 创建更新操作。
     *
     * @param tableName 目标表名
     * @return {@link UpdateBuilder}
     */
    @NotNull UpdateBuilder<PreparedSQLUpdateAction.Base<Integer>> updateInto(@NotNull String tableName);

    /**
     * 创建删除操作。
     *
     * @param tableName 目标表名
     * @return {@link DeleteBuilder}
     */
    @NotNull DeleteBuilder<PreparedSQLUpdateAction.Base<Integer>> deleteFrom(@NotNull String tableName);

}
