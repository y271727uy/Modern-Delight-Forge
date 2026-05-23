package com.y271727uy.moderndelight.util.block_util.power_util;

public interface ACConsumer {
    long getConsumedValue();
    boolean isWorking();
    void energize();
}
