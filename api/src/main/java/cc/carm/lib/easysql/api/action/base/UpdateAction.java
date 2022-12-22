package cc.carm.lib.easysql.api.action.base;

import cc.carm.lib.easysql.api.action.SQLAction;

public interface UpdateAction<T extends Number> extends SQLAction<T> {

    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link UpdateAction}
     */
    UpdateAction<T> returnGeneratedKey();

    /**
     * 设定该操作返回自增键序列。
     *
     * @param keyTypeClass 自增序列的数字类型
     * @param <N>          自增键序列类型 {@link Number}
     * @return {@link UpdateAction}
     * @since 0.4.0
     */
    <N extends Number> UpdateAction<N> returnGeneratedKey(Class<N> keyTypeClass);


}
