```text
    ______                    _____ ____    __ 
   / ____/___ ________  __   / ___// __ \  / / 
  / __/ / __ `/ ___/ / / /   \__ \/ / / / / /  
 / /___/ /_/ (__  ) /_/ /   ___/ / /_/ / / /___
/_____/\__,_/____/\__, /   /____/\___\_\/_____/
                 /____/                        
```

# EasySQL

[![version](https://img.shields.io/github/v/release/CarmJos/EasySQL)](https://github.com/CarmJos/EasySQL/releases)
[![License](https://img.shields.io/github/license/CarmJos/EasySQL)](https://opensource.org/licenses/GPL-3.0)
[![workflow](https://github.com/CarmJos/EasySQL/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/EasySQL/actions/workflows/maven.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/easysql/badge)](https://www.codefactor.io/repository/github/carmjos/easysql)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/EasySQL)
![](https://visitor-badge.glitch.me/badge?page_id=EasySQL.readme)

简单便捷的数据库操作工具，可自定义连接池来源。

随项目分别提供 [BeeCP](https://github.com/Chris2018998/BeeCP) 与 [Hikari](https://github.com/brettwooldridge/HikariCP~~~~)
两个连接池的版本。

## 优势

- 基于JDBC开发，可自选连接池、JDBC驱动。
- 简单便捷的增删改查接口，无需手写SQL语句。
- 额外提供部分常用情况的SQL操作
- 自动关闭数据流
- 支持同步操作与异步操作

## 开发

详细开发介绍请 [点击这里](.documentation/README.md) , JavaDoc(最新Release) 请 [点击这里](https://carmjos.github.io/EasySQL) 。

### 示例代码

您可以 [点击这里](easysql-demo/src/main/java/EasySQLDemo.java) 查看部分代码演示，更多演示详见 [开发介绍](.documentation/README.md) 。

### 依赖方式

#### Maven 依赖

<details>
<summary>远程库配置</summary>

```xml

<project>
    <repositories>
        <repository>
            <!--采用github依赖库，安全稳定，但需要配置 (推荐)-->
            <id>EasySQL</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/CarmJos/EasySQL</url>
        </repository>

        <repository>
            <!--采用我的私人依赖库，简单方便，但可能因为变故而无法使用-->
            <id>carm-repo</id>
            <name>Carm's Repo</name>
            <url>https://repo.carm.cc/repository/maven-public/</url>
        </repository>

    </repositories>
</project>
```

</details>

<details>
<summary>原生依赖</summary>

```xml

<project>
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

    </dependencies>
</project>
```

</details>

<details>
<summary>含连接池版本</summary>

```xml

<project>
    <dependencies>
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

</details>

#### Gradle 依赖

<details>
<summary>远程库配置</summary>

```groovy
repositories {
    // 采用github依赖库，安全稳定，但需要配置 (推荐)
    maven { url 'https://maven.pkg.github.com/CarmJos/EasySQL' }

    // 采用我的私人依赖库，简单方便，但可能因为变故而无法使用
    maven { url 'https://repo.carm.cc/repository/maven-public/' }
}
```

</details>

<details>
<summary>原生依赖</summary>

```groovy

dependencies {

    //对于需要提供公共接口的项目，可以仅打包API部分，方便他人调用
    api "cc.carm.lib:easysql-api:[LATEST RELEASE]"

    //如需自定义连接池，则可以仅打包实现部分，自行创建SQLManager
    api "cc.carm.lib:easysql-impl:[LATEST RELEASE]"

}
```

</details>

<details>
<summary>含连接池版本</summary>

```groovy

dependencies {

    //也可直接选择打包了连接池的版本

    api "cc.carm.lib:easysql-beecp:[LATEST RELEASE]"

    api "cc.carm.lib:easysql-hikaricp:[LATEST RELEASE]"

}
```

</details>

## 支持与捐赠

若您觉得本插件做的不错，您可以通过捐赠支持我！

感谢您对开源项目的支持！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>

## 开源协议

本项目源码采用 [The MIT License](https://opensource.org/licenses/MIT) 开源协议。
<details>
<summary>关于 MIT 协议</summary>

> MIT 协议可能是几大开源协议中最宽松的一个，核心条款是：
>
> 该软件及其相关文档对所有人免费，可以任意处置，包括使用，复制，修改，合并，发表，分发，再授权，或者销售。唯一的限制是，软件中必须包含上述版 权和许可提示。
>
> 这意味着：
> - 你可以自由使用，复制，修改，可以用于自己的项目。
> - 可以免费分发或用来盈利。
> - 唯一的限制是必须包含许可声明。
>
> MIT 协议是所有开源许可中最宽松的一个，除了必须包含许可声明外，再无任何限制。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
</details>