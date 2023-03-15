package cc.carm.lib.easysql.api.action.update;

import cc.carm.lib.easysql.api.action.SQLBaseAction;
import cc.carm.lib.easysql.api.action.SQLAdvancedAction;

public interface SQLUpdateAction<T extends Number, B extends SQLUpdateAction<T, B>> extends SQLBaseAction<T> {

    interface Base<T extends Number> extends SQLUpdateAction<T, Base<T>> {

    }

    interface Advanced<T extends Number> extends SQLUpdateAction<T, Advanced<T>>, SQLAdvancedAction<T> {

    }

    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link SQLUpdateAction}
     */
    B returnGeneratedKey();

    /**
     * 设定该操作返回自增键序列。
     *
     * @param keyTypeClass 自增序列的数字类型
     * @param <N>          自增键序列类型 {@link Number}
     * @return {@link SQLUpdateAction}
     * @since 0.4.0
     */
    <N extends Number, D extends SQLUpdateAction<N, D>> D returnGeneratedKey(Class<N> keyTypeClass);

}
