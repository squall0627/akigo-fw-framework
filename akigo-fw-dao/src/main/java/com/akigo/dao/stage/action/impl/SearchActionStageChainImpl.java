package com.akigo.dao.stage.action.impl;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.stage.action.SearchActionStageChain;
import com.akigo.dao.stage.impl.SearchWhereStageImpl;

public class SearchActionStageChainImpl extends SearchWhereStageImpl implements SearchActionStageChain {
    public SearchActionStageChainImpl(DataSourceDao dao) {
        super(dao);
    }

//    @Override
//    public HavingStage groupBy(DBColumn<?>... column) {
//        return new GroupByStageImpl(getDataSourceDao()).groupBy(column);
//    }
//
//    @Override
//    public void orderBy(DBColumn<?>... column) {
//        new OrderByStageImpl(getDataSourceDao()).orderBy(column);
//    }
//
//    @Override
//    public GroupByStage where(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
//        return new SearchWhereStageImpl(getDataSourceDao()).where(column, operator, value);
//    }
//
//    @Override
//    public GroupByStage where(DBColumn<?> column, Object value) {
//        return new SearchWhereStageImpl(getDataSourceDao()).where(column, value);
//    }
//
//    @Override
//    public GroupByStage where(Consumer<WhereAndOrStage> andOrStage) {
//        return new SearchWhereStageImpl(getDataSourceDao()).where(andOrStage);
//    }
}
