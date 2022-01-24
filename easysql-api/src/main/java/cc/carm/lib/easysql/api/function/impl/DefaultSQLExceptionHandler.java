package cc.carm.lib.easysql.api.function.impl;

import cc.carm.lib.easysql.api.SQLAction;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

public class DefaultSQLExceptionHandler implements SQLExceptionHandler {

	private static @Nullable SQLExceptionHandler customDefaultHandler = null;

	public static void setCustomHandler(@Nullable SQLExceptionHandler handler) {
		DefaultSQLExceptionHandler.customDefaultHandler = handler;
	}

	public static @Nullable SQLExceptionHandler getCustomHandler() {
		return customDefaultHandler;
	}

	private final SQLManager sqlManager;

	public DefaultSQLExceptionHandler(SQLManager manager) {
		this.sqlManager = manager;
	}

	protected SQLManager getManager() {
		return sqlManager;
	}

	@Override
	public void accept(SQLException exception, SQLAction<?> sqlAction) {
		getManager().getLogger().severe("Error when execute [" + sqlAction.getSQLContent() + "]");
		getManager().getLogger().severe(exception.getLocalizedMessage());
		exception.printStackTrace();
	}


}
