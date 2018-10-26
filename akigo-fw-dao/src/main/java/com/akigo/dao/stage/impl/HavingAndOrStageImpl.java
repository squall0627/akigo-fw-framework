package com.akigo.dao.stage.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.stage.HavingAndOrStage;

import java.util.function.Consumer;

public class HavingAndOrStageImpl extends AbstractStageImpl implements HavingAndOrStage {


    protected HavingAndOrStageImpl(DataSourceDao dao) {
        super(dao);
    }

    @Override
    public HavingAndOrStage and(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public HavingAndOrStage and(DBColumn<?> column, Object value) {
        return null;
    }

    @Override
    public HavingAndOrStage and(Consumer<HavingAndOrStage> andOrStage) {
        return null;
    }

    @Override
    public HavingAndOrStage or(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public HavingAndOrStage or(DBColumn<?> column, Object value) {
        return null;
    }

    @Override
    public HavingAndOrStage or(Consumer<HavingAndOrStage> andOrStage) {
        return null;
    }
}
