package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.SQLUpdateBatchAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SQLUpdateBatchActionImpl
		extends AbstractSQLAction<List<Integer>>
		implements SQLUpdateBatchAction {

	List<String> sqlContents = new ArrayList<>();

	public SQLUpdateBatchActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
		super(manager, sql);
		this.sqlContents.add(sql);
	}

	@Override
	public @NotNull List<String> getSQLContents() {
		return this.sqlContents;
	}

	@Override
	public SQLUpdateBatchAction addBatch(@NotNull String sql) {
		Objects.requireNonNull(sql, "sql could not be null");
		this.sqlContents.add(sql);
		return this;
	}

	@Override
	public @NotNull List<Integer> execute() throws SQLException {
		try (Connection connection = getManager().getConnection()) {

			try (Statement statement = connection.createStatement()) {
				outputDebugMessage();

				for (String content : this.sqlContents) {
					statement.addBatch(content);
				}

				int[] executed = statement.executeBatch();

				return Arrays.stream(executed).boxed().collect(Collectors.toList());
			}

		}
	}

	@Override
	protected void outputDebugMessage() {
		getManager().debug("# " + getShortID() + " -> [");
		for (String content : getSQLContents()) getManager().debug(" { " + content + " }");
		getManager().debug("]");

	}

}
