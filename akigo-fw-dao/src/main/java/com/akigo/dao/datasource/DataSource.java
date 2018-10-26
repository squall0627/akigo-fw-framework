package com.akigo.dao.datasource;

import com.akigo.dao.builder.DataSourceDaoBuilder;
import com.akigo.dao.entity.DBTable;

import java.util.stream.Stream;

public interface DataSource<ROW> {
//    T getDataSource();

    Stream<ROW> getRowStream(DBTable table);

    DataSourceDaoBuilder getDaoBuilder();
}
