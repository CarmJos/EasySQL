package cc.carm.lib.easysql.api.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Objects;

@FunctionalInterface
public interface SQLBiFunction<T, U, R> {

    @Nullable
    R apply(@NotNull T t, @NotNull U u) throws SQLException;

    default <V> SQLBiFunction<T, U, V> then(@NotNull SQLFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> {
            R r = apply(t, u);
            if (r == null) return null;
            else return after.apply(r);
        };
    }

}
