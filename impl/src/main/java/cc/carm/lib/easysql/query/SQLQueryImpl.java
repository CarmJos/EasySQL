package cc.carm.lib.easysql.query;

import cc.carm.lib.easysql.action.query.QueryActionImpl;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.manager.SQLManagerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLQueryImpl implements SQLQuery {

    protected final long executeTime;

    protected final SQLManagerImpl sqlManager;
    final Connection connection;
    final Statement statement;
    final ResultSet resultSet;
    protected QueryActionImpl queryAction;

    public SQLQueryImpl(
            SQLManagerImpl sqlManager, QueryActionImpl queryAction,
            Connection connection, Statement statement, ResultSet resultSet
    ) {
        this(sqlManager, queryAction, connection, statement, resultSet, System.currentTimeMillis());
    }

    public SQLQueryImpl(
            SQLManagerImpl sqlManager, QueryActionImpl queryAction,
            Connection connection, Statement statement, ResultSet resultSet,
            long executeTime
    ) {
        this.executeTime = executeTime;
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
            if (getResultSet() != null && !getResultSet().isClosed()) getResultSet().close();
            if (getStatement() != null && !getStatement().isClosed()) getStatement().close();
            if (getConnection() != null && !getConnection().isClosed()) getConnection().close();

            if (getManager().isDebugMode()) {
                try {
                    getManager().getDebugHandler().afterQuery(this, getExecuteTime(), System.currentTimeMillis());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            getManager().getActiveQuery().remove(getAction().getActionUUID());
        } catch (SQLException e) {
            getAction().handleException(getAction().defaultExceptionHandler(), e);
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
