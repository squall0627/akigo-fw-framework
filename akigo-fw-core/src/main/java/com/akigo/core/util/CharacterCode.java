package com.akigo.core.util;

public final class CharacterCode {
    private CharacterCode() {
    }

    /**
     * 全角スペースコードです。
     */
    protected static final int FULL_SPACE = 0x3000;

    /**
     * 半角カナ文字のコードの最初のコードです。
     */
    protected static final int HALF_KANA_START = 0xFF61;

    /**
     * 半角カナ文字のコードの最後のコードです。
     */
    protected static final int HALF_KANA_END = 0xFF9F;

    /**
     * 半角小文字カナ文字のコードの最初のコードです。
     */
    protected static final int HALF_LOWER_KANA_START = 0xFF67;

    /**
     * 半角小文字カナ文字のコードの最後のコードです。
     */
    protected static final int HALF_LOWER_KANA_END = 0xFF6F;

    /**
     * 全角カナ文字のコードの最初のコードです。
     */
    protected static final int FULL_KANA_START = 0x30A1;

    /**
     * 全角カナ文字のコードの最後のコードです。
     */
    protected static final int FULL_KANA_END = 0x30FE;

    /**
     * 長音（「ー」）のコードのコードです。
     */
    public static final int FULL_KANA_HYPHEN = 0x30FC;

    /**
     * 全角ひらがな文字のコードの最初のコードです。
     */
    protected static final int FULL_HIRA_START = 0x3041;

    /**
     * 全角ひらがな文字のコードの最後のコードです。
     */
    protected static final int FULL_HIRA_END = 0x3096;

    /**
     * UTF8エンコード文字列のコードです。
     */
    public static final String UTF8_ENCODING = "UTF8";
}
