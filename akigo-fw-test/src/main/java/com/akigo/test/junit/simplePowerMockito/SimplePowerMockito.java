/*
 * SimplePowerMockito.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit.simplePowerMockito;

import java.util.function.Consumer;

import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

/**
 * 単体テスト支援ツール簡単化PowerMockito<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class SimplePowerMockito<T> {
    /**
     * モックした対象インスタンス
     */
    private T targetInstance;
    /**
     * モックした対象クラス
     */
    private Class<T> targetClass;

    protected SimplePowerMockito(T mockedTarget) {
        this.targetInstance = mockedTarget;
    }

    protected SimplePowerMockito(Class<T> mockedTargetClass) {
        this.targetClass = mockedTargetClass;
    }

    /**
     * モックした対象インスタンス取得処理
     * <br>
     *
     * @return モックした対象インスタンス
     */
    public T getTargetInstance() {
        return this.targetInstance;
    }

    /**
     * モックした対象クラス取得処理
     * <br>
     *
     * @return モックした対象クラス
     */
    public Class<T> getTargetClass() {
        return this.targetClass;
    }

    /**
     * モックケース追加処理
     * <br>
     *
     * @param stubCase スタブケース
     * @return 当該クラスのインスタンス自身 {@link SimplePowerMockito}
     */
    public SimplePowerMockito<T> addCase(Consumer<SimplePowerMockito<T>> stubCase) {
        stubCase.accept(this);
        return this;
    }

    /**
     * 返却値なしのメソッドのスタブ
     * <br>
     *
     * @param simpleAnswer {@link SimpleAnswer} モック先メソッド実行時の期待処理
     * @return スタブ {@link SimpleStubber}
     */
    public SimpleStubber<T> doAnswer(SimpleAnswer<?> simpleAnswer) {
        Answer<?> answer = invocation -> {
            simpleAnswer.answer(invocation);
            return null;
        };
        if (this.targetClass != null) {
            return new StaticMockitoStubber<T>(targetClass, PowerMockito.doAnswer(answer));
        } else if (this.targetInstance != null) {
            return new InstanceMockitoStubber<T>(targetInstance, PowerMockito.doAnswer(answer));
        } else {
            throw new RuntimeException("Mock対象はありません");
        }
    }

    /**
     * 例外投げされるスタブ
     * <br>
     *
     * @param toBeThrown {@link Throwable} 投げたい例外
     * @return スタブ {@link SimpleStubber}
     */
    public SimpleStubber<T> doThrow(Throwable toBeThrown) {
        if (this.targetClass != null) {
            return new StaticMockitoStubber<T>(targetClass, PowerMockito.doThrow(toBeThrown));
        } else if (this.targetInstance != null) {
            return new InstanceMockitoStubber<T>(targetInstance, PowerMockito.doThrow(toBeThrown));
        } else {
            throw new RuntimeException("Mock対象はありません");
        }
    }

    /**
     * 真実のメソッド実行されるスタブ
     * <br>
     *
     * @return スタブ {@link SimpleStubber}
     */
    public SimpleStubber<T> doCallRealMethod() {
        if (this.targetClass != null) {
            return new StaticMockitoStubber<T>(targetClass, PowerMockito.doCallRealMethod());
        } else if (this.targetInstance != null) {
            return new InstanceMockitoStubber<T>(targetInstance, PowerMockito.doCallRealMethod());
        } else {
            throw new RuntimeException("Mock対象はありません");
        }
    }

    /**
     * 何もさせないスタブ
     * <br>
     *
     * @return スタブ {@link SimpleStubber}
     */
    public SimpleStubber<T> doNothing() {
        if (this.targetClass != null) {
            return new StaticMockitoStubber<T>(targetClass, PowerMockito.doNothing());
        } else if (this.targetInstance != null) {
            return new InstanceMockitoStubber<T>(targetInstance, PowerMockito.doNothing());
        } else {
            throw new RuntimeException("Mock対象はありません");
        }
    }

    /**
     * 期待値を返却されたいスタブ
     * <br>
     *
     * @param toBeReturned {@link Object} 期待値
     * @return スタブ {@link SimpleStubber}
     */
    public SimpleStubber<T> doReturn(Object toBeReturned) {
        if (this.targetClass != null) {
            return new StaticMockitoStubber<T>(targetClass, PowerMockito.doReturn(
                    toBeReturned));
        } else if (this.targetInstance != null) {
            return new InstanceMockitoStubber<T>(targetInstance, PowerMockito.doReturn(
                    toBeReturned));
        } else {
            throw new RuntimeException("Mock対象はありません");
        }
    }

    /**
     * 期待値を返却されたいスタブ
     * <br>
     *
     * @param toBeReturned       {@link Object} 1回目期待値
     * @param othersToBeReturned {@link Object} 2回目以降期待値
     * @return スタブ {@link SimpleStubber}
     */
    public SimpleStubber<T> doReturn(Object toBeReturned, Object... othersToBeReturned) {
        if (this.targetClass != null) {
            return new StaticMockitoStubber<T>(targetClass, PowerMockito.doReturn(
                    toBeReturned,
                    othersToBeReturned));
        } else if (this.targetInstance != null) {
            return new InstanceMockitoStubber<T>(targetInstance, PowerMockito.doReturn(
                    toBeReturned,
                    othersToBeReturned));
        } else {
            throw new RuntimeException("Mock対象はありません");
        }
    }
}
