package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import org.jetbrains.annotations.NotNull;

public interface TableQueryBuilder extends ConditionalBuilder<PreparedQueryAction> {

	@NotNull String getTableName();

	TableQueryBuilder selectColumns(@NotNull String... columnNames);

}
