package com.akigo.test.junit4;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiPredicate;

import static org.junit.Assert.assertThat;

/**
 * 機能名 : SimpleMatcherTest使用例<br>
 * <br>
 * 
 * @author 作成者：chenhao
 * @since 作成日：2019/2/26
 */
public class SimpleMatcherTest extends AkigoTestRunner {

    @SuppressWarnings("serial")
    private static class TestDto implements Serializable {

        private String dd_jax04006;

        @SuppressWarnings("unused")
        public String getDd_jax04006() {
            return dd_jax04006;
        }

        public void setDd_jax04006(String dd_jax04006) {
            this.dd_jax04006 = dd_jax04006;
        }

    }

    private static enum TestEnum {
        A, B
    }

    /**
     * Map比較例
     */
    @Test
    public void test1() {
        TestDto dto1 = new TestDto();
        dto1.setDd_jax04006("v1");
        TestDto dto2 = new TestDto();
        dto2.setDd_jax04006("v2");
        Map<String, TestDto> expectedResult = new HashMap<>();
        expectedResult.put("Key1", dto1);
        expectedResult.put("Key2", dto2);

        TestDto dto3 = new TestDto();
        dto3.setDd_jax04006("v1");
        TestDto dto4 = new TestDto();
        dto4.setDd_jax04006("v2");
        Map<String, TestDto> actualResult = new HashMap<>();
        actualResult.put("Key1", dto3);
        actualResult.put("Key2", dto4);
//        actualResult.put("Key3", dto4);

        assertThat(actualResult, matchMap(expectedResult));
        // 空マップ比較
        assertThat(new HashMap<>(), matchMap(new HashMap<>()));

        assertThat(actualResult, match(expectedResult));
        // 空マップ比較
        assertThat(new HashMap<>(), match(new HashMap<>()));
    }

    /**
     * Collection比較例(順番比較)
     */
    @Test
    public void test2() {
        TestDto dto1 = new TestDto();
        dto1.setDd_jax04006("v1");
        TestDto dto2 = new TestDto();
        dto2.setDd_jax04006("v2");
        List<TestDto> expectedResult = new ArrayList<>();
        expectedResult.add(dto1);
        expectedResult.add(dto2);

        TestDto dto3 = new TestDto();
        dto3.setDd_jax04006("v1");
        TestDto dto4 = new TestDto();
        dto4.setDd_jax04006("v2");
        List<TestDto> actualResult = new ArrayList<>();
        actualResult.add(dto3);
        actualResult.add(dto4);

        assertThat(actualResult, matchInSameOrder(expectedResult));
        // 空リスト比較
        assertThat(new ArrayList<>(), matchInSameOrder(new ArrayList<>()));

        assertThat(actualResult, match(expectedResult));
        // 空リスト比較
        assertThat(new ArrayList<>(), match(new ArrayList<>()));
    }

    /**
     * Collection比較例(順番比較しない)
     */
    @Test
    public void test3() {
        TestDto dto1 = new TestDto();
        dto1.setDd_jax04006("v1");
        TestDto dto2 = new TestDto();
        dto2.setDd_jax04006("v2");
        List<TestDto> expectedResult = new ArrayList<>();
        expectedResult.add(dto1);
        expectedResult.add(dto2);

        TestDto dto3 = new TestDto();
        dto3.setDd_jax04006("v2");
        TestDto dto4 = new TestDto();
        dto4.setDd_jax04006("v1");
        List<TestDto> actualResult = new ArrayList<>();
        actualResult.add(dto3);
        actualResult.add(dto4);

        assertThat(actualResult, matchInAnyOrder(expectedResult));
        // 空リスト比較
        assertThat(new ArrayList<>(), matchInAnyOrder(new ArrayList<>()));
    }

    /**
     * 配列比較例(順番比較)
     */
    @Test
    public void test4() {
        TestDto dto1 = new TestDto();
        dto1.setDd_jax04006("v1");
        TestDto dto2 = new TestDto();
        dto2.setDd_jax04006("v2");
        TestDto[] expectedResult = new TestDto[2];
        expectedResult[0] = dto1;
        expectedResult[1] = dto2;

        TestDto dto3 = new TestDto();
        dto3.setDd_jax04006("v1");
        TestDto dto4 = new TestDto();
        dto4.setDd_jax04006("v2");
        TestDto[] actualResult = new TestDto[2];
        actualResult[0] = dto3;
        actualResult[1] = dto4;

        assertThat(actualResult, matchInSameOrder(expectedResult));
        // 空配列比較
        assertThat(new TestDto[0], matchInSameOrder(new TestDto[0]));
        // 空配列比較
        assertThat(new TestDto[1], matchInSameOrder(new TestDto[1]));

        assertThat(actualResult, match(expectedResult));
        // 空配列比較
        assertThat(new TestDto[0], match(new TestDto[0]));
        // 空配列比較
        assertThat(new TestDto[1], match(new TestDto[1]));
    }

