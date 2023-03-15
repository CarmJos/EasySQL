package cc.carm.lib.easysql.api.action.query;

import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.SQLAdvancedAction;
import cc.carm.lib.easysql.api.function.SQLHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;

public interface PreparedSQLQueryAction<B extends PreparedSQLQueryAction<B>> extends SQLQueryAction<B> {

    interface Base extends PreparedSQLQueryAction<Base> {
    }

    interface Advanced extends PreparedSQLQueryAction<Advanced>, SQLAdvancedAction<SQLQuery> {
    }

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLQueryAction}
     */
    @NotNull B setParams(@Nullable Object... params);

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLQueryAction}
     */
    @NotNull B setParams(@Nullable Iterable<Object> params);

    /**
     * 直接对 {@link PreparedStatement} 进行处理
     *
     * @param statement 通过 {@link SQLHandler} 处理操作
     *                  若为空则不进行处理
     * @return {@link PreparedSQLQueryAction}
     */
    @NotNull B handleStatement(@Nullable SQLHandler<PreparedStatement> statement);

}
