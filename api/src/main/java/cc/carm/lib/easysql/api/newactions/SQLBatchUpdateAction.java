package cc.carm.lib.easysql.api.newactions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface SQLBatchUpdateAction extends SQLAction<List<Integer>> {

    /**
     * 添加一条批量执行的SQL语句
     *
     * @param sql SQL语句
     * @return {@link SQLBatchUpdateAction}
     */
    SQLBatchUpdateAction addBatch(@NotNull String sql);

    @Override
    default @NotNull String getSQLContent() {
        return getSQLContents().get(0);
    }

    @Override
    @NotNull List<String> getSQLContents();

}
