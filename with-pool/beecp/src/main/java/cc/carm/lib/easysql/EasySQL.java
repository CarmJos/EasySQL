package cc.carm.lib.easysql;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.util.TimeDateUtils;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EasySQL {

    public static SQLManagerImpl createManager(
            @NotNull String driver, @NotNull String url,
            @NotNull String username, @Nullable String password) {
        return createManager(new BeeDataSourceConfig(driver, url, username, password));
    }

    public static SQLManagerImpl createManager(@NotNull BeeDataSourceConfig config) {
        return new SQLManagerImpl(new BeeDataSource(config));
    }


    public static void shutdownManager(SQLManager manager, boolean forceClose, boolean outputActiveQuery) {
        if (!manager.getActiveQuery().isEmpty()) {
            manager.getLogger().warn("There are " + manager.getActiveQuery().size() + " connections still running");
            for (SQLQuery value : manager.getActiveQuery().values()) {
                if (outputActiveQuery) {
                    manager.getLogger().warn(String.format("#%s -> %s", value.getAction().getShortID(), value.getSQLContent()));
                    manager.getLogger().warn(String.format("- execute at %s", TimeDateUtils.getTimeString(value.getExecuteTime())));
                }
                if (forceClose) value.close();
            }
        }
        if (manager.getDataSource() instanceof BeeDataSource) {
            //Close bee connection pool
            ((BeeDataSource) manager.getDataSource()).close();
        }
    }

    public static void shutdownManager(SQLManager manager) {
        shutdownManager(manager, true, manager.isDebugMode());
    }

}
