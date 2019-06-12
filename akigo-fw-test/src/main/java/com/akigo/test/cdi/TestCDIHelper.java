package com.akigo.test.cdi;

import com.akigo.core.exception.SystemException;
import com.akigo.core.util.CDIHelper;
import com.akigo.core.util.ClassUtils;
import com.akigo.dao.transactional.annotation.Tx;
import com.akigo.dao.transactional.annotation.TxNew;
import com.akigo.test.jdbc.interceptor.TestTxInterceptor;
import com.akigo.test.jdbc.interceptor.TestTxNewInterceptor;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.CallbackFilter;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.NoOp;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 機能名 : 単体テスト用CDI支援ツール<br>
 *
 * <pre>
 * ・すべてのフィールドがデフォルトInjectする場合
 * Hoge hoge = TestCDIHelper.instance(Hoge.class).create();
 *
 * ・一部フィールドのInject対象がカスタマイズ指定する場合
 * Hoge hoge = TestCDIHelper.instance(Hoge.class)
 *                            .withInjectClass(Interface1.class, Interface1Impl.class)
 *                            .withInjectInstance(Interface2.class, impl2)
 *                            .withInjectFunction(Interface3.class, (clazz) -> impl3)
 *                            .create();
 *
 * ・トランザクション（@Txまたは@TxNew）を有効させる場合
 * Hoge hoge = TestCDIHelper.instanceTransactionally(Hoge.class).create();
 *
 * </pre>
 * <p>
 * ｸﾗｽ名 : TestCDIHelper<br>
 * <br>
 *
 * @param <E> 編集オブジェクト型
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public class TestCDIHelper<E> {

    private final Logger logger = LoggerFactory.getLogger(TestCDIHelper.class);

    private Class<E> clazz;
    private E instance;
    private static Reflections reflections = null;

    private final TestCDIHelper<?> parent;

    static {
        reflections = new Reflections("jp.co.jfe_steel");
    }

    private TestCDIHelper(Class<E> clazz) {
        this.parent = this;
        this.clazz = clazz;
        initCustomizeInjectMap();
        mockCDIHelper();
        this.instance = createInstance(clazz);
    }

    private TestCDIHelper(Class<E> clazz, E instance) {
        this.parent = this;
        this.clazz = clazz;
        initCustomizeInjectMap();
        mockCDIHelper();
        this.instance = instance;
    }

    private TestCDIHelper(TestCDIHelper<?> parent, Class<E> clazz) {
        this.parent = parent;
        this.clazz = clazz;
        initCustomizeInjectMap();
        mockCDIHelper();
        this.instance = createInstance(clazz);
    }

    private void initCustomizeInjectMap() {
        withInjectFunction(Logger.class, (c) -> LoggerFactory.getLogger(c));
    }

    private void mockCDIHelper() {
        if (CdiMockingProgress.getInstance().isCDIHelperMocked()) {
            return;
        }

        try {
            PowerMockito.spy(CDIHelper.class);
        } catch (IllegalArgumentException e) {
            logger.warn(
                    "{}がMOCKできない。{}を使って、かつ@PrepareForTestにCDIHelper.classを入れてください。",
                    CDIHelper.class.getName(),
                    PowerMockRunner.class.getName());
            CdiMockingProgress.getInstance().setCDIHelperMocked();
            return;
        }

        try {
            PowerMockito.doAnswer(new Answer<Object>() {

                @Override
                public Object answer(InvocationOnMock invocation)
                        throws Throwable {
                    Class<?> param1 = (Class<?>) invocation.getArguments()[0];
                    return TestCDIHelper.instanceNest(parent, param1).create();
                }
            }).when(CDIHelper.class, "getReference", Mockito.any());
        } catch (Exception e) {
            throw new SystemException(e);
        }

        CdiMockingProgress.getInstance().setCDIHelperMocked();
    }

    /**
     * 編集オブジェクト型および編集オブジェクト型を設定した本オブジェクト型を返します。
     *
     * @param <T>      編集オブジェクト型
     * @param clazz    編集オブジェクト型
     * @param instance 編集インスタンス
     * @return 本インスタンス
     */
    public static <T> TestCDIHelper<T> instance(Class<T> clazz, T instance) {
        return new TestCDIHelper<>(clazz, instance);
    }

    /**
     * 編集オブジェクト型を設定した本オブジェクト型を返します。
     *
     * @param <T>   編集オブジェクト型
     * @param clazz 編集オブジェクト型
     * @return 本インスタンス
     */
    public static <T> TestCDIHelper<T> instance(Class<T> clazz) {
        return new TestCDIHelper<>(clazz);
    }

    /**
     * 編集オブジェクト型を設定した本オブジェクト型を返します。
     *
     * @param <T>   編集オブジェクト型
     * @param clazz 編集オブジェクト型
     * @return 本インスタンス
     */
    public static <T> TestCDIHelper<T> instanceTransactionally(Class<T> clazz) {
        TestCDIHelper<T> testCDIHelper = new TestCDIHelper<>(clazz);
        CdiInjectingProgress.getInstance(testCDIHelper).setTransactional();
        return testCDIHelper;
    }

    /**
     * 編集オブジェクト型を設定した本オブジェクト型を返します。
     *
     * @param <T>   編集オブジェクト型
     * @param clazz 編集オブジェクト型
     * @return 本インスタンス
     */
    private static <T> TestCDIHelper<T> instanceNest(TestCDIHelper<?> parent, Class<T> clazz) {
        return new TestCDIHelper<>(parent, clazz);
    }

    /**
     * Inject対象インスタンスをカスタマイズ指定<br>
     *
     * @param targetClass 対象クラス
     * @param targetImpl  Injectするインスタンス
     * @param <T>         編集オブジェクト型
     * @return 本インスタンス
     */
    public <T> TestCDIHelper<E> withInjectInstance(Class<? extends T> targetClass, T targetImpl) {
        Function<Class<?>, T> targetFunction = (c) -> targetImpl;
        return withInjectFunction(targetClass, targetFunction);
    }

    /**
     * Inject対象Classをカスタマイズ指定<br>
     *
     * @param targetClass     対象クラス
     * @param targetImplClass InjectするClass
     * @param <T>             編集オブジェクト型
     * @return 本インスタンス
     */
    public <T> TestCDIHelper<E> withInjectClass(Class<T> targetClass, Class<
            ? extends T> targetImplClass) {
        return withInjectFunction(
                targetClass,
                (c) -> TestCDIHelper.instanceNest(this.parent, targetImplClass).create());
    }

    /**
     * Inject対象Functionをカスタマイズ指定<br>
     *
     * @param targetClass    対象クラス
     * @param targetFunction InjectするFunction
     * @param <T>            編集オブジェクト型
     * @return 本インスタンス
     */
    public <T> TestCDIHelper<E> withInjectFunction(Class<? extends T> targetClass, Function<Class<
            ?>, T> targetFunction) {
        CdiInjectingProgress.getInstance(this.parent).reportInjectFunc(targetClass, targetFunction);
        return this;
    }

    /**
     * 本インスタンスを返します。
     * 引数で指定したfiledNameのフィールドを本インスタンスに保持するE型のインスタンスから取得し、引数のclsで指定さられた型のインスタンスを設定します。
     *
     * @param filedName フィールド名
     * @param cls       本インスタンス型
     * @return 本インスタンス
     */
    public TestCDIHelper<E> set(String filedName, Class<?> cls) {
        Object o = TestCDIHelper.instanceNest(this.parent, cls).create();
        return set(filedName, o);
    }

    /**
     * 本インスタンスを返します。
     * 引数で指定したfiledNameのフィールドを本インスタンスに保持するE型のインスタンスから取得し、引数のvalueを設定します。
     *
     * @param filedName フィールド名
     * @param value     設定値
     * @return 本インスタンス
     * @throws IllegalStateException 引数のfiledNameで指定したFieldが取得できない場合
     */
    public TestCDIHelper<E> set(String filedName, Object value) {
        Field f = ClassUtils.getField(filedName, clazz);
        if (f == null) {
            throw new IllegalStateException("No such field '" + filedName + "'");
        }
        f.setAccessible(true);
        ClassUtils.set(f, instance, value);
        return this;
    }

    /**
     * E型のインスタンスを返します。
     *
     * @return E
     */
    public E create() {
        if (!CdiPersistenceBeanContainer.getInstance(this.parent).isPersistenceBean(clazz) ||
                !CdiPersistenceBeanContainer.getInstance(this.parent).containsBean(clazz)) {

            doInject();
            doPostConstruct();

            if (CdiPersistenceBeanContainer.getInstance(this.parent).isPersistenceBean(clazz)) {
                CdiPersistenceBeanContainer.getInstance(this.parent).reportBean(clazz, instance);
            }
        }

        if (this == this.parent) {
            CdiInjectingProgress.clear(this);
            CdiPersistenceBeanContainer.clear(this);
        }

        return instance;
    }

    private TestCDIHelper<E> doInject() {
        // 自動Injectの対象フィールドを洗い出す
        Set<Field> injectTargetFields = ClassUtils.getFields(Inject.class, clazz);
        for (Field f : injectTargetFields) {
            f.setAccessible(true);
            Class<?> targetClass = f.getType();
            if (CdiInjectingProgress.getInstance(this.parent).containsInjectFunc(targetClass)) {
                // カスタマイズInjectマップに存在するクラスに対して、指定したインスタンスでInject
                this.set(
                        f.getName(),
                        CdiInjectingProgress.getInstance(this.parent).getInjectFunc(targetClass).apply(
                                clazz));
            } else {
                Class<?> targetImplClazz = null;
                Set<?> decorators = null;
                if (targetClass.isInterface() || Modifier.isAbstract(targetClass.getModifiers())) {
                    // インタフェースまたはAbstractの場合、Implクラスを探す
                    Set<?> allTargetImpls = reflections.getSubTypesOf(targetClass);
                    Set<?> targetImplClazzes = allTargetImpls.stream().filter(
                            c -> !((Class<?>) c).isAnnotationPresent(Decorator.class)).collect(
                            Collectors.toSet());
                    decorators = allTargetImpls.stream().filter(
                            c -> ((Class<?>) c).isAnnotationPresent(Decorator.class)).collect(
                            Collectors.toSet());
                    targetImplClazz = findImplClassByQualifier(f, targetClass, targetImplClazzes);
                } else {
                    targetImplClazz = targetClass;
                }

                if (CdiMockingProgress.getInstance().containsStubber(targetImplClazz)) {
                    // @StubでインクルードしたMOCKオブジェクトがある場合、MOCKしたオブジェクトでInject
                    this.set(
                            f.getName(),
                            CdiMockingProgress.getInstance().getStubber(targetImplClazz));
                } else {
                    // ネストInject
                    Object target = TestCDIHelper.instanceNest(this.parent, targetImplClazz)
                            .create();
                    if (decorators != null && !decorators.isEmpty()) {
                        target = installDecorator(targetClass, target, decorators);
                        if (CdiPersistenceBeanContainer.getInstance(this.parent).isPersistenceBean(
                                targetImplClazz)) {
                            CdiPersistenceBeanContainer.getInstance(this.parent).reportBean(
                                    target.getClass(),
                                    target);
                        }
                    }
                    // ネストInject
                    this.set(f.getName(), target);
                }
            }
        }

        return this;
    }

    private Class<?> findImplClassByQualifier(Field f, Class<?> targetInterface,
                                              Set<?> targetImplClazzes) {
        Class<?> targetImpl = null;
        if (targetImplClazzes.isEmpty()) {
            // Implクラスが見つからない場合
            // CHECKSTYLE:OFF
            throw new SystemException(targetInterface + "のImplクラスが見つからない。");
            // CHECKSTYLE:ON
        } else {
            List<Class<?>> targetInjectableImpls = new ArrayList<>();
            if (targetImplClazzes.size() > 1) {
                // Implクラスが2個以上見つかった場合、QualifierがInjectで指定したQualifierと同じのImplクラスを洗い出す
                List<Annotation> qualifiersOfInterface = getQualifiers(
                        f.getDeclaredAnnotations());
                for (Object impl : targetImplClazzes) {
                    Class<?> implClass = (Class<?>) impl;
                    List<Annotation> qualifiersOfImpl = getQualifiers(
                            implClass.getAnnotations());
                    if (qualifiersOfInterface.stream().anyMatch(
                            q -> qualifiersOfImpl.contains(q))) {
                        targetInjectableImpls.add(implClass);
                    }
                }
                if (targetInjectableImpls.isEmpty()) {
                    // Implクラスが見つからない場合
                    // CHECKSTYLE:OFF
                    throw new SystemException("Qualifierが" + qualifiersOfInterface + "の"
                            + targetInterface + "のImplクラスが見つからない。");
                    // CHECKSTYLE:ON
                }
            } else {
                // Implクラスが1個だけ見つかった場合、そのImplでInject
                targetInjectableImpls.add((Class<?>) targetImplClazzes.iterator().next());
            }
            if (targetInjectableImpls.size() > 1) {
                // 同じQualifierのImplも複数個あり場合、エラー
                throw new SystemException(targetInterface + "のImplクラスが複数見つかりました。"
                        + targetInjectableImpls);
            } else {
                targetImpl = targetInjectableImpls.get(0);
            }
        }
        return targetImpl;
    }

    private List<Annotation> getQualifiers(Annotation[] annotations) {
        return Arrays.asList(annotations)
                .stream()
                .filter(a -> Arrays.asList(a.annotationType().getDeclaredAnnotations())
                        .stream()
                        .anyMatch(aa -> aa.annotationType() == Qualifier.class))
                .collect(Collectors.toList());
    }

    private void doPostConstruct() {
        Arrays.stream(clazz.getDeclaredMethods()).filter(
                m -> m.isAnnotationPresent(PostConstruct.class))
                .forEach(m -> {
                    try {
                        m.setAccessible(true);
                        m.invoke(instance);
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new SystemException(clazz.getName() + "のPostConstructメソッド" + m
                                .getName() + "の実行が失敗しました。\r\n" + e);
                    }
                });
    }

    private E createInstance(Class<E> clazz) {
        if (CdiPersistenceBeanContainer.getInstance(this.parent).isPersistenceBean(clazz) &&
                CdiPersistenceBeanContainer.getInstance(this.parent).containsBean(clazz)) {
            // PersistenceBeanに対して、唯一のインスタンスを生成する
            return CdiPersistenceBeanContainer.getInstance(this.parent).getBean(clazz);
        }

        return installInterceptor(clazz);
    }

    @SuppressWarnings("unchecked")
    private E installInterceptor(Class<E> clazz) {
        Map<Callback, Method[]> callbackMap = new LinkedHashMap<>();

        // トランザクション有効の場合、TxまたはTxNewインターセプタを実装する
        if (CdiInjectingProgress.getInstance(this.parent).isTransactional()) {
            callbackMap.putAll(getInterceptorCallbackMapOfTransaction(clazz));
        }

        if (!callbackMap.isEmpty()) {
            Map<Callback, Method[]> newCallbackMap = new LinkedHashMap<>();
            newCallbackMap.put(NoOp.INSTANCE, new Method[0]);
            newCallbackMap.putAll(callbackMap);

            CallbackFilter filter = new CallbackFilter() {
                @Override
                public int accept(Method method) {
                    int index = 0;
                    for (Method[] methods : newCallbackMap.values()) {
                        if (Arrays.stream(methods).anyMatch(m -> m.equals(method))) {
                            return index;
                        }
                        index++;
                    }
                    return 0;
                }
            };

            Callback[] callbacks = newCallbackMap.keySet().stream().toArray(
                    (size) -> new Callback[size]);
            return (E) Enhancer.create(clazz, null, filter, callbacks);
        }

        return ClassUtils.newInstance(clazz);
    }

    private Map<Callback, Method[]> getInterceptorCallbackMapOfTransaction(Class<E> clazz) {
        Map<Callback, Method[]> callbackMap = new LinkedHashMap<>();
        Method[] txMethods = getMethodsInterceptWith(clazz, Tx.class);
        if (txMethods.length > 0) {
            callbackMap.put(
                    new TestTxInterceptor(),
                    txMethods);
        }

        Method[] txNewMethods = getMethodsInterceptWith(clazz, TxNew.class);
        if (txNewMethods.length > 0) {
            callbackMap.put(
                    new TestTxNewInterceptor(),
                    txNewMethods);
        }
        return callbackMap;
    }

    private Method[] getMethodsInterceptWith(Class<E> clazz, Class<
            ? extends Annotation> annotation) {
        Method[] methods = null;
        if (clazz.isAnnotationPresent(annotation)) {
            methods = Arrays.stream(clazz.getMethods()).filter(m -> {
                int mod = m.getModifiers();
                return !Modifier.isStatic(mod) && !Modifier.isFinal(mod);
            }).toArray((size) -> new Method[size]);
        } else {
            methods = Arrays.stream(clazz.getMethods())
                    .filter(m -> m.isAnnotationPresent(annotation))
                    .filter(m -> {
                        int mod = m.getModifiers();
                        return !Modifier.isStatic(mod) && !Modifier.isFinal(mod);
                    }).toArray((size) -> new Method[size]);
        }
        return methods;
    }

    private Object installDecorator(Class<?> interfaceClazz, Object target, Set<?> decorators) {
        List<Class<?>> sortedDecorators = decorators.stream()
                .map(d -> (Class<?>) d)
                .sorted(
                        (d1, d2) -> {
                            if (d1.isAnnotationPresent(Priority.class) &&
                                    d2.isAnnotationPresent(Priority.class)) {
                                return d1.getAnnotation(Priority.class).value() - d2
                                        .getAnnotation(Priority.class).value();
                            } else if (!d1.isAnnotationPresent(Priority.class) &&
                                    d2.isAnnotationPresent(Priority.class)) {
                                return 1;
                            } else if (d1.isAnnotationPresent(Priority.class) &&
                                    !d2.isAnnotationPresent(Priority.class)) {
                                return -1;
                            } else {
                                return 0;
                            }
                        })
                .collect(Collectors.toList());
        Object targetDecorator = target;
        for (Class<?> decorator : sortedDecorators) {
            if (decorator == this.clazz) {
                // Decorator自分自身をインスタンス作成する場合、他のDecoratorを実装しない
                break;
            }
            targetDecorator = TestCDIHelper.instanceNest(this.parent, decorator)
                    .withInjectInstance(interfaceClazz, targetDecorator)
                    .create();
        }
        CdiInjectingProgress.getInstance(this.parent).removeInjectFunc(interfaceClazz);
        return targetDecorator;
    }
}