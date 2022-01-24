package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
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

	default @NotNull String getSQLContent() {
		return getSQLContents().get(0);
	}

	List<String> getSQLContents();

	@Override
	default SQLExceptionHandler defaultExceptionHandler() {
		return (exception, action) -> {
			getManager().getLogger().severe("Error when execute SQLs : ");
			int i = 1;
			for (String content : getSQLContents()) {
				getManager().getLogger().severe("#" + i + " [" + content + "]");
				i++;
			}
			getManager().getLogger().severe(exception.getLocalizedMessage());
			exception.printStackTrace();
		};
	}

}
