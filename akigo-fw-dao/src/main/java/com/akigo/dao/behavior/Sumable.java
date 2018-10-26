package com.akigo.dao.behavior;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.GroupableItem;
import com.akigo.dao.item.SelectItem;

public interface Sumable extends Aggregatable {
    SelectItem sumOf(DBColumn<?> column);
}
