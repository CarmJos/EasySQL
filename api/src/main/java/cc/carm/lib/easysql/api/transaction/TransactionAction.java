package cc.carm.lib.easysql.api.transaction;

import cc.carm.lib.easysql.api.action.SQLAction;
import cc.carm.lib.easysql.api.old.SQLSource;
import org.jetbrains.annotations.NotNull;

public interface TransactionAction<T> extends SQLAction<T> {

    @NotNull SQLTransaction getTransaction();

    default @NotNull SQLSource getSource() {
        return getTransaction().getManager();
    }

}
