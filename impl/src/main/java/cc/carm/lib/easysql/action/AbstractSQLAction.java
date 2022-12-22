package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.function.SQLHandler;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSQLAction<T> implements SQLAction<T> {

    protected final @NotNull String sqlContent;
    private final @NotNull SQLManagerImpl sqlManager;
    private final @NotNull UUID uuid;
    private final long createNanoTime;

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql) {
        this(manager, sql, System.nanoTime());
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql, @NotNull UUID uuid) {
        this(manager, sql, uuid, System.nanoTime());
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql, long createNanoTime) {
        this(manager, sql, UUID.randomUUID(), createNanoTime);
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql,
                             @NotNull UUID uuid, long createNanoTime) {
        Objects.requireNonNull(manager);
        Objects.requireNonNull(sql);
        Objects.requireNonNull(uuid);
        this.sqlManager = manager;
        this.sqlContent = sql;
        this.uuid = uuid;
        this.createNanoTime = createNanoTime;
    }


    @Override
    public @NotNull UUID getActionUUID() {
        return this.uuid;
    }

    @Override
    public @NotNull String getShortID() {
        return getActionUUID().toString().substring(0, 8);
    }

    @Override
    public long getCreateTime(TimeUnit unit) {
        return unit.convert(createNanoTime, TimeUnit.NANOSECONDS);
    }

    @Override
    public @NotNull String getSQLContent() {
        return this.sqlContent.trim();
    }

    @Override
    public @NotNull SQLManagerImpl getManager() {
        return this.sqlManager;
    }

    protected void debugMessage(List<Object[]> params) {
        if (getManager().isDebugMode()) {
            try {
                getManager().getDebugHandler().beforeExecute(this, params);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    @SuppressWarnings("FutureReturnValueIgnored")
    public void executeAsync(SQLHandler<T> success, SQLExceptionHandler failure) {
        getManager().getExecutorPool().submit(() -> {
            try {
                T returnedValue = execute();
                if (success != null) success.accept(returnedValue);
            } catch (SQLException e) {
                handleException(failure, e);
            }
        });
    }

    @Override
    public @NotNull <R> CompletableFuture<R> executeFuture(@NotNull SQLFunction<T, R> handler) {
        CompletableFuture<R> future = new CompletableFuture<>();
        executeAsync((t -> future.complete(handler.apply(t))), (e, q) -> future.completeExceptionally(e));
        return future;
    }
}
