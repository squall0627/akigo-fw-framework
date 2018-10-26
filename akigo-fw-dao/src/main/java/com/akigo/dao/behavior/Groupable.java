package com.akigo.dao.behavior;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.GroupableItem;

public interface Groupable {
    GroupableItem groupBy(DBColumn<?>... column);
}
