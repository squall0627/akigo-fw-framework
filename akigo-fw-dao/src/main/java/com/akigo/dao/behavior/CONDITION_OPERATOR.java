package com.akigo.dao.behavior;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public enum CONDITION_OPERATOR {
    /**
     * 条件記号=
     */
    EQ("=", testEq()),
    /**
     * 条件記号<>
     */
    NE("<>", testNe()),
    /**
     * 条件記号<
     */
    LT("<", testLt()),
    /**
     * 条件記号<=
     */
    LE("<=", testLe()),
    /**
     * 条件記号>
     */
    GT(">", testGt()),
    /**
     * 条件記号>=
     */
    GE(">=", testGe()),
    /**
     * 条件記号in
     */
    IN("IN", testIn()),
    /**
     * 条件記号not in
     */
    NOTIN("NOT IN", testNotIn()),
    /**
     * 条件記号like
     */
    LIKE("LIKE", testLike()),
    /**
     * 条件記号not like
     */
    NOTLIKE("NOT LIKE", testNotLike());

    private static final String LIKE_REGEX = "%";
    private static final String ANY = ".*";
    private static final String SINGLE_QUOTATION = "'";
    private static final String COMMA = ",";

    /**
     * SQL条件記号
     */
    @Getter
    private String sqlOperator;
    /**
     * 検証メソッド
     */
    @Getter
    private BiPredicate<Object, Object> varificationMethod;

    /**
     * コンストラクター
     * <br>
     *
     * @param sqlOperator        SQLオプレーたー
     * @param varificationMethod 検証メソッド
     */
    private CONDITION_OPERATOR(String sqlOperator, BiPredicate<Object, Object> varificationMethod) {
        this.sqlOperator = sqlOperator;
        this.varificationMethod = varificationMethod;
    }

    /**
     * 条件記号文字列から条件記号ENUMの変換処理
     * <br>
     *
     * @param operator 条件記号文字列
     * @return 条件記号ENUM
     */
    public static CONDITION_OPERATOR of(String operator) {
        Objects.requireNonNull(operator);
        return Arrays.stream(CONDITION_OPERATOR.values()).filter(
                p -> p.getSqlOperator().equals(operator)).findFirst().orElseThrow(
                () -> new NoSuchElementException());
    }

    /**
     * 正しい条件記号文字列判定処理
     * <br>
     *
     * @param operator 条件記号文字列
     * @return true of false
     */
    public static boolean isValidOperator(String operator) {
        if (operator == null) {
            return false;
        } else {
            return Arrays.stream(CONDITION_OPERATOR.values()).anyMatch(
                    var -> var.getSqlOperator().equals(operator));
        }
    }

    /**
     * 検証条件記号"="の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testEq() {
//        return (p1, p2) -> {
//            if (Strings.isNumeric(p1) && Strings.isNumeric(p2)) {
//                BigDecimal num1 = new BigDecimal(p1);
//                BigDecimal num2 = new BigDecimal(p2);
//                return num1.compareTo(num2) == 0;
//            } else {
//                return Objects.equals(p1, p2);
//            }
//        };
        return null;
    }

    /**
     * 検証条件記号"<>"の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testNe() {
//        return (p1, p2) -> !testEq().test(p1, p2);
        return null;
    }

    /**
     * 検証条件記号"<"の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testLt() {
//        return (p1, p2) -> {
//            try {
//                BigDecimal num1 = new BigDecimal(p1);
//                BigDecimal num2 = new BigDecimal(p2);
//                return num1.compareTo(num2) < 0;
//            } catch (NumberFormatException ex) {
//                return false;
//            }
//        };
        return null;
    }

    /**
     * 検証条件記号"<="の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testLe() {
//        return (p1, p2) -> {
//            try {
//                BigDecimal num1 = new BigDecimal(p1);
//                BigDecimal num2 = new BigDecimal(p2);
//                return num1.compareTo(num2) <= 0;
//            } catch (NumberFormatException ex) {
//                return false;
//            }
//        };
        return null;
    }

    /**
     * 検証条件記号">"の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testGt() {
//        return (p1, p2) -> {
//            try {
//                BigDecimal num1 = new BigDecimal(p1);
//                BigDecimal num2 = new BigDecimal(p2);
//                return num1.compareTo(num2) > 0;
//            } catch (NumberFormatException ex) {
//                return false;
//            }
//        };
        return null;
    }

    /**
     * 検証条件記号">="の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testGe() {
//        return (p1, p2) -> {
//            try {
//                BigDecimal num1 = new BigDecimal(p1);
//                BigDecimal num2 = new BigDecimal(p2);
//                return num1.compareTo(num2) >= 0;
//            } catch (NumberFormatException ex) {
//                return false;
//            }
//        };
        return null;
    }

    /**
     * 検証条件記号"IN"の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testIn() {
//        return (p1, p2) -> {
//            if (p2 == null || p2.length() == 0) {
//                return false;
//            } else {
//                String[] p2Arr = p2.split(COMMA);
//                return Arrays.stream(p2Arr).anyMatch(str -> Objects.equals(p1, str));
//            }
//        };
        return null;
    }

    /**
     * 検証条件記号"NOT IN"の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testNotIn() {
//        return (p1, p2) -> !testIn().test(p1, p2);
        return null;
    }

    /**
     * 検証条件記号"LIKE"の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testLike() {
//        return (p1, p2) -> {
//            if (p1 == null || p2 == null || p2.length() == 0) {
//                return false;
//            } else {
//                String regex = p2.replace(LIKE_REGEX, ANY);
//                return p1.matches(regex);
//            }
//        };
        return null;
    }

    /**
     * 検証条件記号"NOT LIKE"の検証メソッド
     * <br>
     *
     * @return 検証メソッド
     */
    private static BiPredicate<Object, Object> testNotLike() {
//        return (p1, p2) -> !testLike().test(p1, p2);
        return null;
    }

    /**
     * SQLStringの前後のシングルコーテーションを削除<br>
     * <br>
     *
     * @param sqlString SQLString
     * @return シングルコーテーション削除後文字列
     */
    public String convertToJavaString(String sqlString) {
        if (sqlString == null) {
            return sqlString;
        } else {
            if (IN.equals(this) || NOTIN.equals(this)) {
                String[] sqlStringArr = sqlString.split(COMMA);
                return Arrays.stream(sqlStringArr).map(str -> str.trim()).map(
                        this::convertToJavaStringCmn).collect(Collectors.joining(COMMA));
            } else {
                return convertToJavaStringCmn(sqlString);
            }
        }
    }

    /**
     * SQLStringの前後のシングルコーテーションを削除<br>
     * <br>
     *
     * @param sqlString SQLString
     * @return シングルコーテーション削除後文字列
     */
    private String convertToJavaStringCmn(String sqlString) {
        if (sqlString == null) {
            return sqlString;
        } else {
            String returnStr = sqlString;
            if (returnStr.startsWith(SINGLE_QUOTATION) && returnStr.endsWith(SINGLE_QUOTATION)
                    && returnStr.length() > 1) {
                if (returnStr.length() > 2) {
                    returnStr = returnStr.substring(1, returnStr.length() - 1);
                } else {
                    returnStr = "";
                }
            }
            return returnStr;
        }
    }
}
