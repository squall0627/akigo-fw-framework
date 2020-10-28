/*
 * StartsWith.java
 * Created on  2020/10/22 10:28
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
 * 関数startsWithのオペレーター解析クラス<br>
 * startsWith(abcd, a)はJavaの"abcd".startsWith("a")と同義<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.STARTSWITH, priority = OperatorPriority.RIORITY_6)
public class StartsWith extends Function {

    @Override
    protected void doFunction(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String prefix = getString(valStack.pop());
        String targetStr = getString(valStack.pop());

        Object result = targetStr.startsWith(prefix);

        LOGGER.debug("startsWith(" + targetStr + ", " + prefix + ") = " + result);

        valStack.push(result);
    }
}
