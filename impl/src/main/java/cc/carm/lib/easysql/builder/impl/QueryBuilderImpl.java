package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.query.PreparedQueryActionImpl;
import cc.carm.lib.easysql.action.query.QueryActionImpl;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import cc.carm.lib.easysql.api.builder.QueryBuilder;
import cc.carm.lib.easysql.api.builder.TableQueryBuilder;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
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
        return new QueryActionImpl(getManager(), sql);
    }

    @Override
    public PreparedQueryAction withPreparedSQL(@NotNull String sql) {
        Objects.requireNonNull(sql, "sql could not be null");
        return new PreparedQueryActionImpl(getManager(), sql);
    }

    @Override
    public TableQueryBuilder inTable(@NotNull String tableName) {
        Objects.requireNonNull(tableName, "tableName could not be null");
        return new TableQueryBuilderImpl(getManager(), tableName);
    }

}
