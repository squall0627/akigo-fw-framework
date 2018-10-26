package com.akigo.dao.stage.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.stage.WhereAndOrStage;

import java.util.function.Consumer;

public class WhereAndOrStageImpl extends AbstractStageImpl implements WhereAndOrStage {


    protected WhereAndOrStageImpl(DataSourceDao dao) {
        super(dao);
    }


    @Override
    public WhereAndOrStage and(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public WhereAndOrStage and(DBColumn<?> column, Object value) {
        return null;
    }

    @Override
    public WhereAndOrStage and(Consumer<WhereAndOrStage> andOrStage) {
        return null;
    }

    @Override
    public WhereAndOrStage or(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public WhereAndOrStage or(DBColumn<?> column, Object value) {
        return null;
    }

    @Override
    public WhereAndOrStage or(Consumer<WhereAndOrStage> andOrStage) {
        return null;
    }
}
