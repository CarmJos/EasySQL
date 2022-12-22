package cc.carm.lib.easysql.api.function;

import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.base.PreparedUpdateAction;
import cc.carm.lib.easysql.api.action.base.PreparedBatchUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 异常处理器。
 * <br> 在使用 {@link SQLAction#execute(SQLExceptionHandler)} 等相关方法时，
 * 如果发生异常，则会调用错误处理器进行错误内容的输出提示。
 */

public interface SQLDebugHandler {
    /**
     * 该方法将在 {@link SQLAction#execute()} 执行前调用。
     *
     * @param action {@link SQLAction} 对象
     * @param params 执行传入的参数列表。
     *               实际上，仅有 {@link PreparedUpdateAction} 和 {@link PreparedBatchUpdateAction} 才会有传入参数。
     */
    void beforeExecute(@NotNull SQLAction<?> action, @NotNull List<@Nullable Object[]> params);

    /**
     * 该方法将在 {@link SQLQuery#close()} 执行后调用。
     *
     * @param query           {@link SQLQuery} 对象
     * @param executeNanoTime 该次查询开始执行的时间 (单位：纳秒)
     * @param closeNanoTime   该次查询彻底关闭的时间 (单位：纳秒)
     */
    void afterQuery(@NotNull SQLQuery query, long executeNanoTime, long closeNanoTime);

    default String parseParams(@Nullable Object[] params) {
        if (params == null) return "<#NULL>";
        else if (params.length == 0) return "<#EMPTY>";

        List<String> paramsString = new ArrayList<>();
        for (Object param : params) {
            if (param == null) paramsString.add("NULL");
            else paramsString.add(param.toString());
        }
        return String.join(", ", paramsString);
    }

    @SuppressWarnings("DuplicatedCode")
    static SQLDebugHandler defaultHandler(Logger logger) {
        return new SQLDebugHandler() {

            @Override
            public void beforeExecute(@NotNull SQLAction<?> action, @NotNull List<@Nullable Object[]> params) {
                logger.info("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                logger.info("┣# 操作编号: {}", action.getActionUUID());
                logger.info("┣# 操作类型: {}", action.getClass().getSimpleName());
                if (action.getSQLContents().size() == 1) {
                    logger.info("┣# SQL语句: {}", action.getSQLContents().get(0));
                } else {
                    logger.info("┣# SQL语句: ");
                    int i = 0;
                    for (String sqlContent : action.getSQLContents()) {
                        logger.info("┃ - [{}] {}", ++i, sqlContent);
                    }
                }
                if (params.size() == 1) {
                    Object[] param = params.get(0);
                    if (param != null) {
                        logger.info("┣# SQL参数: {}", parseParams(param));
                    }
                } else if (params.size() > 1) {
                    logger.info("┣# SQL参数: ");
                    int i = 0;
                    for (Object[] param : params) {
                        logger.info("┃ - [{}] {}", ++i, parseParams(param));
                    }
                }
                logger.info("┣# 创建时间: {}", action.getCreateTime(TimeUnit.MILLISECONDS));
                logger.info("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }

            @Override
            public void afterQuery(@NotNull SQLQuery query, long executeNanoTime, long closeNanoTime) {
                logger.info("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                logger.info("┣# 操作编号: {}", query.getAction().getActionUUID());
                logger.info("┣# SQL语句: {}", query.getSQLContent());
                logger.info("┣# 关闭时间: {}  (cost {} ms)",
                        TimeUnit.NANOSECONDS.toMillis(closeNanoTime),
                        ((double) (closeNanoTime - executeNanoTime) / 1000000)
                );
                logger.info("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
        };
    }

}
