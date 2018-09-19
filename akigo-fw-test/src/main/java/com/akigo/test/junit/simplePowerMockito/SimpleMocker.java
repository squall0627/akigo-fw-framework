/*
 * SimpleMocker.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit.simplePowerMockito;

import org.powermock.api.mockito.PowerMockito;

/**
 * 単体テスト支援ツールモック機能インタフェース<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public interface SimpleMocker {
    /**
     * Staticメソッドモック処理
     * <br>
     * public、private、static、finalなどすべてのstaticメソッドもモックできる<br>
     * スタブケース(addCaseで作ったケース)付くメソッドに対して、スタブ期待値で実行され、<br>
     * スタブケース(addCaseで作ったケース)付かないメソッドに対して、実際の処理が実行される<br>
     *
     * @param targetCLazz モック先クラス
     * @return 簡単化PowerMockito {@link SimplePowerMockito}
     */
    default <T> SimplePowerMockito<T> mockAnyStatic(Class<T> targetCLazz) {
        PowerMockito.spy(targetCLazz);
        return new SimplePowerMockito<T>(targetCLazz);
    }

    /**
     * Instanceメソッドモック処理
     * <br>
     * public、private、finalなどすべてのInstanceメソッドもモックできる<br>
     * スタブケース(addCaseで作ったケース)付くメソッドに対して、スタブ期待値で実行され、<br>
     * スタブケース(addCaseで作ったケース)付かないメソッドに対して、実際の処理が実行される<br>
     *
     * @param targetInstance モック先インスタンス
     * @return 簡単化PowerMockito {@link SimplePowerMockito}
     */
    default <T> SimplePowerMockito<T> mockAnyInstance(T targetInstance) {
        return new SimplePowerMockito<T>(PowerMockito.spy(targetInstance));
    }
}
