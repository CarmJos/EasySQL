package cc.carm.lib.easysql.api.action.asyncable;

import cc.carm.lib.easysql.api.action.SQLAsyncableAction;
import cc.carm.lib.easysql.api.action.base.UpdateAction;

public interface AsyncableUpdateAction<T extends Number>
        extends SQLAsyncableAction<T> {


    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link UpdateAction}
     */
    AsyncableUpdateAction<T> returnGeneratedKey();

    /**
     * 设定该操作返回自增键序列。
     *
     * @param keyTypeClass 自增序列的数字类型
     * @param <N>          自增键序列类型 {@link Number}
     * @return {@link UpdateAction}
     * @since 0.4.0
     */
    <N extends Number> AsyncableUpdateAction<N> returnGeneratedKey(Class<N> keyTypeClass);

}
