package com.akigo.test.cdi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 機能名 : CDI用Inject情報保持クラス<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/25
 */
public class CdiInjectingProgress {

    private static final ThreadLocal<Map<TestCDIHelper<?>, CdiInjectingProgress>> mockingProgress =
            new ThreadLocal<>();

    private final Map<Class<?>, Function<Class<?>, ?>> injectMap = new HashMap<>();

    private boolean isTransactional = false;

    private CdiInjectingProgress() {
    }

    /**
     * インスタンス取得処理<br>
     * ThreadLocalインスタンス<br>
     * <br>
     *
     * @return CdiMockingProgressインスタンス
     */
    public static CdiInjectingProgress getInstance(TestCDIHelper<?> testCDIHelper) {
        if (mockingProgress.get() == null) {
            mockingProgress.set(new HashMap<>());
        }
        if (!mockingProgress.get().containsKey(testCDIHelper)) {
            mockingProgress.get().put(testCDIHelper, new CdiInjectingProgress());
        }
        return mockingProgress.get().get(testCDIHelper);
    }

    /**
     * ThreadLocalインスタンス削除処理<br>
     */
    public static void clear() {
        if (mockingProgress.get() != null) {
            mockingProgress.remove();
        }
    }

    /**
     * ThreadLocalインスタンス削除処理<br>
     */
    public static void clear(TestCDIHelper<?> testCDIHelper) {
        if (mockingProgress.get() != null && mockingProgress.get().containsKey(testCDIHelper)) {
            mockingProgress.get().remove(testCDIHelper);
        }
        if (mockingProgress.get() != null && mockingProgress.get().isEmpty()) {
            clear();
        }
    }

    /**
     * Stubber保存処理<br>
     * <br>
     *
     * @param <T>   Stubberの型
     * @param clazz Stubberのクラス
     */
    public <T> void reportInjectFunc(Class<? extends T> clazz, Function<Class<?>, T> injectFunc) {
        this.injectMap.put(clazz, injectFunc);
    }

    /**
     * Stubber存在チェック処理<br>
     * <br>
     *
     * @param <T>   Stubberの型
     * @param clazz Stubberのクラス
     * @return true of false
     */
    public <T> boolean containsInjectFunc(Class<? extends T> clazz) {
        return this.injectMap.containsKey(clazz);
    }

    /**
     * Stubber存在チェック処理<br>
     * <br>
     *
     * @param <T>   Stubberの型
     * @param clazz Stubberのクラス
     * @return true of false
     */
    public <T> void removeInjectFunc(Class<? extends T> clazz) {
        if (containsInjectFunc(clazz)) {
            this.injectMap.remove(clazz);
        }
    }

    /**
     * Stubber取得処理<br>
     * <br>
     *
     * @param <T>   Stubberの型
     * @param clazz Stubberのクラス
     * @return Stubberインスタンス
     */
    @SuppressWarnings("unchecked")
    public <T> Function<Class<?>, T> getInjectFunc(Class<? extends T> clazz) {
        return (Function<Class<?>, T>) this.injectMap.get(clazz);
    }

    public boolean isTransactional() {
        return isTransactional;
    }

    public void setTransactional() {
        this.isTransactional = true;
    }
}
