/*
 * Function.java
 * Created on  2020/10/21 12:55
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator.function;

import com.akigo.core.el.operator.AbstractOperator;
import com.akigo.core.el.operator.OperatorFactory;

import java.util.Stack;

/**
 * 関数オペレーターの抽象化クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public abstract class Function extends AbstractOperator {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        try {
            doFunction(opStack, valStack);
        } finally {
            /*
             * 当該関数が解析完了した後、必ずこの関数のパラメーターカウントをクリアしてください。
             */
            OperatorFactory.ofOperator(ParamSeparator.class).clearFuncParamCount();
        }
    }

    protected abstract void doFunction(Stack<AbstractOperator> opStack, Stack<Object> valStack);
}
