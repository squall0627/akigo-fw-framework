package com.akigo.test.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import java.util.HashMap;
import java.util.Map;

/**
 * 機能名 : CdiPersistenceBeanContainer<br>
 *
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/25
 */
public class CdiPersistenceBeanContainer {

    private static final ThreadLocal<Map<TestCDIHelper<?>,
            CdiPersistenceBeanContainer>> injectProgress =
            new ThreadLocal<>();

    private final Map<Class<?>, Object> persistenceBeanMap = new HashMap<>();

    private CdiPersistenceBeanContainer() {
    }

    /**
     * インスタンス取得処理<br>
     * ThreadLocalインスタンス<br>
     * <br>
     *
     * @return CdiInjectingProgressインスタンス
     */
    public static CdiPersistenceBeanContainer getInstance(TestCDIHelper<?> testCDIHelper) {
        if (injectProgress.get() == null) {
            injectProgress.set(new HashMap<>());
        }
        if (!injectProgress.get().containsKey(testCDIHelper)) {
            injectProgress.get().put(testCDIHelper, new CdiPersistenceBeanContainer());
        }
        return injectProgress.get().get(testCDIHelper);
    }

    /**
     * ThreadLocalインスタンス削除処理<br>
     */
    public static void clear() {
        if (injectProgress.get() != null) {
            injectProgress.remove();
        }
    }

    /**
     * ThreadLocalインスタンス削除処理<br>
     */
    public static void clear(TestCDIHelper<?> testCDIHelper) {
        if (injectProgress.get() != null && injectProgress.get().containsKey(testCDIHelper)) {
            injectProgress.get().remove(testCDIHelper);
        }
        if (injectProgress.get() != null && injectProgress.get().isEmpty()) {
            clear();
        }
    }

    /**
     * PersistenceBean情報保存処理<br>
     * <br>
     *
     * @param <T>   Injectしたインスタンスの型
     * @param clazz Injectしたインスタンスのクラス
     * @param bean  Injectしたインスタンス
     */
    public <T> void reportBean(Class<? extends T> clazz, T bean) {
        this.persistenceBeanMap.put(clazz, bean);
    }

    /**
     * Injectインスタンス存在チェック処理<br>
     * <br>
     *
     * @param <T>   Injectしたインスタンスの型
     * @param clazz Injectしたインスタンスのクラス
     * @return true of false
     */
    public <T> boolean containsBean(Class<? extends T> clazz) {
        return this.persistenceBeanMap.containsKey(clazz);
    }

    /**
     * Injectインスタンス取得処理<br>
     * <br>
     *
     * @param <T>   Injectしたインスタンスの型
     * @param clazz Injectしたインスタンスのクラス
     * @return Injectしたインスタンス
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<? extends T> clazz) {
        return (T) this.persistenceBeanMap.get(clazz);
    }

    /**
     * PersistenceBean判定処理<br>
     * <br>
     *
     * @param <T>   Injectしたインスタンスの型
     * @param clazz Injectしたインスタンスのクラス
     * @return true of false
     */
    public <T> boolean isPersistenceBean(Class<? extends T> clazz) {
        return this.persistenceBeanMap.containsKey(clazz) ||
                clazz.isAnnotationPresent(SessionScoped.class) ||
                clazz.isAnnotationPresent(ApplicationScoped.class);
    }

}
