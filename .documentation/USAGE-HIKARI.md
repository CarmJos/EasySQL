> 本文档由 GitHub 用户 @Ghost-chu 创建。  
> 本文撰写于 2022/02/09，适配 EasySQL 版本 `v0.3.6`。  
> 本文基于 `EasySQL-HikariPool` 版本编写。

# EasySQL - HikariPool 使用指南

## 和 EasySQL 说你好：创建你的第一个 SQLManager

```java
public class HiEasySQL {
    public static void createYourSQLManager() {
        HikariConfig hikari = YOUR_HIKARI_CONFIG;
        SQLManager sqlManager = EasySQL.createManager(hikari);
        try {
            if (!sqlManager.getConnection().isValid(5)) {
                logger.warning("Connection invalid!");
            }
        } catch (SQLException e) {
            logger.warning("Failed to connect to database!", e);
        }
    }
}
```

至此，你已经创建了一个 SQLManager 对象，与 EasySQL 的故事由此开始。

## SQL起步： 查询 (Query)

EasySQL 可以使用异步查询以避免产生性能影响和手动关闭连接的麻烦。本节我们将展示使用 "异步查询" 的示例代码，并讲解如何使用 "查询处理器" 和 "错误处理器"。

```java
public class HiEasySQL {
    public static void trySomeQuery(SQLManager sqlManager) {
        sqlManager.createQuery() // 创建一个查询
                .inTable("table_name") // 指定表名
                .selectColumns("name", "sex", "age") // 选择 "name", "sex", "age" 三个列
                .addCondition("name", "Bob") // 限定条件，"name" 必须是 Bob
                .build()/*构建查询体*/.executeAsync(
                        (query) -> { /*处理查询结果-SQLQuery*/ },
                        ((exception, sqlAction) -> { /*SQL异常处理-SQLExceptionHandler*/ })
                ); // 异步查询~~~~
    }
}
```

### SQLQuery

SQLQuery 是 EasySQL 执行查询类请求统一返回的对象，包括如下内容：

* ResultSet - 查询结果
* SQLAction - 执行的 SQL 操作
* Action - 操作类型
* ExecuteTime - 查询耗时
* SQLContent - 最终执行的 SQL 语句的内容

如果需要，SQLQuery 还额外提供了一些其他信息，如：

* SQLManager - 创建此 SQLQuery 对象的 SQLManager 实例
* Connection - 执行 SQL 操作的链接

等信息。

### SQLExceptionHandler

当出现 SQLException 异常时，如果你在查询中指定了一个 SQLExceptionHandler，则会被调用。 SQLExceptionHandler 接受两个参数：

* SQLException - 发生的 SQL 异常
* SQLAction - 执行的 SQL 操作

### SQLAction

SQLAction 包含 EasySQL 在处理 SQL 请求时所使用到的信息：

* SQLContent - 最终执行的 SQL 语句的内容
* ActionUUID - 执行的 SQL 操作的唯一标识
* ShortID - 执行的 SQL 操作的短 ID (短8位)
* CreateTime - SQLAction 创建的时间
* SQLManager - 与 SQLAction 有关的 SQLManager 的实例

## 不仅能读，也得能写： 插入(Insert)

除了 SELECT 查询操作以外，EasySQL 也当然支持 INSERT 插入操作。

```java
public class HiEasySQL {
    public static void doSomeInsert(SQLManager sqlManager) {
        sqlManager.createInsert("table_name")
                .setColumnNames("name", "sex", "age")
                .setParams("Alex", "female", 16)
                .executeAsync();
    }
}
```

EasySQL 使用 PreparedStatement 来填充参数，无需担心 SQL 注入问题。  
对于常见类型，EasySQL 也对正确的对其进行转换。

### 静默处理

细心的的小伙伴可能发现，这一次我们的 executeAsync 内容为空，没有任何 Handler。  
在这种情况下， EasySQL 将会静默失败，不会产生任何日志。

## 信息总是千变万化的：更新(Update)

Bob 是个喜欢改名的人，于是他今天给自己起了个新名字叫 Steve。因此我们需要更新数据库中已经存在的数据：

```java
public class HiEasySQL {
    public static void updateSomething(SQLManager sqlManager) {
        sqlManager.createUpdate("table_name")
                .addCondition("name", "Bob")
                .setColumnValues("name", "Steve")
                .build().executeAsync();
    }
}

```

至此，Bob 就改名为 Steve 啦！

## 旧的不去，新的不来：删除(Delete)

最近 Steve 把它人生中买的一套房给卖了，于是我们需要将这套房从数据库中删除。  
不过，Steve 说它不记得这套房子是多久之前买的了，不过肯定是 10 年之前。

```java
public class HiEasySQL {
    public static void sayGoodBye(SQLManager sqlManager) {
        Date date = new Date(); // 使用当前日期时间戳创建一个 Date
        date.setYear(date.getYear() - 10); // 把时间滑动到 10 年之前
        sqlManager.createDelete("steve_house") // 进行删除
                .addTimeCondition("purchase_date", new Date(0), date) // 选择从1970年1月1日0点一直到10年前的所有数据
                .build()
                .executeAsync(); //执行
    }
}
```

现在 Steve 真的没有他的这套房了。

## 不管有没有，反正都要写：替换(Replace)

Steve 买了一盒牛奶，他要在他的购物清单中标记牛奶已经买了。  
不过，Steve 忘记了自己有没有将牛奶加入过购物清单。但是如果暴力 INSERT 肯定会出错，但是又觉得写个 INSERT OR UPDATE 太麻烦了，于是这件棘手的事情又丢到了我们的头上来。

