package com.akigo.dao.stage;

import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;

import java.util.function.Consumer;

public interface HavingStage extends OrderByStage {
    OrderByStage having(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    OrderByStage having(DBColumn<?> column, Object value);

    OrderByStage having(Consumer<HavingAndOrStage> andOrStage);
}
