package cc.carm.lib.easysql.api.migrate;

import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.MigrateResult;
import org.jetbrains.annotations.Contract;

import java.sql.SQLException;

/**
 * 将现有的表结构与数据迁移之新的表中
 *
 * @author huanmeng_qwq
 * @since 0.4.7
 */
public interface SQLMigrate {

    /**
     * 为迁移工作定义前后的表名信息
     *
     * @param oldTableName     旧表名
     * @param newTableName     新表名
     * @param newTableSettings 新表的设置
     * @return {@link SQLMigrate}
     */
    @Contract("null,_,_ -> fail;!null,_,_ -> this")
    SQLMigrate tableName(String oldTableName, String newTableName, String newTableSettings);

    /**
     * 为迁移工作定义前后的表名信息
     *
     * @param oldColumnName     旧列名
     * @param newColumnName     新列名
     * @param newColumnSettings 新列的设置
     * @return {@link SQLMigrate}
     */
    @Contract("null,_,_ -> fail;!null,_,_ -> this")
    default SQLMigrate column(String oldColumnName, String newColumnName, String newColumnSettings) {
        return column(oldColumnName, newColumnName, newColumnSettings, null);
    }

    /**
     * 为迁移工作定义前后的表名信息
     *
     * @param oldColumnName     旧列名
     * @param newColumnName     新列名
     * @param newColumnSettings 新列的设置
     * @param indexType         索引类型
     * @return {@link SQLMigrate}
     */
    @Contract("null,_,_,_ -> fail;!null,_,_,_ -> this")
    SQLMigrate column(String oldColumnName, String newColumnName, String newColumnSettings, IndexType indexType);

    /**
     * @param oldColumnName 旧列名
     * @param newColumnName 新列名
     * @return {@link SQLMigrate}
     */
    @Contract("null,_->fail;!null,_ -> this")
    SQLMigrate autoIncrementColumn(String oldColumnName, String newColumnName);

    /**
     * 迁移数据
     *
     * @return {@link MigrateResult}
     */
    MigrateResult migrate() throws SQLException;

}
