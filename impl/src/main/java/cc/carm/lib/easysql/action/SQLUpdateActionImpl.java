package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class SQLUpdateActionImpl<T extends Number>
        extends AbstractSQLAction<T>
        implements SQLUpdateAction<T> {

    protected final @NotNull Class<T> numberClass;

    protected boolean returnGeneratedKeys = false;

    public SQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                               @NotNull String sql) {
        super(manager, sql);
        this.numberClass = numberClass;
    }

    public SQLUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                               @NotNull UUID uuid, @NotNull String sql) {
        super(manager, sql, uuid);
        this.numberClass = numberClass;
    }

    @Override
    public @NotNull T execute() throws SQLException {
        debugMessage(new ArrayList<>());

        try (Connection connection = getManager().getConnection()) {
            try (Statement statement = connection.createStatement()) {

                if (!returnGeneratedKeys) {
                    return numberClass.cast(statement.executeUpdate(getSQLContent()));
                } else {
                    statement.executeUpdate(getSQLContent(), Statement.RETURN_GENERATED_KEYS);

                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        return resultSet.next() ? resultSet.getObject(1, numberClass) : numberClass.cast(0);
                    }
                }
            }
        }
    }

    @Override
    public SQLUpdateAction<T> returnGeneratedKey() {
        this.returnGeneratedKeys = true;
        return this;
    }

    @Override
    public <N extends Number> SQLUpdateAction<N> returnGeneratedKey(Class<N> keyTypeClass) {
        return new SQLUpdateActionImpl<>(getManager(), keyTypeClass, getActionUUID(), getSQLContent()).returnGeneratedKey();
    }

}
