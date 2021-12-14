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
	 *                       若该值 > 0，则 {@link #execute()} 返回自增主键数值
	 *                       若该值 ≤ 0，则 {@link #execute()} 返回变更的行数
	 * @return {@link PreparedSQLUpdateBatchAction}
	 */
	PreparedSQLUpdateBatchAction setKeyIndex(int keyColumnIndex);

	/**
	 * 默认主键序列的数值为 -1 (≤0) ，即默认返回发生变更的行数。
	 *
	 * @return 默认主键序列
	 */
	default PreparedSQLUpdateBatchAction defaultKeyIndex() {
		return setKeyIndex(-1); // will return changed lines number
	}

}
