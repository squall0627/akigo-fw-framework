package com.akigo.dao.datasource.nosql;

import com.akigo.dao.datasource.DataSourceRow;
import com.akigo.dao.entity.DBColumn;

import java.util.Map;

public class DefaultNoSQLDataSourceRow implements DataSourceRow {

    private final Map<String, ? extends Object> row;

    protected DefaultNoSQLDataSourceRow(Map<String, ? extends Object> row) {
        this.row = row;
    }

    @Override
    public Object getColValue(DBColumn<?> column) {
        return row.get(column.getName());
    }
}
