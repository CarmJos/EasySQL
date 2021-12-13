package cc.carm.lib.easysql.api.action.query;

import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

public interface PreparedQueryAction extends QueryAction {

	PreparedQueryAction setParams(@Nullable Object... params);

	PreparedQueryAction setParams(@Nullable Iterable<Object> params);

	PreparedQueryAction handleStatement(@Nullable Consumer<PreparedStatement> statement);

}
