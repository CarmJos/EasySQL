package cc.carm.lib.easysql.api.action;

public interface PreparedSQLUpdateAction extends SQLUpdateAction {

	PreparedSQLUpdateAction setParams(Object... params);

}
