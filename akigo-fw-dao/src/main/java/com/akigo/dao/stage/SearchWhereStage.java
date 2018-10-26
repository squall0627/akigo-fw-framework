package com.akigo.dao.stage;

import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;

import java.util.function.Consumer;

public interface SearchWhereStage extends GroupByStage {
    GroupByStage where(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    GroupByStage where(DBColumn<?> column, Object value);

    GroupByStage where(Consumer<WhereAndOrStage> andOrStage);
}
