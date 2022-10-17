package cc.carm.lib.easysql.api.transaction;

import cc.carm.lib.easysql.api.NewSQLManager;
import org.jetbrains.annotations.Nullable;

public interface SQLTransaction extends NewSQLManager, AutoCloseable {

    void commit();

    SQLSavepoint savepoint(String name);

    default void rollback() {
        rollback(null);
    }

    void rollback(@Nullable SQLSavepoint savepoint);

}
