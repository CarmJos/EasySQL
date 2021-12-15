package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.query.PreparedQueryActionImpl;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import cc.carm.lib.easysql.api.builder.TableQueryBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TableQueryBuilderImpl
		extends AbstractConditionalBuilder<PreparedQueryAction>
		implements TableQueryBuilder {

	@NotNull String tableName;

	ArrayList<Object> params = new ArrayList<>();
	String[] columns;

	@Nullable String orderBy;

	public TableQueryBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
		super(manager);
		this.tableName = tableName;
	}

	@Override
	public PreparedQueryActionImpl build() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT").append(" ");
		if (columns == null || columns.length < 1) {
			sqlBuilder.append("*");
		} else {
			for (int i = 0; i < columns.length; i++) {
				String name = columns[i];
				sqlBuilder.append("`").append(name).append("`");
				if (i != columns.length - 1) {
					sqlBuilder.append(",");
				}
			}
		}

		sqlBuilder.append(" ").append("FROM").append(" ");
		sqlBuilder.append("`").append(tableName).append("`");

		if (hasConditions()) sqlBuilder.append(" ").append(buildConditionSQL());
		if (limit > 0) sqlBuilder.append(" ").append(buildLimitSQL());
		if (orderBy != null) sqlBuilder.append(orderBy);

		return new PreparedQueryActionImpl(getManager(), sqlBuilder.toString())
				.setParams(hasConditionParams() ? params : null);
	}

	@Override
	public @NotNull String getTableName() {
		return tableName;
	}

	@Override
	public TableQueryBuilderImpl selectColumns(@NotNull String[] columnNames) {
		this.columns = columnNames;
		return this;
	}

	@Override
	public TableQueryBuilder orderBy(@NotNull String columnName, boolean asc) {
		this.orderBy = "ORDER BY `" + columnName + "` " + (asc ? "ASC" : "DESC");
		return this;
	}

}
