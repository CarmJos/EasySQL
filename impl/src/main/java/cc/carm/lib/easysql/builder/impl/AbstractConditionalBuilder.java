package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.builder.ConditionalBuilder;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import static cc.carm.lib.easysql.api.SQLBuilder.withBackQuote;

public abstract class AbstractConditionalBuilder<B extends ConditionalBuilder<B, T>, T extends SQLAction<?>>
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
    public B addCondition(@NotNull String columnName, @Nullable Object queryValue) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        if (queryValue == null) {
            return addCondition(withBackQuote(columnName) + " IS NULL");
        } else {
            return addCondition(columnName, "=", queryValue);
        }
    }

    @Override
    public B addCondition(
            @NotNull String columnName, @NotNull String operator, @Nullable Object queryValue
    ) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        Objects.requireNonNull(operator, "operator could not be null (e.g. > or = or <) ");
        addCondition(withBackQuote(columnName) + " " + operator + " ?");
        this.conditionParams.add(queryValue);
        return getThis();
    }

    @Override
    public B addCondition(
            @NotNull String[] columnNames, @Nullable Object[] queryValues
    ) {
        Objects.requireNonNull(columnNames, "columnName could not be null");
        if (queryValues == null || columnNames.length != queryValues.length) {
            throw new RuntimeException("queryNames are not match with queryValues");
        }
        for (int i = 0; i < columnNames.length; i++) {
            addCondition(columnNames[i], queryValues[i]);
        }
        return getThis();
    }


    @Override
    public B addNotNullCondition(@NotNull String columnName) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        return addCondition(withBackQuote(columnName) + " IS NOT NULL");
    }


    @Override
    public B addTimeCondition(
            @NotNull String columnName, @Nullable Date startDate, @Nullable Date endDate
    ) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        if (startDate == null && endDate == null) return getThis(); // 都不限定时间，不用判断了
        if (startDate != null) {
            addCondition(withBackQuote(columnName) + " BETWEEN ? AND ?");
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
            addCondition(columnName, "<=", endDate);
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
                if (iterator.hasNext()) conditionBuilder.append(" AND ");
            }
            return conditionBuilder.toString().trim();
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
