package cc.carm.lib.easysql.action.query;

import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import cc.carm.lib.easysql.query.SQLQueryImpl;
import cc.carm.lib.easysql.util.StatementUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PreparedQueryActionImpl extends QueryActionImpl implements PreparedQueryAction {

	Consumer<PreparedStatement> handler;
	Object[] params;

	public PreparedQueryActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
		super(manager, sql);
	}

	@Override
	public PreparedQueryActionImpl setParams(@Nullable Object[] params) {
		this.params = params;
		return this;
	}

	@Override
	public PreparedQueryActionImpl setParams(@Nullable Iterable<Object> params) {
		if (params == null) {
			return setParams((Object[]) null);
		} else {
			List<Object> paramsList = new ArrayList<>();
			for (Object param : params) {
				paramsList.add(param);
			}
			return setParams(paramsList.toArray());
		}
	}

	@Override
	public PreparedQueryActionImpl handleStatement(@Nullable Consumer<PreparedStatement> statement) {
		this.handler = statement;
		return this;
	}


	@Override
	public @NotNull SQLQueryImpl execute() throws SQLException {
		Connection connection = getManager().getConnection();
		getManager().debug("#" + getShortID() + " ->" + getSQLContent());
		PreparedStatement preparedStatement;
		if (handler == null) {
			preparedStatement = StatementUtil.createPrepareStatement(connection, getSQLContent(), this.params);
		} else {
			preparedStatement = connection.prepareStatement(getSQLContent());
			handler.accept(preparedStatement);
		}

		ResultSet resultSet = preparedStatement.executeQuery();

		return new SQLQueryImpl(getManager(), this, connection, preparedStatement, resultSet);
	}

	@Override
	public void executeAsync(Consumer<SQLQuery> success, Consumer<SQLException> failure) {

	}

}
