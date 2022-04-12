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
import java.util.Collections;
import java.util.List;

public class PreparedSQLUpdateActionImpl
        extends SQLUpdateActionImpl
        implements PreparedSQLUpdateAction {

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
    public PreparedSQLUpdateActionImpl setParams(Object... params) {
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
    public @NotNull Long execute() throws SQLException {
        debugMessage(Collections.singletonList(params));

        try (Connection connection = getManager().getConnection()) {

            try (PreparedStatement statement = StatementUtil.createPrepareStatement(
                    connection, getSQLContent(), params, returnGeneratedKeys
            )) {

                int changes = statement.executeUpdate();
                if (!returnGeneratedKeys) return (long) changes;
                else {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        return resultSet.next() ? resultSet.getLong(1) : -1;
                    }
                }

            }
        }

    }

}
