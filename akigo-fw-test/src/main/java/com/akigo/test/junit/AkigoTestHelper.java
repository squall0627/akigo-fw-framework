/*
 * AkigoTestHelper.java
 * Created on  2018/9/19 下午10:18
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.test.junit;

import com.akigo.test.junit.simplePowerMockito.SimpleMatcher;
import com.akigo.test.junit.simplePowerMockito.SimpleMocker;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 単体テスト支援ツール<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({
})
@PrepareForTest({
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AkigoTestHelper implements SimpleMocker, SimpleMatcher {



    private boolean hasDBConfig = true;

    @Rule
    public TestName testName = new TestName();

    private Map<String, Runnable> stubMap = new HashMap<>();

    {
        // Configurationをモックする
//        mockConfiguration();
    }

    /**
     * テストケース実行前処理
     * <br>
     * DBコネクション取得<br>
     * {@link @TestData}付くテストケースに対して、テストデータをDBに投入<br>
     * 
     */
    @Before
    public void beforeTest() {
//        logger.info(testName.getMethodName() + "実行開始");

//        hasDBConfig = true;

//        seeder = null;
//        try {
//            seeder = new Seeder("test-data", (s) -> logger.info(s));
//        } catch (FileNotFoundException e) {
//            // test-datフォルダまたはdbフォルダはない場合、DB繋がらないようにする
//            hasDBConfig = false;
//            logger.info(testName.getMethodName() + "no database.yml");
//        } catch (IOException e1) {
//            throw new RuntimeException(e1);
//        }

//        if (hasDBConfig) {
//            // DBコネクション取得
//            try {
//                Connection con = seeder.getConnection();
//                con.setAutoCommit(false);
//                ConnectionUtils.getInstance().setSpyConnection(con);
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

    @After
    public void afterTest() {
//        if (hasDBConfig) {
//            try {
//                // テストで変更したデータをロールバック
//                ConnectionUtils.getInstance().getConnection().rollback();
//                // DBコネンクション閉じる
//                ConnectionUtils.getInstance().clearSpyConnection();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        logger.info(testName.getMethodName() + "実行終了");
    }

//    private void mockConfiguration() {
//        // Web側のConfigクラスが見れないので、テスト用ConfigクラスでMockする
//        mockAnyStatic(ConfigurationFactory.class).addCase(
//            mock -> mock.doReturn(testConfiguration).when("get"));
//
//        // AppにテストConfigurationを設定する
//        try {
//            Field configField = App.class.getDeclaredField("config");
//            configField.setAccessible(true);
//            configField.set(null, testConfiguration);
//        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
//                        | IllegalAccessException e2) {
//            throw new RuntimeException(e2);
//        }
//    }

    private void installStub() {
        try {
            // 自分自身のメソッド名から取得する
            Method m = this.getClass().getMethod(testName.getMethodName());
            Stub stub = m.getAnnotation(Stub.class);
            if (stub != null && stub.value().length > 0) {
//                logger.info("スタブ実装開始");
                Arrays.stream(stub.value()).forEach(stubName -> {
                    if (stubMap.containsKey(stubName)) {
                        stubMap.get(stubName).run();
//                        logger.info("スタブ[" + stubName + "]が実装されました");
                    } else {
                        throw new IllegalArgumentException("名前が[" + stubName + "]のスタブが定義されていません。");
                    }
                });
//                logger.info("スタブ実装終了");
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

//    private void insertTestData() {
//        try {
//            // 自分自身のメソッド名から取得する
//            Method m = this.getClass().getMethod(testName.getMethodName());
//            TestData data = m.getAnnotation(TestData.class);
//            if (data != null && data.value().length > 0) {
//                logger.info("データ投入開始");
//                Arrays.stream(data.value()).forEach(d -> {
//                    try {
//                        seeder.execute(d);
//                        logger.info("データ[" + d + "]が投入されました");
//                    } catch (IOException e) {
//                        throw new UncheckedIOException(e);
//                    }
//                });
//                logger.info("データ投入終了");
//            }
//        } catch (NoSuchMethodException | SecurityException e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * スタブ作成処理
     * <br>
     * 
     * @param stubName スタブ名
     * @param stub スタブ実体
     */
    public void createStub(String stubName, Runnable stub) {
        if (stubMap.containsKey(stubName)) {
            throw new IllegalArgumentException("スタブ名[" + stubName + "]が重複定義されている。");
        }
        stubMap.put(stubName, stub);
    }

}
