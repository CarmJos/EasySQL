package cc.carm.lib.easysql.api.action.asyncable;

import cc.carm.lib.easysql.api.action.SQLAsyncableAction;
import cc.carm.lib.easysql.api.action.base.BatchUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface AsyncableBatchUpdateAction
        extends SQLAsyncableAction<List<Integer>> {

    /**
     * 添加一条批量执行的SQL语句
     *
     * @param sql SQL语句
     * @return {@link BatchUpdateAction}
     */
    BatchUpdateAction addBatch(@NotNull String sql);

    @Override
    default @NotNull String getSQLContent() {
        return getSQLContents().get(0);
    }

    @Override
    @NotNull List<String> getSQLContents();
}
