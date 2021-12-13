package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SQLUpdateBatchAction extends SQLAction<List<Integer>> {

	SQLUpdateBatchAction addBatch(@NotNull String sql);

}
