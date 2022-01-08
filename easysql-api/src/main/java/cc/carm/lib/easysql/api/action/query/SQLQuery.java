package cc.carm.lib.easysql.api.action.query;

import cc.carm.lib.easysql.api.SQLManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public interface SQLQuery extends AutoCloseable {

    /**
     * 获取该查询创建的时间
     *
     * @return 创建时间
     */
    long getExecuteTime();

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
    void close();

    Statement getStatement();

    Connection getConnection();

}
