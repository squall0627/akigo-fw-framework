package com.akigo.dao.builder.nosql;

import com.akigo.dao.*;
import com.akigo.dao.builder.DataSourceDaoBuilder;
import com.akigo.dao.datasource.DataSource;
import com.akigo.dao.nosql.DefaultNoSQLDataSourceSearchDao;
import com.akigo.dao.stage.action.CreateActionStageChain;
import com.akigo.dao.stage.action.DeleteActionStageChain;
import com.akigo.dao.stage.action.SearchActionStageChain;
import com.akigo.dao.stage.action.UpdateActionStageChain;
import com.akigo.dao.stage.action.impl.SearchActionStageChainImpl;

import java.util.function.Consumer;

public class DefaultNoSQLDataSourceDaoBuilder implements DataSourceDaoBuilder {

    private final DataSource dataSource;

    public DefaultNoSQLDataSourceDaoBuilder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

//    @Override
//    public BehaviorBuilder getBehaviorBuilder() {
//        return new DefaultNoSQLBehaviorBuilder();
//    }

    @Override
    public DataSourceSearchDao withSearch(Consumer<SearchActionStageChain> stage) {
        DataSourceSearchDao dao = new DefaultNoSQLDataSourceSearchDao(this.dataSource);
        stage.accept(new SearchActionStageChainImpl(dao));
        return dao;
    }

    @Override
    public DataSourceCreateDao withCreate(Consumer<CreateActionStageChain> stage) {
        return null;
    }

    @Override
    public DataSourceUpdateDao withUpdate(Consumer<UpdateActionStageChain> stage) {
        return null;
    }

    @Override
    public DataSourceDeleteDao withDelete(Consumer<DeleteActionStageChain> stage) {
        return null;
    }


//    @Override
//    public DataSourceDao build() {
//        return null;
//    }


}
