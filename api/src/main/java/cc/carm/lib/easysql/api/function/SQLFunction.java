package cc.carm.lib.easysql.api.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Objects;

@FunctionalInterface
public interface SQLFunction<T, R> {

    @Nullable
    R apply(@NotNull T t) throws SQLException;

    default <V> SQLFunction<V, R> compose(@NotNull SQLFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> {
            T t = before.apply(v);
            if (t == null) return null;
            else return apply(t);
        };
    }

    default <V> SQLFunction<T, V> then(@NotNull SQLFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            R r = apply(t);
            if (r == null) return null;
            else return after.apply(r);
        };
    }


}
