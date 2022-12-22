package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.LinkedHashMap;

public interface ConditionalBuilder<B extends ConditionalBuilder<B, T>, T extends SQLAction<?>> extends SQLBuilder {

    /**
     * 将现有条件构建完整的SQL语句用于执行。
     *
     * @return {@link SQLAction}
     */
    T build();

    /**
     * 设定限定的条目数
     *
     * @param limit 条数限制
     * @return {@link B}
     */
    B limit(int limit);

    /**
     * 直接设定条件的源文本,不需要以WHERE开头。
     * <br>如 {@code id = 1 AND name = 'test' OR name = 'test2'} 。
     *
     * @param condition 条件文本，不需要以WHERE开头。
     * @return {@link B}
     */
    B where(@Nullable String condition);

    /**
     * 直接设定每个条件的文本与其对应数值,将以AND链接，且不需要以WHERE开头。
     * <br>条件如 {@code id = ? }，问号将被以对应的数值填充。。
     *
     * @param conditionSQLs 条件内容，将以AND链接，且不需要以WHERE开头。
     * @return {@link B}
     */
    B where(LinkedHashMap<@NotNull String, @Nullable Object> conditionSQLs);

    B addCondition(@Nullable String condition);

    B addCondition(@NotNull String columnName, @NotNull String operator, @Nullable Object queryValue);

    B addCondition(@NotNull String columnName, @Nullable Object queryValue);

    B addCondition(@NotNull String[] columnNames, @Nullable Object[] queryValues);

    B addNotNullCondition(@NotNull String columnName);

    /**
     * 添加时间的限定条件。 若设定了开始时间，则限定条件为 {@code endMillis >= startMillis}；
     *
     * @param columnName  判断的行
     * @param startMillis 开始时间戳，若{@code <0}则不作限定
     * @param endMillis   结束时间戳，若{@code <0}则不作限定
     * @return {@link B}
     */
    default B addTimeCondition(@NotNull String columnName, long startMillis, long endMillis) {
        return addTimeCondition(columnName,
                startMillis > 0 ? new Date(startMillis) : null,
                endMillis > 0 ? new Date(endMillis) : null
        );
    }

    /**
     * 添加时间的限定条件。 若设定了开始时间，则限定条件为 {@code endDate >= startTime}；
     *
     * @param columnName 判断的行
     * @param startDate  开始时间，若为null则不作限定
     * @param endDate    结束时间，若为null则不作限定
     * @return {@link B}
     */
    B addTimeCondition(@NotNull String columnName, @Nullable java.util.Date startDate, @Nullable java.util.Date endDate);


}
