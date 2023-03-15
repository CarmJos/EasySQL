package cc.carm.lib.easysql.api.action.query;

import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.SQLBaseAction;
import cc.carm.lib.easysql.api.action.SQLAdvancedAction;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.function.SQLHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

/**
 * SQLQueryAction 是用于承载SQL查询语句并进行处理、返回并自动关闭连接的基本类。
 *
 * <ul>
 *     <li>同步执行 {@link #execute()}, {@link #execute(SQLFunction, SQLExceptionHandler)}
 *     <br>同步执行方法中有会抛出异常的方法与不抛出异常的方法，
 *     <br>若选择不抛出异常，则返回值可能为空，需要特殊处理。</li>
 *
 *     <li>异步执行 {@link Advanced#executeAsync(SQLHandler, SQLExceptionHandler)}
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
public interface SQLQueryAction<B extends SQLQueryAction<B>> extends SQLBaseAction<SQLQuery> {

    interface Base extends SQLQueryAction<Base> {
    }

    interface Advanced extends SQLQueryAction<Advanced>, SQLAdvancedAction<SQLQuery> {
    }

    @Override
    @Contract("_,!null -> !null")
    default <R> @Nullable R executeFunction(@NotNull SQLFunction<@NotNull SQLQuery, R> function,
                                            @Nullable R defaultResult) throws SQLException {
        try (SQLQuery value = execute()) {
            R result = function.apply(value);
            return result == null ? defaultResult : result;
        }
    }

}
