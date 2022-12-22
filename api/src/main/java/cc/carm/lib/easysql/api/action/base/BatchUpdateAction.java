package cc.carm.lib.easysql.api.action.base;

import cc.carm.lib.easysql.api.action.SQLAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface BatchUpdateAction extends SQLAction<List<Integer>> {

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
