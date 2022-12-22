package cc.carm.lib.easysql;

import cc.carm.lib.easysql.manager.SQLManagerImpl;
import cc.carm.lib.easysql.tests.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

public class EasySQLTest {

    protected SQLManager sqlManager;

    @Before
    public void initialize() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL;");

        this.sqlManager = new SQLManagerImpl(new HikariDataSource(config), "test");
        this.sqlManager.setDebugMode(true);
    }

    @After
    public void shutdown() {
        if (sqlManager.getDataSource() instanceof HikariDataSource) {
            //Close bee connection pool
            ((HikariDataSource) sqlManager.getDataSource()).close();
        }
    }


    @Test
    public void onTest() {

        print("加载测试类...");
        Set<TestHandler> tests = new LinkedHashSet<>();
        tests.add(new TableCreateTest());
//		tests.add(new TableAlterTest());
//		tests.add(new TableRenameTest());
//		tests.add(new QueryNotCloseTest());

        tests.add(new SQLUpdateBatchTests());
        tests.add(new SQLUpdateReturnKeysTest());
        tests.add(new QueryCloseTest());
        tests.add(new QueryFunctionTest());
        tests.add(new QueryFutureTest());
        tests.add(new QueryAsyncTest());

        tests.add(new TableMetadataTest());
//        tests.add(new DeleteTest());

        print("准备进行测试...");

        int index = 1;
        int success = 0;

        for (TestHandler currentTest : tests) {
            print("-------------------------------------------------");
            if (currentTest.executeTest(index, sqlManager)) {
                success++;
            }

            index++;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        print(" ");
        print("全部测试执行完毕，成功 %s 个，失败 %s 个。",
                success, (tests.size() - success)
        );


    }


    public static void print(@NotNull String format, Object... params) {
        System.out.printf((format) + "%n", params);
    }

}
