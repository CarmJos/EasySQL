package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import org.jetbrains.annotations.NotNull;

public interface TableQueryBuilder extends ConditionalBuilder<TableQueryBuilder, PreparedQueryAction> {

	@NotNull String getTableName();

	/**
	 * 选定用于查询的列名
	 *
	 * @param columnNames 列名
	 * @return {@link TableQueryBuilder}
	 */
	TableQueryBuilder selectColumns(@NotNull String... columnNames);

	/**
	 * 对结果进行排序
	 *
	 * @param columnName 排序使用的列名
	 * @param asc        是否为正序排序 (为false则倒序排序)
	 * @return {@link TableQueryBuilder}
	 */
	TableQueryBuilder orderBy(@NotNull String columnName, boolean asc);

	/**
	 * 限制查询条数，用于分页查询。
	 *
	 * @param start 开始数
	 * @param end   结束条数
	 * @return {@link TableQueryBuilder}
	 * @since 0.2.6
	 */
	TableQueryBuilder setPageLimit(int start, int end);

}
