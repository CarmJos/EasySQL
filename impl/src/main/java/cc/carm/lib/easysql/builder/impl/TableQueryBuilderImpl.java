package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.query.PreparedSQLQueryActionImpl;
import cc.carm.lib.easysql.api.action.base.PreparedQueryAction;
import cc.carm.lib.easysql.api.builder.TableQueryBuilder;
import cc.carm.lib.easysql.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static cc.carm.lib.easysql.api.SQLBuilder.withBackQuote;

public class TableQueryBuilderImpl
        extends AbstractConditionalBuilder<TableQueryBuilder, PreparedQueryAction>
        implements TableQueryBuilder {


    protected final @NotNull String tableName;

    String[] columns;

    @Nullable String orderBy;

    int[] pageLimit;

    public TableQueryBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
        super(manager);
        this.tableName = tableName;
    }

    @Override
    public PreparedSQLQueryActionImpl build() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT").append(" ");
        if (columns == null || columns.length < 1) {
            sqlBuilder.append("*");
        } else {
            for (int i = 0; i < columns.length; i++) {
                String name = columns[i];
                sqlBuilder.append(withBackQuote(name));
                if (i != columns.length - 1) {
                    sqlBuilder.append(",");
                }
            }
        }

        sqlBuilder.append(" ").append("FROM").append(" ").append(withBackQuote(tableName));

        if (hasConditions()) sqlBuilder.append(" ").append(buildConditionSQL());

        if (orderBy != null) sqlBuilder.append(" ").append(orderBy);

        if (pageLimit != null && pageLimit.length == 2) {
            sqlBuilder.append(" LIMIT ").append(pageLimit[0]).append(",").append(pageLimit[1]);
        } else if (limit > 0) {
            sqlBuilder.append(" ").append(buildLimitSQL());
        }


        return new PreparedSQLQueryActionImpl(getManager(), sqlBuilder.toString())
                .setParams(hasConditionParams() ? getConditionParams() : null);
    }

    @Override
    public @NotNull String getTableName() {
        return tableName;
    }

    @Override
    public TableQueryBuilderImpl select(@NotNull String... columnNames) {
        Objects.requireNonNull(columnNames, "columnNames could not be null");
        this.columns = columnNames;
        return this;
    }

    @Override
    public TableQueryBuilder orderBy(@NotNull String columnName, boolean asc) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        this.orderBy = "ORDER BY " + withBackQuote(columnName) + " " + (asc ? "ASC" : "DESC");
        return this;
    }

    @Override
    public TableQueryBuilder limit(int start, int end) {
        this.pageLimit = new int[]{start, end};
        return this;
    }

    @Override
    protected TableQueryBuilderImpl getThis() {
        return this;
    }
}
