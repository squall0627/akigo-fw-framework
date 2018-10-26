package com.akigo.dao.behavior;

import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.GroupableItem;
import com.akigo.dao.item.SelectItem;

public interface Minable extends Aggregatable {
    SelectItem minOf(DBColumn<?> column);
}
