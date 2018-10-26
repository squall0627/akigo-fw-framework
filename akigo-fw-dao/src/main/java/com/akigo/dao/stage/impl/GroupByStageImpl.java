package com.akigo.dao.stage.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.stage.GroupByStage;
import com.akigo.dao.stage.HavingStage;

public class GroupByStageImpl extends OrderByStageImpl implements GroupByStage {

    protected GroupByStageImpl(DataSourceDao dao) {
        super(dao);
    }

    @Override
    public HavingStage groupBy(DBColumn<?>... column) {
        this.getDataSourceDao().getBehaviorBuilder().groupBy(column);
        return new HavingStageImpl(getDataSourceDao());
    }
}
