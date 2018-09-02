/*
 * AkigoException.java
 * Created on  2018/9/2 下午9:45
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core;

/**
 * フレームワーク例外<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class AkigoException extends RuntimeException {
    /**
     * AkigoExceptionを構築します。
     *
     * @param message 詳細メッセージ
     * @param cause   原因
     */
    public AkigoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * AkigoExceptionを構築します。
     *
     * @param message 詳細メッセージ
     */
    public AkigoException(String message) {
        super(message);
    }

    /**
     * * AkigoExceptionを構築します。
     *
     * @param cause 原因
     */
    public AkigoException(Throwable cause) {
        super(cause);
    }
}
