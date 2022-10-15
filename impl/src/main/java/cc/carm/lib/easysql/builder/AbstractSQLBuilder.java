package cc.carm.lib.easysql.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractSQLBuilder implements SQLBuilder {

    @NotNull
    final SQLManagerImpl sqlManager;

    public AbstractSQLBuilder(@NotNull SQLManagerImpl manager) {
        Objects.requireNonNull(manager, "SQLManager must not be null");
        this.sqlManager = manager;
    }

    @Override
    public @NotNull SQLManagerImpl getManager() {
        return this.sqlManager;
    }
}
