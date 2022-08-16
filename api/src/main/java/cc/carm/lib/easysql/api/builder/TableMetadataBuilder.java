package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;

import java.util.concurrent.CompletableFuture;

public interface TableMetadataBuilder extends SQLBuilder {

    /**
     * @return 本表是否存在
     */
    CompletableFuture<Boolean> validateExist();

    /**
     * @param columnName 需要判断的列名
     * @return 对应列是否存在
     */
    CompletableFuture<Boolean> isColumnExists(String columnName);

    // More coming soon.

}
