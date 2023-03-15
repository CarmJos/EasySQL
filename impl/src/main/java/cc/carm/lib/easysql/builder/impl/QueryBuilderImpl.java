package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.query.PreparedSQLQueryActionImpl;
import cc.carm.lib.easysql.action.query.SQLQueryActionImpl;
import cc.carm.lib.easysql.api.action.base.PreparedQueryAction;
import cc.carm.lib.easysql.api.action.base.QueryAction;
import cc.carm.lib.easysql.api.builder.QueryBuilder;
import cc.carm.lib.easysql.api.builder.TableQueryBuilder;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class QueryBuilderImpl extends AbstractSQLBuilder implements QueryBuilder {
    public QueryBuilderImpl(@NotNull SQLManagerImpl manager) {
        super(manager);
    }

    @Override
    @Deprecated
    public QueryAction withSQL(@NotNull String sql) {
        Objects.requireNonNull(sql, "sql could not be null");
        return new SQLQueryActionImpl(getManager(), sql);
    }

    @Override
    public PreparedQueryAction withPreparedSQL(@NotNull String sql) {
        Objects.requireNonNull(sql, "sql could not be null");
        return new PreparedSQLQueryActionImpl(getManager(), sql);
    }

    @Override
    public TableQueryBuilder fromTable(@NotNull String tableName) {
        Objects.requireNonNull(tableName, "tableName could not be null");
        return new TableQueryBuilderImpl(getManager(), tableName);
    }

}
