/*
 * AGELParser.java
 * Created on  2020/10/22 15:51
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el;

import com.akigo.core.el.operator.AbstractOperator;
import com.akigo.core.el.operator.OperatorFactory;
import com.akigo.core.el.operator.function.ParamSeparator;
import com.akigo.core.el.operator.parenthesis.LeftParenthesis;
import com.akigo.core.el.operator.parenthesis.RightParenthesis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AkiGo Expression Language解析クラス<br>
 * <pre>
 *   #{}で囲んでいる表現式の解析
 *   例：#{($CONSTANTS.S1$ == $CONSTANTS.S2$) ? substring($CONSTANTS.S3$, 1, 4) : abcd}
 * </pre>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class AGELParser {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AGELParser.class);

    private static final String EXPRESSION_STARTER = "#\\{";
    private static final String EXPRESSION_FINISHER = "\\}";
    private static final String EXPRESSION_REGEX = EXPRESSION_STARTER + ".+" + EXPRESSION_FINISHER;

    /**
     * タブ、改行、スペース
     */
    private static final String TOKEN_SEPARATOR = "[\t\r\n ]+";

    private final ValueExpressionParser valueExpressionParser;
    private final Map<String, String> constantsMap;

    public AGELParser() {
        this(null);
    }

    public AGELParser(Map<String, String> constantsMap) {
        /*
         * 当該スレッドが異常終了した可能性があるので、
         * 新AGELParserインスタンスは当該スレッドを再利用する場合、前の残った関数のパラメーターカウントをクリア必要がある。
         */
        OperatorFactory.ofOperator(ParamSeparator.class).clearFuncParamCount();

        this.constantsMap = constantsMap;
        this.valueExpressionParser = new ValueExpressionParser(this);
    }

    public String eval(String expression) {
        if (isLogicExpression(expression)) {
            LOGGER.debug(expression + " の解析開始");

            String returnValue = expression;

            Pattern p = Pattern.compile(EXPRESSION_REGEX);
            Matcher m = p.matcher(expression);
            while (m.find()) {
                String matchedRegex = m.group();
                returnValue = m.replaceFirst(evalAGEL(matchedRegex));
                m = p.matcher(returnValue);
            }

            LOGGER.debug(expression + " の解析結果 = " + returnValue);

            return returnValue;
        } else {
            return this.valueExpressionParser.parseValue(expression);
        }
    }

    public boolean isLogicExpression(String expression) {
        Pattern p = Pattern.compile(EXPRESSION_REGEX);
        Matcher m = p.matcher(expression);
        return m.find();
    }

    private String evalAGEL(String expression) {
        LOGGER.debug(expression + " の解析開始");
        List<String> tokens = parseTokens(expression);

        Stack<AbstractOperator> opStack = new Stack<>();
        Stack<Object> valStack = new Stack<>();

        for (String token : tokens) {
            if (OperatorFactory.isOperator(token)) {
                AbstractOperator op = OperatorFactory.ofOperator(token).get();
                if (op.equals(OperatorFactory.ofOperator(RightParenthesis.class))) {
                    op.accept(opStack, valStack);
                } else {
                    while (!opStack.isEmpty()
                            && op.getPriority() <= opStack.peek().getPriority()
                            && !opStack.peek().equals(OperatorFactory.ofOperator(ParamSeparator.class))
                            && !opStack.peek().equals(OperatorFactory.ofOperator(LeftParenthesis.class))) {
                        AbstractOperator peekOp = opStack.pop();
                        peekOp.accept(opStack, valStack);
                    }
                    opStack.push(op);
                }
            } else {
                valStack.push(this.valueExpressionParser.parseValue(token));
            }
        }

        while (!opStack.isEmpty()) {
            opStack.pop().accept(opStack, valStack);
        }

        String result = AbstractOperator.getString(valStack.peek());

        LOGGER.debug(expression + " の解析結果 = " + result);

        return result;
    }

    private List<String> parseTokens(String expression) {
        String expressionValue = expression.substring(2, expression.length() - 1);

        String[] tokens = expressionValue.split(TOKEN_SEPARATOR);

        List<String> tokenList = new ArrayList<>();
        for (String token : tokens) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < token.length(); j++) {
                String c = token.substring(j, j + 1);
                if (!c.equals(OperatorFactory.ofOperator(LeftParenthesis.class).getSymbol())
                        && !c.equals(OperatorFactory.ofOperator(RightParenthesis.class).getSymbol())
                        && !c.equals(OperatorFactory.ofOperator(ParamSeparator.class).getSymbol())) {
                    sb.append(c);
                } else {
                    if (sb.length() > 0) {
                        tokenList.add(sb.toString());
                    }
                    tokenList.add(c);
                    sb = new StringBuilder();
                }
            }
            if (sb.length() > 0) {
                tokenList.add(sb.toString());
            }
        }
        return tokenList;
    }

    public final Map<String, String> getConstantsMap() {
        return this.constantsMap;
    }
}
