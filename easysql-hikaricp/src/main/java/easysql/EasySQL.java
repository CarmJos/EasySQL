package easysql;

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

}
