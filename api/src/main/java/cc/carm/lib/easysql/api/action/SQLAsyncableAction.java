package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.function.SQLHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface SQLAsyncableAction<T> extends SQLAction<T> {

    /**
     * 异步执行SQL语句，采用默认异常处理，无需返回值。
     */
    default void executeAsync() {
        executeAsync(null);
    }

    /**
     * 异步执行SQL语句
     *
     * @param success 成功时的操作
     */
    default void executeAsync(@Nullable SQLHandler<T> success) {
        executeAsync(success, null);
    }

    /**
     * 异步执行SQL语句
     *
     * @param success 成功时的操作
     * @param failure 异常处理器 默认为 {@link SQLAction#defaultExceptionHandler()}
     */
    void executeAsync(@Nullable SQLHandler<T> success,
                      @Nullable SQLExceptionHandler failure);

    /**
     * 以异步Future方式执行SQL语句。
     *
     * @return 异步执行的Future实例，可通过 {@link Future#get()} 阻塞并等待结果。
     */
    default @NotNull CompletableFuture<Void> executeFuture() {
        return executeFuture((t -> null));
    }

    /**
     * 以异步Future方式执行SQL语句。
     *
     * @return 异步执行的Future实例，可通过 {@link Future#get()} 阻塞并等待结果。
     */
    <R> @NotNull CompletableFuture<R> executeFuture(@NotNull SQLFunction<T, R> handler);

}
