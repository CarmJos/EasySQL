package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;

public interface SQLUpdateAction extends SQLAction<Integer> {

	/**
	 * 设定自增主键的序列
	 *
	 * @param keyColumnIndex 自增主键的序列
	 *                       <br>若该值 ＞ 0，则 {@link #execute()} 返回自增主键数值
	 *                       <br>若该值 ≤ 0，则 {@link #execute()} 返回变更的行数
	 * @return {@link SQLUpdateAction}
	 * @see #setReturnGeneratedKey(boolean)
	 */
	@Deprecated
	default SQLUpdateAction setKeyIndex(int keyColumnIndex) {
		return setReturnGeneratedKey(keyColumnIndex > 0);
	}

	/**
	 * 设定该操作返回自增键序列。
	 *
	 * @return {@link SQLUpdateAction}
	 */
	default SQLUpdateAction returnGeneratedKey() {
		return setReturnGeneratedKey(true);
	}

	/**
	 * 设定该操作是否返回自增键序列。
	 *
	 * @return {@link SQLUpdateAction}
	 */
	SQLUpdateAction setReturnGeneratedKey(boolean returnGeneratedKey);

}
