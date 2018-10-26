package com.akigo.dao.stage;

import com.akigo.dao.entity.DBColumn;

public interface GroupByStage extends OrderByStage {
    HavingStage groupBy(DBColumn<?>... column);
}