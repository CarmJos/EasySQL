package cc.carm.lib.easysql.api.action;

import org.jetbrains.annotations.Nullable;

public interface PreparedSQLUpdateAction<T extends Number> extends SQLUpdateAction<T> {

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLUpdateAction}
     */
    PreparedSQLUpdateAction<T> setParams(Object... params);

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLUpdateAction}
     * @since 0.4.0
     */
    PreparedSQLUpdateAction<T> setParams(@Nullable Iterable<Object> params);

}
