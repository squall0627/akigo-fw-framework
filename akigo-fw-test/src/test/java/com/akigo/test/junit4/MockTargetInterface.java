package com.akigo.test.junit4;

/**
 * 機能名 : テスト用インタフェース<br>
 * <br>
 * 
 * @author 作成者：chenhao
 * @since 作成日：2019/2/26
 */
public interface MockTargetInterface {
    default String defaultMethod(String str) {
        return str;
    }
}