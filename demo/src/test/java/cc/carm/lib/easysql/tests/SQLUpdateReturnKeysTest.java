package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.api.SQLManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SQLUpdateReturnKeysTest extends SQLUpdateBatchTests {


    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {
        List<Integer> generatedKeys = sqlManager.createInsertBatch("test_user_table")
                .columns("uuid", "username", "age")
                .allValues(generateParams())
                .returnGeneratedKeys(Integer.class)
                .execute();

        System.out.println("generated " + Arrays.toString(generatedKeys.toArray()));
    }


}
