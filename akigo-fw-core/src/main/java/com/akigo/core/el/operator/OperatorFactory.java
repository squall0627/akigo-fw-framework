/*
 * OperatorFactory.java
 * Created on  2020/10/20 18:35
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator;

import com.akigo.core.exception.SystemException;
import com.akigo.core.util.ReflectionUtils;

import java.util.*;

/**
 * {@link AbstractOperator}のインスタンスファクトリー<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class OperatorFactory {

    private static final Map<OperatorSymbol, AbstractOperator> OPERATOR_MAP = new HashMap<>();

    static {
        Set<Class<?>> operatorClasses = ReflectionUtils.getAllClassesWith("com.akigo.core.el.operator", Operator.class);
        operatorClasses.forEach(clazz -> {
            try {
                AbstractOperator instance = (AbstractOperator) clazz.newInstance();
                OperatorSymbol symbol = clazz.getAnnotation(Operator.class).symbol();
                if (OPERATOR_MAP.containsKey(symbol)) {
                    // 符号重複チェック
                    throw new SystemException("重複の" + symbol.symbol() + "が存在している。");
                }
                OPERATOR_MAP.put(symbol, instance);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new SystemException(e);
            }
        });
    }

    public static <T extends AbstractOperator> T ofOperator(Class<T> operatorClass) {
        Objects.requireNonNull(operatorClass);
        return ofOperator(operatorClass.getAnnotation(Operator.class).symbol());
    }

    public static <T extends AbstractOperator> T ofOperator(OperatorSymbol symbol) {
        Objects.requireNonNull(symbol);
        return (T) OPERATOR_MAP.get(symbol);
    }

    public static <T extends AbstractOperator> Optional<T> ofOperator(String symbol) {
        Objects.requireNonNull(symbol);
        return OperatorSymbol.of(symbol).map(OperatorFactory::ofOperator);
    }

    public static boolean isOperator(String symbol) {
        Objects.requireNonNull(symbol);
        return OperatorSymbol.of(symbol).map(OPERATOR_MAP::containsKey).orElse(false);
    }
}
