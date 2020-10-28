/*
 * LeftParenthesis.java
 * Created on  2020/10/22 11:01
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.parenthesis;

import com.akigo.core.el.operator.AbstractOperator;
import com.akigo.core.el.operator.Operator;
import com.akigo.core.el.operator.OperatorPriority;
import com.akigo.core.el.operator.OperatorSymbol;
import com.akigo.core.el.operator.function.Function;

import java.util.Stack;

/**
 * 左カッコ”(”のオペレーター解析クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.LEFT_PARENTHESIS, priority = OperatorPriority.RIORITY_7)
public class LeftParenthesis extends Parenthesis {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        if (opStack.peek() instanceof Function) {
            AbstractOperator lastOp = opStack.pop();
            lastOp.accept(opStack, valStack);
        }
    }
}
