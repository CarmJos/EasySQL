import cc.carm.lib.easysql.api.table.SQLTable;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.function.Consumer;

public enum DataTables2 implements SQLTable {

    USER((table) -> {
        table.addAutoIncrementColumn("id", NumberType.INT, true, true);
        table.addColumn("uuid", "VARCHAR(32) NOT NULL UNIQUE KEY");
        table.addColumn("username", "VARCHAR(16) NOT NULL");
        table.addColumn("age", "TINYINT NOT NULL DEFAULT 1");
        table.addColumn("email", "VARCHAR(32)");
        table.addColumn("phone", "VARCHAR(16)");
        table.addColumn("registerTime", "DATETIME NOT NULL");
        table.setIndex("username", IndexType.UNIQUE_KEY); // 添加唯一索引
        table.setIndex(IndexType.INDEX, "contact", "email", "phone"); //添加联合索引 (示例)
    });

    private final Consumer<TableCreateBuilder> builder;
    private @Nullable String tablePrefix;
    private @Nullable SQLManager manager;

    DataTables2(Consumer<TableCreateBuilder> builder) {
        this.builder = builder;
    }

    @Override
    public @Nullable SQLManager getSQLManager() {
        return this.manager;
    }

    @Override
    public @NotNull String getTableName() {
        // 这里直接选择用枚举的名称作为table的主名称
        return (tablePrefix != null ? tablePrefix : "") + name().toLowerCase();
    }

    @Override
    public boolean create(SQLManager sqlManager) throws SQLException {
        return create(sqlManager, null);
    }

    public boolean create(@NotNull SQLManager sqlManager, @Nullable String tablePrefix) throws SQLException {
        if (this.manager == null) this.manager = sqlManager;
        this.tablePrefix = tablePrefix;

        TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
        if (builder != null) builder.accept(tableBuilder);
        return tableBuilder.build().executeFunction(l -> l > 0, false);
    }

    public static void initialize(@NotNull SQLManager manager, @Nullable String tablePrefix) {
        for (DataTables2 value : values()) {
            try {
                value.create(manager, tablePrefix);
            } catch (SQLException e) {
                // 提示异常
            }
        }
    }

}