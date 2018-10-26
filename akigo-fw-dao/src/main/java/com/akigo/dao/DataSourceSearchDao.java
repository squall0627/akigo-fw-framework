package com.akigo.dao;

import com.akigo.dao.datasource.DataSourceRow;

import java.util.List;
import java.util.Optional;

public interface DataSourceSearchDao extends DataSourceDao {
    List<DataSourceRow> getResultList();

    Optional<DataSourceRow> getResult();
}
