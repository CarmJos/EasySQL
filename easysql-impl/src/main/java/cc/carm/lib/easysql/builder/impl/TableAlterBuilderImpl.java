package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.SQLUpdateActionImpl;
import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.builder.TableAlertBuilder;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TableAlterBuilderImpl extends AbstractSQLBuilder implements TableAlertBuilder {

	protected final @NotNull String tableName;

	public TableAlterBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
		super(manager);
		this.tableName = tableName;
	}

	public @NotNull String getTableName() {
		return tableName;
	}

	@Override
	public SQLAction<Integer> renameTo(@NotNull String newTableName) {
		return new SQLUpdateActionImpl(getManager(),
				"ALERT TABLE `" + getTableName() + "` RENAME TO `" + newTableName + "`"
		);
	}

	@Override
	public SQLAction<Integer> changeComment(@NotNull String newTableComment) {
		return new SQLUpdateActionImpl(getManager(),
				"ALERT TABLE `" + getTableName() + "` COMMENT '" + newTableComment + "'"
		);
	}

	@Override
	public SQLAction<Integer> setAutoIncrementIndex(int index) {
		return new SQLUpdateActionImpl(getManager(),
				"ALERT TABLE `" + getTableName() + "` AUTO_INCREMENT=" + index
		);
	}

	@Override
	public SQLAction<Integer> addIndex(@NotNull IndexType indexType, @NotNull String indexName, @NotNull String columnName, @NotNull String... moreColumns) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` ADD "
						+ TableCreateBuilderImpl.buildIndexSettings(indexType, indexName, columnName, moreColumns)
		);
	}

	@Override
	public SQLAction<Integer> dropIndex(@NotNull String indexName) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` DROP INDEX `" + indexName + "`"
		);
	}

	@Override
	public SQLAction<Integer> dropForeignKey(@NotNull String keySymbol) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` DROP FOREIGN KEY `" + keySymbol + "`"
		);
	}

	@Override
	public SQLAction<Integer> dropPrimaryKey() {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` DROP PRIMARY KEY"
		);
	}

	@Override
	public SQLAction<Integer> addColumn(@NotNull String columnName, @NotNull String settings, @Nullable String afterColumn) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` ADD `" + columnName + "` " + settings
		);
	}

	@Override
	public SQLAction<Integer> renameColumn(@NotNull String columnName, @NotNull String newName) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` RENAME COLUMN `" + columnName + "` TO `" + newName + "`"
		);
	}

	@Override
	public SQLAction<Integer> modifyColumn(@NotNull String columnName, @NotNull String settings) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` MODIFY COLUMN `" + columnName + "` " + settings
		);
	}

	@Override
	public SQLAction<Integer> removeColumn(@NotNull String columnName) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` DROP `" + columnName + "`"
		);
	}

	@Override
	public SQLAction<Integer> setColumnDefault(@NotNull String columnName, @NotNull String defaultValue) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` ALERT `" + columnName + "` SET DEFAULT " + defaultValue
		);
	}

	@Override
	public SQLAction<Integer> removeColumnDefault(@NotNull String columnName) {
		return createAction(
				"ALERT TABLE `" + getTableName() + "` ALERT `" + columnName + "` DROP DEFAULT"
		);
	}

	private SQLUpdateActionImpl createAction(@NotNull String sql) {
		return new SQLUpdateActionImpl(getManager(), sql);
	}
}
