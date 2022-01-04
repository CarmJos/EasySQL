package cc.carm.lib.easysql;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.SQLQuery;
import cc.carm.lib.easysql.api.util.TimeDateUtils;
import cc.carm.lib.easysql.manager.SQLManagerImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class EasySQL {

	public static SQLManagerImpl createManager(
			@NotNull String driver, @NotNull String url,
			@NotNull String username, @Nullable String password) {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(driver);
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		return createManager(config);
	}

	public static SQLManagerImpl createManager(@NotNull Properties properties) {
		return createManager(new HikariConfig(properties));
	}

	public static SQLManagerImpl createManager(@NotNull HikariConfig config) {
		return new SQLManagerImpl(new HikariDataSource(config));
	}

	public static void shutdownManager(SQLManager manager, boolean forceClose, boolean outputActiveQuery) {
		if (!manager.getActiveQuery().isEmpty()) {
			manager.getLogger().severe("There are " + manager.getActiveQuery().size() + " connections still running");
			for (SQLQuery value : manager.getActiveQuery().values()) {
				if (outputActiveQuery) {
					manager.getLogger().severe("#" + value.getAction().getShortID() + " -> " + value.getSQLContent());
					manager.getLogger().severe("- execute at " + TimeDateUtils.getTimeString(value.getExecuteTime()));
				}
				if (forceClose) value.close();
			}
		}
		if (manager.getDataSource() instanceof HikariDataSource) {
			//Close hikari pool
			((HikariDataSource) manager.getDataSource()).close();
		}
	}

	public static void shutdownManager(SQLManager manager) {
		shutdownManager(manager, true, manager.isDebugMode());
	}

}