    /**
     * 配列比較例(順番比較しない)
     */
    @Test
    public void test5() {
        TestDto dto1 = new TestDto();
        dto1.setDd_jax04006("v1");
        TestDto dto2 = new TestDto();
        dto2.setDd_jax04006("v2");
        TestDto[] expectedResult = new TestDto[2];
        expectedResult[0] = dto1;
        expectedResult[1] = dto2;

        TestDto dto3 = new TestDto();
        dto3.setDd_jax04006("v2");
        TestDto dto4 = new TestDto();
        dto4.setDd_jax04006("v1");
        TestDto[] actualResult = new TestDto[2];
        actualResult[0] = dto3;
        actualResult[1] = dto4;

        assertThat(actualResult, matchInAnyOrder(expectedResult));
        // 空配列比較
        assertThat(new TestDto[0], matchInAnyOrder(new TestDto[0]));
        // 空配列比較
        assertThat(new TestDto[1], matchInAnyOrder(new TestDto[1]));
    }

    /**
     * DTO比較例
     */
    @Test
    public void test6() {
        TestDto dto1 = new TestDto();
        dto1.setDd_jax04006("v1");
        TestDto dto2 = new TestDto();
        dto2.setDd_jax04006("v1");

        assertThat(dto1, match(dto2));
    }

    /**
     * Optional比較例
     */
    @Test
    public void test7() {
        TestDto dto1 = new TestDto();
        dto1.setDd_jax04006("v1");
        TestDto dto2 = new TestDto();
        dto2.setDd_jax04006("v1");

        assertThat(Optional.of(dto1), matchOptional(Optional.of(dto2)));

        assertThat(Optional.of(dto1), match(Optional.of(dto2)));
    }

    /**
     * Enum比較例
     */
    @Test
    public void test8() {
        assertThat(TestEnum.A, match(TestEnum.A));
    }

    /**
     * 文字列型比較例
     */
    @Test
    public void test9() {
        assertThat("ABC", match("ABC"));
    }

    /**
     * BigDecimal、BigInteger型比較例
     */
    @Test
    public void test10() {
        assertThat(new BigDecimal("123"), match(new BigDecimal("123")));
        assertThat(new BigInteger("456"), match(new BigInteger("456")));
    }

    /**
     * Date型比較例
     */
    @Test
    public void test11() {
        assertThat(new Date(10000), match(new Date(10000)));
    }

    /**
     * LocalDate型比較例
     */
    @Test
    public void test12() {
        assertThat(LocalDate.of(2018, 1, 1), match(LocalDate.of(2018, 1, 1)));
    }

    /**
     * LocalTime型比較例
     */
    @Test
    public void test13() {
        assertThat(LocalTime.of(1, 2, 2), match(LocalTime.of(1, 2, 2)));
    }

    /**
     * LocalTime型比較例
     */
    @Test
    public void test14() {
        assertThat(
            LocalDateTime.of(2018, 1, 1, 1, 2, 2),
            match(LocalDateTime.of(2018, 1, 1, 1, 2, 2)));
    }

    /**
     * プリミティブ型比較例
     */
    @Test
    public void test15() {
        assertThat((short) 1, match((short) 1));
        assertThat((int) 1, match((int) 1));
        assertThat(1l, match(1l));
        assertThat(1.1f, match(1.1f));
        assertThat(1.1d, match(1.1d));
        assertThat((char) 1, match((char) 1));
        assertThat((byte) 1, match((byte) 1));
        assertThat(true, match(true));
    }

    /**
     * プリミティブのラップ型比較例
     */
    @Test
    public void test16() {
        assertThat(Short.valueOf("1"), match(Short.valueOf("1")));
        assertThat(Integer.valueOf("1"), match(Integer.valueOf("1")));
        assertThat(Long.valueOf("1"), match(Long.valueOf("1")));
        assertThat(Float.valueOf("1.1"), match(Float.valueOf("1.1")));
        assertThat(Double.valueOf("1.1"), match(Double.valueOf("1.1")));
        assertThat(Character.valueOf((char) 1), match(Character.valueOf((char) 1)));
        assertThat(Byte.valueOf("1"), match(Byte.valueOf("1")));
        assertThat(Boolean.valueOf("true"), match(Boolean.valueOf("true")));
    }

    /**
     * カスタマイズMatcher使用例
     */
    @Test
    public void test17() {
        BiPredicate<String, String> matcherPredicate = (actual, expected) -> actual.startsWith(
            expected);
        Matcher<String> matcher1 = matcher("startsWith {}", matcherPredicate, "a");
        Matcher<String> matcher2 = matcher(matcherPredicate, "d");
        assertThat("abc", matcher1);
        assertThat("def", matcher2);
    }

    /**
     * Class型比較例
     */
    @Test
    public void test18() {
        assertThat(String.class, match(String.class));
        assertThat(int.class, match(int.class));
    }
}