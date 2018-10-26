package com.akigo.dao.behavior.builder.nosql;

import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.behavior.builder.BehaviorBuilder;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.FilterableItem;
import com.akigo.dao.item.GroupableItem;
import com.akigo.dao.item.OrderableItem;
import com.akigo.dao.item.SelectItem;

public class DefaultNoSQLBehaviorBuilder implements BehaviorBuilder {
    @Override
    public SelectItem avgOf(DBColumn<?> column) {
        return null;
    }

    @Override
    public SelectItem countRow() {
        return null;
    }

    @Override
    public SelectItem countOf(DBColumn<?> column) {
        return null;
    }

    @Override
    public SelectItem countDistinctOf(DBColumn<?> column) {
        return null;
    }

    @Override
    public GroupableItem groupBy(DBColumn<?>... column) {
        return null;
    }

    @Override
    public FilterableItem havingOf(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public FilterableItem havingOf(DBColumn<?> column, Object value) {
        return null;
    }

    @Override
    public SelectItem maxOf(DBColumn<?> column) {
        return null;
    }

    @Override
    public SelectItem minOf(DBColumn<?> column) {
        return null;
    }

    @Override
    public OrderableItem orderBy(DBColumn<?>... column) {
        return null;
    }

    @Override
    public SelectItem sumOf(DBColumn<?> column) {
        return null;
    }

    @Override
    public FilterableItem whereOf(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        return null;
    }

    @Override
    public FilterableItem whereOf(DBColumn<?> column, Object value) {
        return null;
    }
}
