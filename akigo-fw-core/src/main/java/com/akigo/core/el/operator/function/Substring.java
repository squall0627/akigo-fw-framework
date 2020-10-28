/*
 * Substring.java
 * Created on  2020/10/22 10:31
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.function;

import com.akigo.core.el.operator.*;
import com.akigo.core.exception.SystemException;

import java.util.Stack;

/**
 * 関数substringのオペレーター解析クラス<br>
 * substring(abcd, 1, 2)はJavaの"abcd".substring(1, 2)と同義<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.SUBSTRING, priority = OperatorPriority.RIORITY_6)
public class Substring extends Function {

    @Override
    protected void doFunction(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        int paramCount = OperatorFactory.ofOperator(ParamSeparator.class).getFuncParamCount();

        Object result;
        if (paramCount == 2) {
            int beginIndex = Double.valueOf(getDouble(valStack.pop())).intValue();
            String targetStr = getString(valStack.pop());

            result = targetStr.substring(beginIndex);

            LOGGER.debug("substring(" + targetStr + ", " + beginIndex + ") = " + result);
        } else if (paramCount == 3) {
            int endIndex = Double.valueOf(getDouble(valStack.pop())).intValue();
            int beginIndex = Double.valueOf(getDouble(valStack.pop())).intValue();
            String targetStr = getString(valStack.pop());

            result = targetStr.substring(beginIndex, endIndex);

            LOGGER.debug("substring(" + targetStr + ", " + beginIndex + ", " + endIndex + ") = " + result);
        } else {
            throw new SystemException(getSymbol() + "のパラメーター個数が不正。（2個または3個が必要）");
        }

        valStack.push(result);
    }
}
