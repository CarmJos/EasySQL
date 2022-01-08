package cc.carm.lib.easysql.api.action.query;

import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

public interface PreparedQueryAction extends QueryAction {

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedQueryAction}
     */
    PreparedQueryAction setParams(@Nullable Object... params);

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedQueryAction}
     */
    PreparedQueryAction setParams(@Nullable Iterable<Object> params);

    /**
     * 直接对 {@link PreparedStatement} 进行处理
     *
     * @param statement {@link Consumer} 处理操作
     *                  若为空则不进行处理
     * @return {@link PreparedQueryAction}
     */
    PreparedQueryAction handleStatement(@Nullable Consumer<PreparedStatement> statement);

}
