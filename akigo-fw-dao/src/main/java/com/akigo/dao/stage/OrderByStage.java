package com.akigo.dao.stage;

import com.akigo.dao.entity.DBColumn;

public interface OrderByStage {
    void orderBy(DBColumn<?>... column);
}
