package com.akigo.dao.stage.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.behavior.builder.BehaviorBuilder;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.stage.OrderByStage;

public class OrderByStageImpl extends AbstractStageImpl implements OrderByStage {

    protected OrderByStageImpl(DataSourceDao dao) {
        super(dao);
    }

    @Override
    public void orderBy(DBColumn<?>... column) {

    }
}
