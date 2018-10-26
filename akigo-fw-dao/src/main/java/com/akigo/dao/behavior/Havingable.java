package com.akigo.dao.behavior;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.FilterableItem;

public interface Havingable extends Filterable {
    FilterableItem havingOf(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    FilterableItem havingOf(DBColumn<?> column, Object value);
}
