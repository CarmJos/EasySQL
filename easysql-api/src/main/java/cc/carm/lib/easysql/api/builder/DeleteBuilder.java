package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.SQLAction;

public interface DeleteBuilder extends ConditionalBuilder<DeleteBuilder, SQLAction<Integer>> {

	String getTableName();

}
