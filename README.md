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

详细开发介绍请 [点击这里](pom.xml) 。

### 示例代码

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
            <version>[LATEST VERSION]</version>
            <scope>compile</scope>
        </dependency>

        <!--如需自定义连接池，则可以仅打包实现部分，自行创建SQLManager-->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easysql-impl</artifactId>
            <version>[LATEST VERSION]</version>
            <scope>compile</scope>
        </dependency>

        <!--如需自定义连接池，则可以仅打包实现部分，自行创建SQLManager-->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easysql-beecp</artifactId>
            <version>[LATEST VERSION]</version>
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
