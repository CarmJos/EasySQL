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
import java.util.stream.Collectors;

public class PreparedSQLBatchUpdateActionImpl
        extends AbstractSQLAction<List<Integer>>
        implements PreparedSQLUpdateBatchAction {

    int keyIndex = -1;
    List<Object[]> allParams;

    public PreparedSQLBatchUpdateActionImpl(@NotNull SQLManagerImpl manager, @NotNull String sql) {
        super(manager, sql);
        this.allParams = new ArrayList<>();
    }

    @Override
    public PreparedSQLUpdateBatchAction setAllParams(Iterable<Object[]> allParams) {
        List<Object[]> paramsList = new ArrayList<>();
        allParams.forEach(paramsList::add);
        this.allParams = paramsList;
        return this;
    }

    @Override
    public PreparedSQLUpdateBatchAction addParamsBatch(Object[] params) {
        this.allParams.add(params);
        return this;
    }

    @Override
    public PreparedSQLUpdateBatchAction setKeyIndex(int keyColumnIndex) {
        this.keyIndex = keyColumnIndex;
        return this;
    }

    @Override
    public @NotNull List<Integer> execute() throws SQLException {
        List<Integer> returnedValues;
        Connection connection = getManager().getConnection();
        PreparedStatement statement = StatementUtil.createPrepareStatementBatch(
                connection, getSQLContent(), allParams, keyIndex > 0
        );
        outputDebugMessage();
        if (keyIndex > 0) {
            statement.executeBatch();
            List<Integer> generatedKeys = new ArrayList<>();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null) {
                while (resultSet.next()) generatedKeys.add(resultSet.getInt(keyIndex));
                resultSet.close();
            }
            returnedValues = generatedKeys;
        } else {
            int[] executed = statement.executeBatch();
            returnedValues = Arrays.stream(executed).boxed().collect(Collectors.toList());
        }

        statement.close();
        connection.close();

        return returnedValues;
    }

}
