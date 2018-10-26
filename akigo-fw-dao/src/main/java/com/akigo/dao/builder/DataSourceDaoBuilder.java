package com.akigo.dao.builder;

import com.akigo.dao.DataSourceCreateDao;
import com.akigo.dao.DataSourceDeleteDao;
import com.akigo.dao.DataSourceSearchDao;
import com.akigo.dao.DataSourceUpdateDao;
import com.akigo.dao.stage.action.CreateActionStageChain;
import com.akigo.dao.stage.action.DeleteActionStageChain;
import com.akigo.dao.stage.action.SearchActionStageChain;
import com.akigo.dao.stage.action.UpdateActionStageChain;

import java.util.function.Consumer;

public interface DataSourceDaoBuilder {
//    BehaviorBuilder getBehaviorBuilder();

    DataSourceSearchDao withSearch(Consumer<SearchActionStageChain> stage);

    DataSourceCreateDao withCreate(Consumer<CreateActionStageChain> stage);

    DataSourceUpdateDao withUpdate(Consumer<UpdateActionStageChain> stage);

    DataSourceDeleteDao withDelete(Consumer<DeleteActionStageChain> stage);

//    DataSourceDao build();
}
