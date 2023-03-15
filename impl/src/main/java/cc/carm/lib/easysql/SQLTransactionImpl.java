package cc.carm.lib.easysql;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.PreparedSQLQueryAction;
import cc.carm.lib.easysql.api.action.query.SQLQueryAction;
import cc.carm.lib.easysql.api.action.update.PreparedSQLBatchUpdateAction;
import cc.carm.lib.easysql.api.action.update.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.builder.*;
import cc.carm.lib.easysql.api.enums.IsolationLevel;
import cc.carm.lib.easysql.api.transaction.SQLSavepoint;
import cc.carm.lib.easysql.api.transaction.SQLTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SQLTransactionImpl implements SQLTransaction {
    @Override
    public @NotNull SQLManager getManager() {
        return null;
    }

    @Override
    public @NotNull IsolationLevel getIsolationLevel() {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public @NotNull SQLSavepoint savepoint(@NotNull String name) {
        return null;
    }

    @Override
    public void rollback(@Nullable SQLSavepoint savepoint) {

    }

    @Override
    public @Nullable Integer executeSQL(String sql) {
        return null;
    }

    @Override
    public @Nullable Integer executeSQL(String sql, Object[] params) {
        return null;
    }

    @Override
    public @Nullable List<Integer> executeSQLBatch(String sql, Iterable<Object[]> paramsBatch) {
        return null;
    }

    @Override
    public @Nullable List<Integer> executeSQLBatch(@NotNull String sql, String... moreSQL) {
        return null;
    }

    @Override
    public @Nullable List<Integer> executeSQLBatch(@NotNull Iterable<String> sqlBatch) {
        return null;
    }

    @Override
    public @NotNull QueryBuilder<SQLQueryAction.Base, PreparedSQLQueryAction.Base> createQuery() {
        return null;
    }

    @Override
    public @NotNull InsertBuilder<PreparedSQLUpdateAction.Base<Integer>> insertInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull InsertBuilder<PreparedSQLBatchUpdateAction.Base<Integer>> insertBatchInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull ReplaceBuilder<PreparedSQLUpdateAction.Base<Integer>> replaceInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull ReplaceBuilder<PreparedSQLBatchUpdateAction.Base<Integer>> replaceBatchInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull UpdateBuilder<PreparedSQLUpdateAction.Base<Integer>> updateInto(@NotNull String tableName) {
        return null;
    }

    @Override
    public @NotNull DeleteBuilder<PreparedSQLUpdateAction.Base<Integer>> deleteFrom(@NotNull String tableName) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
    
}
