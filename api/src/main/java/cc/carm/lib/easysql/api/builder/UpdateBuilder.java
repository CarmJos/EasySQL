package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.action.update.PreparedSQLUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

public interface UpdateBuilder<B extends PreparedSQLUpdateAction<Integer, B>>
        extends ConditionalBuilder<UpdateBuilder<B>, B> {

    String getTableName();

    /**
     * 添加一条需要更新的字段名与值
     *
     * @param columnName  字段名
     * @param columnValue 字段名对应的值
     * @return {@link UpdateBuilder}
     * @since 0.3.7
     */
    UpdateBuilder<B> set(@NotNull String columnName, @Nullable Object columnValue);

    /**
     * 设定更新的全部字段值 <b>(此操作会覆盖之前的设定)</b>
     * <p> <b>此操作会覆盖之前的设定</b>
     *
     * @param columnData 字段名和值的键值对
     * @return {@link UpdateBuilder}
     */
    UpdateBuilder<B> setAll(LinkedHashMap<@NotNull String, @Nullable Object> columnData);

    /**
     * 设定更新的全部字段值 <b>(此操作会覆盖之前的设定)</b>
     * <p> <b>此操作会覆盖之前的设定</b>
     *
     * @param columnNames  字段名
     * @param columnValues 字段名对应的值
     * @return {@link UpdateBuilder}
     */
    UpdateBuilder<B> setAll(@NotNull String[] columnNames, @Nullable Object[] columnValues);

    /**
     * 设定更新的全部字段值 <b>(此操作会覆盖之前的设定)</b>
     * <p> 如需同时更新多条字段，请使用 {@link #setAll(String[], Object[])} 或 {@link #setAll(LinkedHashMap)}
     * <br>也可以使用 {@link #set(String, Object)} 一条条的添加字段
     *
     * @param columnName  字段名
     * @param columnValue 字段名对应的值
     * @return {@link UpdateBuilder}
     */
    default UpdateBuilder<B> setAll(@NotNull String columnName, @Nullable Object columnValue) {
        return setAll(new String[]{columnName}, new Object[]{columnValue});
    }

}
