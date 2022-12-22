package cc.carm.lib.easysql.api.action.asyncable;

import cc.carm.lib.easysql.api.action.base.PreparedUpdateAction;
import org.jetbrains.annotations.Nullable;

public interface AsyncablePreparedUpdateAction<T extends Number>
        extends AsyncableUpdateAction<T> {

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedUpdateAction}
     */
    AsyncablePreparedUpdateAction<T> values(Object... params);

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedUpdateAction}
     * @since 0.4.0
     */
    AsyncablePreparedUpdateAction<T> values(@Nullable Iterable<Object> params);

}
