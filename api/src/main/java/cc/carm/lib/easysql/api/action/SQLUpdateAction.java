package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;

public interface SQLUpdateAction<T extends Number> extends SQLAction<T> {


    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link SQLUpdateAction}
     */
    SQLUpdateAction<T> returnGeneratedKey();

    /**
     * 设定该操作返回自增键序列。
     *
     * @param keyTypeClass 自增序列的数字类型
     * @param <N>          自增键序列类型 {@link Number}
     * @return {@link SQLUpdateAction}
     */
    <N extends Number> SQLUpdateAction<N> returnGeneratedKey(Class<N> keyTypeClass);


}
