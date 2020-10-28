/*
 * OperatorPriority.java
 * Created on  2020/10/21 12:33
 *
 * Copyright (c) 2017-2099. AkiGo科技有限公司 版权所有
 * AkiGo TECHNOLOGY CO.,LTD. All Rights Reserved.
 *
 */
package com.akigo.core.el.operator;

/**
 * オペレーターの優先級Enum<br>
 * <br>
 *
 * @author chenhao
 * @since 1.0.0
 */
public enum OperatorPriority {
    PRIORITY_0(0),
    RIORITY_1(1),
    RIORITY_2(2),
    RIORITY_3(3),
    RIORITY_4(4),
    RIORITY_5(5),
    RIORITY_6(6),
    RIORITY_7(7);

    private final int priority;

    OperatorPriority(int priority) {
        this.priority = priority;
    }

    public final int priority() {
        return this.priority;
    }
}
