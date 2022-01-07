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

public abstract class AbstractConditionalBuilder<B extends ConditionalBuilder<B, T>, T>
		extends AbstractSQLBuilder implements ConditionalBuilder<B, T> {

	ArrayList<String> conditionSQLs = new ArrayList<>();
	ArrayList<Object> conditionParams = new ArrayList<>();
	int limit = -1;

	public AbstractConditionalBuilder(@NotNull SQLManagerImpl manager) {
		super(manager);
	}

	protected abstract B getThis();

	@Override
	public B setConditions(@Nullable String condition) {
		this.conditionSQLs = new ArrayList<>();
		this.conditionParams = new ArrayList<>();
		if (condition != null) this.conditionSQLs.add(condition);
		return getThis();
	}

	@Override
	public B setConditions(
			LinkedHashMap<@NotNull String, @Nullable Object> conditions
	) {
		conditions.forEach(this::addCondition);
		return getThis();
	}

	@Override
	public B addCondition(@Nullable String condition) {
		this.conditionSQLs.add(condition);
		return getThis();
	}

	@Override
	public B addCondition(
			@NotNull String queryName, @NotNull String operator, @Nullable Object queryValue
	) {
		addCondition("`" + queryName + "` " + operator + " ?");
		this.conditionParams.add(queryValue);
		return getThis();
	}

	@Override
	public B addCondition(
			@NotNull String[] queryNames, @Nullable Object[] queryValues
	) {
		if (queryNames.length != queryValues.length) {
			throw new RuntimeException("queryNames are not match with queryValues");
		}
		for (int i = 0; i < queryNames.length; i++) {
			addCondition(queryNames[i], queryValues[i]);
		}
		return getThis();
	}


	@Override
	public B addNotNullCondition(@NotNull String queryName) {
		return addCondition("`" + queryName + "` IS NOT NULL");
	}


	@Override
	public B addTimeCondition(
			@NotNull String queryName, @Nullable Date startDate, @Nullable Date endDate
	) {
		if (startDate == null && endDate == null) return getThis(); // 都不限定时间，不用判断了
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
		return getThis();
	}


	@Override
	public B setLimit(int limit) {
		this.limit = limit;
		return getThis();
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
