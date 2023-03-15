package cc.carm.lib.easysql.api.action.update;

import cc.carm.lib.easysql.api.action.SQLAdvancedAction;
import org.jetbrains.annotations.Nullable;

public interface PreparedSQLUpdateAction<T extends Number, B extends PreparedSQLUpdateAction<T, B>>
        extends SQLUpdateAction<T, B> {

    interface Base<T extends Number> extends PreparedSQLUpdateAction<T, Base<T>> {
    }

    interface Advanced<T extends Number> extends PreparedSQLUpdateAction<T, Advanced<T>>, SQLAdvancedAction<T> {
    }

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLUpdateAction}
     */
    B values(Object... params);

    /**
     * 设定SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLUpdateAction}
     * @since 0.4.0
     */
    B values(@Nullable Iterable<Object> params);

}
