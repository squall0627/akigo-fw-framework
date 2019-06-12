package com.akigo.test.mocker;

import org.mockito.invocation.InvocationOnMock;

/**
 * 機能名 : 単体テスト支援ツールAnswer機能インタフェース<br>
 * <br>
 *
 * @param <T>
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public interface SimpleAnswer<T> {
    /**
     * SimpleAnswer
     * <br>
     *
     * @param invocation InvocationOnMock
     * @throws Throwable 例外
     */
    void answer(InvocationOnMock invocation) throws Throwable;
}
