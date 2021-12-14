package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;

public interface SQLUpdateAction extends SQLAction<Integer> {

	/**
	 * 设定自增主键的序列
	 *
	 * @param keyColumnIndex 自增主键的序列
	 *                       若该值 ＞ 0，则 {@link #execute()} 返回自增主键数值
	 *                       若该值 ≤ 0，则 {@link #execute()} 返回变更的行数
	 * @return {@link SQLUpdateAction}
	 */
	SQLUpdateAction setKeyIndex(int keyColumnIndex);

	/**
	 * 默认主键序列的数值为 -1 (≤0) ，即默认返回发生变更的行数。
	 *
	 * @return 默认主键序列
	 */
	default SQLUpdateAction defaultKeyIndex() {
		return setKeyIndex(-1); // will return changed lines number
	}

}
