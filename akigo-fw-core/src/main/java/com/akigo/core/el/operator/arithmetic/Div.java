/*
 * Div.java
 * Created on  2020/10/20 18:15
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.arithmetic;

import com.akigo.core.el.operator.AbstractOperator;
import com.akigo.core.el.operator.Operator;
import com.akigo.core.el.operator.OperatorPriority;
import com.akigo.core.el.operator.OperatorSymbol;

import java.math.BigDecimal;
import java.util.Stack;

/**
 * 四則演算の割り算（/）のオペレーター解析クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.DIV, priority = OperatorPriority.RIORITY_5)
public class Div extends Arithmetic {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String s2 = getString(valStack.pop());
        String s1 = getString(valStack.pop());

        BigDecimal o2 = new BigDecimal(s2);
        BigDecimal o1 = new BigDecimal(s1);

        String result;
        try {
            result = o1.divide(o2).toPlainString();
        } catch (ArithmeticException e) {
            if ("Non-terminating decimal expansion; no exact representable decimal result.".equals(e.getMessage())) {
                int scale = Math.max(o1.scale(), o2.scale());
                // 結果が無限循環小数の場合、四捨五入
                result = o1.divide(o2, scale, BigDecimal.ROUND_HALF_UP).toPlainString();
            } else {
                throw e;
            }
        }

        LOGGER.debug(s1 + " / " + s2 + " = " + result);

        valStack.push(result);
    }
}
