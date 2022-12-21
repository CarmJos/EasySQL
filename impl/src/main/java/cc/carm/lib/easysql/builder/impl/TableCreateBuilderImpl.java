package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.action.SQLUpdateActionImpl;
import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.ForeignKeyRule;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cc.carm.lib.easysql.api.SQLBuilder.withBackQuote;
import static cc.carm.lib.easysql.api.SQLBuilder.withQuote;

public class TableCreateBuilderImpl extends AbstractSQLBuilder implements TableCreateBuilder {

    protected final @NotNull String tableName;
    @NotNull
    final List<String> indexes = new ArrayList<>();
    @NotNull
    final List<String> foreignKeys = new ArrayList<>();
    @NotNull List<String> columns = new ArrayList<>();
    @NotNull String tableSettings = defaultTablesSettings();
    @Nullable String tableComment;

    public TableCreateBuilderImpl(SQLManagerImpl manager, @NotNull String tableName) {
        super(manager);
        this.tableName = tableName;
    }

    protected static String buildIndexSettings(@NotNull IndexType indexType, @Nullable String indexName,
                                               @NotNull String columnName, @NotNull String... moreColumns) {
        Objects.requireNonNull(indexType, "indexType could not be null");
        Objects.requireNonNull(columnName, "column names could not be null");
        Objects.requireNonNull(moreColumns, "column names could not be null");
        StringBuilder indexBuilder = new StringBuilder();

        indexBuilder.append(indexType.getName()).append(" ");
        if (indexName != null) {
            indexBuilder.append(withBackQuote(indexName));
        }
        indexBuilder.append("(");
        indexBuilder.append(withBackQuote(columnName));

        if (moreColumns.length > 0) {
            indexBuilder.append(", ");

            for (int i = 0; i < moreColumns.length; i++) {
                indexBuilder.append(withBackQuote(moreColumns[i]));
                if (i != moreColumns.length - 1) indexBuilder.append(", ");
            }

        }

        indexBuilder.append(")");

        return indexBuilder.toString();
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
    public SQLUpdateAction<Integer> build() {
        StringBuilder createSQL = new StringBuilder();
        createSQL.append("CREATE TABLE IF NOT EXISTS ").append(withBackQuote(tableName));
        createSQL.append("(");
        createSQL.append(String.join(", ", columns));
        if (indexes.size() > 0) {
            createSQL.append(", ");
            createSQL.append(String.join(", ", indexes));
        }
        if (foreignKeys.size() > 0) {
            createSQL.append(", ");
            createSQL.append(String.join(", ", foreignKeys));
        }
        createSQL.append(") ").append(getTableSettings());

        if (tableComment != null) {
            createSQL.append(" COMMENT ").append(withQuote(tableComment));
        }

        return new SQLUpdateActionImpl<>(getManager(), Integer.class, createSQL.toString());
    }

    @Override
    public TableCreateBuilder addColumn(@NotNull String column) {
        Objects.requireNonNull(column, "column could not be null");
        this.columns.add(column);
        return this;
    }

    @Override
    public TableCreateBuilder addAutoIncrementColumn(@NotNull String columnName, @Nullable NumberType numberType,
                                                     boolean asPrimaryKey, boolean unsigned) {
        return addColumn(columnName,
                (numberType == null ? NumberType.INT : numberType).name()
                        + (unsigned ? " UNSIGNED " : " ")
                        + "NOT NULL AUTO_INCREMENT " + (asPrimaryKey ? "PRIMARY KEY" : "UNIQUE KEY")
        );
    }

    @Override
    public TableCreateBuilder setIndex(@NotNull IndexType type, @Nullable String indexName,
                                       @NotNull String columnName, @NotNull String... moreColumns) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        this.indexes.add(buildIndexSettings(type, indexName, columnName, moreColumns));
        return this;
    }

    @Override
    public TableCreateBuilder addForeignKey(@NotNull String tableColumn, @Nullable String constraintName,
                                            @NotNull String foreignTable, @NotNull String foreignColumn,
                                            @Nullable ForeignKeyRule updateRule, @Nullable ForeignKeyRule deleteRule) {
        Objects.requireNonNull(tableColumn, "tableColumn could not be null");
        Objects.requireNonNull(foreignTable, "foreignTable could not be null");
        Objects.requireNonNull(foreignColumn, "foreignColumn could not be null");

        StringBuilder keyBuilder = new StringBuilder();

        keyBuilder.append("CONSTRAINT ");
        if (constraintName == null) {
            keyBuilder.append(withBackQuote("fk_" + tableColumn + "_" + foreignTable));
        } else {
            keyBuilder.append(withBackQuote(constraintName));
        }
        keyBuilder.append(" ");
        keyBuilder.append("FOREIGN KEY (").append(withBackQuote(tableColumn)).append(") ");
        keyBuilder.append("REFERENCES ").append(withBackQuote(foreignTable)).append("(").append(withBackQuote(foreignColumn)).append(")");

        if (updateRule != null) keyBuilder.append(" ON UPDATE ").append(updateRule.getRuleName());
        if (deleteRule != null) keyBuilder.append(" ON DELETE ").append(deleteRule.getRuleName());

        this.foreignKeys.add(keyBuilder.toString());
        return this;
    }

    @Override
    public TableCreateBuilder setColumns(@NotNull String... columns) {
        Objects.requireNonNull(columns, "columns could not be null");
        this.columns = Arrays.asList(columns);
        return this;
    }

    @Override
    public TableCreateBuilder setTableSettings(@NotNull String settings) {
        Objects.requireNonNull(settings, "settings could not be null");
        this.tableSettings = settings;
        return this;
    }

    @Override
    public TableCreateBuilder setTableComment(@Nullable String comment) {
        this.tableComment = comment;
        return this;
    }

}
