package cc.carm.lib.easysql.api;

import org.jetbrains.annotations.NotNull;

/**
 * SQLBuilder 是用于构建SQL语句以生成SQLAction执行操作的中间类。
 * <br>其连接了{@link SQLManager} 与 {@link SQLAction} ,避免大量的代码堆积
 * <br>也是本接口的核心功能所在
 *
 * @author CarmJos
 */
public interface SQLBuilder {

    static @NotNull String withBackQuote(@NotNull String str) {
        str = str.trim();
        return !str.isEmpty() && str.charAt(0) == '`' && str.charAt(str.length() - 1) == '`' ? str : "`" + str + "`";
    }

    static @NotNull String withQuote(@NotNull String str) {
        str = str.trim();
        return !str.isEmpty() && str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'' ? str : "'" + str + "'";
    }

    /**
     * 得到承载该Builder的对应{@link SQLManager}
     *
     * @return {@link SQLManager}
     */
    @NotNull SQLManager getManager();

}
