package cc.carm.lib.easysql.api.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T, R> {

	@Nullable
	R apply(T t) throws SQLException;

}
