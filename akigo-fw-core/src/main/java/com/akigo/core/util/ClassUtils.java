/*
 * ClassUtils.java
 * Created on  2018/9/2 下午10:00
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.util;

/**
 * JAVAクラスへの汎用機能<<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class ClassUtils {

    /**
     * {@link Class#forName(String)}を安全に実行します
     *
     * @param <T>  クラスの型
     * @param name クラス名
     * @return {@link Class}オブジェクト
     */
    public static <T> Class<T> forName(String name) {
        return forName(name, null);
    }

    /**
     * {@link Class#forName(String)}を安全に実行します
     *
     * @param <T>    クラスの型
     * @param name   クラス名
     * @param loader クラスローダー
     * @return {@link Class}オブジェクト
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(String name, ClassLoader loader) {
        try {
            if (loader == null) {
                return (Class<T>) Class.forName(name);
            }
            return (Class<T>) Class.forName(name, true, loader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@link Class#newInstance()}を安全に実行します
     *
     * @param name クラス名
     * @return {@link Class}が表すクラスのインスタンス
     */
    public static Object newInstance(String name) {
        return newInstance(forName(name));
    }

    /**
     * {@link Class#newInstance()}を安全に実行します
     *
     * @param name   クラス名
     * @param loader クラスローダー
     * @return {@link Class}が表すクラスのインスタンス
     */
    public static Object newInstance(String name, ClassLoader loader) {
        return newInstance(forName(name, loader));
    }

    /**
     * {@link Class#newInstance()}を安全に実行します
     *
     * @param clazz クラス
     * @param <T>   クラスの型
     * @return {@link Class}が表すクラスのインスタンス
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
