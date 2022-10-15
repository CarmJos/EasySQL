package cc.carm.lib.easysql.api.builder;

/**
 * 存在则更新，不存在则插入。
 *
 * @see ReplaceBuilder
 */
@Deprecated
public interface UpsertBuilder {

    String getTableName();

    default UpsertBuilder setColumnNames(String[] columnNames, String updateColumn) {
        throw new UnsupportedOperationException("Please use REPLACE .");
    }

}
