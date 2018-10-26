package com.akigo.dao.behavior;

import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.SelectItem;

public interface Countable extends Aggregatable {
    SelectItem countRow();

    SelectItem countOf(DBColumn<?> column);

    SelectItem countDistinctOf(DBColumn<?> column);
}
