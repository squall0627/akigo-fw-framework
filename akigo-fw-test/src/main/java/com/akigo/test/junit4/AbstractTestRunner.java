package com.akigo.test.junit4;

import com.akigo.core.util.CDIHelper;
import com.akigo.test.cdi.CdiInjectingProgress;
import com.akigo.test.cdi.CdiMockingProgress;
import com.akigo.test.cdi.CdiPersistenceBeanContainer;
import com.akigo.test.matcher.SimpleAssertion;
import com.akigo.test.mocker.SimpleMocker;
import com.akigo.test.mocker.Stub;
import com.akigo.test.seeder.dbsetup.Seeder;
import com.akigo.test.seeder.dbsetup.TestData;
import lombok.Getter;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 機能名 : 単体テスト支援ツール抽象化TestRunner<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        CDIHelper.class
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractTestRunner implements SimpleMocker, SimpleAssertion {

    private static Logger logger = LoggerFactory.getLogger(AbstractTestRunner.class);

    /**
     * Seeder
     */
    @Getter
    private Seeder seeder;

    private boolean hasDBConfig = true;

    /**
     * TestName
     */
    @Rule
    private final TestName testName = new TestName();

    private final Map<String, Runnable> stubMap = new HashMap<>();

    /**
     * テストケース実行前処理
     * <br>
     * DBコネクション取得<br>
     * {@link @TestData}付くテストケースに対して、テストデータをDBに投入<br>
     */
    @Before
    public void beforeTest() {
        logger.info(testName.getMethodName() + "実行開始");

        CdiInjectingProgress.clear();
        CdiMockingProgress.clear();
        CdiPersistenceBeanContainer.clear();

        hasDBConfig = true;

        seeder = null;
        try {
            seeder = new Seeder("test-data", (s) -> logger.info(s));
        } catch (FileNotFoundException e) {
            // test-datフォルダまたはdbフォルダはない場合、DB繋がらないようにする
            hasDBConfig = false;
            logger.info(testName.getMethodName() + "no database.yml");
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }

//        if (hasDBConfig) {
//            // DBコネクション取得
//            try {
//                Connection con = seeder.getConnection();
//                con.setAutoCommit(false);
//                ConnectionStackHolder.getInstance().push(() -> con);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            // アノテーションTestDataに付けたメソッドに、データを投入する
//            insertTestData();
//        }

        // スタブ実行
        installStub();

    }

    /**
     * テストケース実行後処理
     * <br>
     * DBコネクション解放<br>
     */
    @After
    public void afterTest() {

        CdiInjectingProgress.clear();
        CdiMockingProgress.clear();
        CdiPersistenceBeanContainer.clear();

//        if (hasDBConfig) {
//            try {
//                // テストで変更したデータをロールバック
//                ConnectionStackHolder.getInstance().peek().rollback();
//                // DBコネンクション閉じる
//                ConnectionStackHolder.getInstance().peek().close();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }

        logger.info(testName.getMethodName() + "実行終了");
    }

    /**
     * スタブ実行処理
     * <br>
     */
    private void installStub() {

        // App.classスタブインストール
        installAppStub();

        // テストケースのカスタマイズスタブインストール
        try {
            // 自分自身のメソッド名から取得する
            Method m = this.getClass().getMethod(testName.getMethodName());
            Stub stub = m.getAnnotation(Stub.class);
            if (stub != null && stub.value().length > 0) {
                logger.info("スタブ実装開始");
                Arrays.stream(stub.value()).forEach(stubName -> {
                    if (stubMap.containsKey(stubName)) {
                        stubMap.get(stubName).run();
                        logger.info("スタブ[" + stubName + "]が実装されました");
                    } else {
                        throw new IllegalArgumentException("名前が[" + stubName + "]のスタブが定義されていません。");
                    }
                });
                logger.info("スタブ実装終了");
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * テストデータ投入処理
     * <br>
     */
    private void insertTestData() {
        try {
            // 自分自身のメソッド名から取得する
            Method m = this.getClass().getMethod(testName.getMethodName());
            TestData data = m.getAnnotation(TestData.class);
            if (data != null && data.value().length > 0) {
                logger.info("データ投入開始");
                Arrays.stream(data.value()).forEach(d -> {
                    try {
                        seeder.execute(d);
                        logger.info("データ[" + d + "]が投入されました");
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
                logger.info("データ投入終了");
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * スタブ定義処理
     * <br>
     *
     * @param stubName スタブ名
     * @param stub     スタブ実体
     */
    public void stubDefine(String stubName, Runnable stub) {
        if (stubMap.containsKey(stubName)) {
            throw new IllegalArgumentException("スタブ名[" + stubName + "]が重複定義されている。");
        }
        stubMap.put(stubName, stub);
    }

    abstract void installAppStub();

}