package cc.carm.lib.easysql.api.enums;

/**
 * 2022/11/28<br>
 * EasySQL<br>
 *
 * @author huanmeng_qwq
 */
public class MigrateResult {
    /**
     * 旧表不存在
     */
    public static final MigrateResult OLD_TABLE_NOT_EXIST = new MigrateResult(true);

    /**
     * 新表已存在
     */
    public static final MigrateResult NEW_TABLE_EXIST = new MigrateResult(false);

    /**
     * 新表字段为空
     */
    public static final MigrateResult NEW_COLUMN_EMPTY = new MigrateResult(false);

    /**
     * 旧表字段为在新表这设置对应的字段名
     */
    public static final MigrateResult COLUMN_NOT_SET = new MigrateResult(false);

    /**
     * 迁移成功
     */
    public static final MigrateResult SUCCESS = new MigrateResult(true);

    private final boolean success;
    private Throwable error;

    MigrateResult(boolean success) {
        this.success = success;
    }

    public MigrateResult(boolean success, Throwable error) {
        this.success = success;
        this.error = error;
    }

    public static MigrateResult from(MigrateResult migrateResult, Throwable error) {
        return new MigrateResult(migrateResult.success, error);
    }

    public boolean success() {
        return success;
    }

    public Throwable error() {
        return error;
    }
}
