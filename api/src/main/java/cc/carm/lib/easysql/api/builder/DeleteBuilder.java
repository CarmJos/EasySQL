package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.action.update.PreparedSQLUpdateAction;
import org.jetbrains.annotations.NotNull;

public interface DeleteBuilder<B extends PreparedSQLUpdateAction<Integer, B>>
        extends ConditionalBuilder<DeleteBuilder<B>, B> {

    @NotNull String getTableName();

}
