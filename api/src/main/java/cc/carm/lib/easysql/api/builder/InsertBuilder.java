package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLAction;

import java.util.Arrays;
import java.util.List;

public interface InsertBuilder<T extends SQLAction<?>> {

    String getTableName();

    T setColumnNames(List<String> columnNames);

    default T setColumnNames(String... columnNames) {
        return setColumnNames(columnNames == null ? null : Arrays.asList(columnNames));
    }


}
