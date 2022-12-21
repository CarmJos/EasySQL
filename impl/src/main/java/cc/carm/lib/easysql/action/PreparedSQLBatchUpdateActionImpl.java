package cc.carm.lib.easysql.action;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import cc.carm.lib.easysql.util.StatementUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PreparedSQLBatchUpdateActionImpl<T extends Number>
        extends AbstractSQLAction<List<T>>
        implements PreparedSQLUpdateBatchAction<T> {

    boolean returnKeys = false;
    @NotNull List<Object[]> allParams = new ArrayList<>();

    protected final @NotNull Class<T> numberClass;

    public PreparedSQLBatchUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                                            @NotNull String sql) {
        super(manager, sql);
        this.numberClass = numberClass;
        this.allParams = new ArrayList<>();
    }

    public PreparedSQLBatchUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull Class<T> numberClass,
                                            @NotNull UUID uuid, @NotNull String sql) {
        super(manager, sql, uuid);
        this.numberClass = numberClass;
    }

    @Override
    public PreparedSQLBatchUpdateActionImpl<T> allValues(Iterable<Object[]> allValues) {
        List<Object[]> paramsList = new ArrayList<>();
        allValues.forEach(paramsList::add);
        this.allParams = paramsList;
        return this;
    }

    @Override
    public PreparedSQLBatchUpdateActionImpl<T> values(Object... values) {
        this.allParams.add(values);
        return this;
    }

    @Override
    public PreparedSQLBatchUpdateActionImpl<T> returnGeneratedKeys() {
        this.returnKeys = true;
        return this;
    }

    @Override
    public <N extends Number> PreparedSQLBatchUpdateActionImpl<N> returnGeneratedKeys(Class<N> keyTypeClass) {
        return new PreparedSQLBatchUpdateActionImpl<>(getManager(), keyTypeClass, getActionUUID(), getSQLContent())
                .allValues(allParams).returnGeneratedKeys();
    }

    @Override
    public @NotNull List<T> execute() throws SQLException {
        debugMessage(allParams);

        try (Connection connection = getManager().getConnection()) {
            try (PreparedStatement statement = StatementUtil.createPrepareStatementBatch(
                    connection, getSQLContent(), allParams, returnKeys
            )) {
                int[] executed = statement.executeBatch();

                if (!returnKeys) {
                    return Arrays.stream(executed).mapToObj(numberClass::cast).collect(Collectors.toList());
                } else {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        List<T> generatedKeys = new ArrayList<>();
                        while (resultSet.next()) {
                            generatedKeys.add(resultSet.getObject(1, numberClass));
                        }
                        return generatedKeys;
                    }
                }
            }

        }
    }

}
