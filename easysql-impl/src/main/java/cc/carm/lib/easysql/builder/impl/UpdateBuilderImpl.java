package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.PreparedSQLUpdateActionImpl;
import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.builder.UpdateBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UpdateBuilderImpl
		extends AbstractConditionalBuilder<UpdateBuilder, SQLAction<Integer>>
		implements UpdateBuilder {

	String tableName;

	List<String> columnNames;
	List<Object> columnValues;

	public UpdateBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
		super(manager);
		this.tableName = tableName;
	}

	@Override
	public PreparedSQLUpdateAction build() {

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("UPDATE `").append(getTableName()).append("` SET ");

		Iterator<String> iterator = this.columnNames.iterator();
		while (iterator.hasNext()) {
			sqlBuilder.append("`").append(iterator.next()).append("` = ?");
			if (iterator.hasNext()) sqlBuilder.append(" , ");
		}
		List<Object> allParams = new ArrayList<>(this.columnValues);

		if (hasConditions()) {
			sqlBuilder.append(" ").append(buildConditionSQL());
			allParams.addAll(getConditionParams());
		}

		if (limit > 0) sqlBuilder.append(" ").append(buildLimitSQL());

		return new PreparedSQLUpdateActionImpl(getManager(), sqlBuilder.toString(), allParams);
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public UpdateBuilder setColumnValues(LinkedHashMap<String, Object> columnData) {
		this.columnNames = new ArrayList<>();
		this.columnValues = new ArrayList<>();
		columnData.forEach((name, value) -> {
			this.columnNames.add(name);
			this.columnValues.add(value);
		});
		return this;
	}

	@Override
	public UpdateBuilder setColumnValues(String[] columnNames, Object[] columnValues) {
		if (columnNames.length != columnValues.length) {
			throw new RuntimeException("columnNames are not match with columnValues");
		}
		this.columnNames = Arrays.asList(columnNames);
		this.columnValues = Arrays.asList(columnValues);
		return this;
	}


	@Override
	protected UpdateBuilder getThis() {
		return this;
	}
}
