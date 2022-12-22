package cc.carm.lib.easysql.api.action.asyncable;

import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

public interface AsyncablePreparedQueryAction extends AsyncableQueryAction {

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link AsyncablePreparedQueryAction}
     */
    AsyncablePreparedQueryAction setParams(@Nullable Object... params);

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link AsyncablePreparedQueryAction}
     */
    AsyncablePreparedQueryAction setParams(@Nullable Iterable<Object> params);

    /**
     * 直接对 {@link PreparedStatement} 进行处理
     *
     * @param statement {@link Consumer} 处理操作
     *                  若为空则不进行处理
     * @return {@link AsyncablePreparedQueryAction}
     */
    AsyncablePreparedQueryAction handleStatement(@Nullable Consumer<PreparedStatement> statement);

}
