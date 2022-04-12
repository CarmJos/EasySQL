package cc.carm.lib.easysql.api.function;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

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
     *               实际上，仅有 {@link PreparedSQLUpdateAction} 和 {@link PreparedSQLUpdateBatchAction} 才会有传入参数。
     */
    void beforeExecute(@NotNull SQLAction<?> action, @NotNull List<Object[]> params);

    /**
     * 该方法将在 {@link SQLQuery#close()} 执行后调用。
     *
     * @param query       {@link SQLQuery} 对象
     * @param executeTime 该次查询开始执行的时间
     * @param closeTime   该次查询彻底关闭的时间
     */
    void afterQuery(@NotNull SQLQuery query, long executeTime, long closeTime);

    @SuppressWarnings("DuplicatedCode")
    static SQLDebugHandler defaultHandler(Logger logger) {
        return new SQLDebugHandler() {
            @Override
            public void beforeExecute(@NotNull SQLAction<?> action, @NotNull List<Object[]> params) {
                logger.info("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                logger.info("┣# ActionUUID: {}", action.getActionUUID());
                logger.info("┣# ActionType: {}", action.getClass().getName());
                if (action.getSQLContents().size() == 1) {
                    logger.info("┣# SQLContent: {}", action.getSQLContents().get(0));
                } else {
                    logger.info("┣# SQLContents: ");
                    int i = 0;
                    for (String sqlContent : action.getSQLContents()) {
                        logger.info("┃ [{}] {}", ++i, sqlContent);
                    }
                }
                if (params.size() == 1) {
                    Object[] param = params.get(0);
                    logger.info("┣# SQLParams({}): {}", param.length, Arrays.stream(param).map(Object::toString).reduce((a, b) -> a + ", " + b).orElse(""));
                } else if (params.size() > 1) {
                    logger.info("┣# SQLParams: ");
                    int i = 0;
                    for (Object[] param : params) {
                        logger.info("┃ [{}] {}", ++i, Arrays.stream(param).map(Object::toString).reduce((a, b) -> a + ", " + b).orElse(""));
                    }
                }
                logger.info("┣# createTime: {}", action.getCreateTime());
                logger.info("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }

            @Override
            public void afterQuery(@NotNull SQLQuery query, long executeTime, long closeTime) {
                logger.info("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                logger.info("┣# ActionUUID: {}", query.getAction().getActionUUID());
                logger.info("┣# SQLContent: {}", query.getSQLContent());
                logger.info("┣# executeCote: {} ms", (closeTime - executeTime));
                logger.info("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }
        };
    }

}
