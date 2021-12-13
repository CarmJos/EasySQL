package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;

public interface SQLUpdateAction extends SQLAction<Integer> {

	SQLUpdateAction setKeyIndex(int keyColumnIndex);

	default SQLUpdateAction defaultKeyIndex() {
		return setKeyIndex(-1); // will return changed lines number
	}

}
