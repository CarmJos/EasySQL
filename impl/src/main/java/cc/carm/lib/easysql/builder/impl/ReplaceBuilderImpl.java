package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.api.builder.ReplaceBuilder;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ReplaceBuilderImpl<T extends SQLAction<?>>
        extends AbstractSQLBuilder implements ReplaceBuilder<T> {

    protected final @NotNull String tableName;

    public ReplaceBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tableName) {
        super(manager);
        this.tableName = tableName;
    }

    protected static String buildSQL(String tableName, List<String> columnNames) {
        return InsertBuilderImpl.buildSQL("REPLACE INTO", tableName, columnNames);
    }

    @Override
    public @NotNull String getTableName() {
        return tableName;
    }
}
