/*
 * AbstractOperator.java
 * Created on  2020/10/20 17:59
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator;

import com.akigo.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * AGEL表現式（AkiGo Expression Language）の抽象化クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public abstract class AbstractOperator {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperator.class);

    private static final String EMPTY_STR_PLACEHOLDER = "null";

    public static boolean isEmptyStr(Object str) {
        return Strings.isNullOrEmpty(str == null ? null : str.toString()) || EMPTY_STR_PLACEHOLDER.equals(str.toString());
    }

    public static String getString(Object str) {
        if (isEmptyStr(str)) {
            return "";
        }
        return str.toString();
    }

    public static boolean isNumber(Object str) {
        String target = getString(str);
        try {
            Double.valueOf(target);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static double getDouble(Object str) {
        String target = getString(str);
        return Double.parseDouble(target);
    }

    public static boolean isBoolean(Object str) {
        String target = getString(str);
        return Boolean.TRUE.toString().equalsIgnoreCase(target) || Boolean.FALSE.toString().equalsIgnoreCase(target);
    }

    public static boolean getBoolean(Object str) {
        String target = getString(str);
        return Boolean.parseBoolean(target);
    }

    private final Operator operator;

    public abstract void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack);

    public AbstractOperator() {
        this.operator = this.getClass().getAnnotation(Operator.class);
    }

    public String getSymbol() {
        return this.operator.symbol().symbol();
    }

    public int getPriority() {
        return this.operator.priority().priority();
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }
}
