/*
 * ParamSeparator.java
 * Created on  2020/10/21 12:58
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
 * ファンクションパラメーターの分割符号”,”のオペレーター解析クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.PARAM_SEPARATOR, priority = OperatorPriority.PRIORITY_0)
public class ParamSeparator extends AbstractOperator {

    private final ThreadLocal<Integer> funcParamCount = new ThreadLocal<>();

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        // パラメーター個数が不固定の関数に対して、解析中の関数のパラメーター個数をカウントする
        if (funcParamCount.get() == null) {
            funcParamCount.set(2);
        } else {
            funcParamCount.set(funcParamCount.get() + 1);
        }
    }

    public void clearFuncParamCount() {
        /*
         * 当該関数が解析完了した後、必ずこの関数のパラメーターカウントをクリアしてください。
         */
        funcParamCount.remove();
    }

    public int getFuncParamCount() {
        return funcParamCount.get() == null ? 1 : funcParamCount.get();
    }
}
