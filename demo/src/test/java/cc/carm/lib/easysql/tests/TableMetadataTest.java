package cc.carm.lib.easysql.tests;

import cc.carm.lib.easysql.TestHandler;
import cc.carm.lib.easysql.api.SQLManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TableMetadataTest extends TestHandler {

    @Override
    public void onTest(SQLManager sqlManager) throws Exception {

        print("	获取数据库中所有的表");
        CompletableFuture<List<String>> tables = sqlManager.fetchMetadata(
                (meta) -> meta.getTables(null, null, "%", new String[]{"TABLE"}),
                (rs) -> {
                    List<String> data = new ArrayList<>();
                    while (rs.next()) {
                        data.add(rs.getString("TABLE_NAME"));
                    }
                    return data;
                }
        );
        print("表名：" + Arrays.toString(tables.get().toArray()));

        print("	获取数据库中所有的列");
        CompletableFuture<Set<String>> columns = sqlManager.fetchTableMetadata("test%").listColumns();
        print("列名：" + Arrays.toString(columns.get().toArray()));

        print("表是否存在 " + sqlManager.fetchTableMetadata("test_user_info").validateExist().get());
        print("列是否存在 " + sqlManager.fetchTableMetadata("test_user_info").isColumnExists("uid").get());

    }

}
