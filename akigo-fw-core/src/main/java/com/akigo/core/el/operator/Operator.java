/*
 * Operator.java
 * Created on  2020/10/20 17:51
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator;

import java.lang.annotation.*;

/**
 * AGEL表現式（AkiGo Expression Language）のオペレーター<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Operator {

    OperatorSymbol symbol();

    OperatorPriority priority();
}
