package cc.carm.lib.easysql.api.builder;

public interface UpsertBuilder {

    String getTableName();

    UpsertBuilder setColumnNames(String[] columnNames, String updateColumn);

}
