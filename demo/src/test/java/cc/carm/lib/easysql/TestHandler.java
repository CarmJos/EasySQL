package cc.carm.lib.easysql;

import cc.carm.lib.easysql.api.SQLManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public abstract class TestHandler {

    protected static void print(@NotNull String format, Object... params) {
        EasySQLTest.print(format, params);
    }

    @ApiStatus.OverrideOnly
    public abstract void onTest(SQLManager sqlManager) throws SQLException, ExecutionException, InterruptedException;

    public boolean executeTest(int index, SQLManager sqlManager) {
        String testName = getClass().getSimpleName();

        print("  #%s 测试 @%s 开始", index, testName);
        long start = System.currentTimeMillis();

        try {
            onTest(sqlManager);
            print("  #%s 测试 @%s 成功，耗时 %s ms。", index, testName, (System.currentTimeMillis() - start));
            return true;
        } catch (Exception exception) {
            print("  #%s 测试 @%s 失败，耗时 %s ms。", index, testName, (System.currentTimeMillis() - start));
            exception.printStackTrace();
            return false;
        }
    }


}
