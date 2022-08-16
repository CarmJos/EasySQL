package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.api.builder.TableMetadataBuilder;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class TableMetadataBuilderImpl
        extends AbstractSQLBuilder
        implements TableMetadataBuilder {

    protected final @NotNull String tablePattern;

    public TableMetadataBuilderImpl(@NotNull SQLManagerImpl manager, @NotNull String tablePattern) {
        super(manager);
        this.tablePattern = tablePattern;
    }

    @Override
    public CompletableFuture<Boolean> validateExist() {
        return validate((meta) -> meta.getTables(null, null, tablePattern, new String[]{"TABLE"}));
    }

    @Override
    public CompletableFuture<Boolean> isColumnExists(String columnPattern) {
        return validate((meta) -> meta.getColumns(null, null, tablePattern, columnPattern));
    }

    /**
     * fast validate EXISTS.
     *
     * @param supplier supplier to get result set
     * @return result future
     */
    private CompletableFuture<Boolean> validate(SQLFunction<DatabaseMetaData, ResultSet> supplier) {
        return getManager().fetchMetadata(supplier, ResultSet::next);
    }

}
