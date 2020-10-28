/*
 * Length.java
 * Created on  2020/10/22 10:24
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.function;

import com.akigo.core.el.operator.AbstractOperator;
import com.akigo.core.el.operator.Operator;
import com.akigo.core.el.operator.OperatorPriority;
import com.akigo.core.el.operator.OperatorSymbol;

import java.util.Stack;

/**
 * 関数lengthのオペレーター解析クラス<br>
 * length(abcd)はJavaの"abcd".length()と同義<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.LENGTH, priority = OperatorPriority.RIORITY_6)
public class Length extends Function {

    @Override
    protected void doFunction(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String target = getString(valStack.pop());

        Object result = target.length();

        LOGGER.debug("length(" + target + ") = " + result);

        valStack.push(result);
    }
}
