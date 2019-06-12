package com.akigo.test.junit4;

import lombok.Getter;
import lombok.Setter;

/**
 * 機能名 : 単体テスト支援ツールサンプルMOCK対象<br>
 * <br>
 * 
 * @author 作成者：chenhao
 * @since 作成日：2019/2/26
 */
public class MockTargetDemo {

    public static final class TestDto {

        @Getter
        @Setter
        private String item1;
        @Getter
        @Setter
        private String item2;

        public TestDto(String item1, String item2) {
            this.item1 = item1;
            this.item2 = item2;
        }
    }

    /** 引数なしStaticメソッド */
    public static String staticMethod() {
        System.out.println("staticMethod");
        return "staticMethod";
    }

    private static String privateStaticMethod() {
        System.out.println("privateStaticMethod");
        return "privateStaticMethod";
    }

    public static String privateStaticMethodTest() {
        return privateStaticMethod();
    }

    public static final String finalStaticMethod() {
        System.out.println("finalStaticMethod");
        return "finalStaticMethod";
    }

    private static final String privateFinalStaticMethod() {
        System.out.println("privateFinalStaticMethod");
        return "privateFinalStaticMethod";
    }

    public static final String privateFinalStaticMethodTest() {
        return privateFinalStaticMethod();
    }

    /** 引数ありStaticメソッド */
    public static String staticMethodWithParam(String param) {
        System.out.println("staticMethodWithParam");
        return "staticMethodWithParam";
    }

    private static String privateStaticMethodWithParam(String param) {
        System.out.println("privateStaticMethodWithParam");
        return "privateStaticMethodWithParam";
    }

    public static String privateStaticMethodWithParamTest(String param) {
        return privateStaticMethodWithParam(param);
    }

    public static final String finalStaticMethodWithParam(String param) {
        System.out.println("finalStaticMethodWithParam");
        return "finalStaticMethodWithParam";
    }

    private static final String privateFinalStaticMethodWithParam(String param) {
        System.out.println("privateFinalStaticMethodWithParam");
        return "privateFinalStaticMethodWithParam";
    }

    public static final String privateFinalStaticMethodWithParamTest(String param) {
        return privateFinalStaticMethodWithParam(param);
    }

    /** 引数あり（引数がオブジェクト）Staticメソッド */
    public static String staticMethodWithObjectParam(TestDto param) {
        System.out.println("staticMethodWithObjectParam");
        return "staticMethodWithObjectParam";
    }

    private static String privateStaticMethodWithObjectParam(TestDto param) {
        System.out.println("privateStaticMethodWithObjectParam");
        return "privateStaticMethodWithObjectParam";
    }

    public static String privateStaticMethodWithObjectParamTest(TestDto param) {
        return privateStaticMethodWithObjectParam(param);
    }

    public static final String finalStaticMethodWithObjectParam(TestDto param) {
        System.out.println("finalStaticMethodWithObjectParam");
        return "finalStaticMethodWithObjectParam";
    }

    private static final String privateFinalStaticMethodWithObjectParam(TestDto param) {
        System.out.println("privateFinalStaticMethodWithObjectParam");
        return "privateFinalStaticMethodWithObjectParam";
    }

    public static final String privateFinalStaticMethodWithObjectParamTest(TestDto param) {
        return privateFinalStaticMethodWithObjectParam(param);
    }

    /** 返却値なしStaticメソッド */
    public static void staticMethodWithoutReturn() {
        System.out.println("staticMethodWithoutReturn");
    }

    private static void privateStaticMethodWithoutReturn() {
        System.out.println("privateStaticMethodWithoutReturn");
    }

    public static void privateStaticMethodWithoutReturnTest() {
        privateStaticMethodWithoutReturn();
    }

    public static final void finalStaticMethodWithoutReturn() {
        System.out.println("finalStaticMethodWithoutReturn");
    }

