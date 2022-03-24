package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLAction;

import java.util.Arrays;
import java.util.List;

/**
 * REPLACE 语句用于将一组值更新进数据表中。
 * <br> 执行后，将通过表中键判断该数据是否存在，若存在则用新数据替换原来的值，若不存在则会插入该数据。
 * <br> 在使用REPLACE时，表与所给行列数据中必须包含唯一索引(或主键)，且索引不得为空值，否则将等同于插入语句。
 *
 * @param <T> 最终构建出的 {@link SQLAction} 类型
 */
public interface ReplaceBuilder<T extends SQLAction<?>> {

    String getTableName();

    T setColumnNames(List<String> columnNames);

    default T setColumnNames(String... columnNames) {
        return setColumnNames(columnNames == null ? null : Arrays.asList(columnNames));
    }

}
