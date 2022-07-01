package cc.carm.lib.easysql.api.table;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;


public abstract class NamedSQLTable implements SQLTable {

    private final @NotNull String tableName;

    protected @Nullable String tablePrefix;
    protected @Nullable SQLManager manager;

    /**
     * 请调用 {@link NamedSQLTable} 下的静态方法进行对象的初始化。
     *
     * @param tableName 该表的名称
     */
    public NamedSQLTable(@NotNull String tableName) {
        this.tableName = tableName;
    }

    public @NotNull String getTableName() {
        return (tablePrefix != null ? tablePrefix : "") + tableName;
    }

    @Override
    public @Nullable SQLManager getSQLManager() {
        return this.manager;
    }

    /**
     * 使用指定 SQLManager 进行本示例的初始化。
     *
     * @param sqlManager  {@link SQLManager}
     * @param tablePrefix 表名前缀
     * @return 本表是否为首次创建
     * @throws SQLException 出现任何错误时抛出
     */
    public abstract boolean create(@NotNull SQLManager sqlManager, @Nullable String tablePrefix) throws SQLException;

    public boolean create(@NotNull SQLManager sqlManager) throws SQLException {
        return create(sqlManager, null);
    }

}
