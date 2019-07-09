package com.akigo.core.util;

import com.akigo.core.exception.SystemException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ArithmeticOperationsTest {

    @Test
    public void evalTest01() {
        ArithmeticOperations calculator = new ArithmeticOperations();
        BigDecimal result = calculator.eval(
                "(3 + 4 * 2.4 / 2.123 + ( (-1 --2- (5)) * (2 + -100) + 1.12345 / 2.22 )--1)");
    }

    @Test
    public void evalTest02() {
        ArithmeticOperations calculator = new ArithmeticOperations();
        try {
            BigDecimal result = calculator.eval(
                    "(3 + A * 2.4 / 2.123 + ( (-1 - (5)) * (2 + 100) + 1.12345 / 2.22 ))");
        } catch (SystemException e) {
            System.out.println(e);
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    @Test
    public void evalTest03() {
        ArithmeticOperations calculator = new ArithmeticOperations();
        try {
            BigDecimal result = calculator.eval(
                    "(3 + 4 * 2.4 / 2.123 + ( -(-1 - (5)) * (2 + 100) + 1.12345 / 2.22 ))");
        } catch (SystemException e) {
            System.out.println(e);
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }
}
