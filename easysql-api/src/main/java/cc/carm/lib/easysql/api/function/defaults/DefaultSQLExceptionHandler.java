package cc.carm.lib.easysql.api.function.defaults;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.action.SQLUpdateBatchAction;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.logging.Logger;

public class DefaultSQLExceptionHandler implements SQLExceptionHandler {

	private static @Nullable SQLExceptionHandler customDefaultHandler = null;

	public static void setCustomHandler(@Nullable SQLExceptionHandler handler) {
		DefaultSQLExceptionHandler.customDefaultHandler = handler;
	}

	public static @Nullable SQLExceptionHandler getCustomHandler() {
		return customDefaultHandler;
	}

	public static @NotNull SQLExceptionHandler get(Logger logger) {
		if (getCustomHandler() != null) return getCustomHandler();
		else return new DefaultSQLExceptionHandler(logger);
	}

	private final Logger logger;

	public DefaultSQLExceptionHandler(Logger logger) {
		this.logger = logger;
	}

	protected Logger getLogger() {
		return logger;
	}

	@Override
	public void accept(SQLException exception, SQLAction<?> sqlAction) {
		if (sqlAction instanceof SQLUpdateBatchAction) {

			getLogger().severe("Error when execute SQLs : ");
			int i = 1;
			for (String content : ((SQLUpdateBatchAction) sqlAction).getSQLContents()) {
				getLogger().severe("#" + i + " {" + content + "}");
				i++;
			}

		} else {
			getLogger().severe("Error when execute { " + sqlAction.getSQLContent() + " }");
		}
		exception.printStackTrace();
	}


}
