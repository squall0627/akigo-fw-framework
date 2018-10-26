package com.akigo.dao.stage.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.stage.GroupByStage;
import com.akigo.dao.stage.SearchWhereStage;
import com.akigo.dao.stage.WhereAndOrStage;

import java.util.function.Consumer;

public class SearchWhereStageImpl extends GroupByStageImpl implements SearchWhereStage {


    protected SearchWhereStageImpl(DataSourceDao dao) {
        super(dao);
    }

    @Override
    public GroupByStage where(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public GroupByStage where(DBColumn<?> column, Object value) {
        return null;
    }

    @Override
    public GroupByStage where(Consumer<WhereAndOrStage> andOrStage) {
        return null;
    }
}
