package com.akigo.dao.nosql;

import com.akigo.dao.datasource.DataSource;
import com.akigo.dao.datasource.DataSourceRow;
import com.akigo.dao.DataSourceSearchDao;
import com.akigo.dao.behavior.builder.BehaviorBuilder;
import com.akigo.dao.behavior.builder.nosql.DefaultNoSQLBehaviorBuilder;

import java.util.List;
import java.util.Optional;

public class DefaultNoSQLDataSourceSearchDao implements DataSourceSearchDao {
    private final DataSource dataSource;

    public DefaultNoSQLDataSourceSearchDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<DataSourceRow> getResultList() {
        return null;
    }

    @Override
    public Optional<DataSourceRow> getResult() {
        return null;
    }

    @Override
    public BehaviorBuilder getBehaviorBuilder() {
        return new DefaultNoSQLBehaviorBuilder();
    }
}
