package com.akigo.dao.behavior;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.OrderableItem;

public interface Orderable {
    OrderableItem orderBy(DBColumn<?>... column);
}
