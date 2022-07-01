> 本文档由 GitHub 用户 @CarmJos 创建。  
> 本文撰写于 2022/07/01，基于 EasySQL 版本 `0.4.2` 。

# 在**小项目中**推荐使用的**数据库表**实现方案


## 简介
在小型项目中，我们常常需要编写数据库的表结构，并需要在开发中不断的参考、维护该结构。

在 EasySQL 中，我们提供了一个简单快捷的数据库表创建工具 `TableCreateBuilder` 。
基于该工具，又在后续版本中提供了 `SQLTable` 类用于快速针对指定表创建不同的数据库操作。

以下内容是我在许多项目中的使用方法，由于其 `便捷`、`易于管理` 且 `支持引用查询` ，我十分推荐您参考我的方案，并应用到自己的项目中。

### 实例项目: 
- [QuickShop-Hikari (DataTables)](https://github.com/Ghost-chu/QuickShop-Hikari/blob/hikari/quickshop-bukkit/src/main/java/com/ghostchu/quickshop/database/DataTables1.java)

## 利用 NamedSQLTable 快速创建枚举类以管理

这种方案的优势在于无需复制大量代码，仅需使用EasySQL已经提供的 `NamedSQLTable` 类快捷进行数据库操作。

首先，我们需要创建一个枚举类，[示例代码](../demo/src/main/java/DataTables1.java)如下所示：

```java
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import cc.carm.lib.easysql.api.table.NamedSQLTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

public enum DataTables {

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

    DataTables(NamedSQLTable table) {
        this.table = table;
    }

    public NamedSQLTable get() {
        return this.table;
    }

    public static void initialize(@NotNull SQLManager manager, @Nullable String tablePrefix) {
        for (DataTables value : values()) {
            try {
                value.get().create(manager, tablePrefix);
            } catch (SQLException e) {
                // 提示异常
            }
        }
    }

}
```

随后，我们便可以在数据库初始化时调用 `DataTables#initialize(manager,tablePrefix)` 方法快捷的进行表的初始化。

初始化后，我们便可以通过 `DataTables#get()` 方法获取对应表的 `NamedSQLTable` 实例，以进行 `createQuery()` 等操作。

注意，NamedSQLTable同时提供了有SQLManager参数与无参的操作方法，其中无参方法将自动调用初始化时使用的SQLManager进行操作。

## 利用枚举类实现 SQLTable 进行操作

这种方法相较于前者代码量稍多些，但无需在每次使用时调用 `DataTables#get()` 方法获取 NamedSQLTable 实例，代码上更为简介。

且可以通过重写 `getTableName()` 方法来自行规定表前缀。

_该方法为本人最常用，也是最推荐的方法。_

[示例代码](../demo/src/main/java/DataTables2.java)如下：

```java
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.function.Consumer;

public enum DataTables implements SQLTable {

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

    DataTables(Consumer<TableCreateBuilder> builder) {
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
        for (DataTables1 value : values()) {
            try {
                value.create(manager, tablePrefix);
            } catch (SQLException e) {
                // 提示异常
            }
        }
    }

}
```