package cc.carm.lib.easysql.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * SQLAction 是用于承载SQL语句并进行处理、返回的基本类。
 *
 * <ul>
 *     <li>同步执行 {@link #execute()}, {@link #execute(Function, BiConsumer)}
 *     <br>同步执行方法中有会抛出异常的方法与不抛出异常的方法，
 *     <br>若选择不抛出异常，则返回值可能为空，需要特殊处理。</li>
 *
 *     <li>异步执行 {@link #executeAsync(Consumer, BiConsumer)}
 *     <br>异步执行时将提供成功与异常两种处理方式
 *     <br>可自行选择是否对数据或异常进行处理
 *     <br>默认的异常处理器为 {@link #defaultExceptionHandler()}</li>
 * </ul>
 *
 * <b>注意： 无论是否异步，都不需要自行关闭ResultSet，本API已自动关闭</b>
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
	 * 执行语句并处理返回值
	 *
	 * @param function         处理方法
	 * @param exceptionHandler 异常处理器 默认为 {@link #defaultExceptionHandler()}
	 * @param <R>              需要返回的内容
	 * @return 指定类型数据
	 */
	@Nullable
	default <R> R execute(@NotNull Function<T, R> function, @Nullable BiConsumer<SQLException, SQLAction<T>> exceptionHandler) {
		T value = execute(exceptionHandler);
		if (value == null) return null;
		else return function.apply(value);
	}

	/**
	 * 执行语句并返回值
	 *
	 * @param exceptionHandler 异常处理器 默认为 {@link #defaultExceptionHandler()}
	 * @return 指定类型数据
	 */
	@Nullable
	default T execute(@Nullable BiConsumer<SQLException, SQLAction<T>> exceptionHandler) {
		if (exceptionHandler == null) exceptionHandler = defaultExceptionHandler();
		T value = null;
		try {
			value = execute();
		} catch (SQLException exception) {
			exceptionHandler.accept(exception, this);
		}
		return value;
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
	default void executeAsync(@Nullable Consumer<T> success) {
		executeAsync(success, null);
	}

	/**
	 * 异步执行SQL语句
	 *
	 * @param success 成功时的操作
	 * @param failure 异常处理器 默认为 {@link SQLAction#defaultExceptionHandler()}
	 */
	void executeAsync(@Nullable Consumer<T> success, @Nullable BiConsumer<SQLException, SQLAction<T>> failure);

	/**
	 * @return 默认的异常处理器
	 */
	default BiConsumer<SQLException, SQLAction<T>> defaultExceptionHandler() {
		return (exception, action) -> {
			getManager().getLogger().severe("Error when execute [" + action.getSQLContent() + "]");
			getManager().getLogger().severe(exception.getLocalizedMessage());
			exception.printStackTrace();
		};
	}


}
