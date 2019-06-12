package com.akigo.test.matcher;

import static com.akigo.test.matcher.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

/**
 * 機能名 : 単体テスト支援ツールAssertion機能インタフェース<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/25
 */
public interface SimpleAssertion extends SimpleMatcher {

    /**
     * リスト型結果判定処理<br>
     * 順番を比較する<br>
     * <br>
     *
     * @param <T>      元素の型
     * @param expected 期待値
     * @param actual   実際値
     */
    default <T> void assertSameOrder(List<T> expected, List<T> actual) {
        assertEquals("リストサイズが一致しません。", expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertThat(
                    "データが一致しません。Index:" + String.valueOf(i),
                    actual.get(i),
                    match(expected.get(i)));
        }
    }

    /**
     * 配列型結果判定処理<br>
     * 順番を比較する<br>
     * <br>
     *
     * @param <T>      元素の型
     * @param expected 期待値
     * @param actual   実際値
     */
    default <T> void assertSameOrder(T[] expected, T[] actual) {
        assertEquals("配列サイズが一致しません。", expected.length, actual.length);
        assertThat(actual, matchInSameOrder(expected));
        for (int i = 0; i < expected.length; i++) {
            assertThat(
                    "データが一致しません。Index:" + String.valueOf(i),
                    actual[i],
                    match(expected[i]));
        }
    }

    /**
     * リスト型結果判定処理<br>
     * 順番を比較しない<br>
     * <br>
     *
     * @param <T>      元素の型
     * @param expected 期待値
     * @param actual   実際値
     */
    default <T> void assertAnyOrder(List<T> expected, List<T> actual) {
        assertEquals("リストサイズが一致しません。", expected.size(), actual.size());
        assertThat(actual, matchInAnyOrder(expected));
    }

    /**
     * 配列型結果判定処理<br>
     * 順番を比較しない<br>
     * <br>
     *
     * @param <T>      元素の型
     * @param expected 期待値
     * @param actual   実際値
     */
    default <T> void assertAnyOrder(T[] expected, T[] actual) {
        assertEquals("配列サイズが一致しません。", expected.length, actual.length);
        assertThat(actual, matchInAnyOrder(expected));
    }

    /**
     * マップ型結果判定処理<br>
     * 順番を比較しない<br>
     * <br>
     *
     * @param <K>      キーの型
     * @param <V>      値の型
     * @param expected 期待値
     * @param actual   実際値
     */
    default <K, V> void assertSameMap(Map<K, V> expected, Map<K, V> actual) {
        assertEquals("マップサイズが一致しません。", expected.size(), actual.size());
        assertThat(actual, matchMap(expected));
    }

    /**
     * DTO結果判定処理<br>
     * すべてのプロパティーの値を比較<br>
     * <br>
     *
     * @param <T>      DTOの型
     * @param expected 期待値
     * @param actual   実際値
     */
    default <T> void assertSamePropertyValues(T expected, T actual) {
        assertThat("プロパーティー値が一致しません。", actual, samePropertyValuesAs(expected));
    }

    /**
     * 結果判定処理<br>
     * すべての型でも使える<br>
     * <br>
     *
     * @param <T>      比較対象の型
     * @param expected 期待値
     * @param actual   実際値
     */
    default <T> void assertSame(T expected, T actual) {
        assertThat("データが一致しません。", actual, match(expected));
    }
}
