package cc.carm.lib.easysql.api.builder;

import java.util.Arrays;
import java.util.List;

public interface InsertBuilder<T> {

	String getTableName();

	InsertBuilder<T> setTableName(String tableName);

	T setColumnNames(List<String> columnNames);

	default T setColumnNames(String... columnNames) {
		return setColumnNames(columnNames == null ? null : Arrays.asList(columnNames));
	}


}
