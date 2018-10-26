package com.akigo.dao.datasource.nosql;

import com.akigo.dao.builder.DataSourceDaoBuilder;
import com.akigo.dao.datasource.DataSource;
import com.akigo.dao.entity.DBTable;
import com.akigo.dao.builder.nosql.DefaultNoSQLDataSourceDaoBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultNoSQLDataSource implements DataSource<DefaultNoSQLDataSourceRow> {

    private final Map<String, List<Map<String, ? extends Object>>> dataSource;

    public DefaultNoSQLDataSource(Map<String, List<Map<String, ? extends Object>>> dataSource) {
        this.dataSource = dataSource;
    }

//    @Override
//    public Map<String, List<Map<String, ? extends Object>>> getDataSource() {
//        return this.dataSource;
//    }

    @Override
    public Stream<DefaultNoSQLDataSourceRow> getRowStream(DBTable table) {
        return this.dataSource.get(table.getSimpleName()).stream().map((row) -> new DefaultNoSQLDataSourceRow(row));
    }

    @Override
    public DataSourceDaoBuilder getDaoBuilder() {
        return new DefaultNoSQLDataSourceDaoBuilder(this);
    }


}
