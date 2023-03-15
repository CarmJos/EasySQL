package cc.carm.lib.easysql;

import cc.carm.lib.easysql.action.PreparedSQLBatchUpdateActionImpl;
import cc.carm.lib.easysql.action.PreparedSQLUpdateActionImpl;
import cc.carm.lib.easysql.action.SQLBatchUpdateActionImpl;
import cc.carm.lib.easysql.action.UpdateActionImpl;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.base.BatchUpdateAction;
import cc.carm.lib.easysql.api.action.base.PreparedBatchUpdateAction;
import cc.carm.lib.easysql.api.action.base.PreparedUpdateAction;
import cc.carm.lib.easysql.api.action.update.PreparedSQLBatchUpdateAction;
import cc.carm.lib.easysql.api.action.update.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.builder.*;
import cc.carm.lib.easysql.api.enums.IsolationLevel;
import cc.carm.lib.easysql.api.function.SQLBiFunction;
import cc.carm.lib.easysql.api.function.SQLDebugHandler;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.transaction.SQLTransaction;
import cc.carm.lib.easysql.builder.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class SQLManagerImpl extends SQLSourceImpl implements SQLManager {

    public SQLManagerImpl(@NotNull DataSource dataSource) {
        super(dataSource);
    }

    public SQLManagerImpl(@NotNull DataSource dataSource, @Nullable String name) {
        super(dataSource, name);
    }

    public SQLManagerImpl(@NotNull DataSource dataSource, @NotNull Logger logger) {
        super(dataSource, logger);
    }

    public SQLManagerImpl(@NotNull DataSource dataSource, @NotNull Logger logger, @Nullable String name) {
        super(dataSource, logger, name);
    }

    public SQLManagerImpl(@NotNull DataSource dataSource, @NotNull Logger logger, @NotNull ExecutorService executorPool,
                          @NotNull Supplier<Boolean> debugMode, @NotNull SQLDebugHandler debugHandler,
                          @NotNull SQLExceptionHandler exceptionHandler) {
        super(dataSource, logger, executorPool, debugMode, debugHandler, exceptionHandler);
    }

    @Override
    public Integer executeSQL(String sql) {
        return new UpdateActionImpl<>(this, Integer.class, sql).execute(null);
    }

    @Override
    public Integer executeSQL(String sql, Object[] params) {
        return new PreparedSQLUpdateActionImpl<>(this, Integer.class, sql, params).execute(null);
    }

    @Override
    public List<Integer> executeSQLBatch(String sql, Iterable<Object[]> paramsBatch) {
        return new PreparedSQLBatchUpdateActionImpl<>(this, Integer.class, sql).allValues(paramsBatch).execute(null);
    }

    @Override
    public List<Integer> executeSQLBatch(@NotNull String sql, String... moreSQL) {
        BatchUpdateAction action = new SQLBatchUpdateActionImpl(this, sql);
        if (moreSQL != null && moreSQL.length > 0) {
            Arrays.stream(moreSQL).forEach(action::addBatch);
        }
        return action.execute(null);
    }

    @Override
    public @Nullable List<Integer> executeSQLBatch(@NotNull Iterable<String> sqlBatch) {
        Iterator<String> iterator = sqlBatch.iterator();
        if (!iterator.hasNext()) return null; // PLEASE GIVE IT SOMETHING

        BatchUpdateAction action = new SQLBatchUpdateActionImpl(this, iterator.next());
        while (iterator.hasNext()) {
            action.addBatch(iterator.next());
        }

        return action.execute(null);
    }

    @Override
    public @NotNull SQLTransaction createTransaction(@Nullable IsolationLevel level) {
        return null;
    }

    @Override
    public <R> CompletableFuture<R> fetchMetadata(@NotNull SQLBiFunction<DatabaseMetaData, Connection, R> reader) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = getConnection()) {
                return reader.apply(conn.getMetaData(), conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }, this.executorPool);
    }

    @Override
    public <R> CompletableFuture<R> fetchMetadata(@NotNull SQLBiFunction<DatabaseMetaData, Connection, ResultSet> supplier,
                                                  @NotNull SQLFunction<@NotNull ResultSet, R> reader) {
        return fetchMetadata((meta, conn) -> {
            try (ResultSet rs = supplier.apply(conn.getMetaData(), conn)) {
                if (rs == null) throw new NullPointerException("Metadata返回的ResultSet为null。");
                else return reader.apply(rs);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public TableCreateBuilder createTable(@NotNull String tableName) {
        return new TableCreateBuilderImpl(this, tableName);
    }

    @Override
    public TableAlterBuilder alterTable(@NotNull String tableName) {
        return new TableAlterBuilderImpl(this, tableName);
    }

    @Override
    public TableMetadataBuilder fetchTableMetadata(@NotNull String tablePattern) {
        return new TableMetadataBuilderImpl(this, tablePattern);
    }

    @Override
    public QueryBuilder createQuery() {
        return new QueryBuilderImpl(this);
    }

    @Override
    public InsertBuilder<PreparedBatchUpdateAction<Integer>> createInsertBatch(@NotNull String tableName) {
        return new InsertBuilderImpl<PreparedBatchUpdateAction<Integer>>(this, tableName) {
            @Override
            public PreparedBatchUpdateAction<Integer> columns(List<String> columnNames) {
                return new PreparedSQLBatchUpdateActionImpl<>(getManager(), Integer.class, buildSQL(getTableName(), columnNames));
            }
        };
    }

    @Override
    public InsertBuilder<PreparedUpdateAction<Integer>> insertInto(@NotNull String tableName) {
        return new InsertBuilderImpl<PreparedUpdateAction<Integer>>(this, tableName) {
            @Override
            public PreparedUpdateAction<Integer> columns(List<String> columnNames) {
                return new PreparedSQLUpdateActionImpl<>(getManager(), Integer.class, buildSQL(getTableName(), columnNames));
            }
        };
    }

    @Override
    public @NotNull InsertBuilder<PreparedSQLBatchUpdateAction.Advanced<Integer>> insertBatchInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull ReplaceBuilder<PreparedSQLUpdateAction.Advanced<Integer>> replaceInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull ReplaceBuilder<PreparedSQLBatchUpdateAction.Advanced<Integer>> replaceBatchInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull UpdateBuilder<PreparedSQLUpdateAction.Advanced<Integer>> updateInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull DeleteBuilder<PreparedSQLUpdateAction.Advanced<Integer>> deleteFrom(@NotNull String tableName) {
        return null;
    }

    @Override
    public ReplaceBuilder<PreparedBatchUpdateAction<Integer>> createReplaceBatch(@NotNull String tableName) {
        return new ReplaceBuilderImpl<PreparedBatchUpdateAction<Integer>>(this, tableName) {
            @Override
            public PreparedBatchUpdateAction<Integer> columns(List<String> columnNames) {
                return new PreparedSQLBatchUpdateActionImpl<>(getManager(), Integer.class, buildSQL(getTableName(), columnNames));
            }
        };
    }

    @Override
    public ReplaceBuilder<PreparedUpdateAction<Integer>> createReplace(@NotNull String tableName) {
        return new ReplaceBuilderImpl<PreparedUpdateAction<Integer>>(this, tableName) {
            @Override
            public PreparedUpdateAction<Integer> columns(List<String> columnNames) {
                return new PreparedSQLUpdateActionImpl<>(getManager(), Integer.class, buildSQL(getTableName(), columnNames));
            }
        };
    }

    @Override
    public UpdateBuilder createUpdate(@NotNull String tableName) {
        return new UpdateBuilderImpl(this, tableName);
    }

    @Override
    public DeleteBuilder createDelete(@NotNull String tableName) {
        return new DeleteBuilderImpl(this, tableName);
    }


}

