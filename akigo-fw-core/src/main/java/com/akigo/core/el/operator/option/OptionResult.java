/*
 * OptionResult.java
 * Created on  2020/10/22 10:50
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.option;

import com.akigo.core.el.operator.AbstractOperator;
import com.akigo.core.el.operator.Operator;
import com.akigo.core.el.operator.OperatorPriority;
import com.akigo.core.el.operator.OperatorSymbol;
import com.akigo.core.exception.SystemException;

import java.util.Stack;

/**
 * 選択肢判定結果のオペレーター解析クラス<br>
 * 例：(a == b) ? 1 : 2<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.OPTION_RESULT, priority = OperatorPriority.RIORITY_1)
public class OptionResult extends Option {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        Object result2 = valStack.pop();
        Object result1 = valStack.pop();
        Object predicate = valStack.pop();

        if (!isBoolean(predicate)) {
            throw new SystemException("OPTION:" + getSymbol() + "の判定式の型が不正。（boolean型が必要）");
        }

        Object result = getBoolean(predicate) ? result1 : result2;

        LOGGER.debug(predicate + " ? " + result1 + " : " + result2 + " = " + result);

        valStack.push(result);
    }
}
