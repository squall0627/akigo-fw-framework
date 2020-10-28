/*
 * ConstantValue.java
 * Created on  2020/10/22 12:43
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.value;

import com.akigo.core.el.ValueExpressionParser;
import com.akigo.core.exception.SystemException;

/**
 * 定数値表現式クラス<br>
 * <pre>
 *  CONSTANTS.NAME表現式の正規表現式<br>
 *  例：<br>
 *  $CONSTANTS.TD$<br>
 * </pre>
 *
 * @author chenhao
 * @since 1.0.0
 */
@ValueExpression(regex = "\\$CONSTANTS\\.[_0-9a-zA-Z]+\\$")
public class ConstantValue extends AbstractValueExpression {

    /**
     * 値表現式（シングル値表現式）の解析処理<br>
     *
     * @param valueExpression 値表現式
     * @param parser          値表現式解析用{@link ValueExpressionParser}
     * @return 解析後文字列
     */
    @Override
    public String eval(String valueExpression, ValueExpressionParser parser) {
        LOGGER.debug(valueExpression + " の解析開始。");

        String valueExpressionTmp = valueExpression.substring(1, valueExpression.length() - 1);
        String[] valueExpressionArr = valueExpressionTmp.split("\\.");
        String constantName = valueExpressionArr[1];

        if (!parser.getConstantsMap().containsKey(constantName)) {
            throw new SystemException("Constant:" + constantName + "が見つかりませんでした。");
        }
        String value = parser.getConstantsMap().get(constantName);

        value = parser.parseValue(value);
        // 解析したConstantをマップに更新
        parser.getConstantsMap().put(constantName, value);

        LOGGER.debug(valueExpression + " の解析結果= " + value);

        return value;
    }
}
