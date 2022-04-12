package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.SQLUpdateActionImpl;
import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.builder.TableAlterBuilder;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static cc.carm.lib.easysql.api.SQLBuilder.withBackQuote;
import static cc.carm.lib.easysql.api.SQLBuilder.withQuote;

public class TableAlterBuilderImpl extends AbstractSQLBuilder implements TableAlterBuilder {

    protected final @NotNull String tableName;

    public TableAlterBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
        super(manager);
        this.tableName = tableName;
    }

    public @NotNull String getTableName() {
        return tableName;
    }

    @Override
    public SQLAction<Long> renameTo(@NotNull String newTableName) {
        Objects.requireNonNull(newTableName, "table name could not be null");
        return new SQLUpdateActionImpl(getManager(),
                "ALTER TABLE " + withBackQuote(tableName) + " RENAME TO " + withBackQuote(newTableName)
        );
    }

    @Override
    public SQLAction<Long> changeComment(@NotNull String newTableComment) {
        Objects.requireNonNull(newTableComment, "table comment could not be null");
        return new SQLUpdateActionImpl(getManager(),
                "ALTER TABLE " + withBackQuote(getTableName()) + " COMMENT " + withQuote(newTableComment)
        );
    }

    @Override
    public SQLAction<Long> setAutoIncrementIndex(int index) {
        return new SQLUpdateActionImpl(getManager(),
                "ALTER TABLE " + withBackQuote(getTableName()) + " AUTO_INCREMENT=" + index
        );
    }

    @Override
    public SQLAction<Long> addIndex(@NotNull IndexType indexType, @Nullable String indexName,
                                    @NotNull String columnName, @NotNull String... moreColumns) {
        Objects.requireNonNull(indexType, "indexType could not be null");
        Objects.requireNonNull(columnName, "column names could not be null");
        Objects.requireNonNull(moreColumns, "column names could not be null");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " ADD "
                        + TableCreateBuilderImpl.buildIndexSettings(indexType, indexName, columnName, moreColumns)
        );
    }

    @Override
    public SQLAction<Long> dropIndex(@NotNull String indexName) {
        Objects.requireNonNull(indexName, "indexName could not be null");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " DROP INDEX " + withBackQuote(indexName)
        );
    }

    @Override
    public SQLAction<Long> dropForeignKey(@NotNull String keySymbol) {
        Objects.requireNonNull(keySymbol, "keySymbol could not be null");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " DROP FOREIGN KEY " + withBackQuote(keySymbol)
        );
    }

    @Override
    public SQLAction<Long> dropPrimaryKey() {
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " DROP PRIMARY KEY"
        );
    }

    @Override
    public SQLAction<Long> addColumn(@NotNull String columnName, @NotNull String settings, @Nullable String afterColumn) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        Objects.requireNonNull(settings, "settings could not be null");
        String orderSettings = null;
        if (afterColumn != null) {
            if (afterColumn.length() > 0) {
                orderSettings = "AFTER " + withBackQuote(afterColumn);
            } else {
                orderSettings = "FIRST";
            }
        }

        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " ADD " + withBackQuote(columnName) + " " + settings
                        + (orderSettings != null ? " " + orderSettings : "")
        );
    }

    @Override
    public SQLAction<Long> renameColumn(@NotNull String columnName, @NotNull String newName) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        Objects.requireNonNull(newName, "please specify new column name");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " RENAME COLUMN " + withBackQuote(columnName) + " TO " + withBackQuote(newName)
        );
    }

    @Override
    public SQLAction<Long> modifyColumn(@NotNull String columnName, @NotNull String settings) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        Objects.requireNonNull(settings, "please specify new column settings");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " MODIFY COLUMN " + withBackQuote(columnName) + " " + settings
        );
    }

    @Override
    public SQLAction<Long> removeColumn(@NotNull String columnName) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " DROP " + withBackQuote(columnName)
        );
    }

    @Override
    public SQLAction<Long> setColumnDefault(@NotNull String columnName, @NotNull String defaultValue) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        Objects.requireNonNull(defaultValue, "defaultValue could not be null, if you need to remove the default value, please use #removeColumnDefault().");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " ALTER " + withBackQuote(columnName) + " SET DEFAULT " + defaultValue
        );
    }

    @Override
    public SQLAction<Long> removeColumnDefault(@NotNull String columnName) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        return createAction(
                "ALTER TABLE " + withBackQuote(getTableName()) + " ALTER " + withBackQuote(columnName) + " DROP DEFAULT"
        );
    }

    private SQLUpdateActionImpl createAction(@NotNull String sql) {
        return new SQLUpdateActionImpl(getManager(), sql);
    }
}
