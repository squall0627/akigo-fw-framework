/*
 * IsEmpty.java
 * Created on  2020/10/22 10:13
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
 * 関数isEmptyのオペレーター解析クラス<br>
 * isEmpty(abcd)はJavaの"abcd".length() == 0と同義<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.ISEMPTY, priority = OperatorPriority.RIORITY_6)
public class IsEmpty extends Function {

    @Override
    protected void doFunction(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        String target = getString(valStack.pop());

        Object result = isEmptyStr(target);

        LOGGER.debug("isEmpty(" + target + ") = " + result);

        valStack.push(result);
    }
}
