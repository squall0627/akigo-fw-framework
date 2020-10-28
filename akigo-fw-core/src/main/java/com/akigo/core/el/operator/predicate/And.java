/*
 * And.java
 * Created on  2020/10/22 11:59
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
import com.akigo.core.exception.SystemException;

import java.util.Stack;

/**
 * 判定式”&&”のオペレーター解析クラス<br>
 * Javaの”&&”と同義<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.AND, priority = OperatorPriority.RIORITY_2)
public class And extends Predicate {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String s2 = getString(valStack.pop());
        String s1 = getString(valStack.pop());

        if (!isBoolean(s1) || !isBoolean(s2)) {
            throw new SystemException("PREDICATE:" + getSymbol() + "のパラメーター型が不正。（boolean型が必要）");
        }

        Object result = getBoolean(s1) && getBoolean(s2);

        LOGGER.debug(s1 + " && " + s2 + " = " + result);

        valStack.push(result);
    }
}
