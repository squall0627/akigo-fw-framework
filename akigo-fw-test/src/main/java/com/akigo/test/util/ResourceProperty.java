package com.akigo.test.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 機能名 : 単体テスト支援ツールリソース読込機能用プロパティーモジュール<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/25
 */
@Getter
@Setter
@AllArgsConstructor
public class ResourceProperty {

    /**
     * プロパティー英名
     */
    private final String propertyName;
    /**
     * プロパティー日本語名
     */
    private final String itemName;
}
