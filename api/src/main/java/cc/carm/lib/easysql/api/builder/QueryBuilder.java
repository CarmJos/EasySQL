package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import org.jetbrains.annotations.NotNull;

public interface QueryBuilder extends SQLBuilder {

    /**
     * 通过一条 SQL语句创建查询。
     * 该方法使用 Statement 实现，请注意SQL注入风险！
     *
     * @param sql SQL语句
     * @return {@link QueryAction}
     * @deprecated 存在SQL注入风险，建议使用 {@link QueryBuilder#withPreparedSQL(String)}
     */
    @Deprecated
    QueryAction withSQL(@NotNull String sql);

    /**
     * 通过一条 SQL语句创建预查询
     *
     * @param sql SQL语句
     * @return {@link PreparedQueryAction}
     */
    PreparedQueryAction withPreparedSQL(@NotNull String sql);

    /**
     * 创建表查询
     *
     * @param tableName 表名
     * @return {@link TableQueryBuilder}
     */
    TableQueryBuilder fromTable(@NotNull String tableName);

}
