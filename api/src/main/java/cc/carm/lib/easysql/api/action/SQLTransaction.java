package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLManager;

public class SQLTransaction implements SQLManager {

    public SQLTransaction savepoint() {
        return this;
    }

    public SQLTransaction releaseSavepoint() {
        return this;
    }

    public SQLTransaction rollbackToSavepoint() {
        return this;
    }

}
