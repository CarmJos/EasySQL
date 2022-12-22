package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.action.query.QueryAction;
import cc.carm.lib.easysql.api.action.query.PreparedQueryAction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * SQLQuery 是一个查询中间接口，用于查询操作的封装。
 *
 * @author CarmJos
 */
public interface SQLQuery extends AutoCloseable {

    /**
     * 获取该查询创建的时间
     * <br>注意，此处获得的时间非时间戳毫秒数，仅用于计算耗时。
     *
     * @return 创建时间
     */
    default long getExecuteTime() {
        return getExecuteTime(TimeUnit.MILLISECONDS);
    }

    /**
     * 获取该查询创建的时间
     * <br>注意，此处获得的时间非时间戳毫秒数，仅用于计算耗时。
     *
     * @param timeUnit 时间单位
     * @return 创建时间
     */
    long getExecuteTime(TimeUnit timeUnit);

    /**
     * 得到承载该SQLQuery的对应{@link SQLManager}
     *
     * @return {@link SQLManager}
     */
    SQLManager getManager();

    /**
     * 得到承载该SQLQuery的对应{@link QueryAction}
     *
     * @return {@link QueryAction} 或 {@link PreparedQueryAction}
     */
    QueryAction getAction();

    ResultSet getResultSet();

    /**
     * 得到设定的SQL语句
     *
     * @return SQL语句
     */
    String getSQLContent();

    /**
     * 关闭所有内容
     */
    @Override
    void close();

    Statement getStatement();

    Connection getConnection();

}
