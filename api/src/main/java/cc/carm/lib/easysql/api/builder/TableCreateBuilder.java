package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import cc.carm.lib.easysql.api.action.SQLUpdateAction;
import cc.carm.lib.easysql.api.enums.ForeignKeyRule;
import cc.carm.lib.easysql.api.enums.IndexType;
import cc.carm.lib.easysql.api.enums.NumberType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static cc.carm.lib.easysql.api.SQLBuilder.withBackQuote;
import static cc.carm.lib.easysql.api.SQLBuilder.withQuote;


public interface TableCreateBuilder extends SQLBuilder {

    /**
     * 将现有条件构建完整的SQL语句用于执行。
     *
     * @return {@link SQLUpdateAction}
     */
    SQLUpdateAction<Integer> build();

    @NotNull String getTableName();

    /**
     * 得到表的设定。
     * <p> 若未使用 {@link #setTableSettings(String)} 方法则会采用 {@link #defaultTablesSettings()} 。
     *
     * @return TableSettings
     */
    @NotNull String getTableSettings();

    TableCreateBuilder setTableSettings(@NotNull String settings);

    /**
     * 设定表的标注，一般用于解释该表的作用。
     *
     * @param comment 表标注
     * @return {@link TableCreateBuilder}
     */
    TableCreateBuilder setTableComment(@Nullable String comment);

    /**
     * 直接设定表的所有列信息
     *
     * @param columns 列的相关信息 (包括列设定)
     * @return {@link TableCreateBuilder}
     */
    TableCreateBuilder setColumns(@NotNull String... columns);

    /**
     * 为该表添加一个列
     *
     * @param column 列的相关信息
     *               <br>如 `uuid` VARCHAR(36) NOT NULL UNIQUE KEY
     * @return {@link TableCreateBuilder}
     */
    TableCreateBuilder addColumn(@NotNull String column);

    /**
     * 为该表添加一个列
     *
     * @param columnName 列名
     * @param settings   列的设定
     *                   <br>如 VARCHAR(36) NOT NULL UNIQUE KEY
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addColumn(@NotNull String columnName, @NotNull String settings) {
        Objects.requireNonNull(columnName, "columnName could not be null");
        return addColumn(withBackQuote(columnName) + " " + settings);
    }

    /**
     * 为该表添加一个列
     *
     * @param columnName 列名
     * @param settings   列的设定
     *                   <br>如 VARCHAR(36) NOT NULL UNIQUE KEY
     * @param comments   列的注解，用于解释该列数据的作用
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addColumn(@NotNull String columnName, @NotNull String settings, @NotNull String comments) {
        return addColumn(columnName, settings + " COMMENT " + withQuote(comments));
    }

    /**
     * 为该表添加一个自增列
     * <p> 自增列强制要求为数字类型，非空，且为UNIQUE。
     * <p> 注意：一个表只允许有一个自增列！
     *
     * @param columnName   列名
     * @param numberType   数字类型，若省缺则为 {@link NumberType#INT}
     * @param asPrimaryKey 是否为主键，若为false则设定为唯一键
     * @param unsigned     是否采用 UNSIGNED (即无负数，可以增加自增键的最高数，建议为true)
     * @return {@link TableCreateBuilder}
     */
    TableCreateBuilder addAutoIncrementColumn(@NotNull String columnName, @Nullable NumberType numberType,
                                              boolean asPrimaryKey, boolean unsigned);

    /**
     * 为该表添加一个INT类型的自增主键列
     * <p> 自增列强制要求为数字类型，非空，且为UNIQUE。
     * <p> 注意：一个表只允许有一个自增列！
     *
     * @param columnName   列名
     * @param asPrimaryKey 是否为主键，若为false则设定为唯一键
     * @param unsigned     是否采用 UNSIGNED (即无负数，可以增加自增键的最高数，建议为true)
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addAutoIncrementColumn(@NotNull String columnName,
                                                      boolean asPrimaryKey, boolean unsigned) {
        return addAutoIncrementColumn(columnName, NumberType.INT, asPrimaryKey, unsigned);
    }


    /**
     * 为该表添加一个INT类型的自增列
     * <p> 自增列强制要求为数字类型，非空，且为UNIQUE。
     * <p> 注意：一个表只允许有一个自增列！
     *
     * @param columnName   列名
     * @param asPrimaryKey 是否为主键，若为false则设定为唯一键
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addAutoIncrementColumn(@NotNull String columnName, boolean asPrimaryKey) {
        return addAutoIncrementColumn(columnName, asPrimaryKey, true);
    }

    /**
     * 为该表添加一个INT类型的自增主键列
     * <p> 自增列强制要求为数字类型，非空，且为UNIQUE。
     * <p> 注意：一个表只允许有一个自增列！
     *
     * @param columnName 列名
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addAutoIncrementColumn(@NotNull String columnName) {
        return addAutoIncrementColumn(columnName, true);
    }

    /**
     * 设定表中的某列为索引或键。
     *
     * <p>创建索引时，你需要确保该索引是应用在 SQL 查询语句的条件(一般作为 WHERE 子句的条件)。
     * <br>虽然索引大大提高了查询速度，同时却会降低更新表的速度，如对表进行INSERT、UPDATE 和DELETE。
     * <br>因此，请合理的设计索引。
     *
     * @param type       索引类型
     * @param columnName 索引包含的列
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder setIndex(@NotNull String columnName,
                                        @NotNull IndexType type) {
        return setIndex(type, null, columnName);
    }

    /**
     * 设定表中的某列为索引或键。
     *
     * <p>创建索引时，你需要确保该索引是应用在 SQL 查询语句的条件(一般作为 WHERE 子句的条件)。
     * <br>虽然索引大大提高了查询速度，同时却会降低更新表的速度，如对表进行INSERT、UPDATE 和DELETE。
     * <br>因此，请合理的设计索引。
     *
     * @param type        索引类型
     * @param indexName   索引名称，缺省时将根据第一个索引列赋一个名称
     * @param columnName  索引包含的列
     * @param moreColumns 联合索引需要包含的列
     * @return {@link TableCreateBuilder}
     */
    TableCreateBuilder setIndex(@NotNull IndexType type, @Nullable String indexName,
                                @NotNull String columnName, @NotNull String... moreColumns);


