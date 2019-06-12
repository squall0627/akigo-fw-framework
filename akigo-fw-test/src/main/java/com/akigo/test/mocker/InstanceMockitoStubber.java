package com.akigo.test.mocker;

import java.lang.reflect.Method;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;
import org.powermock.reflect.exceptions.MethodNotFoundException;
import org.powermock.reflect.exceptions.TooManyMethodsFoundException;
import org.powermock.reflect.internal.WhiteboxImpl;

/**
 * 機能名 : 単体テスト支援ツールインスタンススタブ<br>
 * <br>
 *
 * @param <T>
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public class InstanceMockitoStubber<T> implements SimpleStubber<T> {
    private T mockedTarget;
    private PowerMockitoStubber stubber;

    /**
     * コンストラクター
     * <br>
     *
     * @param mockedTarget モック対象
     * @param stubber      スタブ
     */
    protected InstanceMockitoStubber(T mockedTarget, PowerMockitoStubber stubber) {
        this.mockedTarget = mockedTarget;
        this.stubber = stubber;
    }

    @Override
    public PrivatelyExpectedArguments when(String methodToExpect, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = PowerMockito.method(
                    this.mockedTarget.getClass(),
                    methodToExpect,
                    parameterTypes);
        } catch (TooManyMethodsFoundException | MethodNotFoundException e) {
            // instance publicメソッド、またはインタフェースのdefaultメソッドなど場合、PowerMockitoが複数のメソッドを探した、
            // または探せない可能性があるので、この場合は、SimpleStubberのgetMethodでもう一回探す
            method = SimpleStubber.getMethod(
                    methodToExpect,
                    parameterTypes,
                    this.mockedTarget.getClass());
        }
        if (method == null) {
            WhiteboxImpl.throwExceptionIfMethodWasNotFound(
                    this.mockedTarget.getClass(),
                    methodToExpect,
                    method,
                    (Object[]) parameterTypes);
        }
        try {
            return new DefaultPrivatelyExpectedArguments(this.stubber.when(
                    this.mockedTarget,
                    method));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
