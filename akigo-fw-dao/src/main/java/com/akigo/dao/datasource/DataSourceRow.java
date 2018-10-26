package com.akigo.dao.datasource;

import com.akigo.dao.entity.DBColumn;

public interface DataSourceRow {
    Object getColValue(DBColumn<?> column);
}
