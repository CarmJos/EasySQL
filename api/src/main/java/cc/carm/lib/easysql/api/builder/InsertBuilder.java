package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLAction;

import java.util.Arrays;
import java.util.List;

public interface InsertBuilder<T extends SQLAction<?>> {

    String getTableName();

    T columns(List<String> columnNames);

    default T columns(String... columnNames) {
        return columns(columnNames == null ? null : Arrays.asList(columnNames));
    }


}
