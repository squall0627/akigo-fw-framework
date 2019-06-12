package com.akigo.test.cdi;

import java.util.HashMap;
import java.util.Map;

/**
 * 機能名 : CDI用MOCK Stubber保持クラス<br>
 *
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/25
 */
public class CdiMockingProgress {

    private static final ThreadLocal<CdiMockingProgress> mockingProgress = new ThreadLocal<>();

    private final Map<Class<?>, Object> stubberMap = new HashMap<>();

    private boolean isCDIHelperMocked = false;

    private CdiMockingProgress() {
    }

    /**
     * インスタンス取得処理<br>
     * ThreadLocalインスタンス<br>
     * <br>
     *
     * @return CdiMockingProgressインスタンス
     */
    public static CdiMockingProgress getInstance() {
        if (mockingProgress.get() == null) {
            mockingProgress.set(new CdiMockingProgress());
        }
        return mockingProgress.get();
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
     * Stubber保存処理<br>
     * <br>
     *
     * @param <T>     Stubberの型
     * @param clazz   Stubberのクラス
     * @param stubber Stubberインスタンス
     */
    public <T> void reportStubber(Class<? extends T> clazz, T stubber) {
        this.stubberMap.put(clazz, stubber);
    }

    /**
     * Stubber存在チェック処理<br>
     * <br>
     *
     * @param <T>   Stubberの型
     * @param clazz Stubberのクラス
     * @return true of false
     */
    public <T> boolean containsStubber(Class<? extends T> clazz) {
        return this.stubberMap.containsKey(clazz);
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
    public <T> T getStubber(Class<? extends T> clazz) {
        return (T) this.stubberMap.get(clazz);
    }

    public boolean isCDIHelperMocked() {
        return isCDIHelperMocked;
    }

    public void setCDIHelperMocked() {
        this.isCDIHelperMocked = true;
    }

}
