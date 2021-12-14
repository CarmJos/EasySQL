package cc.carm.lib.easysql.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SQLAction<T> {

	@NotNull UUID getActionUUID();

	@NotNull String getShortID();

	long getCreateTime();

	@NotNull String getSQLContent();

	@NotNull SQLManager getManager();

	@NotNull T execute() throws SQLException;

	@Nullable
	default <R> R execute(@NotNull Function<T, R> function, @Nullable BiConsumer<SQLException, SQLAction<T>> exceptionHandler) {
		T value = execute(exceptionHandler);
		if (value == null) return null;
		else return function.apply(value);
	}

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

	default void executeAsync() {
		executeAsync(null);
	}

	default void executeAsync(@Nullable Consumer<T> success) {
		executeAsync(success, null);
	}

	void executeAsync(@Nullable Consumer<T> success, @Nullable BiConsumer<SQLException, SQLAction<T>> failure);

	default BiConsumer<SQLException, SQLAction<T>> defaultExceptionHandler() {
		return (exception, action) -> {
			getManager().getLogger().severe("Error when execute [" + action.getSQLContent() + "]");
			getManager().getLogger().severe(exception.getLocalizedMessage());
		};
	}


}
