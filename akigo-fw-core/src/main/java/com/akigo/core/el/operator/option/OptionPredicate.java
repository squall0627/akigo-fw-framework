/*
 * OptionPredicate.java
 * Created on  2020/10/22 10:46
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

import java.util.Stack;

/**
 * 選択肢判定式のオペレーター解析クラス<br>
 * 例：(a == b) ? 1 : 2<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Operator(symbol = OperatorSymbol.OPTION_PREDICATE, priority = OperatorPriority.RIORITY_1)
public class OptionPredicate extends Option {

    @Override
    public void accept(Stack<AbstractOperator> opStack, Stack<Object> valStack) {
        // 何もしない
    }
}
