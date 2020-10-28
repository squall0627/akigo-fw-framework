/*
 * ValueExpressionParser.java
 * Created on  2020/10/22 12:30
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el;

import com.akigo.core.el.value.ValueExpressionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * 値表現式解析クラス<br>
 * <pre>
 *   $$で囲んでいる表現式の解析
 *   例：$CONSTANTS.TD$
 * </pre>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class ValueExpressionParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValueExpressionParser.class);

    private final Map<String, String> constantsMap;
    private final AGELParser agelParser;

    public ValueExpressionParser(AGELParser agelParser) {
        this.agelParser = agelParser;
        this.constantsMap = agelParser.getConstantsMap();
    }

    /**
     * 表現式解析処理<br>
     *
     * @param value 解析対象
     * @return 解析後値
     */
    public final String parseValue(String value) {
        if (agelParser.isLogicExpression(value)) {
            return agelParser.eval(value);
        } else if (isValueExpression(value) || hasValueExpression(value)) {
            return parseValueExpression(value);
        }
        return value;
    }

    private boolean isValueExpression(String value) {
        return ValueExpressionFactory.getValueExpressions().stream().anyMatch(ve -> ve.isValueExpression(value));
    }

    private boolean hasValueExpression(String value) {
        return ValueExpressionFactory.getValueExpressions().stream().anyMatch(ve -> ve.hasValueExpression(value));
    }

    private String parseValueExpression(String valueExpression) {
        LOGGER.debug(valueExpression + " の解析開始");

        String result;

        Optional<String> parsedValue = ValueExpressionFactory.getValueExpressions()
                .stream()
                .filter(ve -> ve.isValueExpression(valueExpression))
                .map(ve -> ve.eval(valueExpression, this))
                .findFirst();
        if (parsedValue.isPresent()) {
            result = parsedValue.get();
        } else {
            result = ValueExpressionFactory.getValueExpressions()
                    .stream()
                    .filter(ve -> ve.hasValueExpression(valueExpression))
                    .reduce(valueExpression, (s, ve) -> ve.evalAll(s, this), (s, s2) -> s = s2);
        }

        LOGGER.debug(valueExpression + " の解析結果 = " + result);

        return result;
    }

    public final Map<String, String> getConstantsMap() {
        return this.constantsMap;
    }
}
