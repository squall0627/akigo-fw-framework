package com.akigo.test.mocker;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;

/**
 * 機能名 : 単体テスト支援ツールクラススタブ<br>
 * <br>
 *
 * @param <T>
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public class StaticMockitoStubber<T> implements SimpleStubber<T> {

    private Class<T> classMock;
    private PowerMockitoStubber stubber;

    /**
     * コンストラクター
     * <br>
     *
     * @param classMock モック対象クラス
     * @param stubber   スタブ
     */
    protected StaticMockitoStubber(Class<T> classMock, PowerMockitoStubber stubber) {
        this.classMock = classMock;
        this.stubber = stubber;
    }

    @Override
    public PrivatelyExpectedArguments when(String methodToExpect, Class<?>... parameterTypes) {
        try {
            return new DefaultPrivatelyExpectedArguments(this.stubber.when(
                    this.classMock,
                    PowerMockito.method(this.classMock, methodToExpect, parameterTypes)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}