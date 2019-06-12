package com.akigo.test.junit4;

import com.akigo.test.junit4.AkigoTestRunner;
import com.akigo.test.mocker.SimplePowerMockito;
import com.akigo.test.junit4.MockTargetDemo.TestDto;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * 機能名 : SimpleMockerTest使用例<br>
 * <br>
 * 
 * @author 作成者：chenhao
 * @since 作成日：2019/2/26
 */
@PrepareForTest({
    MockTargetDemo.class // ※※※※※※ インスタンスのpublicメソッドの以外をモックしたい場合、モック先クラスはぜひここに設定してください ※※※※※※ //
})
public class SimpleMockerTest extends AkigoTestRunner {

    /**
     * StaticメソッドのMock例
     */
    @Test
    public void test1() {
        mockAnyStatic(MockTargetDemo.class) // クラスをモック

                        // *** パラメータなしメソッド *** //
                        .addCase(
                            mock -> mock.doReturn("mockstaticMethod").when("staticMethod")
                                            .withNoArguments())  // public static method
                        .addCase(
                            mock -> mock.doReturn("mockprivateStaticMethod").when(
                                "privateStaticMethod").withNoArguments())                                      // private
                                                                                                               // static
                                                                                                               // method
                        .addCase(
                            mock -> mock.doReturn("mockfinalStaticMethod").when(
                                "finalStaticMethod").withNoArguments())                                        // public
                                                                                                               // static
                                                                                                               // final
                                                                                                               // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateFinalStaticMethod").when(
                                "privateFinalStaticMethod").withNoArguments())                                 // private
                                                                                                               // static
                                                                                                               // final
                                                                                                               // method

                        // *** パラメータありメソッド *** //
                        .addCase(
                            mock -> mock.doReturn("mockstaticMethodWithParam").when(
                                "staticMethodWithParam",
                                String.class).withArguments("aaa"))                          // public
                                                                                             // static
                                                                                             // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateStaticMethodWithParam").when(
                                "privateStaticMethodWithParam",
                                String.class).withArguments("aaa"))                          // private
                                                                                             // static
                                                                                             // method
                        .addCase(
                            mock -> mock.doReturn("mockfinalStaticMethodWithParam").when(
                                "finalStaticMethodWithParam",
                                String.class).withArguments("aaa"))                          // public
                                                                                             // static
                                                                                             // final
                                                                                             // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateFinalStaticMethodWithParam").when(
                                "privateFinalStaticMethodWithParam",
                                String.class).withArguments("aaa"))                          // private
                                                                                             // static
                                                                                             // final
                                                                                             // method

                        // *** パラメータ(オブジェクト型)ありメソッド *** //
                        .addCase(
                            mock -> mock.doReturn("mockstaticMethodWithObjectParam").when(
                                "staticMethodWithObjectParam",
                                TestDto.class).withArguments(new TestDto("aaa", "bbb")))                          // public
                                                                                                                  // static
                                                                                                                  // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateStaticMethodWithObjectParam").when(
                                "privateStaticMethodWithObjectParam",
                                TestDto.class).withArguments(new TestDto("aaa", "bbb")))                          // private
                                                                                                                  // static
                                                                                                                  // method
                        .addCase(
                            mock -> mock.doReturn("mockfinalStaticMethodWithObjectParam").when(
                                "finalStaticMethodWithObjectParam",
                                TestDto.class).withArguments(new TestDto("aaa", "bbb")))                          // public
                                                                                                                  // static
                                                                                                                  // final
                                                                                                                  // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateFinalStaticMethodWithObjectParam")
                                            .when(
                                                "privateFinalStaticMethodWithObjectParam",
                                                TestDto.class).withArguments(
                                                    new TestDto("aaa", "bbb")))                          // private
                                                                                                         // static
                                                                                                         // final
                                                                                                         // method

                        // *** 返却値なしメソッド *** //
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockstaticMethodWithoutReturn");
                            }).when("staticMethodWithoutReturn").withNoArguments())                           // public
                                                                                                              // static
                                                                                                              // method
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockprivateStaticMethodWithoutReturn");
                            }).when("privateStaticMethodWithoutReturn").withNoArguments())                    // private
                                                                                                              // static
                                                                                                              // method
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockfinalStaticMethodWithoutReturn");
                            }).when("finalStaticMethodWithoutReturn").withNoArguments())                      // public
                                                                                                              // static
                                                                                                              // final
                                                                                                              // method
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockprivateFinalStaticMethodWithoutReturn");
                            }).when("privateFinalStaticMethodWithoutReturn").withNoArguments());              // private
                                                                                                              // static
                                                                                                              // final
                                                                                                              // method

        System.out.println("test1 start");
        System.out.println(MockTargetDemo.staticMethod());
        System.out.println(MockTargetDemo.privateStaticMethodTest());
        System.out.println(MockTargetDemo.finalStaticMethod());
        System.out.println(MockTargetDemo.privateFinalStaticMethodTest());

        System.out.println(MockTargetDemo.staticMethodWithParam("aaa"));
        System.out.println(MockTargetDemo.privateStaticMethodWithParamTest("aaa"));
        System.out.println(MockTargetDemo.finalStaticMethodWithParam("aaa"));
        System.out.println(MockTargetDemo.privateFinalStaticMethodWithParamTest("aaa"));

        System.out.println(MockTargetDemo.staticMethodWithObjectParam(new TestDto("aaa", "bbb")));
        System.out.println(
            MockTargetDemo.privateStaticMethodWithObjectParamTest(new TestDto("aaa", "bbb")));
        System.out.println(
            MockTargetDemo.finalStaticMethodWithObjectParam(new TestDto("aaa", "bbb")));
        System.out.println(
            MockTargetDemo.privateFinalStaticMethodWithObjectParamTest(new TestDto("aaa", "bbb")));

        MockTargetDemo.staticMethodWithoutReturn();
        MockTargetDemo.privateStaticMethodWithoutReturnTest();
        MockTargetDemo.finalStaticMethodWithoutReturn();
        MockTargetDemo.privateFinalStaticMethodWithoutReturnTest();
        System.out.println("test1 end");
    }

    /**
     * InstanceメソッドのMock例
     */
    @Test
    public void test2() {
        SimplePowerMockito<MockTargetDemo> mockTarget = mockAnyInstance(new MockTargetDemo()); // インスタンスをモック

        mockTarget
                        // *** パラメータなしメソッド *** //
                        .addCase(
                            mock -> mock.doReturn("mockinstanceMethod").when("instanceMethod")
                                            .withNoArguments())  // public method
                        .addCase(
                            mock -> mock.doReturn("mockprivateInstanceMethod").when(
                                "privateInstanceMethod").withNoArguments())                                      // private
                                                                                                                 // method
                        .addCase(
                            mock -> mock.doReturn("mockfinalInstanceMethod").when(
                                "finalInstanceMethod").withNoArguments())                                        // public
                                                                                                                 // final
                                                                                                                 // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateFinalInstanceMethod").when(
                                "privateFinalInstanceMethod").withNoArguments())                                 // private
                                                                                                                 // final
                                                                                                                 // method

                        // *** パラメータありメソッド *** //
                        .addCase(
                            mock -> mock.doReturn("mockinstanceMethodWithParam").when(
                                "instanceMethodWithParam",
                                String.class).withArguments("aaa"))                          // public
                                                                                             // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateInstanceMethodWithParam").when(
                                "privateInstanceMethodWithParam",
                                String.class).withArguments("aaa"))                          // private
                                                                                             // method
                        .addCase(
                            mock -> mock.doReturn("mockfinalInstanceMethodWithParam").when(
                                "finalInstanceMethodWithParam",
                                String.class).withArguments("aaa"))                          // public
                                                                                             // final
                                                                                             // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateInstanceStaticMethodWithParam").when(
                                "privateInstanceStaticMethodWithParam",
                                String.class).withArguments("aaa"))                          // private
                                                                                             // final
                                                                                             // method

                        // *** パラメータ（オブジェクト型）ありメソッド *** //
                        .addCase(
                            mock -> mock.doReturn("mockinstanceMethodWithObjectParam").when(
                                "instanceMethodWithObjectParam",
                                TestDto.class).withArguments(new TestDto("aaa", "bbb")))                          // public
                                                                                                                  // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateInstanceMethodWithObjectParam").when(
                                "privateInstanceMethodWithObjectParam",
                                TestDto.class).withArguments(new TestDto("aaa", "bbb")))                          // private
                                                                                                                  // method
                        .addCase(
                            mock -> mock.doReturn("mockfinalInstanceMethodWithObjectParam").when(
                                "finalInstanceMethodWithObjectParam",
                                TestDto.class).withArguments(new TestDto("aaa", "bbb")))                          // public
                                                                                                                  // final
                                                                                                                  // method
                        .addCase(
                            mock -> mock.doReturn("mockprivateInstanceStaticMethodWithObjectParam")
                                            .when(
                                                "privateInstanceStaticMethodWithObjectParam",
                                                TestDto.class).withArguments(
                                                    new TestDto("aaa", "bbb")))                          // private
                                                                                                         // final
                                                                                                         // method

                        // *** 返却値なしメソッド *** //
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockinstanceMethodWithoutReturn");
                            }).when("instanceMethodWithoutReturn").withNoArguments())                           // public
                                                                                                                // method
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockprivateInstanceMethodWithoutReturn");
                            }).when("privateInstanceMethodWithoutReturn").withNoArguments())                    // private
                                                                                                                // method
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockfinalInstanceMethodWithoutReturn");
                            }).when("finalInstanceMethodWithoutReturn").withNoArguments())                      // public
                                                                                                                // final
                                                                                                                // method
                        .addCase(
                            mock -> mock.doAnswer(invocation -> {
                                System.out.println("mockprivateFinalInstanceMethodWithoutReturn");
                            }).when("privateFinalInstanceMethodWithoutReturn").withNoArguments());              // private
                                                                                                                // final
                                                                                                                // method

        // モックしたオブジェクト取得
        MockTargetDemo target = mockTarget.getTargetInstance();

        System.out.println("test2 start");
        System.out.println(target.instanceMethod());
        System.out.println(target.privateInstanceMethodTest());
        System.out.println(target.finalInstanceMethod());
        System.out.println(target.privateFinalInstanceMethodTest());

        System.out.println(target.instanceMethodWithParam("aaa"));
        System.out.println(target.privateInstanceMethodWithParamTest("aaa"));
        System.out.println(target.finalInstanceMethodWithParam("aaa"));
        System.out.println(target.privateFinalInstanceMethodWithParamTest("aaa"));

        System.out.println(target.instanceMethodWithObjectParam(new TestDto("aaa", "bbb")));
        System.out.println(
            target.privateInstanceMethodWithObjectParamTest(new TestDto("aaa", "bbb")));
        System.out.println(target.finalInstanceMethodWithObjectParam(new TestDto("aaa", "bbb")));
        System.out.println(
            target.privateFinalInstanceMethodWithObjectParamTest(new TestDto("aaa", "bbb")));

        target.instanceMethodWithoutReturn();
        target.privateInstanceMethodWithoutReturnTest();
        target.finalInstanceMethodWithoutReturn();
        target.privateFinalInstanceMethodWithoutReturnTest();
        System.out.println("test2 end");
    }

    /**
     * 1条件に対して呼び出す回数によって返却値違う場合のMock例
     */
    @Test
    public void test3() {
        mockAnyStatic(MockTargetDemo.class)
                        .addCase(
                            mock -> mock.doReturn(
                                "mockstaticMethod1",
                                "mockstaticMethod2",
                                "mockstaticMethod3").when("staticMethod").withNoArguments());

        System.out.println("test3 start");
        System.out.println(MockTargetDemo.staticMethod()); // 1回目
        System.out.println(MockTargetDemo.staticMethod()); // 2回目
        System.out.println(MockTargetDemo.staticMethod()); // 3回目
        System.out.println("test3 end");
    }

    /**
     * Mock先メソッドに何もさせないようにしようとする場合
     */
    @Test
    public void test4() {
        // doNothing()を使用
        mockAnyStatic(MockTargetDemo.class)
                        .addCase(
                            mock -> mock.doNothing().when("privateFinalStaticMethodWithoutReturn")
                                            .withNoArguments());

        System.out.println("test4 start");
        MockTargetDemo.privateFinalStaticMethodWithoutReturnTest();
        System.out.println("test4 end");
    }

    /**
     * Mock先メソッドに例外を投げさせたい場合
     */
    @Test
    public void test5() {
        // doThrow()を使用
        mockAnyStatic(MockTargetDemo.class)
                        .addCase(
                            mock -> mock.doThrow(new RuntimeException("排他例外")).when(
                                "privateFinalStaticMethodWithoutReturn").withNoArguments());

        System.out.println("test5 start");
        try {
            MockTargetDemo.privateFinalStaticMethodWithoutReturnTest();
        } catch (RuntimeException e) {
            System.out.println(e);
        }
        System.out.println("test5 end");
    }

    /**
     * Mock先メソッドに実際のメソッドを実行させたい場合
     */
    @Test
    public void test6() {

        mockAnyStatic(MockTargetDemo.class)
                        .addCase(
                            mock -> mock.doReturn("mockstaticMethodWithParam").when(
                                "staticMethodWithParam",
                                String.class).withArguments("aaa"))         // "aaa"の場合は、モックした値を返却
                        .addCase(mock -> mock.doCallRealMethod().when(
                            "staticMethodWithParam",
                            String.class).withArguments("bbb"));            // doCallRealMethod()で、"bbb"の場合は、本物の値を返却

        System.out.println("test6 start");
        System.out.println(MockTargetDemo.staticMethodWithParam("aaa"));
        System.out.println(MockTargetDemo.staticMethodWithParam("bbb"));
        System.out.println("test6 end");
    }

    /**
     * Lambda以外の書き方例
     */
    @Test
    public void test7() {
        // staticモック
        SimplePowerMockito<MockTargetDemo> staticMock = mockAnyStatic(MockTargetDemo.class); // クラスをモック
        staticMock.doReturn("mockstaticMethod").when("staticMethod").withNoArguments();  // public
                                                                                         // static
                                                                                         // method
        staticMock.doReturn("mockprivateStaticMethod").when("privateStaticMethod")
                        .withNoArguments();  // private
        // static
        // method

        // インスタンスモック
        SimplePowerMockito<MockTargetDemo> instanceTarget = mockAnyInstance(new MockTargetDemo()); // インスタンスをモック
        instanceTarget.doReturn("mockinstanceMethod").when("instanceMethod").withNoArguments();  // public
                                                                                                 // method
        instanceTarget.doReturn("mockprivateInstanceMethod").when("privateInstanceMethod")
                        .withNoArguments();  // private method
    }

    /**
     * InterfaceのDeafultメソッドのモック
     */
    @Test
    public void test8() {
        SimplePowerMockito<MockTargetInterfaceImpl> target = mockAnyInstance(
            new MockTargetInterfaceImpl());
        target.doReturn("mockStr").when("defaultMethod", String.class).withArguments("str");

        System.out.println("test8 start");
        System.out.println(target.getTargetInstance().defaultMethod("str"));  // モックの値を返却
        System.out.println(target.getTargetInstance().defaultMethod("str2")); // 実際値を返却
        System.out.println("test8 end");
    }

}
// CHECKSTYLE:ON