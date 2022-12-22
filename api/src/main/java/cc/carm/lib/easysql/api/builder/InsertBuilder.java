package cc.carm.lib.easysql.api.builder;

import java.util.Arrays;
import java.util.List;

public interface InsertBuilder<T> {

    String getTableName();

    T columns(List<String> columnNames);

    default T columns(String... columnNames) {
        return columns(columnNames == null ? null : Arrays.asList(columnNames));
    }


}
