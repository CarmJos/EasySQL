package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.api.action.SQLAdvancedAction;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TableAlterBuilder extends SQLBuilder {

    @NotNull SQLAdvancedAction<Integer> renameTo(@NotNull String newTableName);

    @NotNull SQLAdvancedAction<Integer> changeComment(@NotNull String newTableComment);

    @NotNull SQLAdvancedAction<Integer> setAutoIncrementIndex(int index);

    @NotNull SQLAdvancedAction<Integer> addIndex(@NotNull IndexType indexType, @Nullable String indexName,
                                                 @NotNull String columnName, @NotNull String... moreColumns);

    /**
     * 为该表移除一个索引
     *
     * @param indexName 索引名
     * @return {@link SQLAdvancedAction}
     */
    @NotNull SQLAdvancedAction<Integer> dropIndex(@NotNull String indexName);

    /**
     * 为该表移除一个外键
     *
     * @param keySymbol 外键名
     * @return {@link SQLAdvancedAction}
     */
    @NotNull SQLAdvancedAction<Integer> dropForeignKey(@NotNull String keySymbol);

    /**
     * 为该表移除主键(须添加新主键)
     *
     * @return {@link SQLAdvancedAction}
     */
    @NotNull SQLAdvancedAction<Integer> dropPrimaryKey();

    /**
     * 为表添加一列
     *
     * @param columnName 列名
     * @param settings   列的相关设定
     * @return {@link SQLAdvancedAction}
     */
    default @NotNull SQLAdvancedAction<Integer> addColumn(@NotNull String columnName, @NotNull String settings) {
        return addColumn(columnName, settings, null);
    }

    /**
     * 为表添加一列
     *
     * @param columnName  列名
     * @param settings    列的相关设定
     * @param afterColumn 该列增添到哪个列的后面，
     *                    <p> 该参数若省缺则放于最后一行
     *                    <p> 若为 "" 则置于首行。
     * @return {@link SQLAdvancedAction}
     */
    @NotNull SQLAdvancedAction<Integer> addColumn(@NotNull String columnName,
                                                  @NotNull String settings,
                                                  @Nullable String afterColumn);

    @NotNull SQLAdvancedAction<Integer> renameColumn(@NotNull String columnName, @NotNull String newName);

    @NotNull SQLAdvancedAction<Integer> modifyColumn(@NotNull String columnName, @NotNull String settings);

    default @NotNull SQLAdvancedAction<Integer> modifyColumn(@NotNull String columnName,
                                                             @NotNull String columnSettings,
                                                             @NotNull String afterColumn) {
        return modifyColumn(columnName, columnSettings + " AFTER `" + afterColumn + "`");
    }

    @NotNull SQLAdvancedAction<Integer> removeColumn(@NotNull String columnName);

    @NotNull SQLAdvancedAction<Integer> setColumnDefault(@NotNull String columnName, @NotNull String defaultValue);

    @NotNull SQLAdvancedAction<Integer> removeColumnDefault(@NotNull String columnName);

    /**
     * 为该表添加一个自增列
     * <p> 自增列强制要求为数字类型，非空，且为UNIQUE。
     * <p> 注意：一个表只允许有一个自增列！
     *
     * @param columnName 列名
     * @param numberType 数字类型，若省缺则为 {@link NumberType#INT}
     * @param primary    是否为主键，若否则只为唯一键
     * @param unsigned   是否采用 UNSIGNED (即无负数，可以增加自增键的最高数，建议为true)
     * @return {@link TableCreateBuilder}
     */
    default @NotNull SQLAdvancedAction<Integer> addAutoIncrementColumn(@NotNull String columnName,
                                                                       @Nullable NumberType numberType,
                                                                       boolean primary, boolean unsigned) {
        return addColumn(columnName,
                (numberType == null ? NumberType.INT : numberType).name()
                        + (unsigned ? " UNSIGNED " : " ")
                        + "NOT NULL AUTO_INCREMENT " + (primary ? "PRIMARY KEY" : "UNIQUE KEY"),
                ""
        );
    }

    /**
     * 为该表添加一个自增列
     * <br> 自增列强制要求为数字类型，非空，且为UNIQUE。
     * <p> 注意：一个表只允许有一个自增列！
     *
     * @param columnName 列名
     * @param numberType 数字类型，若省缺则为 {@link NumberType#INT}
     * @return {@link TableAlterBuilder}
     */
    default @NotNull SQLAdvancedAction<Integer> addAutoIncrementColumn(@NotNull String columnName,
                                                                       @NotNull NumberType numberType) {
        return addAutoIncrementColumn(columnName, numberType, false, true);
    }


    /**
     * 为该表添加一个自增列
     * <br> 自增列强制要求为数字类型，非空，且为UNIQUE。
     * <p> 注意：一个表只允许有一个自增列！
     *
     * @param columnName 列名
     * @return {@link TableAlterBuilder}
     */
    default @NotNull SQLAdvancedAction<Integer> addAutoIncrementColumn(@NotNull String columnName) {
        return addAutoIncrementColumn(columnName, NumberType.INT);
    }

}
