package com.akigo.dao.stage.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.stage.HavingAndOrStage;
import com.akigo.dao.stage.HavingStage;
import com.akigo.dao.stage.OrderByStage;

import java.util.function.Consumer;

public class HavingStageImpl extends OrderByStageImpl implements HavingStage {

    protected HavingStageImpl(DataSourceDao dao) {
        super(dao);
    }

    @Override
    public OrderByStage having(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public OrderByStage having(DBColumn<?> column, Object value) {
        return null;
    }

    @Override
    public OrderByStage having(Consumer<HavingAndOrStage> andOrStage) {
        return null;
    }
}
