package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.SQLUpdateActionImpl;
import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.ForeignKeyRule;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableCreateBuilderImpl extends AbstractSQLBuilder implements TableCreateBuilder {

	protected final @NotNull String tableName;

	@NotNull List<String> columns = new ArrayList<>();
	@NotNull List<String> indexes = new ArrayList<>();
	@NotNull List<String> foreignKeys = new ArrayList<>();

	@NotNull String tableSettings = defaultTablesSettings();
	@Nullable String tableComment;

	public TableCreateBuilderImpl(SQLManagerImpl manager, @NotNull String tableName) {
		super(manager);
		this.tableName = tableName;
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

		for (int i = 0; i < indexes.size(); i++) {
			createSQL.append(indexes.get(i));
			if (i != indexes.size() - 1) createSQL.append(", ");
		}

		for (int i = 0; i < foreignKeys.size(); i++) {
			createSQL.append(foreignKeys.get(i));
			if (i != foreignKeys.size() - 1) createSQL.append(", ");
		}

		createSQL.append(") ").append(getTableSettings());

		if (tableComment != null) {
			createSQL.append(" COMMENT '").append(tableComment).append("'");
		}

		return new SQLUpdateActionImpl(getManager(), createSQL.toString());
	}

	@Override
	public TableCreateBuilder addColumn(@NotNull String column) {
		this.columns.add(column);
		return this;
	}

	@Override
	public TableCreateBuilder setIndex(@NotNull IndexType type, @Nullable String indexName,
									   @NotNull String columnName, @NotNull String... moreColumns) {
		this.indexes.add(buildIndexSettings(type, indexName, columnName, moreColumns));
		return this;
	}

	@Override
	public TableCreateBuilder addForeignKey(@NotNull String tableColumn, @Nullable String constraintName,
											@NotNull String foreignTable, @NotNull String foreignColumn,
											@Nullable ForeignKeyRule updateRule, @Nullable ForeignKeyRule deleteRule) {
		StringBuilder keyBuilder = new StringBuilder();

		keyBuilder.append("CONSTRAINT ");
		if (constraintName == null) {
			keyBuilder.append("`").append("fk_").append(tableColumn).append("_").append(foreignTable).append("`");
		} else {
			keyBuilder.append("`").append(constraintName).append("`");
		}
		keyBuilder.append(" ");
		keyBuilder.append("FOREIGN KEY (`").append(tableColumn).append("`) ");
		keyBuilder.append("REFERENCES `").append(foreignTable).append("`(`").append(foreignColumn).append("`)");

		if (updateRule != null) keyBuilder.append(" ON UPDATE ").append(updateRule.getRuleName());
		if (deleteRule != null) keyBuilder.append(" ON DELETE ").append(deleteRule.getRuleName());

		this.foreignKeys.add(keyBuilder.toString());
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

	@Override
	public TableCreateBuilder setTableComment(@Nullable String comment) {
		this.tableComment = comment;
		return this;
	}

	protected static String buildIndexSettings(@NotNull IndexType indexType, @Nullable String indexName,
											 @NotNull String columnName, @NotNull String... moreColumns) {

		StringBuilder indexBuilder = new StringBuilder();

		indexBuilder.append(indexType.getName()).append(" ");
		if (indexName != null) {
			indexBuilder.append("`").append(indexName).append("`");
		}
		indexBuilder.append("(");
		indexBuilder.append("`").append(columnName).append("`");

		if (moreColumns.length > 0) {
			indexBuilder.append(", ");

			for (int i = 0; i < moreColumns.length; i++) {
				indexBuilder.append(moreColumns[i]);
				if (i != moreColumns.length - 1) indexBuilder.append(", ");
			}

		}

		indexBuilder.append(")");

		return indexBuilder.toString();
	}

}
