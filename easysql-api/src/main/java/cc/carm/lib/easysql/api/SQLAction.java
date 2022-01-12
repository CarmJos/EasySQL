package cc.carm.lib.easysql.api;

import cc.carm.lib.easysql.api.function.SQLExceptionHandler;
import cc.carm.lib.easysql.api.function.SQLFunction;
import cc.carm.lib.easysql.api.function.SQLHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.UUID;

/**
 * SQLAction 是用于承载SQL语句并进行处理、返回的基本类。
 *
 * <ul>
 *     <li>同步执行 {@link #execute()}, {@link #execute(SQLFunction, SQLExceptionHandler)}
 *     <br>同步执行方法中有会抛出异常的方法与不抛出异常的方法，
 *     <br>若选择不抛出异常，则返回值可能为空，需要特殊处理。</li>
 *
 *     <li>异步执行 {@link #executeAsync(SQLHandler, SQLExceptionHandler)}
 *     <br>异步执行时将提供成功与异常两种处理方式
 *     <br>可自行选择是否对数据或异常进行处理
 *     <br>默认的异常处理器为 {@link #defaultExceptionHandler()}</li>
 * </ul>
 *
 * @param <T> 需要返回的类型
 * @author CarmJos
 * @since 0.0.1
 */
public interface SQLAction<T> {

	/**
	 * 得到该Action的UUID
	 *
	 * @return UUID
	 */
	@NotNull UUID getActionUUID();

	/**
	 * 得到短八位格式的UUID
	 *
	 * @return UUID(8)
	 */
	@NotNull String getShortID();

	/**
	 * 得到该Action的创建时间
	 *
	 * @return 创建时间
	 */
	long getCreateTime();

	/**
	 * 得到该Action所要执行的源SQL语句
	 *
	 * @return 源SQL语句
	 */
	@NotNull String getSQLContent();

	/**
	 * 得到承载该Action的对应{@link SQLManager}
	 *
	 * @return {@link SQLManager}
	 */
	@NotNull SQLManager getManager();

	/**
	 * 执行该Action对应的SQL语句
	 *
	 * @return 指定数据类型
	 * @throws SQLException 当SQL操作出现问题时抛出
	 */
	@NotNull T execute() throws SQLException;


	/**
	 * 执行语句并返回值
	 *
	 * @param exceptionHandler 异常处理器 默认为 {@link #defaultExceptionHandler()}
	 * @return 指定类型数据
	 */
	@Nullable
	default T execute(@Nullable SQLExceptionHandler exceptionHandler) {
		return execute(t -> t, exceptionHandler);
	}

	/**
	 * 执行语句并处理返回值
	 *
	 * @param function 处理方法
	 * @param <R>      需要返回的内容
	 * @return 指定类型数据
	 * @throws SQLException 当SQL操作出现问题时抛出
	 */
	@Nullable
	default <R> R executeFunction(@NotNull SQLFunction<T, R> function) throws SQLException {
		try {
			T value = execute();
			return function.apply(value);
		} catch (SQLException exception) {
			throw new SQLException(exception);
		}
	}

	/**
	 * 执行语句并处理返回值
	 *
	 * @param function         处理方法
	 * @param exceptionHandler 异常处理器 默认为 {@link #defaultExceptionHandler()}
	 * @param <R>              需要返回的内容
	 * @return 指定类型数据
	 */
	@Nullable
	default <R> R execute(@NotNull SQLFunction<T, R> function,
						  @Nullable SQLExceptionHandler exceptionHandler) {
		try {
			return executeFunction(function);
		} catch (SQLException exception) {
			handleException(exceptionHandler, exception);
			return null;
		}
	}

	/**
	 * 异步执行SQL语句，采用默认异常处理，无需返回值。
	 */
	default void executeAsync() {
		executeAsync(null);
	}

	/**
	 * 异步执行SQL语句
	 *
	 * @param success 成功时的操作
	 */
	default void executeAsync(@Nullable SQLHandler<T> success) {
		executeAsync(success, null);
	}

	/**
	 * 异步执行SQL语句
	 *
	 * @param success 成功时的操作
	 * @param failure 异常处理器 默认为 {@link SQLAction#defaultExceptionHandler()}
	 */
	void executeAsync(@Nullable SQLHandler<T> success,
					  @Nullable SQLExceptionHandler failure);

	default void handleException(@Nullable SQLExceptionHandler handler, SQLException exception) {
		if (handler == null) handler = defaultExceptionHandler();
		handler.accept(exception, this);
	}

	/**
	 * @return 默认的异常处理器
	 */
	default SQLExceptionHandler defaultExceptionHandler() {
		return (exception, action) -> {
			getManager().getLogger().severe("Error when execute [" + action.getSQLContent() + "]");
			getManager().getLogger().severe(exception.getLocalizedMessage());
			exception.printStackTrace();
		};
	}


}
