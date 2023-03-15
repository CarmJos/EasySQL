package cc.carm.lib.easysql.builder.impl;

import cc.carm.lib.easysql.api.builder.TableMetadataBuilder;
import cc.carm.lib.easysql.api.function.SQLBiFunction;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.builder.AbstractSQLBuilder;
import cc.carm.lib.easysql.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
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
        return validate((meta, conn) -> meta.getTables(conn.getCatalog(), conn.getSchema(), tablePattern.toUpperCase(), new String[]{"TABLE"}));
    }

    @Override
    public <R> CompletableFuture<R> fetchColumns(@Nullable String columnPattern,
                                                 @NotNull SQLFunction<ResultSet, R> reader) {
        return getManager().fetchMetadata((meta, conn) -> meta.getColumns(
                conn.getCatalog(), conn.getSchema(), tablePattern.toUpperCase(),
                Optional.ofNullable(columnPattern).map(String::toUpperCase).orElse("%")
        ), reader);
    }

    @Override
    public CompletableFuture<Boolean> isColumnExists(@NotNull String columnPattern) {
        return validate((meta, conn) -> meta.getColumns(
                conn.getCatalog(), conn.getSchema(),
                tablePattern.toUpperCase(), columnPattern.toUpperCase()
        ));
    }

    @Override
    public CompletableFuture<Set<String>> listColumns(@Nullable String columnPattern) {
        return fetchColumns(columnPattern, (rs) -> {
            Set<String> data = new LinkedHashSet<>();
            while (rs.next()) {
                data.add(rs.getString("COLUMN_NAME"));
            }
            return Collections.unmodifiableSet(data);
        });
    }

    /**
     * fast validate EXISTS.
     *
     * @param supplier supplier to get result set
     * @return result future
     */
    private CompletableFuture<Boolean> validate(SQLBiFunction<DatabaseMetaData, Connection, ResultSet> supplier) {
        return getManager().fetchMetadata(supplier, ResultSet::next);
    }

}
