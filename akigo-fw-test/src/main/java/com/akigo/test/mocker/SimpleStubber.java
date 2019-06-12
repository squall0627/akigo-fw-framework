package com.akigo.test.mocker;

import java.lang.reflect.Method;

/**
 * 機能名 : 簡単スタブ<br>
 * <br>
 *
 * @param <T> モック先クラス型
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public interface SimpleStubber<T> {

    /**
     * モック条件(インタフェースのdefaultメソッド以外場合)
     * <br>
     *
     * @param methodToExpect モック先メソッド名
     * @param parameterTypes モック先メソッドパラメータのタイプ（パラメータなし場合、渡さなくていい）
     * @return PrivatelyExpectedArguments
     */
    PrivatelyExpectedArguments when(String methodToExpect, Class<?>... parameterTypes);

    /**
     * メソッド探す処理<br>
     * <br>
     *
     * @param methodName  メソッド名
     * @param paramTypes  メソッドパラメータータイプ
     * @param targetClass メソッド所属するクラス
     * @return {@link Method}
     */
    static Method getMethod(String methodName, Class<?>[] paramTypes, Class<?> targetClass) {
        try {
            return targetClass.getDeclaredMethod(methodName, paramTypes);
        } catch (Exception e) {
            Class<?> superClass = targetClass.getSuperclass();
            if (superClass == null || superClass.equals(Object.class)) {
                return null;
            }
            return getMethod(methodName, paramTypes, superClass);
        }
    }

}