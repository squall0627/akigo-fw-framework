package com.akigo.core.util;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public final class Strings {
    private Strings() {
    }

    private static String halfMarks = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    private static String fullMarks = "！＂＃＄％＆＇（）＊＋，－．／：；＜＝＞？＠［￥］＾＿｀｛｜｝～・。、";
    private static final String P_HALF_KATAKANA = ".*[\\uFF61-\\uFF9F].*";
    private static final String P_HALF_SMALL_KATAKANA_ = ".*[\\uFF67-\\uFF6F].*";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
    private static final Pattern PERIOD_STRAIGHT = Pattern.compile("^.+(\\.{2,}).+$");

    /**
     * <p>文字列がアルファベットのみかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。
     * 本メソッドは、以下の文字で構成されている場合にtrueを返します。<br>
     * ・半角aから半角z
     * ・半角Aから半角Z
     * ・全角aから全角z
     * ・全角Aから全角Z
     * 。</p>
     * <p>
     * <pre>
     * Strings.isAlphabet(null)   = false
     * Strings.isAlphabet("")     = false
     * Strings.isAlphabet("  ")   = false
     * Strings.isAlphabet("abc")  = true
     * Strings.isAlphabet("ab2c") = false
     * Strings.isAlphabet("ab-c") = false
     * Strings.isAlphabet("Ａａ")  = true （全角）
     * Strings.isAlphabet("ＡA")  = true （全角/半角）
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return アルファベットのみの文字列の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isAlphabet(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream
                .anyMatch(charData -> (((charData < 'a') || (charData > 'z')) && ((charData < 'A') || (charData > 'Z'))
                        && ((charData < 'ａ') || (charData > 'ｚ')) && ((charData < 'Ａ') || (charData > 'Ｚ')))));
    }

    /**
     * <p>文字列が数字であるのかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。
     * </p>
     * <p>
     * <pre>
     * Strings.isNumber(null)    = false
     * Strings.isNumber("")      = false
     * Strings.isNumber("  ")    = false
     * Strings.isNumber("123")   = true
     * Strings.isNumber("0")     = true
     * Strings.isNumber("-123")  = false
     * Strings.isNumber("-0")    = false
     * Strings.isNumber("12 3")  = false
     * Strings.isNumber("ab2c")  = false
     * Strings.isNumber("12-3")  = false
     * Strings.isNumber("12.3")  = false
     * Strings.isNumber("012")   = true
     * Strings.isNumber("-012")  = false
     * Strings.isNumber("０１")  = true （全角）
     * Strings.isNumber("０１01")  = true （全角）
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return 数字の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isNumber(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream chars = str.chars();
        return !(chars.anyMatch(charData -> ((charData < '０') || (charData > '９')) && ((charData < '0') || (charData > '9'))));
    }

    /**
     * <p>文字列が数値であるのかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。
     * </p>
     * <p>
     * <pre>
     * Strings.isNumeric(null)    = false
     * Strings.isNumeric("")      = false
     * Strings.isNumeric("  ")    = false
     * Strings.isNumeric("123")   = true
     * Strings.isNumeric("0")     = true
     * Strings.isNumeric("-123")  = true
     * Strings.isNumeric("-0")    = true
     * Strings.isNumeric("12 3")  = false
     * Strings.isNumeric("ab2c")  = false
     * Strings.isNumeric("12-3")  = false
     * Strings.isNumeric("12.3")  = true
     * Strings.isNumeric("012")   = true
     * Strings.isNumeric("-012")  = true
     * Strings.isNumeric("０１")  = true （全角）
     * Strings.isNumber("０１01")  = true （全角）
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return 数値の文字列の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        char charData = str.charAt(0);
        if (charData == '-') {
            str = str.substring(1);
        }
        return (isNumber(str) || isInteger(str) || isDecimal(str));
    }

    /**
     * <p>文字列が0以上の整数であるのかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。
     * 本メソッドは、以下の文字で構成されている場合にtrueを返します。<br>
     * ・半角0から半角9
     * 。</p>
     * <p>
     * <pre>
     * Strings.isUnsignedInteger(null)    = false
     * Strings.isUnsignedInteger("")      = false
     * Strings.isUnsignedInteger("  ")    = false
     * Strings.isUnsignedInteger("123")   = true
     * Strings.isUnsignedInteger("0")     = true
     * Strings.isUnsignedInteger("-123")  = false
     * Strings.isUnsignedInteger("-0")    = false
     * Strings.isUnsignedInteger("12 3")  = false
     * Strings.isUnsignedInteger("ab2c")  = false
     * Strings.isUnsignedInteger("12-3")  = false
     * Strings.isUnsignedInteger("12.3")  = false
     * Strings.isUnsignedInteger("012")   = false
     * Strings.isUnsignedInteger("-012")  = false
     * Strings.isUnsignedInteger("０１")  = false （全角）
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return 正の整数のみの文字列の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isUnsignedInteger(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        int length = countLength(str);
        if ((length != 1) && (str.startsWith("0"))) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> ((charData < '0') || (charData > '9'))));
    }

    /**
     * <p>文字列が整数かをチェックします。</p>
     * <p>
     * <p>nullは、trueを返します。
     * 空文字列("") は、trueを返します。
     * 本メソッドは、以下の文字で構成されている場合にtrueを返します。<br>
     * ・半角0から半角9(先頭文字が-マイナスを含む)
     * 。</p>
     * <p>
     * <pre>
     * Strings.isInteger(null)    = false
     * Strings.isInteger("")      = false
     * Strings.isInteger("  ")    = false
     * Strings.isInteger("123")   = true
     * Strings.isInteger("0")     = true
     * Strings.isInteger("-123")  = true
     * Strings.isInteger("-0")    = true
     * Strings.isInteger("12 3")  = false
     * Strings.isInteger("ab2c")  = false
     * Strings.isInteger("12-3")  = false
     * Strings.isInteger("12.3")  = false
     * Strings.isInteger("012")   = false
     * Strings.isInteger("-012")  = false
     * Strings.isInteger("０１")  = false （全角）
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return 整数のみの文字列の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isInteger(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        char charData = str.charAt(0);
        if (!((charData >= '0') && (charData <= '9')) && (charData != '-')) {
            return false;
        }
        if (charData == '-') {
            str = str.substring(1);
        }
        return isUnsignedInteger(str);
    }

    /**
     * <p>文字列が0以上の小数であるのかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。
     * 以下の文字で構成されている場合にtrueを返します。<br>
     * ・半角0から半角9 と "."(カンマ)
     * 。</p>
     * <p>
     * <pre>
     * Strings.isUnsignedDecimal(null)     = false
     * Strings.isUnsignedDecimal("")       = false
     * Strings.isUnsignedDecimal("  ")     = false
     * Strings.isUnsignedDecimal("123")    = true
     * Strings.isUnsignedDecimal("0")      = true
     * Strings.isUnsignedDecimal("0.0")    = true
     * Strings.isUnsignedDecimal("-123")   = false
     * Strings.isUnsignedDecimal("12 3")   = false
     * Strings.isUnsignedDecimal("ab2c")   = false
     * Strings.isUnsignedDecimal("12-3")   = false
     * Strings.isUnsignedDecimal(".123")   = false
     * Strings.isUnsignedDecimal("0.12.3") = false
     * Strings.isUnsignedDecimal("12.3")   = true
     * Strings.isUnsignedDecimal("-12.3")  = false
     * Strings.isUnsignedDecimal("-0")     = false
     * Strings.isUnsignedDecimal("01.2")   = false
     * Strings.isUnsignedDecimal("123.")   = false
     * Strings.isUnsignedDecimal("-01.2")  = false
     * Strings.isUnsignedDecimal("０１")   = false （全角）
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return 0以上の小数のみの文字列の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isUnsignedDecimal(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }

        if (str.startsWith(".") || str.startsWith("00")) {
            return false;
        }
        if (str.endsWith(".")) {
            return false;
        }
        if (str.startsWith("0") && !(str.contains(".")) && countLength(str) != 1) {
            return false;
        }

		/* "." 判定フラグ*/
        boolean pointFlag = false;
        for (char charData : str.toCharArray()) {
            /* pointFlag が false で、 "."がチェック文字の場合、pointFlagを true にする */
            if (charData == '.' && !pointFlag) {
                pointFlag = true;
            /* pointFlag が true で、 "."がチェック文字の場合(例: 12.3.4 )、すでに、"."が存在していた事になるため false を返す */
            } else if (charData == '.' && pointFlag) {
                return false;
            } else if ((charData < '0') || (charData > '9')) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>文字列が小数(マイナスを含む)であるのかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。
     * 以下の文字で構成されている場合にtrueを返します。<br>
     * ・半角0から半角9 と "."(カンマ)
     * 。</p>
     * <p>
     * <pre>
     * Strings.isDecimal(null)     = false
     * Strings.isDecimal("")       = false
     * Strings.isDecimal("  ")     = false
     * Strings.isDecimal("123")    = true
     * Strings.isDecimal("0")      = true
     * Strings.isDecimal("0.0")    = true
     * Strings.isDecimal("-123")   = true
     * Strings.isDecimal("12 3")   = false
     * Strings.isDecimal("ab2c")   = false
     * Strings.isDecimal("12-3")   = false
     * Strings.isDecimal(".123")   = false
     * Strings.isDecimal("0.12.3") = false
     * Strings.isDecimal("12.3")   = true
     * Strings.isDecimal("-12.3")  = true
     * Strings.isDecimal("-0")     = true
     * Strings.isDecimal("01.2")   = false
     * Strings.isDecimal("-01.2")  = false
     * Strings.isDecimal("０１")   = false （全角）
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return 小数(マイナスを含む)のみの文字列の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isDecimal(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        char charData = str.charAt(0);
        if (!((charData >= '0') && (charData <= '9')) && (charData != '-')) {
            return false;
        }

        if (charData == '-') {
            str = str.substring(1);
        }
        return isUnsignedDecimal(str);
    }

    /**
     * <p>文字列が半角文字のみであることをチェックします。</p>
     * <p>
     * <pre>
     * Strings.isHalfOnly("a"),is(true)
     * Strings.isHalfOnly("100"),is(true)
     * Strings.isHalfOnly("ｦｧｨ"),is(true)
     * Strings.isHalfOnly("*"),is(true)
     *
     * Strings.isHalfOnly(null),is(false)
     * Strings.isHalfOnly(""),is(false)
     * Strings.isHalfOnly(" "),is(false)
     *
     * Strings.isHalfOnly("Ａ"),is(false)
     * Strings.isHalfOnly("１００"),is(false)
     * Strings.isHalfOnly("テスト"),is(false)
     * Strings.isHalfOnly("ま"),is(false)
     * Strings.isHalfOnly("漢字"),is(false)
     * Strings.isHalfOnly("＊"),is(false)
     * </pre>
     *
     * @param str 文字列
     * @return boolean 半角文字のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfOnly(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }

        boolean result = true;

        //1文字1文字が半英、半数、半カナ、半記号のいずれかであればtrue
        for (char charData : str.toCharArray()) {
            boolean match = false;

            if (Strings.isHalfAlphabet(String.valueOf(charData))) {
                match = true;
            }
            if (Strings.isHalfNumber(String.valueOf(charData))) {
                match = true;
            }
            if (Strings.isHalfKana(String.valueOf(charData))) {
                match = true;
            }
            if (Strings.isHalfMark(String.valueOf(charData))) {
                match = true;
            }
            if (!match) {
                //1文字でも該当しない場合はチェックエラーとしてfalseを返す
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * <p>文字列が半角小文字カナを除く半角文字のみであることをチェックします。</p>
     * <p>
     * {@link #isHalfOnly(String)}
     * <pre>
     * Strings.isHalfOnlyExcludesLowerKana("ｧｨ") = false
     * </pre>
     *
     * @param str 文字列
     * @return boolean 半角小文字カナを除く半角文字のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfOnlyExcludesLowerKana(String str) {
        if (isHalfOnly(str) && !isHalfLowerKana(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 文字列が半角アルファベットのみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 半角アルファベットのみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfAlphabet(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> (((charData < 'a') || (charData > 'z')) && ((charData < 'A') || (charData > 'Z')))));
    }

    /**
     * 文字列が半角小文字アルファベットのみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 半角小文字アルファベットのみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfAlphabetLowerCase(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> ((charData < 'a') || (charData > 'z'))));
    }

    /**
     * 文字列が半角大文字アルファベットのみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 半角大文字アルファベットのみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfAlphabetUpperCase(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> ((charData < 'A') || (charData > 'Z'))));
    }

    /**
     * 文字列が半角数字のみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 半角数字のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfNumber(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> ((charData < '0') || (charData > '9'))));
    }

    /**
     * 文字列が半角英数字のみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 半角英数字のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfAlphanumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> (((charData < 'a') || (charData > 'z')) && ((charData < 'A') || (charData > 'Z')) && ((charData < '0') || (charData > '9')))));
    }

    /**
     * <p>文字列が半角カタカナであるかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。</p>
     * <p>
     * <pre>
     * Strings.isHalfKana(null)   = false
     * Strings.isHalfKana("")     = false
     * Strings.isHalfKana("  ")   = false
     * Strings.isHalfKana("abc")  = false
     * Strings.isHalfKana("ｱ")    = true
     * Strings.isHalfKana("ア")   = false
     * Strings.isHalfKana("ｱｲ-ｶ") = false
     * </pre>
     *
     * @param str チェック対象の文字列
     * @return 文字列が半角カタカナの場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfKana(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> ((charData < CharacterCode.HALF_KANA_START) || (charData > CharacterCode.HALF_KANA_END))));
    }

    /**
     * <p>文字列が半角小文字カナを除く半角カタカナであるかをチェックします。</p>
     * {@link #isHalfKana(String)}
     * <pre>
     * Strings.isHalfKanaExcludesLowerKana("ｧｨ") = false
     * </pre>
     *
     * @param str チェック対象の文字列
     * @return 文字列が半角小文字カナを除く半角カタカナの場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfKanaExcludesLowerKana(String str) {
        if (isHalfKana(str) && !isHalfLowerKana(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 文字列が半角記号のみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 半角記号のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHalfMark(String str) {
        return isOnlyExtra(str, halfMarks);
    }

    /**
     * <p>文字列が全角文字のみであることをチェックします。</p>
     * <p>
     * <pre>
     * Strings.isFullOnly("Ａ"),is(true)
     * Strings.isFullOnly("１００"),is(true)
     * Strings.isFullOnly("テスト"),is(true)
     * Strings.isFullOnly("ま"),is(true)
     * Strings.isFullOnly("漢字"),is(true)
     * Strings.isFullOnly("＊＊"),is(true)
     *
     * Strings.isFullOnly(null),is(false)
     * Strings.isFullOnly(""),is(false)
     * Strings.isFullOnly(" "),is(false)
     *
     * Strings.isFullOnly("a"),is(false)
     * Strings.isFullOnly("100"),is(false)
     * Strings.isFullOnly("ｦｧｨ"),is(false)
     * Strings.isFullOnly("*"),is(false)
     * </pre>
     *
     * @param str 文字列
     * @return boolean 全角文字のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isFullOnly(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }

        boolean result = true;

        //半角文字があればfalse
        for (char charData : str.toCharArray()) {
            String s = String.valueOf(charData);
            if (isHalfOnly(s)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 文字列が全角アルファベットのみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 全角アルファベットのみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isFullAlphabet(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> (((charData < 'ａ') || (charData > 'ｚ')) && ((charData < 'Ａ') || (charData > 'Ｚ')))));
    }


    /**
     * 文字列が全角小文字アルファベットのみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 全角小文字アルファベットのみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isFullAlphabetLowerCase(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream chars = str.chars();
        return !(chars.anyMatch(charData -> ((charData < 'ａ') || (charData > 'ｚ'))));
    }

    /**
     * 文字列が全角大文字アルファベットのみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 全角大文字アルファベットのみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isFullAlphabetUpperCase(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream chars = str.chars();
        return !(chars.anyMatch(charData -> ((charData < 'Ａ') || (charData > 'Ｚ'))));
    }


    /**
     * 文字列が全角数字のみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 全角数字のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isFullNumber(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream chars = str.chars();
        return !(chars.anyMatch(charData -> ((charData < '０') || (charData > '９'))));
    }

    /**
     * <p>文字列が全角カタカナであるかをチェックします。</p>
     * <p>
     * <p>nullは、falseを返します。
     * 空文字列("") は、falseを返します。</p>
     * <p>
     * <pre>
     * Strings.isFullKana(null)   = false
     * Strings.isFullKana("")     = false
     * Strings.isFullKana("  ")   = false
     * Strings.isFullKana("abc")  = false
     * Strings.isFullKana("アイ") = true
     * Strings.isFullKana("ｱｲ")   = false
     * Strings.isFullKana("あい")   = false
     * Strings.isFullKana("ア--イ") = false
     * </pre>
     *
     * @param str チェック対象の文字列
     * @return 文字列が全角カタカナの場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isFullKana(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream chars = str.chars();
        return !chars.anyMatch(charData -> ((charData < CharacterCode.FULL_KANA_START) || (charData > CharacterCode.FULL_KANA_END)));
    }

    /**
     * 文字列がひらがなのみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 文字列がひらがなのみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isHiragana(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream chars = str.chars();
        return chars.allMatch(charData -> (charData >= CharacterCode.FULL_HIRA_START && charData <= CharacterCode.FULL_HIRA_END) || charData == CharacterCode.FULL_KANA_HYPHEN);

    }

    /**
     * 文字列が漢字のみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 漢字のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isKanji(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> UnicodeBlock.of(charData) != UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS));
    }

    /**
     * 文字列が全角記号のみであることを判定します。
     *
     * @param str 文字列
     * @return boolean 全角記号のみである場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isFullMark(String str) {
        return isOnlyExtra(str, fullMarks);
    }

    /**
     * 指定した文字列の文字のみであることを判定します。
     * 半角スペースなどの文字を判定する時に用いる
     *
     * @param str   文字列
     * @param extra 指定文字列
     * @return boolean 指定した文字列の文字のみならtrue、左記以外はfalseを返します。
     */
    public static boolean isOnlyExtra(String str, String extra) {
        if (isNullOrEmpty(str) || isNullOrEmpty(extra)) {
            return false;
        }
        int matchCount = 0;
        for (char charData : str.toCharArray()) {
            for (char extraChar : extra.toCharArray()) {
                if (charData == extraChar) {
                    matchCount++;
                    break;
                }
            }
        }
        return (countLength(str) == matchCount);
    }

    /**
     * 指定した文字列の文字のみであることを判定します。
     * 記号などの文字を判定する時に用いる
     *
     * @param str   文字列
     * @param extra 指定文字列
     * @return boolean 指定した文字列の文字のみならtrue、左記以外はfalseを返します。
     */
    @Deprecated // 0.24.0
    public static boolean isInExtra(String str, String extra) {
        int addsz;
        if (extra != null && countLength(extra) != 0) {
            addsz = countLength(extra);
            for (int j = 0; j < addsz; j++) {
                if (str.charAt(j) == extra.charAt(j)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * <p>文字列が空文字列("")またはnullであることをチェックします。</p>
     * <p>
     * <pre>
     * Strings.isNullOrEmpty(null)      = true
     * Strings.isNullOrEmpty("")        = true
     * Strings.isNullOrEmpty(" ")       = false
     * Strings.isNullOrEmpty("aaa")     = false
     * Strings.isNullOrEmpty("  aaa  ") = false
     * </pre>
     * <p>
     * <p>空文字列("")は、文字列の長さが0である文字列オブジェクトを示す。</p>
     *
     * @param str チェック対象の文字列、null可
     * @return 指定された文字列が空文字列("")またはnullであるときにtrue、左記以外はfalseを返します。
     */
    public static boolean isNullOrEmpty(String str) {
        return (str == null || countLength(str) == 0);
    }

    /**
     * <p>文字列が全角スペース、半角スペース、空文字列("")またはnullであることをチェックします。</p>
     * <p>
     * <pre>
     * Strings.isNullOrEmptyOrWhitespace(null)       = true
     * Strings.isNullOrEmptyOrWhitespace("")          = true
     * Strings.isNullOrEmptyOrWhitespace(" ")         = true
     * Strings.isNullOrEmptyOrWhitespace("aaa")     = false
     * Strings.isNullOrEmptyOrWhitespace("  aaa  ") = false
     * </pre>
     *
     * @param str チェック対象の文字列、null可
     * @return 指定された文字列が、全角スペース、半角スペース、空文字列またはnullであるときにtrue、左記以外はfalseを返します。
     */
    public static boolean isNullOrEmptyOrWhitespace(String str) {
        if (isNullOrEmpty(str)) {
            return true;
        }
        IntStream stream = str.chars();
        return !(stream.anyMatch(charData -> ((!Character.isWhitespace(charData)) && (charData != CharacterCode.FULL_SPACE))));
    }

    /**
     * 渡された文字列`がnullか空文字なら、指定文字列に変換します。
     *
     * @param from       文字列
     * @param defaultStr デフォルト文字列
     * @return String nullか空文字ならデフォルト文字列を返し, それ以外は渡された文字列をそのまま返します。
     */
    public static String fromNullOrEmptyToDefault(String from, String defaultStr) {
        if (isNullOrEmpty(from)) {
            return defaultStr;
        }
        return from;
    }

    /**
     * 文字列が空白を含んでいるか判定します。
     *
     * @param str 検査したい文字列
     * @return boolean 空白が含まれている場合はtrue、左記以外はfalseを返します。
     */
    public static boolean containsWhitespace(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return (str.contains(" ") || str.contains("　"));
    }

    /**
     * 二つの文字列が同じ文字列であることを判定します。
     *
     * @param str1 文字列
     * @param str2 文字列
     * @return boolean str1とstr2が同じ文字列の場合はtrue、左記以外はfalseを返します。str1,str2どちらかがnullの場合はfalseを返します。
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 二つの文字列が同じ文字列であることを判定します。(大文字小文字の区別は行わない)
     *
     * @param str1 文字列
     * @param str2 文字列
     * @return boolean str1とstr2が同じ文字列の場合はtrue、左記以外はfalseを返します。str1,str2どちらかがnullの場合はfalseを返します。
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 文字列が、もう一方の文字列を接頭辞で持つことを判定します。
     *
     * @param str    文字列
     * @param prefix 接頭辞文字列
     * @return boolean str1の語頭がprefixの文字列と等しい場合true、左記以外はfalseを返します。str,prefixのどちらかがnullの場合はfalseを返します。
     */
    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.startsWith(prefix);
    }

    /**
     * 文字列が、もう一方の文字列を接頭辞で持つことを判定します。(大文字小文字の区別は行わない)
     *
     * @param str    文字列
     * @param prefix 接頭辞文字列
     * @return boolean strの語頭がprefixの文字列と等しい場合true、左記以外はfalseを返します。str,prefixどちらかがnullの場合はfalseを返します。
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        String strLower = str.toLowerCase();
        String prefixLower = prefix.toLowerCase();
        return strLower.startsWith(prefixLower);
    }

    /**
     * 文字列が、もう一方の文字列を接尾辞に持つことを判定します。
     *
     * @param str    文字列
     * @param suffix 文字列
     * @return boolean  strの語末がsuffixの文字列と等しい場合true、左記以外はfalseを返します。str,sufixどちらかがnullの場合はfalseを返します。
     */
    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        return str.endsWith(suffix);
    }

    /**
     * 文字列が、もう一方の文字列を接尾辞に持つことを判定します。(大文字小文字の区別は行わない)
     *
     * @param str    文字列
     * @param suffix 文字列
     * @return boolean strの語末がsuffixの文字列と等しい場合true、左記以外はfalseを返します。str,suffixどちらかがnullの場合はfalseを返します。
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        String strLower = str.toLowerCase();
        String suffixLower = suffix.toLowerCase();
        return strLower.endsWith(suffixLower);
    }

    /**
     * 指定された正規表現と一致する個所で文字列を分割し、その結果をリストで返します。
     *
     * @param str   文字列
     * @param regex 正規表現の文字列
     * @return List 指定された正規表現に一致する個所で分割されたリストを返します。引数がnullの場合は空のリストを返します。
     */
    public static List<String> split(String str, String regex) {
        if (str == null || regex == null) {
            return new ArrayList<>();
        }

        String[] strArray = str.split(regex);
        return new ArrayList<>(Arrays.asList(strArray));
    }

    /**
     * 指定された正規表現と一致する個所で文字列を分割し、その結果をリストで返します。
     *
     * @param str   文字列
     * @param regex コンパイル済みの正規表現
     * @return List 指定された正規表現に一致する個所で分割されたリストを返します。引数がnullの場合は空のリストを返します。
     */
    public static List<String> split(String str, Pattern regex) {
        if (str == null || regex == null) {
            return new ArrayList<>();
        }

        String[] strArray = regex.split(str);
        return new ArrayList<>(Arrays.asList(strArray));
    }

    /**
     * 指定文字列の前後の空白を除去します
     *
     * @param str 前後の空白を消去したい文字列
     * @return String 前後の空白を除去した文字列を返します。指定された文字列がnullの場合はOptional.empty();を返します。
     */
    public static Optional<String> trim(String str) {
        if (str == null) {
            return Optional.empty();
        }
        return Optional.of(str.trim());
    }

    /**
     * 指定文字列の末尾の空白を除去します
     *
     * @param str 末尾の空白を消去したい文字列
     * @return String 末尾の空白を除去した文字列を返します。指定された文字列がnullの場合はOptional.empty();を返します。
     */
    public static Optional<String> rtrim(String str) {
        if (str == null) {
            return Optional.empty();
        }
        return Optional.of(str.replaceAll(" +$", ""));
    }

    /**
     * 文字列の部分文字列を返します。
     *
     * @param str        文字列
     * @param beginIndex インデックス
     * @param endIndex   インデックス
     * @return String インデックスで指定された部分文字列を返します。文字列がnullの場合はOptional.empty()を返します。
     */
    public static Optional<String> substring(String str, Integer beginIndex, Integer endIndex) {
        if (str == null || beginIndex == null || endIndex == null) {
            return Optional.empty();
        }
        if (endIndex > countLength(str)) {
            return Optional.of(str.substring(beginIndex));
        }
        return Optional.of(str.substring(beginIndex, endIndex));
    }

    /**
     * 文字列の指定インデックスから末尾までの部分文字列を返します。
     *
     * @param str        文字列
     * @param beginIndex インデックス
     * @return String インデックスで指定された部分文字列を返します。文字列がnullの場合Optional.empty()が返します。
     */
    public static Optional<String> substring(String str, Integer beginIndex) {
        if (str == null || beginIndex == null) {
            return Optional.empty();
        }
        return Optional.of(str.substring(beginIndex));
    }

    /**
     * 文字列の先頭から文字が出現する最初のインデックスを返します。
     *
     * @param str        検索対象
     * @param searchChar 検索対象の文字
     * @return int 検索対象から指定した文字が最初に出現する位置のインデックス、検索対象がnull、もしくは指定した文字が見つからなかった場合は-1を返します。
     */
    public static int indexOf(String str, Character searchChar) {
        if (str == null || searchChar == null) {
            return -1;
        }
        return str.indexOf(searchChar);
    }

    /**
     * 文字列の先頭から文字列が出現する最初のインデックスを返します。
     *
     * @param str       検索対象
     * @param searchStr 検索対象の部分文字列
     * @return int 検索対象から指定した文字列が最初に出現する位置のインデックス、検索対象がnull、もしくは指定した文字が見つからなかった場合は-1を返します。
     */
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }

    /**
     * 文字列の末尾から文字が出現する最初のインデックスを返します。
     *
     * @param str        検索対象
     * @param searchChar 検索対象の文字
     * @return int 検索対象から指定した文字列が最初に出現する位置のインデックス、検索対象がnull、もしくは指定した文字が見つからなかった場合は-1を返します。
     */
    public static int lastIndexOf(String str, Character searchChar) {
        if (str == null || searchChar == null) {
            return -1;
        }
        return str.lastIndexOf(searchChar);
    }

    /**
     * 文字列の末尾から文字が出現する最初のインデックスを返します。
     *
     * @param str       検索対象
     * @param searchStr 検索対象の部分文字列
     * @return int 検索対象から指定した文字列が最初に出現する位置のインデックス、検索対象がnull、もしくは指定した文字が見つからなかった場合は-1を返します。
     */
    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }

    /**
     * 左パディングする
     *
     * @param str         パディングしたい文字列
     * @param paddingSize パディングする長さ
     * @param paddingChar パディングに使われる文字
     * @return String パディングされた文字列
     */
    public static String leftPad(String str, Integer paddingSize, Character paddingChar) {
        if (paddingSize == null || paddingSize == 0 || paddingChar == null) {
            return str;
        }
        StringBuilder formatBuilder = new StringBuilder();
        formatBuilder.append("%");
        formatBuilder.append(paddingSize);
        formatBuilder.append("s");

        String format = formatBuilder.toString();
        if (str == null) {
            return String.format(format, paddingChar).replace(' ', paddingChar);
        }
        return String.format(format, str).replace(' ', paddingChar);
    }

    /**
     * 右パディングする
     *
     * @param str         パディングしたい文字列
     * @param paddingSize パディングする長さ
     * @param paddingChar パディングに使われる文字
     * @return String パディングされた文字列
     */
    public static String rightPad(String str, Integer paddingSize, Character paddingChar) {
        if (paddingSize == null || paddingSize == 0 || paddingChar == null) {
            return str;
        }
        StringBuilder formatBuilder = new StringBuilder();
        formatBuilder.append("%-");
        formatBuilder.append(paddingSize);
        formatBuilder.append("s");

        String format = formatBuilder.toString();
        if (str == null) {
            return String.format(format, paddingChar).replace(' ', paddingChar);
        }
        return String.format(format, str).replace(' ', paddingChar);
    }

    /**
     * 検索対象文字列の中から正規表現で一致した最初の文字列を返します。
     *
     * @param str   検索対象文字列
     * @param regex コンパイル済みの正規表現
     * @return String 検索対象文字列の中から正規表現と一致する前方から最初の文字列を返します。文字列がnull,もしくは一致する文字列がない場合はOptional.empty()を返します。
     */
    public static Optional<String> matchesFirst(String str, Pattern regex) {
        if (str == null || regex == null) {
            return Optional.empty();
        }

        Matcher matcher = regex.matcher(str);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }

    /**
     * 検索対象文字列の中から、正規表現と合致した文字列のリストを返します。
     *
     * @param str   検索対象文字列
     * @param regex コンパイル済みの正規表現
     * @return List 検索対象文字列の中から正規表現と一致する全ての文字列のリストを返します。文字列がnull,もしくは一致する文字列がない場合は空のリストを返します。
     */
    public static List<String> matches(String str, Pattern regex) {
        if (str == null || regex == null) {
            return new ArrayList<>();
        }

        Matcher matcher = regex.matcher(str);
        List<String> matchesList = new ArrayList<>();
        while (matcher.find()) {
            matchesList.add(matcher.group());
        }
        return matchesList;
    }

    /**
     * <p>文字列をバイト数として、長さをチェックします。</p>
     * <p>
     * <p>引数strのバイト数の長さが、引数max以下の場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * encに指定されたエンコードがサポートされていないとき
     * （UnsupportedEncodingException発生時）は、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isLimitedBytes(null,1,"UTF8")   = false
     * StringCheckUtils.isLimitedBytes("",1,"UTF8")     = true
     * StringCheckUtils.isLimitedBytes("a",1,"UTF8")    = true
     * StringCheckUtils.isLimitedBytes("a",2,"UTF8")    = true
     * StringCheckUtils.isLimitedBytes("abc",-1,"UTF8") = false
     * StringCheckUtils.isLimitedBytes("",1,null)       = true
     * StringCheckUtils.isLimitedBytes("",1,"UTFX")     = false
     * </pre>
     *
     * @param str チェック対象の文字列。null可
     * @param max 文字列をバイト数として最大の長さ
     * @param enc 文字列のエンコードを指定します。nullの場合は、"UTF8"を使用します。
     * @return boolean 文字列がmaxサイズ以下の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isLimitedBytes(String str, Integer max, String enc) {
        if (str == null || max == null) {
            return false;
        }
        if (enc == null) {
            enc = CharacterCode.UTF8_ENCODING;
        }
        byte[] b;
        try {
            b = str.getBytes(enc);
            if (b.length > max) {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            return false;
        }

        return true;
    }

    /**
     * <p>文字列をバイト数として、長さをチェックします。</p>
     * <p>
     * <p>引数strのバイト数の長さが、引数max以下の場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * encに指定されたエンコードがサポートされていないとき
     * （UnsupportedEncodingException発生時）は、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isLimitedBytes(null,1)   = false
     * StringCheckUtils.isLimitedBytes("",1)     = true
     * StringCheckUtils.isLimitedBytes("a",1)    = true
     * StringCheckUtils.isLimitedBytes("a",2)    = true
     * StringCheckUtils.isLimitedBytes("abc",-1) = false
     * StringCheckUtils.isLimitedBytes("",1)     = true
     * </pre>
     *
     * @param str チェック対象の文字列。null可
     * @param max 文字列をバイト数として最大の長さ
     * @return boolean 文字列がmaxサイズ以下の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isLimitedBytes(String str, Integer max) {
        return isLimitedBytes(str, max, null);
    }

    /**
     * <p>文字列から文字数を取得して、最大文字列長チェックをします。</p>
     * <p>
     * <p>引数strの文字列長が、引数max以下の場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isUnderMaxLength(null,1,"UTF8")   = false
     * StringCheckUtils.isUnderMaxLength("",1,"UTF8")     = true
     * StringCheckUtils.isUnderMaxLength("a",1,"UTF8")    = true
     * StringCheckUtils.isUnderMaxLength("a",2,"UTF8")    = true
     * StringCheckUtils.isUnderMaxLength("abc",-1,"UTF8") = false
     * StringCheckUtils.isUnderMaxLength("あ",1,"UTF8")   = true
     * StringCheckUtils.isUnderMaxLength("あい",1,"UTF8")   = false
     * StringCheckUtils.isUnderMaxLength("あ",1,null)   = true
     * StringCheckUtils.isUnderMaxLength("あ",1,"UTF9")   = false
     * </pre>
     *
     * @param str チェック対象の文字列。null可
     * @param max 文字列の文字列長そのものの数(半角・全角を意識しない)
     * @param enc 文字列のエンコードを指定します。nullの場合は、"UTF8"を使用します。
     * @return boolean 引数strの文字列長がmax以下の場合はtrue、左記以外はfalseを返します。
     */
    @Deprecated // 0.24.0
    public static boolean isUnderMaxLength(String str, Integer max, String enc) {
        if (str == null || max == null || max < 0) {
            return false;
        }

        int length = countLength(str);

        return length <= max;
    }

    /**
     * <p>文字列から文字数を取得して、最大文字列長チェックをします。</p>
     * <p>
     * <p>引数strの文字列長が、引数max以下の場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isUnderMaxLength(null,1)   = false
     * StringCheckUtils.isUnderMaxLength("",1)     = true
     * StringCheckUtils.isUnderMaxLength("a",1)    = true
     * StringCheckUtils.isUnderMaxLength("a",2)    = true
     * StringCheckUtils.isUnderMaxLength("abc",-1) = false
     * StringCheckUtils.isUnderMaxLength("あ",1)   = true
     * StringCheckUtils.isUnderMaxLength("あい",1)   = false
     * </pre>
     *
     * @param str チェック対象の文字列。null可
     * @param max 文字列の文字列長そのものの数(半角・全角を意識しない)
     * @return boolean 引数strの文字列長がmax以下の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isUnderMaxLength(String str, Integer max) {
        return isUnderMaxLength(str, max, null);
    }

    /**
     * <p>文字列から文字数を取得して、最小文字列長チェックをします。</p>
     * <p>
     * <p>引数strの文字列長が、引数min以上の場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isOverMinLength(null,1,"UTF8")   = false
     * StringCheckUtils.isOverMinLength("",1,"UTF8")     = false
     * StringCheckUtils.isOverMinLength("a",1,"UTF8")    = true
     * StringCheckUtils.isOverMinLength("a",2,"UTF8")    = false
     * StringCheckUtils.isOverMinLength("abc",-1,"UTF8") = false
     * StringCheckUtils.isOverMinLength("あ",1,"UTF8")   = true
     * StringCheckUtils.isOverMinLength("あ",1,null)   = true
     * StringCheckUtils.isOverMinLength("あ",1,"UTFX")   = false
     * </pre>
     *
     * @param str チェック対象の文字列。null可
     * @param min 文字列の文字列長そのものの数(半角・全角を意識しない)
     * @param enc 文字列のエンコードを指定します。nullの場合は、"UTF8"を使用します。
     * @return boolean 引数strの文字列長がmin以上の場合はtrue、左記以外はfalseを返します。
     */
    @Deprecated // 0.24.0
    public static boolean isOverMinLength(String str, Integer min, String enc) {
        if (str == null || min == null || min < 0) {
            return false;
        }

        int length = countLength(str);

        return length >= min;
    }

    /**
     * <p>文字列から文字数を取得して、最小文字列長チェックをします。</p>
     * <p>
     * <p>引数strの文字列長が、引数min以上の場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isOverMinLength(null,1)   = false
     * StringCheckUtils.isOverMinLength("",1)     = false
     * StringCheckUtils.isOverMinLength("a",1)    = true
     * StringCheckUtils.isOverMinLength("a",2)    = false
     * StringCheckUtils.isOverMinLength("abc",-1) = false
     * StringCheckUtils.isOverMinLength("あ",1)   = true
     * </pre>
     *
     * @param str チェック対象の文字列。null可
     * @param min 文字列の文字列長そのものの数(半角・全角を意識しない)
     * @return boolean 引数strの文字列長がmin以上の場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isOverMinLength(String str, Integer min) {
        return isOverMinLength(str, min, null);
    }

    /**
     * <p>文字列から文字数を取得して、固定文字列長チェックをします。</p>
     * <p>
     * <p>引数strの文字列長が、固定文字列長と一致する場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isEqualLength(null,1,"UTF8")   = false
     * StringCheckUtils.isEqualLength("",1,"UTF8")     = false
     * StringCheckUtils.isEqualLength("a",1,"UTF8")    = true
     * StringCheckUtils.isEqualLength("a",2,"UTF8")    = false
     * StringCheckUtils.isEqualLength("abc",-1,"UTF8") = false
     * StringCheckUtils.isEqualLength("あ",1,"UTF8")   = true
     * </pre>
     *
     * @param str       チェック対象の文字列。null可
     * @param fixLength 文字列の文字列長そのものの数(半角・全角を意識しない)
     * @param enc       文字列のエンコードを指定します。nullの場合は、"UTF8"を使用します。
     * @return boolean 引数strの文字列長がfixLengthと等しい場合はtrue、左記以外はfalseを返します。
     */
    @Deprecated // 0.24.0
    public static boolean isEqualLength(String str, Integer fixLength, String enc) {
        if (str == null || fixLength == null || fixLength < 0) {
            return false;
        }

        int length = countLength(str);

        return length == fixLength;
    }

    /**
     * <p>文字列から文字数を取得して、固定文字列長チェックをします。</p>
     * <p>
     * <p>引数strの文字列長が、固定文字列長と一致する場合trueを返します。</p>
     * <p>nullは、falseを返します。
     * 。</p>
     * <p>
     * <pre>
     * StringCheckUtils.isEqualLength(null,1)   = false
     * StringCheckUtils.isEqualLength("",1)     = false
     * StringCheckUtils.isEqualLength("a",1)    = true
     * StringCheckUtils.isEqualLength("a",2)    = false
     * StringCheckUtils.isEqualLength("abc",-1) = false
     * StringCheckUtils.isEqualLength("あ",1)   = true
     * </pre>
     *
     * @param str       チェック対象の文字列。null可
     * @param fixLength 文字列の文字列長そのものの数(半角・全角を意識しない)
     * @return boolean 引数strの文字列長がfixLengthと等しい場合はtrue、左記以外はfalseを返します。
     */
    public static boolean isEqualLength(String str, Integer fixLength) {
        return isEqualLength(str, fixLength, null);
    }

    /**
     * <p>文字列がemailとして正しいかどうかをチェックをします。</p>
     *
     * @param value 文字列
     * @return boolean 指定した文字列がemailの形式として正しければtrue、左記以外はfalse。
     */
    public static boolean isEmail(String value) {
        if (value == null) {
            return true;
        }
        if (!value.contains(".")) {
            return false;
        }

        if (PERIOD_STRAIGHT.matcher(value).matches()) {
            return false;
        }

        return EMAIL_PATTERN.matcher(value).matches();
    }

    /**
     * 対象に半角カナが含まれていないかどうかをチェックをします。
     *
     * @param str 文字列
     * @return strに半角カナが含まなければtrue、半角カナを含むならfalse
     */
    public static boolean isExcludesHalfKana(String str) {
        if (isNullOrEmpty(str)) {
            return true;
        }
        return !str.matches(P_HALF_KATAKANA);
    }


    /**
     * 対象の文字数をカウントします。
     *
     * @param str 文字列
     * @return 文字数
     */
    private static int countLength(String str) {
        // サロゲートペア文字列に対応する場合は本メソッド内で対応
        return str.length();
    }


    /**
     * <p>文字列が半角小文字カナであるかをチェックします。</p>
     *
     * @param str チェック対象の文字列
     * @return 文字列が半角小文字カナの場合はtrue、左記以外はfalseを返します。
     */
    private static boolean isHalfLowerKana(String str) {
        IntStream stream = str.chars();
        //半角小文字カナの場合false
        if ((stream.anyMatch(charData -> ((charData >= CharacterCode.HALF_LOWER_KANA_START)
                && (charData <= CharacterCode.HALF_LOWER_KANA_END))))) {
            return true;
        }
        return false;
    }

}
