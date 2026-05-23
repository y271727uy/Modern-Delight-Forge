package com.y271727uy.moderndelight.util.fluid;

public abstract class SingleVariantStorage<T extends FluidVariant> {
    public T variant = getBlankVariant();
    public long amount = 0L;

    protected abstract T getBlankVariant();

    protected abstract long getCapacity(T variant);

    protected abstract void onFinalCommit();

    public T getFluid() {
        return variant;
    }

    public long getFluidAmount() {
        return amount;
    }

    public long getCapacity() {
        return getCapacity(variant);
    }

    public void setFluid(T variant) {
        this.variant = variant == null ? getBlankVariant() : variant;
        onFinalCommit();
    }

    public void setAmount(long amount) {
        this.amount = Math.max(0L, Math.min(getCapacity(variant), amount));
        if (this.amount == 0L) {
            this.variant = getBlankVariant();
        }
        onFinalCommit();
    }

    public long insert(T variant, long amount, Transaction transaction) {
        if (variant == null || amount <= 0L) {
            return 0L;
        }
        if (this.variant.isBlank() || this.variant.isOf(variant.getFluid())) {
            this.variant = variant;
            long inserted = Math.min(getCapacity(variant) - this.amount, amount);
            if (inserted > 0L) {
                this.amount += inserted;
                onFinalCommit();
            }
            return inserted;
        }
        return 0L;
    }
}

