package cc.carm.lib.easysql.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSQLBuilder implements SQLBuilder {

    @NotNull SQLManagerImpl sqlManager;

    public AbstractSQLBuilder(@NotNull SQLManagerImpl manager) {
        this.sqlManager = manager;
    }

    @Override
    public @NotNull SQLManagerImpl getManager() {
        return this.sqlManager;
    }
}
