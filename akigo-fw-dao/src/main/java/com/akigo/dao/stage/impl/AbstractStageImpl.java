package com.akigo.dao.stage.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.behavior.builder.BehaviorBuilder;

public abstract class AbstractStageImpl {
    private DataSourceDao dao;


    protected AbstractStageImpl(DataSourceDao dao) {
        this.dao = dao;
    }

    protected DataSourceDao getDataSourceDao() {
        return this.dao;
    }


//    @Override
//    public DataSourceDao build() {
//        return this.dao.build();
//    }
}
