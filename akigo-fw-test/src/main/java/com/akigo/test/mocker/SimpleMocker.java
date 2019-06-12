package com.akigo.test.mocker;

import org.powermock.api.mockito.PowerMockito;
import com.akigo.test.cdi.CdiMockingProgress;

/**
 * 機能名 : 単体テスト支援ツールモック機能インタフェース<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public interface SimpleMocker {

    /**
     * Staticメソッドモック処理
     * <br>
     * public、private、static、finalなどすべてのstaticメソッドもモックできる<br>
     * スタブケース(addCaseで作ったケース)付くメソッドに対して、スタブ期待値で実行され、<br>
     * スタブケース(addCaseで作ったケース)付かないメソッドに対して、実際の処理が実行される<br>
     *
     * @param <T>         T
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
     * @param <T>            T
     * @param targetInstance モック先インスタンス
     * @return 簡単化PowerMockito {@link SimplePowerMockito}
     */
    default <T> SimplePowerMockito<T> mockAnyInstance(T targetInstance) {
        T stubber = PowerMockito.spy(targetInstance);
        // Stubberキャッシュに保持して、TestCDIHelperでCDIする時モックしたオブジェクトを使う
        CdiMockingProgress.getInstance().reportStubber(targetInstance.getClass(), stubber);
        return new SimplePowerMockito<T>(stubber);
    }
}