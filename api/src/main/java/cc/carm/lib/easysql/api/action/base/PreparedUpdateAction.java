package cc.carm.lib.easysql.api.action.base;

import org.jetbrains.annotations.Nullable;

public interface PreparedUpdateAction<T extends Number> extends UpdateAction<T> {

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedUpdateAction}
     */
    PreparedUpdateAction<T> values(Object... params);

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedUpdateAction}
     * @since 0.4.0
     */
    PreparedUpdateAction<T> values(@Nullable Iterable<Object> params);

}
