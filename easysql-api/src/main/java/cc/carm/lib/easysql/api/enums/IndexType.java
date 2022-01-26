package cc.carm.lib.easysql.api.enums;

public enum IndexType {


	/**
	 * <b>普通索引</b>（由关键字KEY或INDEX定义的索引）的唯一任务是加快对数据的访问速度。
	 * <br> 因此，应该只为那些最经常出现在查询条件（WHERE column=）或排序条件（ORDER BY column）中的数据列创建索引。
	 * <br> 只要有可能，就应该选择一个数据最整齐、最紧凑的数据列（如一个整数类型的数据列）来创建索引。
	 */
	INDEX("INDEX"),


	/**
	 * <b>唯一索引</b> 是在表上一个或者多个字段组合建立的索引，这个或者这些字段的值组合起来在表中不可以重复，用于保证数据的唯一性。
	 */
	UNIQUE_KEY("UNIQUE KEY"),

	/**
	 * <b>主键索引</b> 是唯一索引的特定类型。表中创建主键时自动创建的索引 。一个表只能建立一个主索引。
	 */
	PRIMARY_KEY("PRIMARY KEY"),

	/**
	 * <b>全文索引</b> 主要用来查找文本中的关键字，而不是直接与索引中的值相比较。
	 * <br> 请搭配 MATCH 等语句使用，而不是使用 WHERE - LIKE 。
	 * <br> 全文索引只可用于 CHAR、 VARCHAR 与 TEXT 系列类型。
	 */
	FULLTEXT_INDEX("FULLTEXT");


	final String name;

	IndexType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
