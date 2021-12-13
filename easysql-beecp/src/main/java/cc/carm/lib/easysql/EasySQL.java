package cc.carm.lib.easysql;

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

}
