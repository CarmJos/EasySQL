package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

public class SQLUpdateActionImpl extends AbstractSQLAction<Integer> implements SQLUpdateAction {

	int keyIndex = -1;

	public SQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
		super(manager, sql);
	}

	@Override
	public @NotNull Integer execute() throws SQLException {
		int returnedValue = -1;
		Connection connection = getManager().getConnection();
		Statement statement = connection.createStatement();
		outputDebugMessage();
		if (keyIndex > 0) {
			statement.executeUpdate(getSQLContent(), Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet != null) {
				if (resultSet.next()) {
					returnedValue = resultSet.getInt(keyIndex);
				}
				resultSet.close();
			}
		} else {
			returnedValue = statement.executeUpdate(getSQLContent());
		}

		statement.close();
		connection.close();

		return returnedValue;
	}

	@Override
	public void executeAsync(Consumer<Integer> success, Consumer<SQLException> failure) {

	}

	@Override
	public SQLUpdateActionImpl setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
		return this;
	}

}
