package com.akigo.test.mocker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 機能名 : スタブアノテーション<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Stub {
    /**
     * スタブキー配列
     * <br>
     *
     * @return スタブキー配列
     */
    String[] value();
}