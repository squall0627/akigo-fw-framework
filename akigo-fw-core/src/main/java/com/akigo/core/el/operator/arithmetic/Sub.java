/*
 * Sub.java
 * Created on  2020/10/21 12:48
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
 * 四則演算の引き算（-）のオペレーター解析クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.SUB, priority = OperatorPriority.RIORITY_4)
public class Sub extends Arithmetic {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String s2 = getString(valStack.pop());
        String s1 = getString(valStack.pop());

        BigDecimal o2 = new BigDecimal(s2);
        BigDecimal o1 = new BigDecimal(s1);

        String result = o1.subtract(o2).toPlainString();

        LOGGER.debug(s1 + " - " + s2 + " = " + result);

        valStack.push(result);
    }
}
