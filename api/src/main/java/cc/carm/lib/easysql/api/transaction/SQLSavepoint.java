package cc.carm.lib.easysql.api.transaction;

import org.jetbrains.annotations.NotNull;

public interface SQLSavepoint {

    int getID();

    @NotNull String getName();

    void release();

    boolean isReleased();


}
