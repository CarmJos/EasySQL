package cc.carm.lib.easysql.api.enums;

/**
 * 事务隔离级别。
 * 部分Javadoc来自文章 <a href="https://cloud.tencent.com/developer/article/1865041">《理解事务的4种隔离级别》</a> 。
 */
public enum IsolationLevel {

    /**
     * 读未提交。即一个事务可以读取另一个未提交事务的数据。
     */
    READ_UNCOMMITTED,
    /**
     * 读提交。即一个事务要等另一个事务提交后才能读取数据。
     */
    READ_COMMITTED,
    /**
     * 重复读。即在开始读取数据（事务开启）时，不再允许修改操作。
     */
    REPEATED_READ,
    /**
     * 序列化读取。此为最高隔离等级。在该级别下，事务串行化顺序执行，可以避免脏读、不可重复读与幻读。
     * <br>注意： 这种事务隔离级别效率低下，比较耗数据库性能，一般不使用。
     */
    SERIALIZABLE;

}
