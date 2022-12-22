package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.function.SQLHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface SQLAction<T> {

    /**
     * 得到该Action的UUID
     *
     * @return UUID
     */
    @NotNull UUID getActionUUID();

    /**
     * 得到短八位格式的UUID
     *
     * @return UUID(8)
     */
    @NotNull String getShortID();

    /**
     * 得到该Action的创建时间。
     * <br>注意，此处获得的时间非时间戳毫秒数，仅用于计算耗时。
     *
     * @return 创建时间 (毫秒)
     */
    default long getCreateTime() {
        return getCreateTime(TimeUnit.MILLISECONDS);
    }

    /**
     * 得到该Action的创建时间
     * <br>注意，此处获得的时间非时间戳毫秒数，仅用于计算耗时。
     *
     * @param unit 时间单位
     * @return 创建时间
     */
    long getCreateTime(TimeUnit unit);

    /**
     * 得到该Action所要执行的源SQL语句
     *
     * @return 源SQL语句
     */
    @NotNull String getSQLContent();

    /**
     * 得到该Action所要执行的源SQL语句列表。
     *
     * @return 源SQL语句列表
     */
    default @NotNull List<String> getSQLContents() {
        return Collections.singletonList(getSQLContent());
    }

    /**
     * 得到承载该Action的对应{@link SQLManager}
     *
     * @return {@link SQLManager}
     */
    @NotNull SQLManager getManager();

    /**
     * 执行该Action对应的SQL语句
     *
     * @return 指定数据类型
     * @throws SQLException 当SQL操作出现问题时抛出
     */
    @NotNull T execute() throws SQLException;


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
     * 执行语句并处理返回值
     *
     * @param function 处理方法
     * @param <R>      需要返回的内容
     * @return 指定类型数据
     * @throws SQLException 当SQL操作出现问题时抛出
     */
    @Nullable
    default <R> R executeFunction(@NotNull SQLFunction<@NotNull T, R> function) throws SQLException {
        return executeFunction(function, null);
    }

    /**
     * 执行语句并处理返回值
     *
     * @param function      处理方法
     * @param defaultResult 默认结果，若处理后的结果为null，则返回该值
     * @param <R>           需要返回的内容
     * @return 指定类型数据
     * @throws SQLException 当SQL操作出现问题时抛出
     */
    @Nullable
    @Contract("_,!null -> !null")
    default <R> R executeFunction(@NotNull SQLFunction<@NotNull T, R> function,
                                  @Nullable R defaultResult) throws SQLException {
        try {
            R result = function.apply(execute());
            return result == null ? defaultResult : result;
        } catch (SQLException exception) {
            throw new SQLException(exception);
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
        return getManager().getExceptionHandler();
    }

    /**
     * 设定通用的异常处理器。
     * <br> 在使用 {@link #execute(SQLExceptionHandler)} 等相关方法时，若传入的处理器为null，则会采用此处理器。
     * <br> 若该方法传入参数为 null，则会使用 {@link #defaultExceptionHandler()} 。
     *
     * @param handler 异常处理器
     */
    default void setExceptionHandler(@Nullable SQLExceptionHandler handler) {
        getManager().setExceptionHandler(handler);
    }

}
