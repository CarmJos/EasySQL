package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import org.jetbrains.annotations.NotNull;

public interface TableCreateBuilder extends SQLBuilder {

	@NotNull String getTableName();

	@NotNull String getTableSettings();

	TableCreateBuilder setTableSettings(@NotNull String settings);

	SQLUpdateAction build();

	default TableCreateBuilder addColumn(@NotNull String columnName, @NotNull String settings) {
		return addColumn("`" + columnName + "` " + settings);
	}

	TableCreateBuilder addColumn(@NotNull String column);

	TableCreateBuilder setColumns(@NotNull String... columns);

	default TableCreateBuilder defaultTablesSettings() {
		return setTableSettings("ENGINE=InnoDB DEFAULT CHARSET=utf8");
	}


}
