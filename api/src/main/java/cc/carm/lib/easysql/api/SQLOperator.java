package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import cc.carm.lib.easysql.api.builder.*;
import org.jetbrains.annotations.NotNull;

public interface SQLOperator {

    /**
     * 新建一个查询。
     *
     * @return {@link QueryBuilder}
     */
    @NotNull QueryBuilder createQuery();

    /**
     * 创建一条插入操作。
     *
     * @param tableName 目标表名
     * @return {@link InsertBuilder}
     */
    @NotNull InsertBuilder<PreparedSQLUpdateAction<Integer>> insertInto(@NotNull String tableName);

    /**
     * 创建支持多组数据的插入操作。
     *
     * @param tableName 目标表名
     * @return {@link InsertBuilder}
     */
    @NotNull InsertBuilder<PreparedSQLUpdateBatchAction<Integer>> insertBatchInto(@NotNull String tableName);

    /**
     * 创建一条替换操作。
     *
     * @param tableName 目标表名
     * @return {@link ReplaceBuilder}
     */
    @NotNull ReplaceBuilder<PreparedSQLUpdateAction<Integer>> replaceInto(@NotNull String tableName);

    /**
     * 创建支持多组数据的替换操作。
     *
     * @param tableName 目标表名
     * @return {@link ReplaceBuilder}
     */
    @NotNull ReplaceBuilder<PreparedSQLUpdateBatchAction<Integer>> replaceBatchInto(@NotNull String tableName);

    /**
     * 创建更新操作。
     *
     * @param tableName 目标表名
     * @return {@link UpdateBuilder}
     */
    @NotNull UpdateBuilder updateInto(@NotNull String tableName);

    /**
     * 创建删除操作。
     *
     * @param tableName 目标表名
     * @return {@link DeleteBuilder}
     */
    @NotNull DeleteBuilder deleteFrom(@NotNull String tableName);

}
