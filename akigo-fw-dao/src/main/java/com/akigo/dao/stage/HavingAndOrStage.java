package com.akigo.dao.stage;

import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;

import java.util.function.Consumer;

public interface HavingAndOrStage {
    HavingAndOrStage and(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    HavingAndOrStage and(DBColumn<?> column, Object value);

    HavingAndOrStage and(Consumer<HavingAndOrStage> andOrStage);

    HavingAndOrStage or(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    HavingAndOrStage or(DBColumn<?> column, Object value);

    HavingAndOrStage or(Consumer<HavingAndOrStage> andOrStage);
}
