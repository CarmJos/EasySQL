package cc.carm.lib.easysql.api.action;

import org.jetbrains.annotations.Nullable;

public interface PreparedSQLUpdateAction extends SQLUpdateAction {

	/**
	 * 设定SQL语句中所有 ? 对应的参数
	 *
	 * @param params 参数内容
	 * @return {@link PreparedSQLUpdateAction}
	 */
	PreparedSQLUpdateAction setParams(Object... params);

	/**
	 * 设定SQL语句中所有 ? 对应的参数
	 *
	 * @param params 参数内容
	 * @return {@link PreparedSQLUpdateAction}
	 */
	PreparedSQLUpdateAction setParams(@Nullable Iterable<Object> params);

}
