package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.action.SQLBaseAction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * REPLACE 语句用于将一组值更新进数据表中。
 * <br> 执行后，将通过表中键判断该数据是否存在，若存在则用新数据替换原来的值，若不存在则会插入该数据。
 * <br> 在使用REPLACE时，表与所给行列数据中必须包含唯一索引(或主键)，且索引不得为空值，否则将等同于插入语句。
 *
 * @param <T> 最终构建出的 {@link SQLBaseAction} 类型
 */
public interface ReplaceBuilder<T extends SQLBaseAction<?>> {

    @NotNull String getTableName();

    @NotNull    T columns(List<String> columnNames);

    default  @NotNull T columns(String... columnNames) {
        return columns(columnNames == null ? null : Arrays.asList(columnNames));
    }

}
