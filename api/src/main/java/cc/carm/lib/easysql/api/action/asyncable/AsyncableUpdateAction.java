package cc.carm.lib.easysql.api.action.asyncable;

import cc.carm.lib.easysql.api.action.SQLAsyncableAction;
import cc.carm.lib.easysql.api.newactions.SQLUpdateAction;

public interface AsyncableUpdateAction<T extends Number>
        extends SQLUpdateAction<T>, SQLAsyncableAction<T> {

}
