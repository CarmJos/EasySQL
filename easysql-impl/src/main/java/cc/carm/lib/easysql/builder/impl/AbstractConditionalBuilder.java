package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.api.builder.ConditionalBuilder;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class AbstractConditionalBuilder<T> extends AbstractSQLBuilder implements ConditionalBuilder<T> {

	ArrayList<String> conditionSQLs = new ArrayList<>();
	ArrayList<Object> conditionParams = new ArrayList<>();
	int limit = -1;

	public AbstractConditionalBuilder(@NotNull SQLManagerImpl manager) {
		super(manager);
	}

	@Override
	public AbstractConditionalBuilder<T> setConditions(@Nullable String condition) {
		this.conditionSQLs = new ArrayList<>();
		this.conditionParams = new ArrayList<>();
		if (condition != null) this.conditionSQLs.add(condition);
		return this;
	}

	@Override
	public AbstractConditionalBuilder<T> setConditions(
			LinkedHashMap<@NotNull String, @Nullable Object> conditions
	) {
		conditions.forEach(this::addCondition);
		return this;
	}

	@Override
	public AbstractConditionalBuilder<T> addCondition(@Nullable String condition) {
		this.conditionSQLs.add(condition);
		return this;
	}

	@Override
	public AbstractConditionalBuilder<T> addNotNullCondition(@NotNull String queryName) {
		return addCondition("`" + queryName + "` IS NOT NULL");
	}


	@Override
	public AbstractConditionalBuilder<T> addCondition(
			@NotNull String queryName, @NotNull String operator, @Nullable Object queryValue
	) {
		addCondition("`" + queryName + "` " + operator + " ?");
		this.conditionParams.add(queryValue);
		return this;
	}

	@Override
	public AbstractConditionalBuilder<T> addCondition(
			@NotNull String[] queryNames, @Nullable Object[] queryValues
	) {
		if (queryNames.length != queryValues.length) {
			throw new RuntimeException("queryNames are not match with queryValues");
		}
		for (int i = 0; i < queryNames.length; i++) {
			addCondition(queryNames[i], queryValues[i]);
		}
		return this;
	}

	@Override
	public AbstractConditionalBuilder<T> addTimeCondition(
			@NotNull String queryName, @Nullable Date startDate, @Nullable Date endDate
	) {
		if (startDate == null && endDate == null) return this; // 都不限定时间，不用判断了
		if (startDate != null) {
			addCondition("`" + queryName + "` BETWEEN ? AND ?");
			this.conditionParams.add(startDate);
			if (endDate != null) {
				this.conditionParams.add(endDate);
			} else {
				if (startDate instanceof java.sql.Date) {
					this.conditionParams.add(new java.sql.Date(System.currentTimeMillis()));
				} else if (startDate instanceof Time) {
					this.conditionParams.add(new Time(System.currentTimeMillis()));
				} else {
					this.conditionParams.add(new Timestamp(System.currentTimeMillis()));
				}
			}
		} else {
			addCondition(queryName, "<=", endDate);
		}
		return this;
	}


	@Override
	public AbstractConditionalBuilder<T> setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	protected String buildConditionSQL() {

		if (!conditionSQLs.isEmpty()) {
			StringBuilder conditionBuilder = new StringBuilder();
			conditionBuilder.append("WHERE").append(" ");
			Iterator<String> iterator = conditionSQLs.iterator();
			while (iterator.hasNext()) {
				conditionBuilder.append(iterator.next());
				if (iterator.hasNext()) conditionBuilder.append(" ");
			}
			return conditionBuilder.toString();
		} else {
			return null;
		}

	}

	protected String buildLimitSQL() {
		return limit > 0 ? "LIMIT " + limit : "";
	}

	protected ArrayList<Object> getConditionParams() {
		return conditionParams;
	}

	protected boolean hasConditions() {
		return this.conditionSQLs != null && !this.conditionSQLs.isEmpty();
	}

	protected boolean hasConditionParams() {
		return hasConditions() && getConditionParams() != null && !getConditionParams().isEmpty();
	}
}
