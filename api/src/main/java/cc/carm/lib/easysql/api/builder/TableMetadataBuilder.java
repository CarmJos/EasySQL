package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.api.function.SQLFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.sql.ResultSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface TableMetadataBuilder extends SQLBuilder {

    /**
     * @return 本表是否存在
     */
    CompletableFuture<Boolean> validateExist();

    /**
     * 对表内的数据列元数据进行读取
     *
     * @param columnPattern 列的名称匹配表达式, 为空则匹配所有列
     * @param reader        读取的方法
     * @param <R>           结果类型
     * @return 读取结果
     */
    <R> CompletableFuture<R> fetchColumns(@Nullable String columnPattern,
                                          @NotNull SQLFunction<ResultSet, R> reader);

    /**
     * @param columnPattern 需要判断的列名表达式
     * @return 对应列是否存在
     */
    CompletableFuture<Boolean> isColumnExists(@NotNull String columnPattern);

    /**
     * 列出所有表内的全部列。
     *
     * @return 表内全部数据列的列名
     */
    default CompletableFuture<@Unmodifiable Set<String>> listColumns() {
        return listColumns(null);
    }

    /**
     * 列出所有满足表达式的列。
     *
     * @param columnPattern 列名表达式，为空则列出全部
     * @return 所有满足表达式的列名
     */
    CompletableFuture<@Unmodifiable Set<String>> listColumns(@Nullable String columnPattern);

    // More coming soon.

}
