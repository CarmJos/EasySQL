package cc.carm.lib.easysql.api.builder;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public interface InsertBuilder<T> {

    @NotNull String getTableName();

    @NotNull T columns(List<String> columnNames);

    default @NotNull T columns(String... columnNames) {
        return columns(columnNames == null ? null : Arrays.asList(columnNames));
    }


}
