package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.TestHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SQLUpdateBatchTests extends TestHandler {


    protected static List<Object[]> generateParams() {
        return IntStream.range(0, 5).mapToObj(i -> generateParam()).collect(Collectors.toList());
    }

    protected static Object[] generateParam() {
        UUID uuid = UUID.randomUUID();
        return new Object[]{uuid, uuid.toString().substring(0, 5), (int) (Math.random() * 50)};
    }

    @Override
    public void onTest(SQLManager sqlManager) throws SQLException {

        List<Integer> updates = sqlManager.createInsertBatch("test_user_table")
                .setColumnNames("uuid", "username", "age")
                .setAllParams(generateParams())
                .execute();

        System.out.println("changes " + Arrays.toString(updates.toArray()));

    }


}
