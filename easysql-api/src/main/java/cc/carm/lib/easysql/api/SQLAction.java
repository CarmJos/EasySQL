package cc.carm.lib.easysql.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public interface SQLAction<T> {

	@NotNull UUID getActionUUID();

	@NotNull String getShortID();

	long getCreateTime();

	@NotNull String getSQLContent();

	@NotNull SQLManager getManager();

	@NotNull T execute() throws SQLException;

	@Nullable
	default T execute(@Nullable Consumer<SQLException> exceptionHandler) {
		if (exceptionHandler == null) exceptionHandler = defaultExceptionHandler();
		T value = null;
		try {
			value = execute();
		} catch (SQLException exception) {
			exceptionHandler.accept(exception);
		}
		return value;
	}

	default void executeAsync() {
		executeAsync(null);
	}

	default void executeAsync(Consumer<T> success) {
		executeAsync(success, null);
	}

	void executeAsync(Consumer<T> success, Consumer<SQLException> failure);

	SQLAction<T> handleException(Consumer<SQLException> failure);

	@NotNull Consumer<SQLException> getExceptionHandler();

	default Consumer<SQLException> defaultExceptionHandler() {
		return Throwable::printStackTrace;
	}

	default Consumer<T> defaultResultHandler() {
		return t -> {
		};
	}


}
