/*
 * RightParenthesis.java
 * Created on  2020/10/22 11:05
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.parenthesis;

import com.akigo.core.el.operator.*;

import java.util.Stack;

/**
 * 右カッコ”)”のオペレーター解析クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.RIGHT_PARENTHESIS, priority = OperatorPriority.RIORITY_7)
public class RightParenthesis extends Parenthesis {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        AbstractOperator lastOp;
        while (true) {
            lastOp = opStack.pop();
            lastOp.accept(opStack, valStack);
            if (lastOp.equals(OperatorFactory.ofOperator(LeftParenthesis.class))) {
                break;
            }
        }
    }
}
