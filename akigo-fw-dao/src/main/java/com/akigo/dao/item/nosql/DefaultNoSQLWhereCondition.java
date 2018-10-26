package com.akigo.dao.item.nosql;

import com.akigo.dao.datasource.DataSourceRow;
import com.akigo.dao.behavior.CONDITION_OPERATOR;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.FilterableItem;

import java.util.function.Predicate;

public class DefaultNoSQLWhereCondition implements FilterableItem<Predicate<DataSourceRow>> {

    private final DBColumn<?> column;
    private final CONDITION_OPERATOR operator;
    private final Object value;

    private final Predicate<DataSourceRow> executeExp;

    public DefaultNoSQLWhereCondition(DBColumn<?> column, CONDITION_OPERATOR operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
        this.executeExp = (row) -> operator.getVarificationMethod().test(row.getColValue(column), value);
    }

//    private DefaultNoSQLWhereCondition(Predicate<DataSourceRow> executeExp) {
//        this.executeExp = executeExp;
//    }

    @Override
    public Predicate<DataSourceRow> getExecuteExp() {
//        return (row) -> this.operator.getVarificationMethod().test(row.getColValue(this.column), this.value);
        return this.executeExp;
    }

    @Override
    public FilterableItem and(FilterableItem<Predicate<DataSourceRow>> target) {
        this.executeExp.and(target.getExecuteExp());
        return this;
    }

    @Override
    public FilterableItem or(FilterableItem<Predicate<DataSourceRow>> target) {
        this.executeExp.or(target.getExecuteExp());
        return this;
    }
}
