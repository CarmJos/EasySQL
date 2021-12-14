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
import java.util.stream.Collectors;

public class SQLUpdateBatchActionImpl extends AbstractSQLAction<List<Integer>> implements SQLUpdateBatchAction {

	List<String> sqlContents = new ArrayList<>();

	public SQLUpdateBatchActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
		super(manager, sql);
		this.sqlContents.add(sql);
	}

	@Override
	public @NotNull String getSQLContent() {
		return this.sqlContents.stream()
				.map(content -> "[" + content + "]" + " ")
				.collect(Collectors.joining());
	}

	@Override
	public SQLUpdateBatchAction addBatch(@NotNull String sql) {
		this.sqlContents.add(sql);
		return this;
	}

	@Override
	public @NotNull List<Integer> execute() throws SQLException {
		Connection connection = getManager().getConnection();
		Statement statement = connection.createStatement();
		outputDebugMessage();
		for (String content : this.sqlContents) {
			statement.addBatch(content);
		}
		int[] executed = statement.executeBatch();
		List<Integer> returnedValues = Arrays.stream(executed).boxed().collect(Collectors.toList());

		statement.close();
		connection.close();

		return returnedValues;
	}

}
