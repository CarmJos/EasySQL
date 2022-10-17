package cc.carm.lib.easysql.api.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TableColumn {

    @NotNull String name;
    @NotNull String type;

    boolean notNull;

    boolean primaryKey;
    boolean uniqueKey;

    boolean autoIncrement;

    @Nullable String defaultValue;
    @Nullable String onUpdate;

    @Nullable String comment;


}
