package cc.carm.lib.easysql.action.query;

import cc.carm.lib.easysql.action.AbstractSQLAction;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLHandler;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import cc.carm.lib.easysql.query.SQLQueryImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class QueryActionImpl extends AbstractSQLAction<SQLQuery> implements QueryAction {

    public QueryActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
        super(manager, sql);
    }

    @Override
    public @NotNull SQLQueryImpl execute() throws SQLException {
        debugMessage(new ArrayList<>());

        Connection connection = getManager().getConnection();
        Statement statement;

        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            connection.close();
            throw ex;
        }

        try {
            SQLQueryImpl query = new SQLQueryImpl(
                    getManager(), this,
                    connection, statement,
                    statement.executeQuery(getSQLContent())
            );
            getManager().getActiveQuery().put(getActionUUID(), query);

            return query;
        } catch (SQLException exception) {
            statement.close();
            connection.close();
            throw exception;
        }
    }


    @Override
    public void executeAsync(SQLHandler<SQLQuery> success, SQLExceptionHandler failure) {
        getManager().getExecutorPool().submit(() -> {
            try (SQLQueryImpl query = execute()) {
                if (success != null) success.accept(query);
            } catch (SQLException exception) {
                handleException(failure, exception);
            }
        });
    }
}
