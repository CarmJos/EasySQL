package cc.carm.lib.easysql;

import cc.carm.lib.easysql.api.action.SQLTransaction;

import java.sql.SQLException;
import java.util.function.Consumer;

public interface DemoTransaction {

    SQLTransactionResult commitTransaction(Consumer<SQLTransaction> consumer);

}