    private static final void privateFinalStaticMethodWithoutReturn() {
        System.out.println("privateFinalStaticMethodWithoutReturn");
    }

    public static final void privateFinalStaticMethodWithoutReturnTest() {
        privateFinalStaticMethodWithoutReturn();
    }

    /** 引数なしInstanceメソッド */
    public String instanceMethod() {
        System.out.println("instanceMethod");
        return "instanceMethod";
    }

    private String privateInstanceMethod() {
        System.out.println("privateInstanceMethod");
        return "privateInstanceMethod";
    }

    public String privateInstanceMethodTest() {
        return privateInstanceMethod();
    }

    public final String finalInstanceMethod() {
        System.out.println("finalInstanceMethod");
        return "finalInstanceMethod";
    }

    private final String privateFinalInstanceMethod() {
        System.out.println("privateFinalInstanceMethod");
        return "privateFinalInstanceMethod";
    }

    public final String privateFinalInstanceMethodTest() {
        return privateFinalInstanceMethod();
    }

    /** 引数ありInstanceメソッド */
    public String instanceMethodWithParam(String param) {
        System.out.println("instanceMethodWithParam");
        return "instanceMethodWithParam";
    }

    private String privateInstanceMethodWithParam(String param) {
        System.out.println("privateInstanceMethodWithParam");
        return "privateInstanceMethodWithParam";
    }

    public String privateInstanceMethodWithParamTest(String param) {
        return privateInstanceMethodWithParam(param);
    }

    public final String finalInstanceMethodWithParam(String param) {
        System.out.println("finalInstanceMethodWithParam");
        return "finalInstanceMethodWithParam";
    }

    private final String privateInstanceStaticMethodWithParam(String param) {
        System.out.println("privateInstanceStaticMethodWithParam");
        return "privateInstanceStaticMethodWithParam";
    }

    public final String privateFinalInstanceMethodWithParamTest(String param) {
        return privateInstanceStaticMethodWithParam(param);
    }

    /** 引数（引数がオブジェクト型）ありInstanceメソッド */
    public String instanceMethodWithObjectParam(TestDto param) {
        System.out.println("instanceMethodWithObjectParam");
        return "instanceMethodWithObjectParam";
    }

    private String privateInstanceMethodWithObjectParam(TestDto param) {
        System.out.println("privateInstanceMethodWithObjectParam");
        return "privateInstanceMethodWithObjectParam";
    }

    public String privateInstanceMethodWithObjectParamTest(TestDto param) {
        return privateInstanceMethodWithObjectParam(param);
    }

    public final String finalInstanceMethodWithObjectParam(TestDto param) {
        System.out.println("finalInstanceMethodWithObjectParam");
        return "finalInstanceMethodWithObjectParam";
    }

    private final String privateInstanceStaticMethodWithObjectParam(TestDto param) {
        System.out.println("privateInstanceStaticMethodWithObjectParam");
        return "privateInstanceStaticMethodWithObjectParam";
    }

    public final String privateFinalInstanceMethodWithObjectParamTest(TestDto param) {
        return privateInstanceStaticMethodWithObjectParam(param);
    }

    /** 返却値なしInstanceメソッド */
    public void instanceMethodWithoutReturn() {
        System.out.println("instanceMethodWithoutReturn");
    }

    private void privateInstanceMethodWithoutReturn() {
        System.out.println("privateInstanceMethodWithoutReturn");
    }

    public void privateInstanceMethodWithoutReturnTest() {
        privateInstanceMethodWithoutReturn();
    }

    public final void finalInstanceMethodWithoutReturn() {
        System.out.println("finalInstanceMethodWithoutReturn");
    }

    private final void privateFinalInstanceMethodWithoutReturn() {
        System.out.println("privateFinalInstanceMethodWithoutReturn");
    }

    public final void privateFinalInstanceMethodWithoutReturnTest() {
        privateFinalInstanceMethodWithoutReturn();
    }
}