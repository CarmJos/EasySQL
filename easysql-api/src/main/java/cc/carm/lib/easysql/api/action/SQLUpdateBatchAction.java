package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SQLUpdateBatchAction extends SQLAction<List<Integer>> {

	/**
	 * 添加一条批量执行的SQL语句
	 *
	 * @param sql SQL语句
	 * @return {@link SQLUpdateBatchAction}
	 */
	SQLUpdateBatchAction addBatch(@NotNull String sql);

}
