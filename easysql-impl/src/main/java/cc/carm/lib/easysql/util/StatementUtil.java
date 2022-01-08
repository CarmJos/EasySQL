package cc.carm.lib.easysql.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatementUtil {

    /**
     * 创建一个 {@link PreparedStatement} 。
     *
     * @param connection 数据库连接
     * @param sql        SQL语句，使用"?"做为占位符
     * @param params     "?"所代表的对应参数列表
     * @return 完成参数填充的 {@link PreparedStatement}
     */
    public static PreparedStatement createPrepareStatement(
            Connection connection, String sql, Object[] params
    ) throws SQLException {
        return createPrepareStatement(connection, sql, params, false);
    }

    /**
     * 创建一个 {@link PreparedStatement} 。
     *
     * @param connection         数据库连接
     * @param sql                SQL语句，使用"?"做为占位符
     * @param params             "?"所代表的对应参数列表
     * @param returnGeneratedKey 是否会返回自增主键
     * @return 完成参数填充的 {@link PreparedStatement}
     */
    public static PreparedStatement createPrepareStatement(
            Connection connection, String sql, Object[] params, boolean returnGeneratedKey
    ) throws SQLException {
        sql = sql.trim();
        PreparedStatement statement = connection.prepareStatement(sql, returnGeneratedKey ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        Map<Integer, Integer> nullTypeMap = new HashMap<>();
        if (params != null) fillParams(statement, Arrays.asList(params), nullTypeMap);
        statement.addBatch();
        return statement;
    }

    /**
     * 创建批量操作的一个 {@link PreparedStatement} 。
     *
     * @param connection  数据库连接
     * @param sql         SQL语句，使用"?"做为占位符
     * @param paramsBatch "?"所代表的对应参数列表
     * @return 完成参数填充的 {@link PreparedStatement}
     */
    public static PreparedStatement createPrepareStatementBatch(
            Connection connection, String sql, Iterable<Object[]> paramsBatch
    ) throws SQLException {
        return createPrepareStatementBatch(connection, sql, paramsBatch, false);
    }

    /**
     * 创建批量操作的一个 {@link PreparedStatement} 。
     *
     * @param connection         数据库连接
     * @param sql                SQL语句，使用"?"做为占位符
     * @param paramsBatch        "?"所代表的对应参数列表
     * @param returnGeneratedKey 是否会返回自增主键
     * @return 完成参数填充的 {@link PreparedStatement}
     */
    public static PreparedStatement createPrepareStatementBatch(
            Connection connection, String sql, Iterable<Object[]> paramsBatch, boolean returnGeneratedKey
    ) throws SQLException {

        sql = sql.trim();
        PreparedStatement statement = connection.prepareStatement(sql, returnGeneratedKey ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        Map<Integer, Integer> nullTypeMap = new HashMap<>();
        for (Object[] params : paramsBatch) {
            fillParams(statement, Arrays.asList(params), nullTypeMap);
            statement.addBatch();
        }

        return statement;
    }


    /**
     * 填充PreparedStatement的参数。
     *
     * @param statement PreparedStatement
     * @param params    SQL参数
     * @return {@link PreparedStatement} 填充参数后的PreparedStatement
     * @throws SQLException SQL执行异常
     */
    public static PreparedStatement fillParams(
            PreparedStatement statement, Iterable<?> params
    ) throws SQLException {
        return fillParams(statement, params, null);
    }

    /**
     * 填充PreparedStatement的参数。
     *
     * @param statement PreparedStatement
     * @param params    SQL参数
     * @param nullCache null参数的类型缓存，避免循环中重复获取类型
     * @return 完成参数填充的 {@link PreparedStatement}
     */
    public static PreparedStatement fillParams(
            PreparedStatement statement, Iterable<?> params, Map<Integer, Integer> nullCache
    ) throws SQLException {
        if (null == params) {
            return statement;// 无参数
        }

        int paramIndex = 1;//第一个参数从1计数
        for (Object param : params) {
            setParam(statement, paramIndex++, param, nullCache);
        }
        return statement;
    }

    /**
     * 获取null字段对应位置的数据类型
     * 如果类型获取失败将使用默认的 {@link Types#VARCHAR}
     *
     * @param statement  {@link PreparedStatement}
     * @param paramIndex 参数序列，第一位从1开始
     * @return 数据类型，默认为 {@link Types#VARCHAR}
     */
    public static int getNullType(PreparedStatement statement, int paramIndex) {
        int sqlType = Types.VARCHAR;

        final ParameterMetaData pmd;
        try {
            pmd = statement.getParameterMetaData();
            sqlType = pmd.getParameterType(paramIndex);
        } catch (SQLException ignore) {
        }

        return sqlType;
    }

    /**
     * 为 {@link PreparedStatement} 设置单个参数
     *
     * @param preparedStatement {@link PreparedStatement}
     * @param paramIndex        参数序列，从1开始
     * @param param             参数，不能为{@code null}
     * @param nullCache         用于缓存参数为null位置的类型，避免重复获取
     */
    private static void setParam(
            PreparedStatement preparedStatement, int paramIndex, Object param,
            Map<Integer, Integer> nullCache
    ) throws SQLException {

        if (param == null) {
            Integer type = (null == nullCache) ? null : nullCache.get(paramIndex);
            if (null == type) {
                type = getNullType(preparedStatement, paramIndex);
                if (null != nullCache) {
                    nullCache.put(paramIndex, type);
                }
            }
            preparedStatement.setNull(paramIndex, type);
        }

        // 针对UUID特殊处理，避免元数据直接插入
        if (param instanceof UUID) {
            preparedStatement.setString(paramIndex, param.toString());
            return;
        }

        // 日期特殊处理，默认按照时间戳传入，避免毫秒丢失
        if (param instanceof java.util.Date) {
            if (param instanceof Date) {
                preparedStatement.setDate(paramIndex, (Date) param);
            } else if (param instanceof Time) {
                preparedStatement.setTime(paramIndex, (Time) param);
            } else {
                preparedStatement.setTimestamp(paramIndex, new Timestamp(((java.util.Date) param).getTime()));
            }
            return;
        }

        // 针对大数字类型的特殊处理
        if (param instanceof Number) {
            if (param instanceof BigDecimal) {
                // BigDecimal的转换交给JDBC驱动处理
                preparedStatement.setBigDecimal(paramIndex, (BigDecimal) param);
                return;
            }
            if (param instanceof BigInteger) {
                // BigInteger转为BigDecimal
                preparedStatement.setBigDecimal(paramIndex, new BigDecimal((BigInteger) param));
                return;
            }
            // 忽略其它数字类型，按照默认类型传入
        }

        // 其它参数类型直接插入
        preparedStatement.setObject(paramIndex, param);
    }

}
