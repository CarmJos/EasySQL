package cc.carm.lib.easysql.api.function;

import cc.carm.lib.easysql.api.SQLAction;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.function.BiConsumer;

/**
 * 异常处理器。
 * <br> 在使用 {@link SQLAction#execute(SQLExceptionHandler)} 等相关方法时，
 * 如果发生异常，则会调用错误处理器进行错误内容的输出提示。
 */
@FunctionalInterface
public interface SQLExceptionHandler extends BiConsumer<SQLException, SQLAction<?>> {

    /**
     * 默认的异常处理器，将详细的输出相关错误与错误来源。
     *
     * @param logger 用于输出错误信息的Logger。
     * @return 输出详细信息的错误处理器。
     */
    static SQLExceptionHandler detailed(Logger logger) {
        return (exception, sqlAction) -> {
            logger.error("Error occurred while executing SQL: ");
            int i = 1;
            for (String content : sqlAction.getSQLContents()) {
                logger.error(String.format("#%d {%s}", i, content));
                i++;
            }
            exception.printStackTrace();
        };
    }

    /**
     * “安静“ 的错误处理器，发生错误什么都不做。
     * 强烈不建议把此处理器作为默认处理器使用！
     *
     * @return 无输出的处理器。
     */
    @Deprecated
    static SQLExceptionHandler silent() {
        return (exception, sqlAction) -> {
        };
    }

}
