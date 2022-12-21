package cc.carm.lib.easysql.api.transaction;

import cc.carm.lib.easysql.api.SQLOperator;
import cc.carm.lib.easysql.api.enums.IsolationLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SQLTransaction extends SQLOperator, AutoCloseable {

    /**
     * 得到本次事务的隔离级别
     *
     * @return {@link IsolationLevel} 隔离级别
     */
    @NotNull IsolationLevel getIsolationLevel();

    /**
     * 提交已有操作
     */
    void commit();

    /**
     * 设定一个记录点
     *
     * @param name 记录点名称
     * @return {@link SQLSavepoint} 事务记录点
     */
    @NotNull SQLSavepoint savepoint(@NotNull String name);

    /**
     * 回退全部操作
     */
    default void rollback() {
        rollback(null);
    }

    /**
     * 回退操作到某个记录点或回退整个事务操作。
     *
     * @param savepoint 记录点，若记录点为NULL则回退整个事务的操作。
     */
    void rollback(@Nullable SQLSavepoint savepoint);

}
