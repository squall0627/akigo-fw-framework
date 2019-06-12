package com.akigo.test.matcher;

import static com.akigo.test.matcher.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasEntry;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

/**
 * 機能名 : 単体テスト支援ツールMatcher機能インタフェース<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public interface SimpleMatcher {

    /**
     * リストなどIterable型比較Matcher
     * <br>
     * 順番も比較する<br>
     *
     * @param <T>            T
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<Iterable<? extends T>> matchInSameOrder(Iterable<T> expectedParams) {
        Matcher<T>[] matchers = createMatcherArray(expectedParams);
        if (matchers.length == 0) {
            return emptyIterable();
        } else {
            return Matchers.anyOf(Matchers.sameInstance(expectedParams), contains(matchers));
        }
    }

    /**
     * 配列比較Matcher
     * <br>
     * 順番も比較する<br>
     *
     * @param <T>            T
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<T[]> matchInSameOrder(@SuppressWarnings("unchecked") T... expectedParams) {
        List<T> list = Arrays.stream(expectedParams).collect(Collectors.toList());
        if (list.size() == 0) {
            return arrayWithSize(0);
        } else {
            return Matchers.anyOf(
                    Matchers.sameInstance(expectedParams),
                    arrayContaining(createMatcherArray(list)));
        }
    }

    /**
     * リストなどIterable型比較Matcher
     * <br>
     * 順番比較しない<br>
     *
     * @param <T>            T
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<Iterable<? extends T>> matchInAnyOrder(Iterable<T> expectedParams) {
        Matcher<T>[] matchers = createMatcherArray(expectedParams);
        if (matchers.length == 0) {
            return emptyIterable();
        } else {
            return Matchers.anyOf(
                    Matchers.sameInstance(expectedParams),
                    containsInAnyOrder(createMatcherArray(expectedParams)));
        }
    }

    /**
     * 配列比較Matcher
     * <br>
     * 順番比較しない<br>
     *
     * @param <T>            T
     * @param expectedParams 期待値
     * @return Matcher
     */
    default <T> Matcher<T[]> matchInAnyOrder(@SuppressWarnings("unchecked") T... expectedParams) {
        List<T> list = Arrays.stream(expectedParams).collect(Collectors.toList());
        if (list.size() == 0) {
            return arrayWithSize(0);
        } else {
            return Matchers.anyOf(
                    Matchers.sameInstance(expectedParams),
                    arrayContainingInAnyOrder(createMatcherArray(list)));
        }
    }

    /**
     * Map比較Matcher
     * <br>
     * 順番を比較しない<br>
     *
     * @param <K>         K
     * @param <V>         V
     * @param expectedMap 期待値
     * @return Matcher
     */
    default <K, V> Matcher<Map<? extends K, ? extends V>> matchMap(Map<K, V> expectedMap) {
        // Entry全部含む判定Matcherリスト
        List<Matcher<? super Map<? extends K, ? extends V>>> mapMatcherList = expectedMap.entrySet()
                .stream().map(en -> {
                    return hasEntry(match(en.getKey()), match(en.getValue()));
                }).collect(Collectors.toList());
        // サイズ判定Matcher
        mapMatcherList.add(matchMapSize(expectedMap.size()));
        return Matchers.anyOf(Matchers.sameInstance(expectedMap), Matchers.allOf(mapMatcherList));
    }

    /**
     * Mapサイズ比較Matcher
     * <br>
     *
     * @param <K>  K
     * @param <V>  V
     * @param size 期待値
     * @return Matcher
     */
    default <K, V> Matcher<Map<? extends K, ? extends V>> matchMapSize(final int size) {
        return matcher(
                " has {} key/value pairs",
                (actual, expected) -> actual.size() == expected,
                size);
    }

    /**
     * Optional比較Matcher
     * <br>
     *
     * @param <T>              T
     * @param expectedOptional 期待値
     * @return Matcher
     */
    default <T> Matcher<Optional<T>> matchOptional(final Optional<T> expectedOptional) {
        return Matchers.anyOf(
                Matchers.sameInstance(expectedOptional),
                matcher(
                        " same values as {}",
                        (actual, expected) -> match(expected.orElse(null)).matches(actual.orElse(null)),
                        expectedOptional));
    }

    /**
     * オブジェクト比較Matcher
     * <br>
     *
     * @param <T>           T
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

            if (Class.class == type) {
                return Matchers.sameInstance(expectedParam);    // Class型
            } else if (Optional.class == type) {                // Optional型
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
            } else if (isDto(type)) {                           // DTO型
                // DTO場合、すべてのプロパティーを比較する
                return Matchers.anyOf(
                        Matchers.sameInstance(expectedParam),
                        samePropertyValuesAs(expectedParam));
            } else {
                return Matchers.anyOf(
                        Matchers.sameInstance(expectedParam),
                        Matchers.equalTo(expectedParam));
            }
        }
    }

    /**
     * Matcher配列作成処理
     * <br>
     *
     * @param <T>            T
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

    /**
     * DTOの判定処理
     *
     * @param clazz クラス
     * @return true of false
     */
    default boolean isDto(Class<?> clazz) {
        try {
            // getter、setterのあるProperty取得
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz)
                    .getPropertyDescriptors();
            // getter、setterあり、かつFieldも存在する場合、DTOと看做される
            return Arrays.stream(propertyDescriptors).anyMatch(des -> {
                try {
                    return clazz.getDeclaredField(des.getDisplayName()) != null;
                } catch (NoSuchFieldException | SecurityException e) {
                    return false;
                }
            });
        } catch (IntrospectionException e) {
            return false;
        }
    }

    /**
     * カスタマイズMatcherの作成処理<br>
     * BiPredicate<T, U>の1個目パラメーターは実際値、2個目パラメーターは期待値<br>
     * <br>
     *
     * @param <T>              実際値の型
     * @param <U>              期待値の型
     * @param matcherPredicate Matcherの判断メソッド
     * @param expectedValue    期待値
     * @return {@link Matcher}
     */
    default <T, U> Matcher<T> matcher(BiPredicate<T, U> matcherPredicate, U expectedValue) {
        return matcher(null, matcherPredicate, expectedValue);
    }

    /**
     * カスタマイズMatcherの作成処理<br>
     * BiPredicate<T, U>の1個目パラメーターは実際値、2個目パラメーターは期待値<br>
     * <br>
     *
     * @param <T>                   実際値の型
     * @param <U>                   期待値の型
     * @param descriptionOfMismatch 期待値と一致しない場合のエラーメッセージ（例： value as {}）
     * @param matcherPredicate      Matcherの判断メソッド
     * @param expectedValue         期待値
     * @return {@link Matcher}
     */
    default <T, U> Matcher<T> matcher(String descriptionOfMismatch,
                                      BiPredicate<T, U> matcherPredicate, U expectedValue) {

        return new TypeSafeMatcher<T>() {

            @Override
            public void describeTo(Description description) {

                final String splitor = "{}";
                final String splitorRegex = "\\{\\}";
                String[] descriptions = null;
                if (descriptionOfMismatch == null || !descriptionOfMismatch.contains(splitor)) {
                    descriptions = new String[]{
                            "value as ", ""
                    };
                } else {
                    String[] descriptionsTmp = descriptionOfMismatch.split(splitorRegex);
                    if (descriptionOfMismatch.endsWith(splitor)) {
                        descriptions = new String[descriptionsTmp.length + 1];
                        System.arraycopy(
                                descriptionsTmp,
                                0,
                                descriptions,
                                0,
                                descriptionsTmp.length);
                        descriptions[descriptions.length - 1] = "";
                    } else {
                        descriptions = descriptionsTmp;
                    }
                }

                for (int i = 0; i < descriptions.length; i++) {
                    description.appendText(descriptions[i]);
                    if (i < descriptions.length - 1) {
                        description.appendValue(expectedValue);
                    }
                }
            }

            @Override
            protected boolean matchesSafely(T actualValue) {
                return matcherPredicate.test(actualValue, expectedValue);
            }
        };
    }
}
