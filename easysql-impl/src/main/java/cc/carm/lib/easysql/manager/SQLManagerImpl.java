package cc.carm.lib.easysql.manager;

import cc.carm.lib.easysql.action.PreparedSQLBatchUpdateActionImpl;
import cc.carm.lib.easysql.action.PreparedSQLUpdateActionImpl;
import cc.carm.lib.easysql.action.SQLUpdateActionImpl;
import cc.carm.lib.easysql.action.SQLUpdateBatchActionImpl;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;
import cc.carm.lib.easysql.api.action.PreparedSQLUpdateBatchAction;
import cc.carm.lib.easysql.api.action.SQLUpdateBatchAction;
import cc.carm.lib.easysql.api.builder.*;
import cc.carm.lib.easysql.builder.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SQLManagerImpl implements SQLManager {

	private final Logger LOGGER;
	private final DataSource dataSource;
	ConcurrentHashMap<UUID, SQLQuery> activeQuery = new ConcurrentHashMap<>();

	boolean debug = false;

	public SQLManagerImpl(@NotNull DataSource dataSource) {
		this(dataSource, null);
	}

	public SQLManagerImpl(@NotNull DataSource dataSource, @Nullable String name) {
		this.LOGGER = Logger.getLogger("SQLManager" + (name != null ? "#" + name : ""));
		this.dataSource = dataSource;
	}

	@Override
	public boolean isDebugMode() {
		return this.debug;
	}

	@Override
	public void setDebugMode(boolean enable) {
		this.debug = enable;
	}

	public void debug(String msg) {
		if (isDebugMode()) getLogger().info("[DEBUG] " + msg);
	}

	public Logger getLogger() {
		return LOGGER;
	}


	@Override
	public @NotNull DataSource getDataSource() {
		return this.dataSource;
	}

	@Override
	public @NotNull Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	@Override
	public @NotNull Map<UUID, SQLQuery> getActiveQuery() {
		return this.activeQuery;
	}

	@Override
	public Integer executeSQL(String sql) {
		return new SQLUpdateActionImpl(this, sql).execute(null);
	}

	@Override
	public Integer executeSQL(String sql, Object[] params) {
		return new PreparedSQLUpdateActionImpl(this, sql, params).execute(null);
	}

	@Override
	public List<Integer> executeSQLBatch(String sql, Iterable<Object[]> paramsBatch) {
		return new PreparedSQLBatchUpdateActionImpl(this, sql)
				.setAllParams(paramsBatch)
				.execute(null);
	}

	@Override
	public List<Integer> executeSQLBatch(@NotNull String sql, String[] moreSQL) {
		SQLUpdateBatchAction action = new SQLUpdateBatchActionImpl(this, sql);
		if (moreSQL != null && moreSQL.length > 0) {
			Arrays.stream(moreSQL).forEach(action::addBatch);
		}
		return action.execute(null);
	}

	@Override
	public @Nullable List<Integer> executeSQLBatch(@NotNull Iterable<String> sqlBatch) {
		Iterator<String> iterator = sqlBatch.iterator();
		if (!iterator.hasNext()) return null; // PLEASE GIVE IT SOMETHING

		SQLUpdateBatchAction action = new SQLUpdateBatchActionImpl(this, iterator.next());
		while (iterator.hasNext()) {
			action.addBatch(iterator.next());
		}

		return action.execute(null);
	}

	@Override
	public TableCreateBuilder createTable(@NotNull String tableName) {
		return new TableCreateBuilderImpl(this, tableName);
	}

	@Override
	public QueryBuilder createQuery() {
		return new QueryBuilderImpl(this);
	}

	@Override
	public InsertBuilder<PreparedSQLUpdateBatchAction> createInsertBatch(@NotNull String tableName) {
		return new InsertBuilderImpl<PreparedSQLUpdateBatchAction>(this, tableName) {
			@Override
			public PreparedSQLUpdateBatchAction setColumnNames(List<String> columnNames) {
				return new PreparedSQLBatchUpdateActionImpl(getManager(), buildSQL(getTableName(), columnNames));
			}
		};
	}

	@Override
	public InsertBuilder<PreparedSQLUpdateAction> createInsert(@NotNull String tableName) {
		return new InsertBuilderImpl<PreparedSQLUpdateAction>(this, tableName) {
			@Override
			public PreparedSQLUpdateAction setColumnNames(List<String> columnNames) {
				return new PreparedSQLUpdateActionImpl(getManager(), buildSQL(getTableName(), columnNames));
			}
		};
	}

	@Override
	public ReplaceBuilder<PreparedSQLUpdateBatchAction> createReplaceBatch(@NotNull String tableName) {
		return new ReplaceBuilderImpl<PreparedSQLUpdateBatchAction>(this, tableName) {
			@Override
			public PreparedSQLUpdateBatchAction setColumnNames(List<String> columnNames) {
				return new PreparedSQLBatchUpdateActionImpl(getManager(), buildSQL(getTableName(), columnNames));
			}
		};
	}

	@Override
	public ReplaceBuilder<PreparedSQLUpdateAction> createReplace(@NotNull String tableName) {
		return new ReplaceBuilderImpl<PreparedSQLUpdateAction>(this, tableName) {
			@Override
			public PreparedSQLUpdateAction setColumnNames(List<String> columnNames) {
				return new PreparedSQLUpdateActionImpl(getManager(), buildSQL(getTableName(), columnNames));
			}
		};
	}

	@Override
	public UpdateBuilder createUpdate(@NotNull String tableName) {
		return new UpdateBuilderImpl(this, tableName);
	}

	@Override
	public DeleteBuilder createDelete(@NotNull String tableName) {
		return new DeleteBuilderImpl(this, tableName);
	}


}

