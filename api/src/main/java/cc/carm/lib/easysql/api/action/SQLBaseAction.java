package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLSource;
import cc.carm.lib.easysql.api.function.SQLFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * SQLBaseAction 是用于承载SQL语句并进行处理、返回的基本类。
 *
 * @param <T> 需要返回的类型
 * @author CarmJos
 * @since 0.0.1
 */
public interface SQLBaseAction<T> {

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
     * 得到承载该Action的对应{@link SQLSource}
     *
     * @return {@link SQLSource}
     */
    @NotNull SQLSource getSource();

    /**
     * 执行该Action对应的SQL语句
     *
     * @return 指定数据类型
     * @throws SQLException 当SQL操作出现问题时抛出
     */
    @NotNull T execute() throws SQLException;

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

}
