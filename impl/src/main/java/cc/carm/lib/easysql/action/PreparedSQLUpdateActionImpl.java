package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.base.PreparedUpdateAction;
import cc.carm.lib.easysql.api.action.base.UpdateAction;
import cc.carm.lib.easysql.SQLManagerImpl;
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
import java.util.UUID;

public class PreparedSQLUpdateActionImpl<T extends Number>
        extends UpdateActionImpl<T>
        implements PreparedUpdateAction<T> {

    Object[] params;

    public PreparedSQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                                       @NotNull String sql) {
        this(manager, numberClass, sql, (Object[]) null);
    }

    public PreparedSQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                                       @NotNull String sql, @Nullable List<Object> params) {
        this(manager, numberClass, sql, params == null ? null : params.toArray());
    }

    public PreparedSQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                                       @NotNull String sql, @Nullable Object[] params) {
        super(manager, numberClass, sql);
        this.params = params;
    }

    public PreparedSQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                                       @NotNull UUID uuid, @NotNull String sql,
                                       Object[] params) {
        super(manager, numberClass, uuid, sql);
        this.params = params;
    }

    @Override
    public PreparedSQLUpdateActionImpl<T> values(Object... params) {
        this.params = params;
        return this;
    }

    @Override
    public PreparedSQLUpdateActionImpl<T> values(@Nullable Iterable<Object> params) {
        if (params == null) {
            return values((Object[]) null);
        } else {
            List<Object> paramsList = new ArrayList<>();
            params.forEach(paramsList::add);
            return values(paramsList.toArray());
        }
    }

    @Override
    public @NotNull T execute() throws SQLException {
        debugMessage(Collections.singletonList(params));

        try (Connection connection = getManager().getConnection()) {

            try (PreparedStatement statement = StatementUtil.createPrepareStatement(
                    connection, getSQLContent(), params, returnGeneratedKeys
            )) {

                int changes = statement.executeUpdate();
                if (!returnGeneratedKeys) return numberClass.cast(changes);
                else {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        return resultSet.next() ? resultSet.getObject(1, numberClass) : numberClass.cast(0);
                    }
                }

            }
        }

    }

    @Override
    public <N extends Number> UpdateAction<N> returnGeneratedKey(Class<N> keyTypeClass) {
        PreparedSQLUpdateActionImpl<N> newAction = new PreparedSQLUpdateActionImpl<>(getManager(), keyTypeClass, getActionUUID(), getSQLContent(), params);
        newAction.returnGeneratedKey();
        return newAction;
    }
}
