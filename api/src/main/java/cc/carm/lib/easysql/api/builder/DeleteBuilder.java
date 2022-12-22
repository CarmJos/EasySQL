package cc.carm.lib.easysql.api.builder;

public interface DeleteBuilder extends ConditionalBuilder<DeleteBuilder, SQLAction<Integer>> {

    String getTableName();

}