    /**
     * 以本表位从表，为表中某列设定自参照外键(即自参照完整性)。
     *
     * <p>外键约束（FOREIGN KEY）是表的一个特殊字段，经常与主键约束一起使用。
     * <br>外键用来建立主表与从表的关联关系，为两个表的数据建立连接，约束两个表中数据的一致性和完整性。
     * <br>主表删除某条记录时，从表中与之对应的记录也必须有相应的改变。
     *
     * @param tableColumn   本表中的列
     * @param foreignColumn 外键关联表中对应的关联列，必须为目标表的主键，即 {@link IndexType#PRIMARY_KEY}
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addForeignKey(@NotNull String tableColumn, @NotNull String foreignColumn) {
        return addForeignKey(tableColumn, getTableName(), foreignColumn);
    }

    /**
     * 以本表位从表，为表中某列设定外键。
     *
     * <p>外键约束（FOREIGN KEY）是表的一个特殊字段，经常与主键约束一起使用。
     * <br>外键用来建立主表与从表的关联关系，为两个表的数据建立连接，约束两个表中数据的一致性和完整性。
     * <br>主表删除某条记录时，从表中与之对应的记录也必须有相应的改变。
     *
     * @param tableColumn   本表中的列
     * @param foreignTable  外键关联主表，必须为已存在的表或本表，且必须有主键。
     * @param foreignColumn 外键关联主表中对应的关联列，须满足
     *                      <p> 1. 为主表的主键，即 {@link IndexType#PRIMARY_KEY}
     *                      <p> 2. 数据类型必须和所要建立主键的列的数据类型相同。
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addForeignKey(@NotNull String tableColumn,
                                             @NotNull String foreignTable, @NotNull String foreignColumn) {
        return addForeignKey(tableColumn, null, foreignTable, foreignColumn);
    }

    /**
     * 以本表位从表，为表中某列设定外键。
     *
     * <p>外键约束（FOREIGN KEY）是表的一个特殊字段，经常与主键约束一起使用。
     * <br>外键用来建立主表与从表的关联关系，为两个表的数据建立连接，约束两个表中数据的一致性和完整性。
     * <br>主表删除某条记录时，从表中与之对应的记录也必须有相应的改变。
     *
     * @param tableColumn    本表中的列
     * @param constraintName 约束名，缺省时将使用参数自动生成，如 <i>fk_[tableColumn]_[foreignTable]</i>
     * @param foreignTable   外键关联主表，必须为已存在的表或本表，且必须有主键。
     * @param foreignColumn  外键关联主表中对应的关联列，须满足
     *                       <p> 1. 为主表的主键，即 {@link IndexType#PRIMARY_KEY}
     *                       <p> 2. 数据类型必须和所要建立主键的列的数据类型相同。
     * @return {@link TableCreateBuilder}
     */
    default TableCreateBuilder addForeignKey(@NotNull String tableColumn, @Nullable String constraintName,
                                             @NotNull String foreignTable, @NotNull String foreignColumn) {
        return addForeignKey(tableColumn, constraintName, foreignTable, foreignColumn, null, null);
    }

    /**
     * 以本表位从表，为表中某列设定外键。
     *
     * <p>外键约束（FOREIGN KEY）是表的一个特殊字段，经常与主键约束一起使用。
     * <br>外键用来建立主表与从表的关联关系，为两个表的数据建立连接，约束两个表中数据的一致性和完整性。
     * <br>主表删除某条记录时，从表中与之对应的记录也必须有相应的改变。
     *
     * @param tableColumn    本表中的列
     * @param constraintName 约束名，缺省时将使用参数自动生成，如 <i>fk_[tableColumn]_[foreignTable]</i>
     * @param foreignTable   外键关联主表，必须为已存在的表或本表，且必须有主键。
     * @param foreignColumn  外键关联主表中对应的关联列，须满足
     *                       <p> 1. 为主表的主键，即 {@link IndexType#PRIMARY_KEY}
     *                       <p> 2. 数据类型必须和所要建立主键的列的数据类型相同。
     * @param updateRule     在外键被更新时采用的规则，缺省时默认为{@link ForeignKeyRule#RESTRICT}
     * @param deleteRule     在外键被删除时采用的规则，缺省时默认为{@link ForeignKeyRule#RESTRICT}
     * @return {@link TableCreateBuilder}
     */
    TableCreateBuilder addForeignKey(@NotNull String tableColumn, @Nullable String constraintName,
                                     @NotNull String foreignTable, @NotNull String foreignColumn,
                                     @Nullable ForeignKeyRule updateRule, @Nullable ForeignKeyRule deleteRule);

    default String defaultTablesSettings() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
    }

}
