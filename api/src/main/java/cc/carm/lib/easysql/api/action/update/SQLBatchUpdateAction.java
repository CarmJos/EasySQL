package cc.carm.lib.easysql.api.action.update;

import cc.carm.lib.easysql.api.action.SQLBaseAction;
import cc.carm.lib.easysql.api.action.SQLAdvancedAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SQLBatchUpdateAction<T extends Number, B extends SQLBatchUpdateAction<T, B>> extends SQLBaseAction<T> {

    interface Base<T extends Number> extends SQLBatchUpdateAction<T, Base<T>>, SQLBaseAction<T> {
    }

    interface Advanced<T extends Number> extends SQLBatchUpdateAction<T, Advanced<T>>, SQLAdvancedAction<T> {
    }

    /**
     * 添加一条批量执行的SQL语句
     *
     * @param sql SQL语句
     * @return {@link SQLBatchUpdateAction}
     */
    B addBatch(@NotNull String sql);

    @Override
    default @NotNull String getSQLContent() {
        return getSQLContents().get(0);
    }

    @Override
    @NotNull List<String> getSQLContents();

}
