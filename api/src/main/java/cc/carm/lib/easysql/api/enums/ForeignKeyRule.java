package cc.carm.lib.easysql.api.enums;

public enum ForeignKeyRule {

    /**
     * 啥也不做
     * <p>注意： 在Mysql中该选项实际上等同于采用默认的 {@link #RESTRICT} 设定！
     */
    NO_ACTION("NO ACTION"),

    /**
     * 拒绝删除要求，直到使用删除键值的辅助表被手工删除，并且没有参照时(这是默认设置，也是最安全的设置)
     */
    RESTRICT("RESTRICT"),

    /**
     * 修改包含与已删除键值有参照关系的所有记录，使用NULL值替换（只能用于已标记为NOT NULL的字段）
     */
    SET_NULL("SET NULL"),

    /**
     * 修改包含与已删除键值有参照关系的所有记录，使用默认值替换（只能用于设定了DEFAULT的字段）
     */
    SET_DEFAULT("SET DEFAULT"),

    /**
     * <b>级联删除</b>，删除包含与已删除键值有参照关系的所有记录
     */
    CASCADE("CASCADE");

    final String ruleName;

    ForeignKeyRule(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }

}
