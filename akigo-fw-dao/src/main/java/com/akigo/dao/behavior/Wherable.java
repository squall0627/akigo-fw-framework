package com.akigo.dao.behavior;

import com.akigo.dao.DataSourceDao;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.FilterableItem;

public interface Wherable extends Filterable {
    FilterableItem whereOf(DBColumn<?> column, CONDITION_OPERATOR operator, Object value);

    FilterableItem whereOf(DBColumn<?> column, Object value);
}
