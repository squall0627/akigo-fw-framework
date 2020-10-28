/*
 * ValueExpression.java
 * Created on  2020/10/22 12:27
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.value;

import java.lang.annotation.*;

/**
 * 値表現式インタフェース<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValueExpression {

    String regex();
}
