package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import org.jetbrains.annotations.NotNull;

public interface TableQueryBuilder extends ConditionalBuilder<PreparedQueryAction> {

	@NotNull String getTableName();

	TableQueryBuilder selectColumns(@NotNull String... columnNames);

	/**
	 * 对结果进行排序
	 *
	 * @param columnName 排序使用的列名
	 * @param asc        是否为正序排序 (为false则倒序排序)
	 * @return {@link TableQueryBuilder}
	 */
	TableQueryBuilder orderBy(@NotNull String columnName, boolean asc);

}
