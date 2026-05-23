/*
package com.y271727uy.moderndelight.util.block_util.power_util;

import com.y271727uy.moderndelight.util.energy.ModEnergyStorage;

public interface PowerStorageAble {
    Power getPower();
    ModEnergyStorage getEnergyStorage();
    default void addPower(long value){
        getPower().addPower(value);
    }
    default void reducePower(long value){
        getPower().reducePower(value);
    }
    default long getPowerValue(){
        return getPower().getPowerValue();
    }
    default boolean isPowerEmpty() {
        return getPowerValue() == 0;
    }
    default boolean isPowerFull() {
        return getPowerValue() == getPower().getMaxPower();
    }
    default void setPower(long value){
        getPower().setPowerValue(value);
    }
    default void resetPower(long value, long maxValue){
        getPower().resetPower(value,maxValue);
    }
    default void addEnergy(long value){
        long remaining = Math.max(0L, value);
        while (remaining > 0L) {
            int inserted = getEnergyStorage().receiveEnergy((int) Math.min(Integer.MAX_VALUE, remaining), false);
            if (inserted <= 0) {
                return;
            }
            remaining -= inserted;
        }
    }
    default void reduceEnergy(long value){
        long remaining = Math.max(0L, value);
        while (remaining > 0L) {
            int extracted = getEnergyStorage().extractEnergy((int) Math.min(Integer.MAX_VALUE, remaining), false);
            if (extracted <= 0) {
                return;
            }
            remaining -= extracted;
        }
    }
}
*/