/*
 * GreatEqual.java
 * Created on  2020/10/22 12:09
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.predicate;

import com.akigo.core.el.operator.AbstractOperator;
import com.akigo.core.el.operator.Operator;
import com.akigo.core.el.operator.OperatorPriority;
import com.akigo.core.el.operator.OperatorSymbol;

import java.util.Stack;

/**
 * 判定式”>=”のオペレーター解析クラス<br>
 * Javaの”>=”と同義<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.GE, priority = OperatorPriority.RIORITY_3)
public class GreatEqual extends Predicate {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String str2 = getString(valStack.pop());
        String str1 = getString(valStack.pop());

        Object result = null;
        if (isNumber(str1) && isNumber(str2)) {
            result = getDouble(str1) >= getDouble(str2);
        } else {
            result = str1.compareTo(str2) >= 0;
        }

        LOGGER.debug(str1 + " >= " + str2 + " = " + result);

        valStack.push(result);
    }
}
