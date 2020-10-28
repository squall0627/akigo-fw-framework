/*
 * EndsWith.java
 * Created on  2020/10/22 10:00
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
 * 関数endsWithのオペレーター解析クラス<br>
 * endsWith(abcd, d)はJavaの"abcd".endsWith("d")と同義<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.ENDSWITH, priority = OperatorPriority.RIORITY_6)
public class EndsWith extends Function {

    @Override
    protected void doFunction(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String suffix = getString(valStack.pop());
        String targetStr = getString(valStack.pop());

        Object result = targetStr.endsWith(suffix);

        LOGGER.debug("endsWith(" + targetStr + ", " + suffix + ") = " + result);

        valStack.push(result);
    }
}
