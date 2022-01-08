package cc.carm.lib.easysql.api.builder;

import cc.carm.lib.easysql.api.action.PreparedSQLUpdateAction;

import java.util.LinkedHashMap;

public interface UpdateBuilder extends ConditionalBuilder<UpdateBuilder, PreparedSQLUpdateAction> {

    String getTableName();

    UpdateBuilder setColumnValues(LinkedHashMap<String, Object> columnData);

    UpdateBuilder setColumnValues(String[] columnNames, Object[] columnValues);

    default UpdateBuilder setColumnValues(String columnName, Object columnValue) {
        return setColumnValues(new String[]{columnName}, new Object[]{columnValue});
    }


}
