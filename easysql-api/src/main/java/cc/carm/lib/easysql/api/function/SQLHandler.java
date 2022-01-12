package cc.carm.lib.easysql.api.function;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

@FunctionalInterface
public interface SQLHandler<T> {

	void accept(@NotNull T t) throws SQLException;

	@NotNull
	@Contract(pure = true)
	default SQLHandler<T> andThen(@NotNull SQLHandler<? super T> after) {
		Objects.requireNonNull(after);
		return (T t) -> {
			accept(t);
			after.accept(t);
		};
	}
}
