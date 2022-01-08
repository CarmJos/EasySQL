package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import cc.carm.lib.easysql.util.StatementUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedSQLUpdateActionImpl extends SQLUpdateActionImpl implements PreparedSQLUpdateAction {

    Object[] params;

    public PreparedSQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
        this(manager, sql, (Object[]) null);
    }

    public PreparedSQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql,
                                       @Nullable List<Object> params) {
        this(manager, sql, params == null ? null : params.toArray());
    }

    public PreparedSQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql,
                                       @Nullable Object[] params) {
        super(manager, sql);
        this.params = params;
    }

    @Override
    public PreparedSQLUpdateActionImpl setParams(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public PreparedSQLUpdateAction setParams(@Nullable Iterable<Object> params) {
        if (params == null) {
            return setParams((Object[]) null);
        } else {
            List<Object> paramsList = new ArrayList<>();
            params.forEach(paramsList::add);
            return setParams(paramsList.toArray());
        }
    }

    @Override
    public @NotNull Integer execute() throws SQLException {
        int value = -1;

        Connection connection = getManager().getConnection();
        PreparedStatement statement = StatementUtil.createPrepareStatement(
                connection, getSQLContent(), params, keyIndex > 0
        );
        outputDebugMessage();
        if (keyIndex > 0) {
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null) {
                if (resultSet.next()) value = resultSet.getInt(keyIndex);
                resultSet.close();
            }
        } else {
            value = statement.executeUpdate();
        }

        statement.close();
        connection.close();

        return value;
    }

}
