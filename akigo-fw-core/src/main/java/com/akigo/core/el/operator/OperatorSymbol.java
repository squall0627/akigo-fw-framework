/*
 * OperatorName.java
 * Created on  2020/10/21 13:12
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator;

import java.util.Arrays;
import java.util.Optional;

/**
 * オペレーターの符号Enum<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public enum OperatorSymbol {
    PLUS("+"),
    SUB("-"),
    MULTI("*"),
    DIV("/"),

    ISEMPTY("isEmpty"),
    ISNOTEMPTY("isNotEmpty"),
    STARTSWITH("startsWith"),
    ENDSWITH("endsWith"),
    LENGTH("length"),
    SUBSTRING("substring"),
    PARAM_SEPARATOR(","),

    EQ("=="),
    NE("!="),
    GT(">"),
    LT("<"),
    GE(">="),
    LE("<="),

    AND("&&"),
    OR("||"),

    OPTION_PREDICATE("?"),
    OPTION_RESULT(":"),

    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")");

    public static Optional<OperatorSymbol> of(String symbol) {
        return Arrays.stream(OperatorSymbol.values()).filter(s -> s.symbol().equals(symbol)).findFirst();
    }

    private final String symbol;

    OperatorSymbol(String symbol) {
        this.symbol = symbol;
    }

    public final String symbol() {
        return this.symbol;
    }
}
