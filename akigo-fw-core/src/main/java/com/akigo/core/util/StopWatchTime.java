package com.akigo.core.util;

import java.io.Serializable;

public class StopWatchTime implements Serializable {

    private static final long serialVersionUID = 1L;

    private long start = 0, lap = 0;

    public void startTime() {

        this.lap = this.start = System.currentTimeMillis();

    }

    public long lapTime() {
        long now = System.currentTimeMillis();

        if (start == 0)
            return -1;

        long result = now - this.lap;
        this.lap = now;
        return result;
    }

    public long stopTime() {
        long now = System.currentTimeMillis();

        return start == 0 ? -1 : (now - start);
    }
}
