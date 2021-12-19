package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;

public interface SQLUpdateBatchAction extends SQLAction<List<Integer>> {

	/**
	 * 添加一条批量执行的SQL语句
	 *
	 * @param sql SQL语句
	 * @return {@link SQLUpdateBatchAction}
	 */
	SQLUpdateBatchAction addBatch(@NotNull String sql);

	List<String> getSQLContents();

	@Override
	default BiConsumer<SQLException, SQLAction<List<Integer>>> defaultExceptionHandler() {
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
