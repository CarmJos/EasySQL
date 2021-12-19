package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.LinkedHashMap;

public interface ConditionalBuilder<T> extends SQLBuilder {

	T build();

	ConditionalBuilder<T> setLimit(int limit);

	ConditionalBuilder<T> setConditions(@Nullable String condition);

	ConditionalBuilder<T> setConditions(LinkedHashMap<@NotNull String, @Nullable Object> conditionSQLs);

	ConditionalBuilder<T> addCondition(@Nullable String condition);

	ConditionalBuilder<T> addCondition(@NotNull String queryName, @NotNull String operator, @Nullable Object queryValue);

	default ConditionalBuilder<T> addCondition(@NotNull String queryName, @Nullable Object queryValue) {
		return addCondition(queryName, "=", queryValue);
	}

	ConditionalBuilder<T> addCondition(@NotNull String[] queryNames, @Nullable Object[] queryValues);

	ConditionalBuilder<T> addNotNullCondition(@NotNull String queryName);

	default ConditionalBuilder<T> addTimeCondition(@NotNull String queryName, long startMillis, long endMillis) {
		return addTimeCondition(queryName,
				startMillis > 0 ? new Date(startMillis) : null,
				endMillis > 0 ? new Date(endMillis) : null
		);
	}

	ConditionalBuilder<T> addTimeCondition(@NotNull String queryName, @Nullable java.util.Date startDate, @Nullable java.util.Date endDate);


}
