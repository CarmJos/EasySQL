package cc.carm.lib.easysql.api.action.base;

import cc.carm.lib.easysql.api.action.SQLAction;

import java.util.List;

public interface PreparedBatchUpdateAction<T extends Number> extends SQLAction<List<T>> {

    /**
     * 设定多组SQL语句中所有 ? 对应的参数
     *
     * @param allValues 所有参数内容
     * @return {@link PreparedBatchUpdateAction}
     */
    PreparedBatchUpdateAction<T> allValues(Iterable<Object[]> allValues);

    /**
     * 添加一组SQL语句中所有 ? 对应的参数
     *
     * @param values 参数内容
     * @return {@link PreparedBatchUpdateAction}
     */
    PreparedBatchUpdateAction<T> values(Object... values);

    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link UpdateAction}
     */
    PreparedBatchUpdateAction<T> returnGeneratedKeys();

    /**
     * 设定该操作返回自增键序列。
     *
     * @param keyTypeClass 自增序列的数字类型
     * @param <N>          自增键序列类型 {@link Number}
     * @return {@link UpdateAction}
     * @since 0.4.0
     */
    <N extends Number> PreparedBatchUpdateAction<N> returnGeneratedKeys(Class<N> keyTypeClass);

}
