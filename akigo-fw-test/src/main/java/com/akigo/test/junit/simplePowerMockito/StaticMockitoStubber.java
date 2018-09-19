/*
 * StaticMockitoStubber.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit.simplePowerMockito;

import org.powermock.api.mockito.expectation.PowerMockitoStubber;

/**
 * 単体テスト支援ツールクラススタブ<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class StaticMockitoStubber<T> implements SimpleStubber<T> {
    private Class<T> classMock;
    private PowerMockitoStubber stubber;

    protected StaticMockitoStubber(Class<T> classMock, PowerMockitoStubber stubber) {
        this.classMock = classMock;
        this.stubber = stubber;
    }

    @Override
    public void when(String methodToExpect, Object... parameters) {
        try {
            this.stubber.when(this.classMock, methodToExpect, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T whenThis() {
        throw new UnsupportedOperationException("Staticモックは当該メソッドが使えません。");
    }
}
