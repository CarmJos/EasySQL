package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.function.SQLHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * SQLAdvancedAction 是用于承载SQL语句并进行处理、返回的高级类。
 *
 * <ul>
 *     <li>同步执行 {@link #execute()}, {@link #execute(SQLFunction, SQLExceptionHandler)}
 *     <br>同步执行方法中有会抛出异常的方法与不抛出异常的方法，
 *     <br>若选择不抛出异常，则返回值可能为空，需要特殊处理。</li>
 *
 *     <li>异步执行 {@link #executeAsync(SQLHandler, SQLExceptionHandler)}
 *     <br>异步执行时将提供成功与异常两种处理方式
 *     <br>可自行选择是否对数据或异常进行处理
 *     <br>默认的异常处理器为 {@link #defaultExceptionHandler()}
 *     <br>若有特殊需要，可通过{@link #setExceptionHandler(SQLExceptionHandler)} 方法修改默认的处理器</li>
 * </ul>
 *
 * @param <T> 需要返回的类型
 * @author CarmJos
 * @since 0.5.0
 */
public interface SQLAdvancedAction<T> extends SQLBaseAction<T> {

    /**
     * 执行语句并返回值
     *
     * @param exceptionHandler 异常处理器 默认为 {@link #defaultExceptionHandler()}
     * @return 指定类型数据
     */
    @Nullable
    default T execute(@Nullable SQLExceptionHandler exceptionHandler) {
        return execute(t -> t, exceptionHandler);
    }

    /**
     * 执行语句并处理返回值
     *
     * @param function         处理方法
     * @param exceptionHandler 异常处理器 默认为 {@link #defaultExceptionHandler()}
     * @param <R>              需要返回的内容
     * @return 指定类型数据
     */
    @Nullable
    default <R> R execute(@NotNull SQLFunction<T, R> function,
                          @Nullable SQLExceptionHandler exceptionHandler) {
        return execute(function, null, exceptionHandler);
    }

    /**
     * 执行语句并处理返回值
     *
     * @param function         处理方法
     * @param defaultResult    默认结果，若处理后的结果为null，则返回该值
     * @param exceptionHandler 异常处理器 默认为 {@link #defaultExceptionHandler()}
     * @param <R>              需要返回的内容
     * @return 指定类型数据
     */
    @Nullable
    @Contract("_,!null,_ -> !null")
    default <R> R execute(@NotNull SQLFunction<T, R> function,
                          @Nullable R defaultResult,
                          @Nullable SQLExceptionHandler exceptionHandler) {
        try {
            return executeFunction(function, defaultResult);
        } catch (SQLException exception) {
            handleException(exceptionHandler, exception);
            return null;
        }
    }

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
     * @param failure 异常处理器 默认为 {@link SQLAdvancedAction#defaultExceptionHandler()}
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


    default void handleException(@Nullable SQLExceptionHandler handler, SQLException exception) {
        if (handler == null) handler = defaultExceptionHandler();
        handler.accept(exception, this);
    }

    /**
     * 获取管理器提供的默认异常处理器。
     * 若未使用过 {@link #setExceptionHandler(SQLExceptionHandler)} 方法，
     * 则默认返回 {@link SQLExceptionHandler#detailed(Logger)} 。
     *
     * @return {@link SQLExceptionHandler}
     */
    default SQLExceptionHandler defaultExceptionHandler() {
        return getSource().getExceptionHandler();
    }

    /**
     * 设定通用的异常处理器。
     * <br> 在使用 {@link #execute(SQLExceptionHandler)} 等相关方法时，若传入的处理器为null，则会采用此处理器。
     * <br> 若该方法传入参数为 null，则会使用 {@link #defaultExceptionHandler()} 。
     *
     * @param handler 异常处理器
     */
    default void setExceptionHandler(@Nullable SQLExceptionHandler handler) {
        getSource().setExceptionHandler(handler);
    }


}
