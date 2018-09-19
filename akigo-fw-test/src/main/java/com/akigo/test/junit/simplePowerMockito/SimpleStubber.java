/*
 * SimpleStubber.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit.simplePowerMockito;

/**
 * 簡単スタブ<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public interface SimpleStubber<T> {
    /**
     * モック条件(インタフェースのdefaultメソッド以外場合)
     * <br>
     *
     * @param methodToExpect モック先メソッド名
     * @param parameters     モック先メソッドパラメータ（パラメータなし場合、渡さなくていい）
     */
    void when(String methodToExpect, Object... parameters);

    /**
     * モック条件(instance publicメソッド、またはインタフェースのdefaultメソッド場合)
     * <br>
     * <p>
     * インタフェースのdefaultメソッドなど、when(String methodToExpect, Object... parameters)で<br>
     * モックできない場合、当該メソッドでモックしてください。<br>
     *
     * @return モックされたインスタンス
     */
    T whenThis();
}
