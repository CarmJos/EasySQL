package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import org.jetbrains.annotations.NotNull;

public interface QueryBuilder extends SQLBuilder {

	QueryAction withSQL(@NotNull String sql);

	PreparedQueryAction withPreparedSQL(@NotNull String sql);

	TableQueryBuilder inTable(@NotNull String tableName);

}
