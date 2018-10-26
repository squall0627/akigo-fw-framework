package com.akigo.dao.behavior;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.SelectItem;

public interface Avgable extends Aggregatable {
    SelectItem avgOf(DBColumn<?> column);
}
