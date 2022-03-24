package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLHandler;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractSQLAction<T> implements SQLAction<T> {

    protected final @NotNull String sqlContent;
    private final @NotNull SQLManagerImpl sqlManager;
    private final @NotNull UUID uuid;
    private final long createTime;

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql) {
        this(manager, sql, System.currentTimeMillis());
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql, @NotNull UUID uuid) {
        this(manager, sql, uuid, System.currentTimeMillis());
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql, long createTime) {
        this(manager, sql, UUID.randomUUID(), createTime);
    }

    public AbstractSQLAction(@NotNull SQLManagerImpl manager, @NotNull String sql,
                             @NotNull UUID uuid, long createTime) {
        Objects.requireNonNull(manager);
        Objects.requireNonNull(sql);
        Objects.requireNonNull(uuid);
        this.sqlManager = manager;
        this.sqlContent = sql;
        this.uuid = uuid;
        this.createTime = createTime;
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
    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public @NotNull String getSQLContent() {
        return this.sqlContent.trim();
    }

    @Override
    public @NotNull SQLManagerImpl getManager() {
        return this.sqlManager;
    }

    protected void outputDebugMessage() {
        getManager().debug("# " + getShortID() + " -> { " + getSQLContent() + " }");
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

}
