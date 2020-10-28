/*
 * AGELParserTest.java
 * Created on  2020/10/28 17:47
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * 类的描述信息<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public class AGELParserTest {

    /**
     * シングルスレッドテスト
     */
    @Test
    public void evalTest001() {

        final Map<String, String> contentsMap = new HashMap<>();
        contentsMap.put("C1", "1");
        contentsMap.put("C2", "#{123 == 123 ? $CONSTANTS.C1$ : 1}");
        contentsMap.put("C3", "abcd");

        AGELParser agelParser = new AGELParser(contentsMap);

        String targetStr1 = "#{substring ( substring ( $CONSTANTS.C3$ , $CONSTANTS.C2$ , ($CONSTANTS.C3$ == xy ? 1 : length($CONSTANTS.C3$) - 2 * 3 + 5)) , length( $CONSTANTS.C3$ ) - 3 ) == substring(abcd, (1 + 2) / 3 * 2, 3)}";
        String targetStr2 = "#{substring(abcd, 1, substring(123, 2)) == bc}";
        String targetStr3 = "#{substring(abcd, 1, startsWith(123, 2) ? 1 : 3) == bc}";
        String result1 = agelParser.eval(targetStr1);
        String result2 = agelParser.eval(targetStr2);
        String result3 = agelParser.eval(targetStr3);

        assertEquals("true", result1);
        assertEquals("true", result2);
        assertEquals("true", result3);
    }

    /**
     * マルチスレッドテスト
     */
    @Test
    public void evalTest002() {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        final AtomicInteger resultCount = new AtomicInteger(0);

        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            Future<Void> task = (Future<Void>) executorService.submit(new Runnable() {
                @Override
                public void run() {
                    evalTest001();

                    resultCount.incrementAndGet();
                }
            });
            futures.add(task);
        }
        while (futures.size() > 0) {
            Iterator<Future<Void>> it = futures.iterator();
            while (it.hasNext()) {
                Future<Void> task = it.next();
                if (task.isDone() || task.isCancelled()) {
                    try {
                        task.get();
                    } catch (InterruptedException | ExecutionException e) {
                        fail(e.getMessage());
                    }
                    it.remove();
                }
            }
        }

        assertEquals(10000, resultCount.get());
    }
}
