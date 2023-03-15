package cc.carm.lib.easysql;

import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.SQLSource;
import cc.carm.lib.easysql.api.function.SQLDebugHandler;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class SQLSourceImpl implements SQLSource {

    protected final @NotNull Logger logger;

    protected final @NotNull DataSource dataSource;
    protected @NotNull ExecutorService executorPool;

    protected @NotNull Supplier<Boolean> debugMode = () -> Boolean.FALSE;
    protected @NotNull SQLDebugHandler debugHandler;
    protected @NotNull SQLExceptionHandler exceptionHandler;

    protected final ConcurrentHashMap<UUID, SQLQuery> activeQueries = new ConcurrentHashMap<>();

    public SQLSourceImpl(@NotNull DataSource dataSource) {
        this(dataSource, (String) null);
    }

    public SQLSourceImpl(@NotNull DataSource dataSource, @Nullable String name) {
        this(dataSource, LoggerFactory.getLogger(SQLManagerImpl.class), name);
    }

    public SQLSourceImpl(@NotNull DataSource dataSource, @NotNull Logger logger) {
        this(dataSource, logger, null);
    }

    public SQLSourceImpl(@NotNull DataSource dataSource, @NotNull Logger logger, @Nullable String name) {
        this(
                dataSource, logger,
                SQLSource.defaultExecutorPool("SQLSource" + (name != null ? "#" + name : "")),
                () -> false, SQLDebugHandler.defaultHandler(logger), SQLExceptionHandler.detailed(logger)
        );
    }

    public SQLSourceImpl(@NotNull DataSource dataSource, @NotNull Logger logger, @NotNull ExecutorService executorPool,
                         @NotNull Supplier<Boolean> debugMode, @NotNull SQLDebugHandler debugHandler,
                         @NotNull SQLExceptionHandler exceptionHandler) {
        this.logger = logger;
        this.dataSource = dataSource;
        this.executorPool = executorPool;
        this.exceptionHandler = exceptionHandler;
        this.debugMode = debugMode;
        this.debugHandler = debugHandler;
    }

    @Override
    public @NotNull Map<UUID, SQLQuery> getActiveQueries() {
        return this.activeQueries;
    }

    @Override
    public @NotNull DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public @NotNull Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.logger;
    }

    @Override
    public @NotNull ExecutorService getExecutorPool() {
        return this.executorPool;
    }

    @Override
    public void setExecutorPool(@NotNull ExecutorService executorPool) {
        this.executorPool = executorPool;
    }

    @Override
    public boolean isDebugMode() {
        return this.debugMode.get();
    }

    @Override
    public void setDebugMode(@NotNull Supplier<@NotNull Boolean> debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public @NotNull SQLDebugHandler getDebugHandler() {
        return this.debugHandler;
    }

    @Override
    public void setDebugHandler(@NotNull SQLDebugHandler debugHandler) {
        this.debugHandler = debugHandler;
    }

    @Override
    public @NotNull SQLExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    @Override
    public void setExceptionHandler(@Nullable SQLExceptionHandler handler) {
        if (handler == null) this.exceptionHandler = SQLExceptionHandler.detailed(getLogger());
        else this.exceptionHandler = handler;
    }

}
