package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.api.action.query.PreparedSQLQueryAction;
import cc.carm.lib.easysql.api.action.query.SQLQueryAction;
import org.jetbrains.annotations.NotNull;

public interface QueryBuilder<Q extends SQLQueryAction<Q>, P extends PreparedSQLQueryAction<P>> extends SQLBuilder {

    /**
     * 通过一条 SQL语句创建查询。
     * 该方法使用 Statement 实现，请注意SQL注入风险！
     *
     * @param sql SQL语句
     * @return {@link SQLQueryAction}
     * @deprecated 存在SQL注入风险，建议使用 {@link QueryBuilder#withPreparedSQL(String)}
     */
    @Deprecated
    @NotNull Q withSQL(@NotNull String sql);

    /**
     * 通过一条 SQL语句创建预查询
     *
     * @param sql SQL语句
     * @return {@link PreparedSQLQueryAction}
     */
    @NotNull P withPreparedSQL(@NotNull String sql);

    /**
     * 创建表查询
     *
     * @param tableName 表名
     * @return {@link TableQueryBuilder}
     */
    @NotNull TableQueryBuilder<P> fromTable(@NotNull String tableName);

}
