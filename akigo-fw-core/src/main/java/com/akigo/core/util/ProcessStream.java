package com.akigo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 機能名 : 処理ストリーム<br>
 * <br>
 *
 * <pre>
 * 当共通部品は、JDK8オリジナル{@link Stream}と似たような処理のストリーム化部品であり、<br>
 * トランザクションを分けって、指定件数ごとに登録するような処理は、よく使っている。<br>
 *
 * 基本的には、処理対象を{@link #processUnit}で指定した処理単位件数ごとに纏めって実行{@link #reduce(Consumer)}<br>
 * と、処理対象を1件ずつ実行{@link #forEach(Consumer)}という二つの実行方式がある。<br>
 *
 * そのほか、{@link #filter(Predicate)}、{@link #map(Function)}など、<br>
 * JDK8オリジナル{@link Stream}と似たようなメソッドも用意しているが、使用方法が一緒である。<br>
 *
 * {@link #parallel(int)}より並列実行スレッド数を指定することで、マルチスレッド並列実行もできる。<br>
 *
 * 使用例：
 * ･下記は、4スレッド並列で、1000件ごとにDBを登録の例：
 * {@code
 *      boolean result = ProcessStream
 *                              .of(list)           // listをストリーム化
 *                              .processUnit(1000)  // 1000ごとに実行
 *                              .parallel(4)        // 4スレッド並列実行
 *                              .filter(target -> target != null) // listをフィルターリング
 *                              .map(TargetA::toTargetB)          // listの元素の型を変換
 *                              .reduce(targets -> dao.createAsBatch(targets)) // 処理実行
 * }
 *
 * ･下記は、4スレッド並列で、1件ずつDBを登録の例：
 * {@code
 *      boolean result = ProcessStream
 *                              .of(list)           // listをストリーム化
 *                              .processUnit(1000)  // forEachを使用する場合、1000件指定しても無視
 *                              .parallel(4)        // 4スレッド並列実行
 *                              .filter(target -> target != null) // listをフィルターリング
 *                              .map(TargetA::toTargetB)          // listの元素の型を変換
 *                              .forEach(target -> dao.create(target)) // 処理実行
 * }
 * </pre>
 *
 * @param <T> 処理対象の型
 * @author 作成者：chenhao
 * @since 作成日：2019/3/15
 */
@SuppressWarnings("serial")
public class ProcessStream<T> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStream.class);

    private static final Object END = new Object();
    private static final Object EXCEPT = new Object();

    private final Supplier<T> targetSupplier;

    private int processUnit;
    private int parallel;

    private List<Predicate<? super T>> filterChain;

    private Consumer<List<T>> consumer;

    @SuppressWarnings("unchecked")
    private ProcessStream(Iterable<T> targets) {
        final Iterator<T> it = targets.iterator();
        this.targetSupplier = () -> {
            synchronized (this) {
                return it.hasNext() ? processFilter(it.next()) : (T) END;
            }
        };
    }

    private ProcessStream(Supplier<T> targetSupplier) {
        this.targetSupplier = targetSupplier;
    }

    @SuppressWarnings("unchecked")
    private <ORI> ProcessStream(Supplier<ORI> oriTargetSupplier,
                                Function<? super ORI, ? extends T> mapper) {
        this.targetSupplier = () -> {
            ORI t = oriTargetSupplier.get();
            if (t == END) {
                return (T) END;
            } else if (t == EXCEPT) {
                return (T) EXCEPT;
            }
            return processFilter(mapper.apply(t));
        };
    }

    /**
     * ストリーム生成処理<br>
     * <br>
     *
     * @param <T>     処理対象の型
     * @param targets 処理対象集合
     * @return {@link ProcessStream}
     */
    public static <T> ProcessStream<T> of(Iterable<T> targets) {
        return new ProcessStream<>(targets);
    }

    /**
     * フィルター処理<br>
     * {@link Stream#filter(Predicate)}を参照してください<br>
     * <br>
     *
     * @param predicate フィルター判定式
     * @return {@link ProcessStream}
     */
    public ProcessStream<T> filter(Predicate<? super T> predicate) {
        if (this.filterChain == null) {
            this.filterChain = new ArrayList<>();
        }
        this.filterChain.add(predicate);
        return this;
    }

    /**
     * 処理対象Mapper設定処理<br>
     * {@link Stream#map(Function)}を参照してください<br>
     * <br>
     *
     * @param <R>    変換後の型
     * @param mapper 処理対象Mapper
     * @return {@link ProcessStream}
     */
    public <R> ProcessStream<R> map(Function<? super T, ? extends R> mapper) {
        return this.copyWith(mapper);
    }

    private <R> ProcessStream<R> copyWith(Function<? super T, ? extends R> mapper) {
        ProcessStream<R> dest = new ProcessStream<>(this.targetSupplier,
                mapper);
        dest.processUnit = this.processUnit;
        dest.parallel = this.parallel;
        return dest;
    }

    @SuppressWarnings("unchecked")
    private T processFilter(T target) {
        if (target != END &&
                target != EXCEPT &&
                this.filterChain != null &&
                !this.filterChain.stream().allMatch(p -> p.test(target))) {
            return (T) EXCEPT;
        }
        return target;
    }

    /**
     * 処理単位設定処理<br>
     * <br>
     *
     * @param processUnit 処理単位
     * @return {@link ProcessStream}
     */
    public ProcessStream<T> processUnit(int processUnit) {
        this.processUnit = processUnit;
        return this;
    }

    /**
     * 並列実行スレッド数設定処理<br>
     * <br>
     *
     * @param parallel 並列実行スレッド数
     * @return {@link ProcessStream}
     */
    public ProcessStream<T> parallel(int parallel) {
        this.parallel = parallel;
        return this;
    }

    /**
     * Reduce実行処理<br>
     * 処理単位{@link #processUnit}ごとに実行<br>
     * 並列実行可<br>
     * <br>
     *
     * @param consumer 処理コールバック
     * @return 実行結果(true : すべて成功 、 false : 失敗あり)
     */
    public boolean reduce(Consumer<List<T>> consumer) {
        this.consumer = consumer;
        return execute();
    }

    /**
     * ForEach実行処理<br>
     * 1件ごとに実行<br>
     * 並列実行可<br>
     * <br>
     *
     * @param consumer 処理コールバック
     * @return 実行結果(true : すべて成功 、 false : 失敗あり)
     */
    public boolean forEach(Consumer<T> consumer) {
        this.consumer = (targets) -> consumer.accept(targets.get(0));
        // ※※※ForEach実行する場合、処理単位に強制的に1を設定する※※※
        this.processUnit = 1;
        return execute();
    }

    private boolean execute() {
        if (isParallel()) {
            // 並列スレッド実行
            return processParallelly();
        } else {
            // シングルスレッド実行
            return processSequentlly();
        }
    }

    private boolean isParallel() {
        return this.parallel > 1 && this.processUnit > 0;
    }

    private boolean processParallelly() {

        List<CompletableFuture<Boolean>> tasks = new ArrayList<>();
        // 並列実行スレッド数まで実行スレッドを分配
        for (int i = 0; i < parallel; i++) {
            tasks.add(CompletableFuture.supplyAsync(() -> processSequentlly()));
        }

        // すべてのスレッドの実行結果を取得
        return tasks.stream().map(task -> {
            try {
                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("処理が失敗しました。異常：{}", e);
                return false;
            }
        }).reduce(true, (a, b) -> a && b);
    }

    private boolean processSequentlly() {
        List<T> targets = new ArrayList<>();
        T t;
        while ((t = this.targetSupplier.get()) != END) {

            if (t == EXCEPT) {
                continue;
            }

            targets.add(t);

            if (this.processUnit > 0 && (targets.size() % this.processUnit) == 0) {
                if (!process(targets)) {
                    return false;
                }
                targets.clear();
            }
        }

        if (!targets.isEmpty()) {
            if (!process(targets)) {
                return false;
            }
            targets.clear();
        }

        return true;
    }

    private boolean process(List<T> targets) {
        try {
            this.consumer.accept(targets);
        } catch (Exception e) {
            LOGGER.error(
                    "処理が失敗しました。処理コールバック:{}、処理対象：{}、異常：{}",
                    this.consumer,
                    targets,
                    e);
            return false;
        }
        return true;
    }

}
