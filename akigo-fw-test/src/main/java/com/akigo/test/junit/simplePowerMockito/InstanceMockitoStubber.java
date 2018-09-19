/*
 * InstanceMockitoStubber.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit.simplePowerMockito;

import org.powermock.api.mockito.expectation.PowerMockitoStubber;

/**
 * 単体テスト支援ツールインスタンススタブ<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class InstanceMockitoStubber<T> implements SimpleStubber<T> {
    private T mockedTarget;
    private PowerMockitoStubber stubber;

    protected InstanceMockitoStubber(T mockedTarget, PowerMockitoStubber stubber) {
        this.mockedTarget = mockedTarget;
        this.stubber = stubber;
    }

    @Override
    public void when(String methodToExpect, Object... parameters) {
        try {
            this.stubber.when(this.mockedTarget, methodToExpect, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T whenThis() {
        return this.stubber.when(this.mockedTarget);
    }
}
