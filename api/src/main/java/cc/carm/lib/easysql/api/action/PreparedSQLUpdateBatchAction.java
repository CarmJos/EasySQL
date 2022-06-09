package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;

import java.util.List;

public interface PreparedSQLUpdateBatchAction<T extends Number> extends SQLAction<List<T>> {

    /**
     * 设定多组SQL语句中所有 ? 对应的参数
     *
     * @param allParams 所有参数内容
     * @return {@link PreparedSQLUpdateBatchAction}
     */
    PreparedSQLUpdateBatchAction<T> setAllParams(Iterable<Object[]> allParams);

    /**
     * 添加一组SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLUpdateBatchAction}
     */
    PreparedSQLUpdateBatchAction<T> addParamsBatch(Object... params);

    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link SQLUpdateAction}
     */
    PreparedSQLUpdateBatchAction<T> returnGeneratedKeys();

    /**
     * 设定该操作返回自增键序列。
     *
     * @param keyTypeClass 自增序列的数字类型
     * @param <N>          自增键序列类型 {@link Number}
     * @return {@link SQLUpdateAction}
     */
    <N extends Number> PreparedSQLUpdateBatchAction<N> returnGeneratedKeys(Class<N> keyTypeClass);

}
