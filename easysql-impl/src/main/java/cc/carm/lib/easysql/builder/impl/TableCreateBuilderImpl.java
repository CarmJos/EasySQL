package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.SQLUpdateActionImpl;
import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableCreateBuilderImpl extends AbstractSQLBuilder implements TableCreateBuilder {

	String tableName;

	List<String> columns;

	String tableSettings;

	public TableCreateBuilderImpl(SQLManagerImpl manager, String tableName) {
		super(manager);
		this.tableName = tableName;
		this.columns = new ArrayList<>();
		defaultTablesSettings();
	}

	@Override
	public @NotNull String getTableName() {
		return this.tableName;
	}

	@Override
	public @NotNull String getTableSettings() {
		return this.tableSettings;
	}

	@Override
	public SQLUpdateAction build() {
		StringBuilder createSQL = new StringBuilder();
		createSQL.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("`");
		createSQL.append("(");
		for (int i = 0; i < columns.size(); i++) {
			createSQL.append(columns.get(i));
			if (i != columns.size() - 1) createSQL.append(", ");
		}
		createSQL.append(") ").append(tableSettings);

		return new SQLUpdateActionImpl(getManager(), createSQL.toString());
	}

	@Override
	public TableCreateBuilder setTableName(@NotNull String tableName) {
		this.tableName = tableName;
		return this;
	}

	@Override
	public TableCreateBuilder addColumn(@NotNull String column) {
		this.columns.add(column);
		return this;
	}

	@Override
	public TableCreateBuilder setColumns(@NotNull String[] columns) {
		this.columns = Arrays.asList(columns);
		return this;
	}

	@Override
	public TableCreateBuilder setTableSettings(@NotNull String settings) {
		this.tableSettings = settings;
		return this;
	}

}
