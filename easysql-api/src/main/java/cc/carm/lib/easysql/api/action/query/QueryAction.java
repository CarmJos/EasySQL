package cc.carm.lib.easysql.api.action.query;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.function.SQLFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * SQLQueryAction 是用于承载SQL查询语句并进行处理、返回并自动关闭连接的基本类。
 *
 * <ul>
 *     <li>同步执行 {@link #execute()}, {@link #execute(SQLFunction, BiConsumer)}
 *     <br>同步执行方法中有会抛出异常的方法与不抛出异常的方法，
 *     <br>若选择不抛出异常，则返回值可能为空，需要特殊处理。</li>
 *
 *     <li>异步执行 {@link #executeAsync(Consumer, BiConsumer)}
 *     <br>异步执行时将提供成功与异常两种处理方式
 *     <br>可自行选择是否对数据或异常进行处理
 *     <br>默认的异常处理器为 {@link #defaultExceptionHandler()}</li>
 * </ul>
 *
 * <b>注意： 无论是否异步，都不需要自行关闭ResultSet，本API已自动关闭</b>
 *
 * @author CarmJos
 * @since 0.2.6
 */
public interface QueryAction extends SQLAction<SQLQuery> {

    /**
     * 执行语句并处理返回值
     *
     * @param function 处理方法
     * @param <R>      需要返回的内容
     * @return 指定类型数据
     * @throws SQLException 当SQL操作出现问题时抛出
     */
    @Nullable
    default <R> R executeFunction(@NotNull SQLFunction<SQLQuery, R> function)
            throws SQLException {
        try (SQLQuery value = execute()) {
            return function.apply(value);
        } catch (SQLException exception) {
            throw new SQLException(exception);
        }
    }

}
