package cc.carm.lib.easysql.query;

import cc.carm.lib.easysql.action.query.QueryActionImpl;
import cc.carm.lib.easysql.api.action.query.SQLQuery;
import cc.carm.lib.easysql.manager.SQLManagerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLQueryImpl implements SQLQuery {

	protected final long executeTime;

	protected SQLManagerImpl sqlManager;
	protected QueryActionImpl queryAction;

	Connection connection;
	Statement statement;

	ResultSet resultSet;

	public SQLQueryImpl(
			SQLManagerImpl sqlManager, QueryActionImpl queryAction,
			Connection connection, Statement statement, ResultSet resultSet
	) {
		this.executeTime = System.currentTimeMillis();
		this.sqlManager = sqlManager;
		this.queryAction = queryAction;
		this.connection = connection;
		this.statement = statement;
		this.resultSet = resultSet;
	}

	@Override
	public long getExecuteTime() {
		return this.executeTime;
	}

	@Override
	public SQLManagerImpl getManager() {
		return this.sqlManager;
	}

	@Override
	public QueryActionImpl getAction() {
		return this.queryAction;
	}

	@Override
	public ResultSet getResultSet() {
		return this.resultSet;
	}

	@Override
	public String getSQLContent() {
		return getAction().getSQLContent();
	}

	@Override
	public void close() {
		try {
			if (getResultSet() != null) getResultSet().close();
			if (getStatement() != null) getStatement().close();
			if (getConnection() != null) getConnection().close();

			getManager().debug("#" + getAction().getShortID() +
					" -> finished after " + (System.currentTimeMillis() - getExecuteTime()) + " ms."
			);
			getManager().getActiveQuery().remove(getAction().getActionUUID());
		} catch (SQLException e) {
			getAction().handleException(e);
		}
		this.queryAction = null;
	}

	@Override
	public Statement getStatement() {
		return this.statement;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}
}
