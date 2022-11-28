package cc.carm.lib.easysql.migrate;

import cc.carm.lib.easysql.api.enums.IndexType;

/**
 * 2022/11/28<br>
 * EasySQL<br>
 *
 * @author huanmeng_qwq
 */
public class MigrateData {
    private final String name;
    private final String settings;
    private final IndexType indexType;

    public MigrateData(String name, String settings, IndexType indexType) {
        this.name = name;
        this.settings = settings;
        this.indexType = indexType;
    }

    public String name() {
        return name;
    }

    public String settings() {
        return settings;
    }

    public IndexType indexType() {
        return indexType;
    }
}
