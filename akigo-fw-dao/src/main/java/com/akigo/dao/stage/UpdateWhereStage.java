package com.akigo.dao.stage;

import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;

import java.util.function.Consumer;

public interface UpdateWhereStage {
    void where(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    void where(DBColumn<?> column, Object value);

    void where(Consumer<WhereAndOrStage> andOrStage);
}
