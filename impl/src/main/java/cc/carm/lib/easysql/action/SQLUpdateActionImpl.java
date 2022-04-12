package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLUpdateActionImpl
        extends AbstractSQLAction<Long>
        implements SQLUpdateAction {

    boolean returnGeneratedKeys = false;

    public SQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
        super(manager, sql);
    }

    @Override
    public @NotNull Long execute() throws SQLException {
        debugMessage(new ArrayList<>());

        try (Connection connection = getManager().getConnection()) {
            try (Statement statement = connection.createStatement()) {

                if (!returnGeneratedKeys) {
                    return (long) statement.executeUpdate(getSQLContent());
                } else {
                    statement.executeUpdate(getSQLContent(), Statement.RETURN_GENERATED_KEYS);

                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        return resultSet.next() ? resultSet.getLong(1) : -1;
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
