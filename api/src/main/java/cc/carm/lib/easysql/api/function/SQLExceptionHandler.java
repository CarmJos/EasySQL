package cc.carm.lib.easysql.api.function;

import cc.carm.lib.easysql.api.SQLAction;

import java.sql.SQLException;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface SQLExceptionHandler extends BiConsumer<SQLException, SQLAction<?>> {


}
