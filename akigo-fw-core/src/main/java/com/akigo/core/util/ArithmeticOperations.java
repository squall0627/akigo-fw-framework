package com.akigo.core.util;

import com.akigo.core.exception.SystemException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class ArithmeticOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticOperations.class);

    @FunctionalInterface
    private interface CalcStrategy extends Serializable {
        void calc(Stack<BigDecimal> numStack, Stack<Operator> opStack);
    }

    private enum Operator {
        /**
         * 足算
         */
        PLUS("+", 1, (numStack, opStack) -> {
            BigDecimal o2 = numStack.pop();
            BigDecimal o1 = numStack.pop();

            BigDecimal result = o1.add(o2);

            LOGGER.debug("{} + {} = {}", o1.toPlainString(), o2.toPlainString(), result);

            numStack.push(result);
        }),
        /**
         * 引算
         */
        SUB("-", 1, (numStack, opStack) -> {
            BigDecimal o2 = numStack.pop();
            BigDecimal o1 = numStack.pop();

            BigDecimal result = o1.subtract(o2);

            LOGGER.debug("{} - {} = {}", o1.toPlainString(), o2.toPlainString(), result);

            numStack.push(result);
        }),
        /**
         * 掛算
         */
        MULTI("*", 2, (numStack, opStack) -> {
            BigDecimal o2 = numStack.pop();
            BigDecimal o1 = numStack.pop();

            BigDecimal result = o1.multiply(o2);

            LOGGER.debug("{} * {} = {}", o1.toPlainString(), o2.toPlainString(), result);

            numStack.push(result);
        }),
        /**
         * 割算
         */
        DIV("/", 2, (numStack, opStack) -> {
            BigDecimal o2 = numStack.pop();
            BigDecimal o1 = numStack.pop();

            int scale = o1.scale() > o2.scale() ? o1.scale() : o2.scale();

            BigDecimal result = o1.divide(o2, scale, RoundingMode.HALF_DOWN);

            LOGGER.debug("{} / {} = {}  scale: {}", o1.toPlainString(), o2.toPlainString(), result, scale);

            numStack.push(result);
        }),
        /**
         * 左括弧
         */
        LEFT_PARENTHESIS("(", 3, (numStack, opStack) -> {
            // 何もしない
        }),
        /**
         * 右括弧
         */
        RIGHT_PARENTHESIS(")", 3, (numStack, opStack) -> {
            Operator lastOp;
            while ((lastOp = opStack.pop()) != LEFT_PARENTHESIS) {
                lastOp.calc(numStack, opStack);
            }
        });

        @Getter
        private String operator;
        @Getter
        private int priority;
        private CalcStrategy calcFunc;

        public static Optional<Operator> of(String operator) {
            return Arrays.stream(Operator.values()).filter(op -> op.getOperator().equals(operator))
                    .findFirst();
        }

        Operator(String operator, int priority,
                 CalcStrategy calcFunc) {
            this.operator = operator;
            this.priority = priority;
            this.calcFunc = calcFunc;
        }

        public void calc(Stack<BigDecimal> numStack, Stack<Operator> opStack) {
            try {
                this.calcFunc.calc(numStack, opStack);
            } catch (Exception e) {
                throw new SystemException("ArithmeticOperations evaluation is failed.", e);
            }
        }
    }

    /**
     * 計算式実行処理<br>
     *
     * @param calcExpression 計算式文字列
     * @return 計算式実行結果{@link BigDecimal}
     */
    public BigDecimal eval(String calcExpression) {
        // 計算式文字列解析
        List<String> elements = parseCalcExpression(calcExpression);

        Stack<BigDecimal> numStack = new Stack<>();
        Stack<Operator> opStack = new Stack<>();

        for (String ele : elements) {
            Optional<Operator> op = Operator.of(ele);
            if (op.isPresent()) {
                if (op.get() == Operator.RIGHT_PARENTHESIS) {
                    op.get().calc(numStack, opStack);
                } else {
                    while (!opStack.isEmpty() &&
                            op.get().getPriority() <= opStack.peek().getPriority() &&
                            opStack.peek() != Operator.LEFT_PARENTHESIS) {
                        Operator peekOp = opStack.pop();
                        peekOp.calc(numStack, opStack);
                    }
                    opStack.push(op.get());
                }
            } else {
                try {
                    numStack.push(new BigDecimal(ele));
                } catch (NumberFormatException e) {
                    throw new SystemException(String.format("[%s] is not a number.", ele), e);
                }
            }
        }

        while (!opStack.isEmpty()) {
            opStack.pop().calc(numStack, opStack);
        }

        BigDecimal result = numStack.peek();

        LOGGER.debug("eval result:{}", result.toPlainString());

        return result;
    }

    /**
     * 計算式文字列解析処理<br>
     *
     * <pre>
     * 例：
     * 1 + 2 * ( -3 + 4 )の解析結果は、
     *
     * [1, +, 2, *, (, -3, +, 4, )]
     * </pre>
     *
     * @param calcExpression 計算式文字列
     * @return 解析後のすべて計算式元素のリスト
     */
    private List<String> parseCalcExpression(String calcExpression) {

        LOGGER.debug("before parse:{}", calcExpression);

        List<String> calcElements = new ArrayList<>();
        List<String> opList = Arrays.stream(Operator.values()).map(Operator::getOperator).collect(
                Collectors.toList());

        String parseStr = calcExpression.trim();
        int idxPointer = 0;
        for (int i = 0; i < parseStr.length(); i++) {
            String tempStr = String.valueOf(parseStr.charAt(i));
            if (opList.contains(tempStr)) {
                // "-"の直後は半角スペースのある場合は、引算と看做されて、半角スペースのない場合は、マイナス数字と看做される
                if (i != 0) {
                    String subStr = parseStr.substring(idxPointer, i).trim();
                    if (!subStr.isEmpty()) {
                        calcElements.add(subStr);
                    }
                }
                calcElements.add(tempStr);
                idxPointer = i + 1;
            }
        }
        if (idxPointer < parseStr.length()) {
            calcElements.add(parseStr.substring(idxPointer, parseStr.length()).trim());
        }

        // マイナス符号のある場合、マイナス符号と直後の文字列を合併する
        LinkedList<String> calcElementsMerged = new LinkedList<>();
        String negativeSignOfBefore = "";
        for (String ele : calcElements) {
            if (isNegativeSign(ele, calcElementsMerged)) {
                negativeSignOfBefore = negativeSignOfBefore + ele;
            } else {
                if (!negativeSignOfBefore.isEmpty()) {
                    calcElementsMerged.add(negativeSignOfBefore + ele);
                    negativeSignOfBefore = "";
                } else {
                    calcElementsMerged.add(ele);
                }
            }
        }
        if (!negativeSignOfBefore.isEmpty()) {
            calcElementsMerged.add(negativeSignOfBefore);
        }

        LOGGER.debug("after parse:{}", calcElementsMerged);

        return calcElementsMerged;
    }

    private boolean isNegativeSign(String str, LinkedList<String> calcElements) {
        // "-"の直前は"+"、 または"-"、 または"*"、 または"/"、 または"(" の場合、マイナス符号と看做される
        return str.equals(Operator.SUB.getOperator()) &&
                (calcElements.peekLast().equals(Operator.PLUS.getOperator()) ||
                        calcElements.peekLast().equals(Operator.SUB.getOperator()) ||
                        calcElements.peekLast().equals(Operator.MULTI.getOperator()) ||
                        calcElements.peekLast().equals(Operator.DIV.getOperator()) ||
                        calcElements.peekLast().equals(Operator.LEFT_PARENTHESIS.getOperator()));
    }
}
