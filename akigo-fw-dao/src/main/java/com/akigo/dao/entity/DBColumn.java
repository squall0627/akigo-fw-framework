package com.akigo.dao.entity;

public interface DBColumn<T> {

    String getName();

    String getOtherName();

    DBTable getTable();
}
