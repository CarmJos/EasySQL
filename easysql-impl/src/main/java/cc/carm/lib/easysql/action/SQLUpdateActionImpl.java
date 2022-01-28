package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUpdateActionImpl
		extends AbstractSQLAction<Integer>
		implements SQLUpdateAction {

	boolean returnGeneratedKeys = false;

	public SQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
		super(manager, sql);
	}

	@Override
	public @NotNull Integer execute() throws SQLException {
		try (Connection connection = getManager().getConnection()) {
			try (Statement statement = connection.createStatement()) {
				outputDebugMessage();

				if (!returnGeneratedKeys) {
					return statement.executeUpdate(getSQLContent());
				} else {
					statement.executeUpdate(getSQLContent(), Statement.RETURN_GENERATED_KEYS);

					try (ResultSet resultSet = statement.getGeneratedKeys()) {
						return resultSet.next() ? resultSet.getInt(1) : -1;
					}
				}
			}
		}
	}


	@Override
	public SQLUpdateAction setReturnGeneratedKey(boolean returnGeneratedKey) {
		this.returnGeneratedKeys = returnGeneratedKey;
		return this;
	}

}
