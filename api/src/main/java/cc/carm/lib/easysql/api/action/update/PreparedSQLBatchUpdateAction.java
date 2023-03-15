package cc.carm.lib.easysql.api.action.update;

import cc.carm.lib.easysql.api.action.SQLBaseAction;
import cc.carm.lib.easysql.api.action.SQLAdvancedAction;

import java.util.List;

public interface PreparedSQLBatchUpdateAction<T extends Number, B extends PreparedSQLBatchUpdateAction<T, B>>
        extends SQLBaseAction<List<T>> {

    interface Base<T extends Number> extends PreparedSQLBatchUpdateAction<T, Base<T>> {
    }

    interface Advanced<T extends Number> extends PreparedSQLBatchUpdateAction<T, Advanced<T>>, SQLAdvancedAction<List<T>> {
    }

    /**
     * 设定多组SQL语句中所有 ? 对应的参数
     *
     * @param allValues 所有参数内容
     * @return {@link PreparedSQLBatchUpdateAction}
     */
    B allValues(Iterable<Object[]> allValues);

    /**
     * 添加一组SQL语句中所有 ? 对应的参数
     *
     * @param values 参数内容
     * @return {@link PreparedSQLBatchUpdateAction}
     */
    B addValues(Object... values);

    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link PreparedSQLBatchUpdateAction}
     */
    B returnGeneratedKeys();

    /**
     * 设定该操作返回自增键序列。
     *
     * @param keyTypeClass 自增序列的数字类型
     * @param <N>          自增键序列类型 {@link Number}
     * @return {@link PreparedSQLBatchUpdateAction}
     * @since 0.4.0
     */
    <N extends Number, D extends PreparedSQLBatchUpdateAction<N, D>> D returnGeneratedKeys(Class<N> keyTypeClass);
}
