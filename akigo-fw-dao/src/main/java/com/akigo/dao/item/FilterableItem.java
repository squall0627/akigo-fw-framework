package com.akigo.dao.item;

public interface FilterableItem<EXP> extends DaoItem<EXP> {

    FilterableItem and(FilterableItem<EXP> target);

    FilterableItem or(FilterableItem<EXP> target);
}
