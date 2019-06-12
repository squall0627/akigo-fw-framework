package com.akigo.dao.util;

import com.akigo.core.util.StopWatchTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 機能名 : クエリ分割実行ユーティリティ<br>
 * <br>
 *
 * <pre>
 * INを使って検索する場合、データベースの制限より、INの項目数の上限が1000件となっている。<br>
 * 当共通部品は、INの上限オーバーエラーを避けるために作成した部品である。<br>
 * 基本的には、INの項目数が1000件超える場合、1000件ごとに数回分けってSQLを実行という仕組みである。<br>
 *
 * 使用例：
 * {@code
 *      List<String> paramList = Arrays.asList("A","B","C");
 *      List<XXXX_Dto> results = QuerySpliterator.splitQuery(
 *                                                    paramList,
 *                                                    params -> dao.search(params),
 *                                                    XXXX_Dto::getUser // 結果リストの重複排除用ユニックキー
 *                                                    )
 * }
 * </pre>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/11
 */
@SuppressWarnings("serial")
public class QuerySpliterator implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuerySpliterator.class);

    /**
     * INのデフォルト最大パラメータ数
     */
    public static final int DEFAULT_IN_MAX_PARAM = 1000;

    private QuerySpliterator() {
    }

    /**
     * クエリ分割実行処理<br>
     * パラメーターをINのデフォルト最大パラメータ数で分割して、数回に分けて実行する。<br>
     * <br>
     *
     * @param params   分割対象パラメーター
     * @param callback コールバックメソッド
     * @param <T>      引数型
     * @param <R>      返却値型
     * @return 実行結果
     */
    public static <T, R> List<R> splitQuery(List<T> params, Function<List<T>, List<R>> callback) {
        return splitQuery(params, callback, null);
    }

    /**
     * クエリ分割実行処理<br>
     * パラメーターをINのデフォルト最大パラメータ数で分割して、数回に分けて実行する。<br>
     * 返却値は、パラメーター{@code uniqueKeyFunc}で指定されたユニックキーで重複を排除する。<br>
     * <br>
     *
     * @param params        分割対象パラメーター
     * @param callback      コールバックメソッド
     * @param uniqueKeyFunc ユニックキー取得Function
     * @param <T>           引数型
     * @param <R>           返却値型
     * @return 実行結果
     */
    public static <T, R> List<R> splitQuery(List<T> params,
                                            Function<List<T>, List<R>> callback, Function<R, String> uniqueKeyFunc) {
        return splitQuery(params, callback, uniqueKeyFunc, null);
    }

    /**
     * クエリ分割実行処理<br>
     * パラメーターをINのデフォルト最大パラメータ数で分割して、数回に分けて実行する。<br>
     * 返却値は、パラメーター{@code uniqueKeyFunc}で指定されたユニックキーで重複を排除した上で、<br>
     * パラメーター{@code resultSort}で指定された{@link Comparator}でソートする。<br>
     * <br>
     *
     * @param params        分割対象パラメーター
     * @param callback      コールバックメソッド
     * @param uniqueKeyFunc ユニックキー取得Function
     * @param resultSort    結果リストソート
     * @param <T>           引数型
     * @param <R>           返却値型
     * @return 実行結果
     */
    public static <T, R> List<R> splitQuery(List<T> params,
                                            Function<List<T>, List<R>> callback, Function<R, String> uniqueKeyFunc,
                                            Comparator<R> resultSort) {
        return splitQuery(params, callback, uniqueKeyFunc, resultSort, DEFAULT_IN_MAX_PARAM);
    }

    /**
     * クエリ分割実行処理<br>
     * パラメーターを指定する分割単位{@code splitUnit}で分割して、数回に分けて実行する<br>
     * 返却値は、パラメーター{@code uniqueKeyFunc}で指定されたユニックキーで重複を排除した上で、<br>
     * パラメーター{@code resultSort}で指定された{@link Comparator}でソートする。<br>
     * <br>
     *
     * @param params        分割対象パラメーター
     * @param callback      コールバックメソッド
     * @param uniqueKeyFunc ユニックキー取得Function
     * @param resultSort    結果リストソート
     * @param splitUnit     分割単位
     * @param <T>           引数型
     * @param <R>           返却値型
     * @return 実行結果
     */
    public static <T, R> List<R> splitQuery(List<T> params,
                                            Function<List<T>, List<R>> callback, Function<R, String> uniqueKeyFunc,
                                            Comparator<R> resultSort, int splitUnit) {

        // 分割対象パラメーターがNULLまたはサイズが分割単位以下の場合、分割しないで実行する
        if (params == null || params.size() <= splitUnit) {
            LOGGER.info("パラメーター数は{}以下なので、分割しないで実行する", splitUnit);
            return callback.apply(params);
        }

        LOGGER.info("パラメーター分割実行開始");

        StopWatchTime sw = new StopWatchTime();
        sw.startTime();

        // 実行結果
        List<R> returnValues = new ArrayList<>();

        // コピーパラメータ作成
        List<T> paramsCopy = params;

        LOGGER.info("パラメーター総個数：{}", paramsCopy.size());

        // 重複キー排除用セット
        Set<String> keySets = new HashSet<>();

        int offset = 0;
        for (; ; ) {

            // 最大パラメータ数で分割
            List<T> subParams = paramsCopy.stream().skip(offset).limit(splitUnit).collect(
                    Collectors.toList());

            // CHECKSTYLE:OFF
            LOGGER.debug("分割OFFSET：{} 実行パラメーター件数：{}", offset, subParams.size());
            // CHECKSTYLE:ON

            // 分割完了場合、返却
            if (subParams.size() == 0) {
                break;
            }

            if (uniqueKeyFunc == null) {
                // ユニックキーFunctionが指定されない場合、返却値をそのままリストにAdd
                returnValues.addAll(callback.apply(subParams));
            } else {
                // ユニックキーFunctionが指定された場合、返却値をユニックキーで重複を排除してリストにAdd
                List<R> thisReturnValues = callback.apply(subParams);
                List<R> returnValuesDistincted = thisReturnValues.stream().filter(dto -> {
                    // ユニックキー取得
                    String uniqueKey = uniqueKeyFunc.apply(dto);
                    if (keySets.contains(uniqueKey)) {
                        return false;
                    } else {
                        keySets.add(uniqueKey);
                        return true;
                    }
                }).collect(Collectors.toList());
                // コールバックメソッド実行して、結果を結果リストに詰込む
                returnValues.addAll(returnValuesDistincted);
            }

            offset = offset + splitUnit;
        }

        // ソートが指定される場合、結果リストをソートする
        if (resultSort != null) {
            Collections.sort(returnValues, resultSort);
        }

        LOGGER.info("パラメーター分割実行終了");
        LOGGER.info("分割実行回数：{}", offset / splitUnit);
        LOGGER.info("所要時間：{} ミリ秒", sw.stopTime());

        return returnValues;
    }

}
