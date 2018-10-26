package com.akigo.dao.stage;

import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;

import java.util.function.Consumer;

public interface WhereAndOrStage {
    WhereAndOrStage and(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    WhereAndOrStage and(DBColumn<?> column, Object value);

    WhereAndOrStage and(Consumer<WhereAndOrStage> andOrStage);

    WhereAndOrStage or(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    WhereAndOrStage or(DBColumn<?> column, Object value);

    WhereAndOrStage or(Consumer<WhereAndOrStage> andOrStage);
}
