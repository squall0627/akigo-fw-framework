/*
 * AbstractValueExpression.java
 * Created on  2020/10/22 12:37
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.value;

import com.akigo.core.el.ValueExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 値表現式抽象化クラス<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public abstract class AbstractValueExpression {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractValueExpression.class);

    private final ValueExpression valueExpression;

    public AbstractValueExpression() {
        this.valueExpression = this.getClass().getAnnotation(ValueExpression.class);
    }

    /**
     * $XXXX$の場合はtrue、その以外場合はfalse<br>
     *
     * @param value 解析対象文字列
     * @return boolean
     */
    public boolean isValueExpression(String value) {
        return value != null && value.matches(getExpressionRegex());
    }

    /**
     * abc$XXXX$_$XXXX$defの場合はtrue、その以外場合はfalse<br>
     *
     * @param value 解析対象文字列
     * @return boolean
     */
    public boolean hasValueExpression(String value) {
        if (value == null) {
            return false;
        }
        Pattern p = Pattern.compile(getExpressionRegex());
        Matcher m = p.matcher(value);
        return m.find();
    }

    /**
     * 値表現式（シングル値表現式）の解析処理<br>
     *
     * @param valueExpression 値表現式
     * @param parser          値表現式解析用{@link ValueExpressionParser}
     * @return 解析後文字列
     */
    public abstract String eval(String valueExpression, ValueExpressionParser parser);

    /**
     * 解析対象の中の全ての値表現式の解析処理<br>
     *
     * @param valueExpression 値表現式
     * @param parser          値表現式解析用{@link ValueExpressionParser}
     * @return 解析後文字列
     */
    public String evalAll(String valueExpression, ValueExpressionParser parser) {
        String value = valueExpression;

        Pattern pattern = Pattern.compile(getExpressionRegex());
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String matchedRegex = matcher.group();
            value = matcher.replaceFirst(parser.parseValue(matchedRegex));
            matcher = pattern.matcher(value);
        }

        return value;
    }

    /**
     * 値表現式の正規表現式の返却処理<br>
     *
     * @return 値表現式の正規表現式
     */
    public String getExpressionRegex() {
        return this.valueExpression.regex();
    }
}
