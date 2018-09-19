/*
 * SimpleMatcher.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit.simplePowerMockito;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.akigo.test.junit.simplePowerMockito.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.Matchers.*;

/**
 * 単体テスト支援ツールMatcher機能インタフェース<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public interface SimpleMatcher {
    /**
     * リストなどIterable型比較Matcher
     * <br>
     * 順番も比較する<br>
     *
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<Iterable<? extends T>> matchInSameOrder(Iterable<T> expectedParams) {
        return Matchers.anyOf(emptyIterable(), contains(createMatcherArray(expectedParams)));
    }

    /**
     * 配列比較Matcher
     * <br>
     * 順番も比較する<br>
     *
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<T[]> matchInSameOrder(@SuppressWarnings("unchecked") T... expectedParams) {
        List<T> list = Arrays.stream(expectedParams).collect(Collectors.toList());
        return Matchers.anyOf(arrayWithSize(0), arrayContaining(createMatcherArray(list)));
    }

    /**
     * リストなどIterable型比較Matcher
     * <br>
     * 順番比較しない<br>
     *
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<Iterable<? extends T>> matchInAnyOrder(Iterable<T> expectedParams) {
        return Matchers.anyOf(
                emptyIterable(),
                containsInAnyOrder(createMatcherArray(expectedParams)));
    }

    /**
     * 配列比較Matcher
     * <br>
     * 順番比較しない<br>
     *
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<T[]> matchInAnyOrder(@SuppressWarnings("unchecked") T... expectedParams) {
        List<T> list = Arrays.stream(expectedParams).collect(Collectors.toList());
        return Matchers.anyOf(
                arrayWithSize(0),
                arrayContainingInAnyOrder(createMatcherArray(list)));
    }

    /**
     * Map比較Matcher
     * <br>
     * 順番を比較しない<br>
     *
     * @param expectedMap 期待値
     * @return Matcher
     */
    default <K, V> Matcher<Map<? extends K, ? extends V>> matchMap(Map<K,
            V> expectedMap) {
        // Entry全部含む判定Matcherリスト
        List<Matcher<? super Map<? extends K, ? extends V>>> mapMatcherList = expectedMap.entrySet()
                .stream().map(en -> {
                    return hasEntry(match(en.getKey()), match(en.getValue()));
                }).collect(Collectors.toList());
        // サイズ判定Matcher
        mapMatcherList.add(matchMapSize(expectedMap.size()));
        return Matchers.allOf(mapMatcherList);
    }

    /**
     * Mapサイズ比較Matcher
     * <br>
     *
     * @param size 期待値
     * @return Matcher
     */
    default <K, V> Matcher<Map<? extends K, ? extends V>> matchMapSize(final int size) {
        return new TypeSafeMatcher<Map<? extends K, ? extends V>>() {
            @Override
            public boolean matchesSafely(Map<? extends K, ? extends V> kvMap) {
                return kvMap.size() == size;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" has ").appendValue(size).appendText(" key/value pairs");
            }
        };
    }

    /**
     * Optional比較Matcher
     * <br>
     *
     * @param expectedOptional 期待値
     * @return Matcher
     */
    default <T> Matcher<Optional<T>> matchOptional(final Optional<T> expectedOptional) {
        return new TypeSafeMatcher<Optional<T>>() {
            @Override
            public boolean matchesSafely(Optional<T> actualOptional) {
                return match(expectedOptional.orElse(null)).matches(actualOptional.orElse(null));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("same values as ").appendValue(expectedOptional);
            }
        };
    }

    /**
     * オブジェクト比較Matcher
     * <br>
     *
     * @param expectedParam 期待値
     * @return Matcher
     */
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    default <T> Matcher<T> match(T expectedParam) {
        if (expectedParam == null) {
            return (Matcher<T>) Matchers.nullValue();
        } else {
            Class type = expectedParam.getClass();

            if (Optional.class == type) {                       // Optional型
                return matchOptional((Optional) expectedParam);
            } else if (type.isEnum()) {                         // Enum型
                return Matchers.equalTo(expectedParam);
            } else if (expectedParam instanceof Iterable) {     // リストなどIterable型
                return matchInSameOrder((Iterable) expectedParam);
            } else if (expectedParam instanceof Map) {          // Map型
                return matchMap((Map) expectedParam);
            } else if (type.isArray()) {                        // 配列型
                return (Matcher<T>) matchInSameOrder((T[]) expectedParam);
            } else if (String.class == type) {                  // 文字列型
                return Matchers.equalTo(expectedParam);
            } else if (BigDecimal.class == type || BigInteger.class == type) {  // BigDecimal、BigInteger型
                return Matchers.equalTo(expectedParam);
            } else if (Date.class.isAssignableFrom(type)) {     // Date型
                return Matchers.equalTo(expectedParam);
            } else if (LocalDate.class == type) {               // LocalDate型
                return Matchers.equalTo(expectedParam);
            } else if (LocalTime.class == type) {               // LocalTime型
                return Matchers.equalTo(expectedParam);
            } else if (LocalDateTime.class == type) {           // LocalDateTime型
                return Matchers.equalTo(expectedParam);
            } else if (type.isPrimitive()) {                    // プリミティブ型
                return Matchers.equalTo(expectedParam);
            } else if (isPrimitiveWrapClass(type)) {            // プリミティブのラップ型
                return Matchers.equalTo(expectedParam);
            } else {                                            // DTOなど他の型
                // DTO場合、すべてのプロパティーを比較する
                return samePropertyValuesAs(expectedParam);
            }
        }
    }

    /**
     * Matcher配列作成処理
     * <br>
     *
     * @param expectedParams 期待値
     * @return Matcher配列
     */
    @SuppressWarnings("unchecked")
    default <T> Matcher<T>[] createMatcherArray(Iterable<T> expectedParams) {
        List<Matcher<? super T>> list = new ArrayList<>();
        expectedParams.forEach(param -> {
            list.add(match(param));
        });
        return list.toArray(new Matcher[list.size()]);
    }

    /**
     * プリミティブのラップ型の判定処理
     * <br>
     *
     * @param clazz クラス
     * @return true of false
     */
    default boolean isPrimitiveWrapClass(Class<?> clazz) {
        try {
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
                | SecurityException e) {
            return false;
        }
    }
}
