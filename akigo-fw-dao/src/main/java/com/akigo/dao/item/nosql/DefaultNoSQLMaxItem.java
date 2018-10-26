package com.akigo.dao.item.nosql;

import com.akigo.dao.datasource.DataSourceRow;
import com.akigo.dao.entity.DBColumn;
import com.akigo.dao.item.DaoItem;

import java.util.Comparator;

public class DefaultNoSQLMaxItem implements DaoItem<Comparator<DataSourceRow>> {

    private final DBColumn<?> column;

    protected DefaultNoSQLMaxItem(DBColumn<?> column) {
        this.column = column;
    }

    @Override
    public Comparator<DataSourceRow> getExecuteExp() {
        return (row1, row2) -> {
            Object str1 = row1.getColValue(column);
            Object str2 = row2.getColValue(column);
            // TODO
            return -1;


//            try {
//                Integer num1 = Integer.valueOf(str1);
//                Integer num2 = Integer.valueOf(str2);
//                return num2 - num1;
//            } catch (NumberFormatException ex) {
//                return str2.compareTo(str1);
//            }
        };
    }
}
