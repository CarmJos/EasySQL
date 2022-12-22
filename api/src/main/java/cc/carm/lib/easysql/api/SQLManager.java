package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.action.asyncable.AsyncableBatchUpdateAction;
import cc.carm.lib.easysql.api.action.asyncable.AsyncableUpdateAction;
import cc.carm.lib.easysql.api.builder.*;
import cc.carm.lib.easysql.api.enums.IsolationLevel;
import cc.carm.lib.easysql.api.function.SQLBiFunction;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.transaction.SQLTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public interface SQLManager extends SQLSource {

    default @NotNull SQLTransaction createTransaction() {
        return createTransaction(null);
    }

    @NotNull SQLTransaction createTransaction(@Nullable IsolationLevel level);

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
    @NotNull InsertBuilder<AsyncableUpdateAction<Integer>> insertInto(@NotNull String tableName);

    /**
     * 创建支持多组数据的插入操作。
     *
     * @param tableName 目标表名
     * @return {@link InsertBuilder}
     */
    @NotNull InsertBuilder<AsyncableBatchUpdateAction<Integer>> insertBatchInto(@NotNull String tableName);

    /**
     * 创建一条替换操作。
     *
     * @param tableName 目标表名
     * @return {@link ReplaceBuilder}
     */
    @NotNull ReplaceBuilder<AsyncableUpdateAction<Integer>> replaceInto(@NotNull String tableName);

    /**
     * 创建支持多组数据的替换操作。
     *
     * @param tableName 目标表名
     * @return {@link ReplaceBuilder}
     */
    @NotNull ReplaceBuilder<AsyncableBatchUpdateAction<Integer>> replaceBatchInto(@NotNull String tableName);

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

    /**
     * 在库中创建一个表。
     *
     * @param tableName 表名
     * @return {@link TableCreateBuilder}
     */
    @NotNull TableCreateBuilder createTable(@NotNull String tableName);

    /**
     * 对库中的某个表执行更改。
     *
     * @param tableName 表名
     * @return {@link TableAlterBuilder}
     */
    @NotNull TableAlterBuilder alterTable(@NotNull String tableName);

    /**
     * 快速获取表的部分元数据。
     * <br> 当需要获取其他元数据时，请使用 {@link #fetchMetadata(SQLFunction, SQLFunction)} 方法。
     *
     * @param tablePattern 表名通配符
     * @return {@link TableMetadataBuilder}
     */
    @NotNull TableMetadataBuilder fetchTableMetadata(@NotNull String tablePattern);

    /**
     * 获取并操作 {@link  DatabaseMetaData} 以得到需要的数据库消息。
     *
     * @param reader 操作与读取的方法
     * @param <R>    最终结果的返回类型
     * @return 最终结果，通过 {@link CompletableFuture#get()} 可阻塞并等待结果返回。
     */
    default <R> CompletableFuture<R> fetchMetadata(@NotNull SQLFunction<DatabaseMetaData, R> reader) {
        return fetchMetadata((meta, conn) -> reader.apply(meta));
    }

    /**
     * 获取并操作 {@link DatabaseMetaData} 提供的指定 {@link ResultSet} 以得到需要的数据库消息。
     * <br> 该方法会自动关闭 {@link ResultSet} 。
     *
     * @param supplier 操作 {@link DatabaseMetaData} 以提供信息所在的 {@link ResultSet}
     * @param reader   读取 {@link ResultSet} 中指定信息的方法
     * @param <R>      最终结果的返回类型
     * @return 最终结果，通过 {@link CompletableFuture#get()} 可阻塞并等待结果返回。
     * @throws NullPointerException 当 supplier 提供的 {@link ResultSet} 为NULL时抛出
     */
    default <R> CompletableFuture<R> fetchMetadata(@NotNull SQLFunction<DatabaseMetaData, ResultSet> supplier, @NotNull SQLFunction<@NotNull ResultSet, R> reader) {
        return fetchMetadata((meta, conn) -> supplier.apply(meta), reader);
    }

    /**
     * 获取并操作 {@link  DatabaseMetaData} 以得到需要的数据库消息。
     *
     * @param reader 操作与读取的方法
     * @param <R>    最终结果的返回类型
     * @return 最终结果，通过 {@link CompletableFuture#get()} 可阻塞并等待结果返回。
     */
    <R> CompletableFuture<R> fetchMetadata(@NotNull SQLBiFunction<DatabaseMetaData, Connection, R> reader);

    /**
     * 获取并操作 {@link DatabaseMetaData} 提供的指定 {@link ResultSet} 以得到需要的数据库消息。
     * <br> 该方法会自动关闭 {@link ResultSet} 。
     *
     * @param supplier 操作 {@link DatabaseMetaData} 以提供信息所在的 {@link ResultSet}
     * @param reader   读取 {@link ResultSet} 中指定信息的方法
     * @param <R>      最终结果的返回类型
     * @return 最终结果，通过 {@link CompletableFuture#get()} 可阻塞并等待结果返回。
     * @throws NullPointerException 当 supplier 提供的 {@link ResultSet} 为NULL时抛出
     */
    <R> CompletableFuture<R> fetchMetadata(@NotNull SQLBiFunction<DatabaseMetaData, Connection, ResultSet> supplier, @NotNull SQLFunction<@NotNull ResultSet, R> reader);

}
