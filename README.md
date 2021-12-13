```text
    ______                    _____ ____    __ 
   / ____/___ ________  __   / ___// __ \  / / 
  / __/ / __ `/ ___/ / / /   \__ \/ / / / / /  
 / /___/ /_/ (__  ) /_/ /   ___/ / /_/ / / /___
/_____/\__,_/____/\__, /   /____/\___\_\/_____/
                 /____/                        
```

# EasySQL

简单便捷的数据库操作工具，可自定义连接池来源。

随项目分别提供 [BeeCP](https://github.com/Chris2018998/BeeCP) 与 [Hikari](https://github.com/brettwooldridge/HikariCP~~~~)
两个连接池的版本。

## 优势

- 基于JDBC开发，可自选连接池、JDBC驱动。
- 简单便捷的增删改查接口，无需手写SQL语句。
- 额外提供部分常用情况的SQL操作
    - 存在则更新，不存在则插入
    - 创建表
    - 修改表
    - ...
- 支持同步操作与异步操作

## 开发

详细开发介绍请 [点击这里](.documentation/INDEX.md) 。

### 示例代码

```java
public class EasySQLDemo {

	public void createTable(SQLManager sqlManager) {
		//异步创建表
		sqlManager.createTable("users")
				.addColumn("id", "INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY")
				.addColumn("username", "VARCHAR(16) NOT NULL UNIQUE KEY")
				.addColumn("email", "VARCHAR(32)")
				.addColumn("phone", "VARCHAR(16)")
				.addColumn("registerTime", "DATETIME NOT NULL")
				.build().execute(null /* 不处理错误 */);
	}

	public void sqlQuery(SQLManager sqlManager) {
		// 同步SQL查询
		try (SQLQuery query = sqlManager.createQuery()
				.inTable("users") // 在users表中查询
				.addCondition("id", ">", 5) // 限定 id 要大于5
				.addCondition("email", null) // 限定查询email字段为空
				.addNotNullCondition("phone") // 限定 phone字段不为空
				.addTimeCondition(
						"registerTime", // 时间字段
						System.currentTimeMillis() - 100000, //限制开始时间
						-1) //不限制结束时间
				.build().execute()) {
			ResultSet resultSet = query.getResultSet();
			//do something

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	public void sqlQueryAsync(SQLManager sqlManager) {
		// 异步SQL查询
		sqlManager.createQuery()
				.inTable("users") // 在users表中查询
				.addCondition("id", 5) // 限定 id 为 5
				.setLimit(1).build().executeAsync(success -> {
					ResultSet resultSet = success.getResultSet();
					//do something
				}, exception -> {
					//do something
				});
	}

	public void sqlInsert(SQLManager sqlManager) {
		// 同步SQL插入 （不使用try-catch的情况下，返回的数值可能为空。）
		Integer id = sqlManager.createInsert("users")
				.setColumnNames("username", "phone", "email", "registerTime")
				.setParams("CarmJos", "18888888888", "carm@carm.cc", TimeDateUtils.getCurrentTime())
				.setKeyIndex(1) // 设定自增主键的index，将会在后续返回自增主键
				.execute(exception -> {
					// 处理异常
				});
	}

}
```

更多演示详见开发介绍。

### 依赖方式 (Maven)

```xml

<project>
    <repositories>
        <repository>
            <id>github</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/CarmJos/EasySQL</url>
        </repository>
    </repositories>
    <dependencies>
        <!--对于需要提供公共接口的项目，可以仅打包API部分，方便他人调用-->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easysql-api</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

        <!--如需自定义连接池，则可以仅打包实现部分，自行创建SQLManager-->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easysql-impl</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

        <!--如需自定义连接池，则可以仅打包实现部分，自行创建SQLManager-->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easysql-beecp</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

        <!--也可直接选择打包了连接池的版本-->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easysql-beecp</artifactId>
            <version>[LATEST VERSION]</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easysql-hikaricp</artifactId>
            <version>[LATEST VERSION]</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>
</project>

```

## 支持与捐赠

若您觉得本插件做的不错，您可以通过捐赠支持我！

感谢您对开源项目的支持！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>
