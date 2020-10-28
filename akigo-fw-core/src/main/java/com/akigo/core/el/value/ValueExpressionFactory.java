/*
 * ValueExpressionFactory.java
 * Created on  2020/10/22 15:21
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.value;

import com.akigo.core.exception.SystemException;
import com.akigo.core.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@link AbstractValueExpression}のインスタンスファクトリー<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class ValueExpressionFactory {

    private static final List<AbstractValueExpression> VALUE_EXPRESSIONS = new ArrayList<>();

    static {
        Set<Class<?>> valueExpressionClasses = ReflectionUtils.getAllClassesWith("com.akigo.core.el.value", ValueExpression.class);
        valueExpressionClasses.forEach(clazz -> {
            try {
                AbstractValueExpression instance = (AbstractValueExpression) clazz.newInstance();
                VALUE_EXPRESSIONS.add(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new SystemException(e);
            }
        });
    }

    public static List<AbstractValueExpression> getValueExpressions() {
        return VALUE_EXPRESSIONS;
    }
}
