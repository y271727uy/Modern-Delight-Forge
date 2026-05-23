package com.y271727uy.moderndelight.util.fluid;

public final class Transaction implements AutoCloseable {
    private Transaction() {}

    public static Transaction openOuter() {
        return new Transaction();
    }

    public void commit() {
        // no-op; retained for source compatibility.
    }

    @Override
    public void close() {
        // no-op; retained for try-with-resources compatibility.
    }
}

