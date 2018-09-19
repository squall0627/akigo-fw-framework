/*
 * SimpleAnswer.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit.simplePowerMockito;

import org.mockito.invocation.InvocationOnMock;

/**
 * Simple Answer<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public interface SimpleAnswer<T> {
    void answer(InvocationOnMock invocation) throws Throwable;
}
