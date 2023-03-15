package cc.carm.lib.easysql.query;

import cc.carm.lib.easysql.action.query.SQLQueryActionImpl;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.SQLManagerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class SQLQueryImpl implements SQLQuery {

    protected final long executeTime;

    protected final SQLManagerImpl sqlManager;
    protected final Connection connection;
    protected final Statement statement;
    protected final ResultSet resultSet;
    protected SQLQueryActionImpl queryAction;

    public SQLQueryImpl(
            SQLManagerImpl sqlManager, SQLQueryActionImpl queryAction,
            Connection connection, Statement statement, ResultSet resultSet
    ) {
        this(sqlManager, queryAction, connection, statement, resultSet, System.nanoTime());
    }

    public SQLQueryImpl(
            SQLManagerImpl sqlManager, SQLQueryActionImpl queryAction,
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
    public long getExecuteTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.executeTime, TimeUnit.NANOSECONDS);
    }

    @Override
    public SQLManagerImpl getManager() {
        return this.sqlManager;
    }

    @Override
    public SQLQueryActionImpl getAction() {
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
                    getManager().getDebugHandler().afterQuery(this, getExecuteTime(TimeUnit.NANOSECONDS), System.nanoTime());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            getManager().getActiveQueries().remove(getAction().getActionUUID());
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
