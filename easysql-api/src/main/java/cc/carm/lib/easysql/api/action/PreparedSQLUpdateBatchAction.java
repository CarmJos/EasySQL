package cc.carm.lib.easysql.api.action;

import cc.carm.lib.easysql.api.SQLAction;

import java.util.List;

public interface PreparedSQLUpdateBatchAction extends SQLAction<List<Integer>> {

	PreparedSQLUpdateBatchAction setAllParams(Iterable<Object[]> allParams);

	PreparedSQLUpdateBatchAction addParamsBatch(Object... params);

	PreparedSQLUpdateBatchAction setKeyIndex(int keyColumnIndex);

	default PreparedSQLUpdateBatchAction defaultKeyIndex() {
		return setKeyIndex(-1); // will return changed lines number
	}

}
