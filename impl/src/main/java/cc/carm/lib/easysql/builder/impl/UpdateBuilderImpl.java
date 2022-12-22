package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.PreparedUpdateActionImpl;
import cc.carm.lib.easysql.api.action.base.PreparedUpdateAction;
import cc.carm.lib.easysql.api.builder.UpdateBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static cc.carm.lib.easysql.api.SQLBuilder.withBackQuote;

public class UpdateBuilderImpl
        extends AbstractConditionalBuilder<UpdateBuilder, SQLAction<Integer>>
        implements UpdateBuilder {

    protected final @NotNull String tableName;

    @NotNull LinkedHashMap<String, Object> columnData;

    public UpdateBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
        super(manager);
        this.tableName = tableName;
        this.columnData = new LinkedHashMap<>();
    }

    @Override
    public PreparedUpdateAction<Integer> build() {

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("UPDATE ").append(withBackQuote(getTableName())).append(" SET ");

        Iterator<String> iterator = this.columnData.keySet().iterator();
        while (iterator.hasNext()) {
            sqlBuilder.append(withBackQuote(iterator.next())).append(" = ?");
            if (iterator.hasNext()) sqlBuilder.append(" , ");
        }
        List<Object> allParams = new ArrayList<>(this.columnData.values());

        if (hasConditions()) {
            sqlBuilder.append(" ").append(buildConditionSQL());
            allParams.addAll(getConditionParams());
        }

        if (limit > 0) sqlBuilder.append(" ").append(buildLimitSQL());

        return new PreparedUpdateActionImpl<>(getManager(), Integer.class, sqlBuilder.toString(), allParams);
    }

    @Override
    public @NotNull String getTableName() {
        return tableName;
    }

    @Override
    public UpdateBuilder set(@NotNull String columnName, Object columnValue) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        this.columnData.put(columnName, columnValue);
        return this;
    }

    @Override
    public UpdateBuilder setAll(LinkedHashMap<String, Object> columnData) {
        this.columnData = columnData;
        return this;
    }

    @Override
    public UpdateBuilder setAll(@NotNull String[] columnNames, @Nullable Object[] columnValues) {
        Objects.requireNonNull(columnNames, "columnName could not be null");
        if (columnNames.length != columnValues.length) {
            throw new RuntimeException("columnNames are not match with columnValues");
        }
        LinkedHashMap<String, Object> columnData = new LinkedHashMap<>();
        for (int i = 0; i < columnNames.length; i++) {
            columnData.put(columnNames[i], columnValues[i]);
        }
        return setAll(columnData);
    }


    @Override
    protected UpdateBuilder getThis() {
        return this;
    }
}
