package com.akigo.dao;

import com.akigo.dao.behavior.Wherable;
import com.akigo.dao.behavior.builder.BehaviorBuilder;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.entity.DBTable;
import com.akigo.dao.item.AggregatableItem;
import com.akigo.dao.item.FilterableItem;
import com.akigo.dao.item.GroupableItem;
import com.akigo.dao.item.OrderableItem;

import java.util.List;
import java.util.Optional;

public interface DataSourceDao {

    BehaviorBuilder getBehaviorBuilder();

//    DataSourceDao where(FilterableItem whereCondition);
//
//    DataSourceDao having(FilterableItem havingCondition);
//
//    DataSourceDao aggregate(AggregatableItem... aggregatableItem);
//
//    DataSourceDao groupBy(GroupableItem... groupableItem);
//
//    DataSourceDao orderBy(OrderableItem... orderableItem);

//    DataSourceDaoBuilder getDaoBuilder();

}
