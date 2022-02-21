package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;

import java.util.List;

public interface PreparedSQLUpdateBatchAction extends SQLAction<List<Integer>> {

    /**
     * 设定多组SQL语句中所有 ? 对应的参数
     *
     * @param allParams 所有参数内容
     * @return {@link PreparedSQLUpdateBatchAction}
     */
    PreparedSQLUpdateBatchAction setAllParams(Iterable<Object[]> allParams);

    /**
     * 添加一组SQL语句中所有 ? 对应的参数
     *
     * @param params 参数内容
     * @return {@link PreparedSQLUpdateBatchAction}
     */
    PreparedSQLUpdateBatchAction addParamsBatch(Object... params);

    /**
     * 设定自增主键的序列
     *
     * @param keyColumnIndex 自增主键的序列
     *                       <br>若该值 ＞ 0，则 {@link #execute()} 返回自增主键数值
     *                       <br>若该值 ≤ 0，则 {@link #execute()} 返回变更的行数
     * @return {@link PreparedSQLUpdateBatchAction}
     * @see #setReturnGeneratedKeys(boolean)
     */
    @Deprecated
    default PreparedSQLUpdateBatchAction setKeyIndex(int keyColumnIndex) {
        return setReturnGeneratedKeys(keyColumnIndex > 0);
    }

    /**
     * 设定该操作返回自增键序列。
     *
     * @return {@link PreparedSQLUpdateBatchAction}
     */
    default PreparedSQLUpdateBatchAction returnGeneratedKeys() {
        return setReturnGeneratedKeys(true);
    }

    /**
     * 设定该操作是否返回自增键序列。
     *
     * @param returnGeneratedKey 是否返回自增键序列
     * @return {@link PreparedSQLUpdateBatchAction}
     */
    PreparedSQLUpdateBatchAction setReturnGeneratedKeys(boolean returnGeneratedKey);

}
