package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import cc.carm.lib.easysql.api.builder.*;
import cc.carm.lib.easysql.api.function.SQLHandler;
import cc.carm.lib.easysql.api.table.NamedSQLTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Optional;

/**
 * SQLTable 基于 {@link TableCreateBuilder} 构建表，用于快速创建与该表相关的操作。
 * <ul>
 *  <li>1. 调用 {@link NamedSQLTable#of(String, String[])} 方法创建一个 SQLTable 对象;</li>
 *  <li>2. 在应用初始化阶段调用 {@link NamedSQLTable#create(SQLManager)} 方法初始化 SQLTable 对象;</li>
 *  <li>3. 获取已创建的{@link NamedSQLTable} 实例，直接调用对应方法进行关于表的相关操作。</li>
 * </ul>
 *
 * @author CarmJos
 * @since 0.3.10
 */
public interface SQLTable {

    static @NotNull NamedSQLTable of(@NotNull String tableName, @Nullable SQLHandler<TableCreateBuilder> table) {
        return new NamedSQLTable(tableName) {
            @Override
            public boolean create(@NotNull SQLManager sqlManager, String tablePrefix) throws SQLException {
                if (this.manager == null) this.manager = sqlManager;
                this.tablePrefix = tablePrefix;

                TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
                if (table != null) table.accept(tableBuilder);
                return tableBuilder.build().executeFunction(l -> l > 0, false);
            }
        };
    }

    static @NotNull NamedSQLTable of(@NotNull String tableName, @NotNull String[] columns) {
        return of(tableName, columns, null);
    }

    static @NotNull NamedSQLTable of(@NotNull String tableName,
                                     @NotNull String[] columns, @Nullable String tableSettings) {
        return of(tableName, builder -> {
            builder.setColumns(columns);
            if (tableSettings != null) builder.setTableSettings(tableSettings);
        });
    }

    /**
     * 以指定的 {@link SQLManager} 实例初始化并创建该表
     *
     * @param sqlManager {@link SQLManager} 实例
     * @return 是否新创建了本表 (若已创建或创建失败则返回false)
     * @throws SQLException 当数据库返回异常时抛出
     */
    boolean create(SQLManager sqlManager) throws SQLException;

    /**
     * 得到 {@link #create(SQLManager)} 用于初始化本实例的 {@link SQLManager} 实例
     *
     * @return {@link SQLManager} 实例
     */
    @Nullable SQLManager getSQLManager();

    /**
     * 得到本表表名，不得为空。
     *
     * @return 本表表名
     */
    @NotNull String getTableName();

    default @NotNull TableQueryBuilder createQuery() {
        return Optional.ofNullable(getSQLManager()).map(this::createQuery)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull TableQueryBuilder createQuery(@NotNull SQLManager sqlManager) {
        return sqlManager.createQuery().inTable(getTableName());
    }

    default @NotNull DeleteBuilder createDelete() {
        return Optional.ofNullable(getSQLManager()).map(this::createDelete)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull DeleteBuilder createDelete(@NotNull SQLManager sqlManager) {
        return sqlManager.createDelete(getTableName());
    }

    default @NotNull UpdateBuilder createUpdate() {
        return Optional.ofNullable(getSQLManager()).map(this::createUpdate)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull UpdateBuilder createUpdate(@NotNull SQLManager sqlManager) {
        return sqlManager.createUpdate(getTableName());
    }

    default @NotNull InsertBuilder<PreparedSQLUpdateAction<Integer>> createInsert() {
        return Optional.ofNullable(getSQLManager()).map(this::createInsert)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull InsertBuilder<PreparedSQLUpdateAction<Integer>> createInsert(@NotNull SQLManager sqlManager) {
        return sqlManager.createInsert(getTableName());
    }

    default @NotNull InsertBuilder<PreparedSQLUpdateBatchAction<Integer>> createInsertBatch() {
        return Optional.ofNullable(getSQLManager()).map(this::createInsertBatch)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull InsertBuilder<PreparedSQLUpdateBatchAction<Integer>> createInsertBatch(@NotNull SQLManager sqlManager) {
        return sqlManager.createInsertBatch(getTableName());
    }

    default @NotNull ReplaceBuilder<PreparedSQLUpdateAction<Integer>> createReplace() {
        return Optional.ofNullable(getSQLManager()).map(this::createReplace)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));

    }

    default @NotNull ReplaceBuilder<PreparedSQLUpdateAction<Integer>> createReplace(@NotNull SQLManager sqlManager) {
        return Optional.ofNullable(getSQLManager()).map(this::createReplace)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull ReplaceBuilder<PreparedSQLUpdateBatchAction<Integer>> createReplaceBatch() {
        return Optional.ofNullable(getSQLManager()).map(this::createReplaceBatch)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull ReplaceBuilder<PreparedSQLUpdateBatchAction<Integer>> createReplaceBatch(@NotNull SQLManager sqlManager) {
        return sqlManager.createReplaceBatch(getTableName());
    }

    default @NotNull TableAlterBuilder alter() {
        return Optional.ofNullable(getSQLManager()).map(this::alter)
                .orElseThrow(() -> new NullPointerException("This table doesn't have a SQLManger."));
    }

    default @NotNull TableAlterBuilder alter(@NotNull SQLManager sqlManager) {
        return sqlManager.alterTable(getTableName());
    }

}
