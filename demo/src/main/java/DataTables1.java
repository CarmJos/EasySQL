import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import cc.carm.lib.easysql.api.table.NamedSQLTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

public enum DataTables1 {

    DATA(SQLTable.of("data", (table) -> {
        table.addAutoIncrementColumn("id", true);
        table.addColumn("user", "INT UNSIGNED NOT NULL");
        table.addColumn("content", "TEXT NOT NULL");
        table.addColumn("time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
    })),

    USER(SQLTable.of("user", (table) -> {
        table.addAutoIncrementColumn("id", NumberType.INT, true, true);
        table.addColumn("uuid", "VARCHAR(32) NOT NULL UNIQUE KEY");
        table.addColumn("username", "VARCHAR(16) NOT NULL");
        table.addColumn("age", "TINYINT NOT NULL DEFAULT 1");
        table.addColumn("email", "VARCHAR(32)");
        table.addColumn("phone", "VARCHAR(16)");
        table.addColumn("registerTime", "DATETIME NOT NULL");
        table.setIndex("username", IndexType.UNIQUE_KEY); // 添加唯一索引
        table.setIndex(IndexType.INDEX, "contact", "email", "phone"); //添加联合索引 (示例)
    }));

    private final NamedSQLTable table;

    DataTables1(NamedSQLTable table) {
        this.table = table;
    }

    public NamedSQLTable get() {
        return this.table;
    }

    public static void initialize(@NotNull SQLManager manager, @Nullable String tablePrefix) {
        for (DataTables1 value : values()) {
            try {
                value.get().create(manager, tablePrefix);
            } catch (SQLException e) {
                // 提示异常
            }
        }
    }

}