```java
public class HiEasySQL {
    public static void putAnyway(SQLManager sqlManager) {
        sqlManager.createReplace("steve_list")
                .setColumnNames("item", "purchased")
                .setParams("milk", true)
                .executeAsync();
    }
}
```

生活总有简单的方法不是吗？

## 上司的任务：建表

Steve 的公司老板开发了一个 IM 软件，但是 Steve 公司运维是土豆，不会搞SQL。  
最要命的是，Steve 的公司老板还不让你碰生产环境，于是你便不能指望土豆会去帮你完成建表的任务了。  
除此之外，由于 IM 软件上的网友特能叭叭，你还需要稍微考虑下性能问题。不然你可能会被送去西伯利亚。

```java
public class HiEasySQL {
    public static void newTablePlease(SQLManager sqlManager) {
        sqlManager.createTable("steve_im_history")
                .addColumn("id", "BIGINT NOT NULL", "记录ID")
                .addColumn("sender", "VARCHAR NOT NULL", "网友UUID")
                .addColumn("message", "TEXT NULL", "网友发言")
                .addAutoIncrementColumn("id") //设置 id 列自增
                .setIndex(IndexType.PRIMARY_KEY, null, "id", "sender") //配置主键
                .setIndex(IndexType.INDEX, "sender_message_index", "sender", "message") //配置索引
                .build().executeAsync();
    }
}
```

## 上司的任务2：改表

Steve 的公司老板和 Steve 提出了一个需求，迫不得已，Steve 要修改表结构。  
然而此时表内已经存储了大量数据，不能删表再建，Steve 要想个办法对表做出相应的修改。

```java
public class HiEasySQL {
    public static void newTablePlease(SQLManager sqlManager) {
        sqlManager.alterTable("steve_im_history")
                .addColumn("ipAddress", "VARCHAR(255)")
                .executeAsync();
        sqlManager.alterTable("steve_im_history")
                .modifyColumn("message", "LONGTEXT")
                .executeAsync();
    }
}

```

多亏了我们的大力帮助。现在，Steve 被送去了南极担任公司的重要工作了。

## 北极熊的快乐生活：批量操作

Steve 到达南极之后，南极的员工把2FA密钥塞给Steve便骑着海豚跑路了。于是 Steve 除了日常工作以外还要照看公司的北极熊。  
北极熊饲养区有一套设备，监控生活在南极的北极熊的生活状态。设备每 1 小时会把缓存的数据存入到服务器里。  
然而，员工跑路的时候删库格盘了，现在 Steve 要自己想办法解决这个烂摊子了。

```java
public class HiEasySQL {
    public static void iDontLikeHere(SQLManager sqlManager) {
        sqlManager.createInsertBatch("polarbear")
                .setColumnNames("name", "temp", "hunger")
                .addParamsBatch("Karl", -17, 100)
                .addParamsBatch("Lucy", -3, 80)
                .addParamsBatch("Lily", -10, 70)
                .setReturnGeneratedKey(true)// 设定在后续返回自增主键
                .executeAsync((list) -> {/*新增行的自增主键*/});
    }
}
```

## 北极熊的熊猫之旅！？：复杂查询

Steve 翻看着跑路员工留下的为数不多的资料，发现公司在南极培育北极熊是为了让它们变成熊猫！  
只要满足 “温度 < -100C, 饱食度 > 70, 名字中带有 `PANDAKING` 关键字并以符合条件的北极熊门的名字倒序排序后的第一条” 的北极熊就有希望变成熊猫！  
现在 Steve 已经迫不及待的看看是哪只熊猫如此幸运了！

```java
public class HiEasySQL {
    public static void noBearsPlease(SQLManager sqlManager) {
        sqlManager.createQuery()
                .inTable("panda_king_proj")
                .addCondition("temp", "<", -100)
                .addCondition("hunger", ">", 70)
                .addCondition("name", "LIKE", "PANDAKING")
                .orderBy("name", false)
                .setLimit(1)
                .build().executeAsync((result) -> {
                    if (result.getResultSet().next()) {
                        System.out.println(result.getResultSet().getString("name"));
                    }
                });
    }
}
```

## 同步请求

经历人生坎坷后的 Steve 回到了自己的家：因为没能培育出熊猫来，他的老板 Async 炒了他，女友 Lambda 甩了他，连朋友 Handler 都放 Steve 鸽子，于是现在他很讨厌任何带有这两个名字的东西。

```java
public class HiEasySQL {
    public static void syncLover(SQLManager sqlManager) {
        try (SQLQuery query = sqlManager.createQuery().inTable("the_end")
                .addCondition("thanks_read", "this_stupid_guide")
                .build().execute()) {
            ResultSet set = query.getResultSet(); // SQLQuery 关闭时，ResultSet 会一同关闭
            if (set.next()) {
                set.getString("see_you_next_time");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
```

当然，有时候 Steve 也会选择更优雅一点的方式。

```java
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

public class HiEasySQL {
    // 调用此方法，直接返回结果，再在调用处统一处理错误
    public static @Nullable String eleganceNeverGone(SQLManager sqlManager) throws SQLException {
        return sqlManager.createQuery().inTable("the_end")
                .addCondition("thanks_read", "this_stupid_guide")
                .build().executeFunction(query -> {
                    if (!query.getResultSet().next()) return null;
                    else return query.getResultSet().getString("see_you_next_time");
                });
    }
}
```

Steve 终究能找到继续生活下去的办法 :)

