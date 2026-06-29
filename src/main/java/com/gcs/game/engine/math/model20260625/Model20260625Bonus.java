package com.gcs.game.engine.math.model20260625;


import com.gcs.game.engine.slots.bonus.BaseWheelBonus;

public class Model20260625Bonus extends BaseWheelBonus {

    /**
     * Wheel animation reward.
     */
    public static final long[] WHEEL_AWARDS = new long[]{
            500, 2500, 12500, 25000
    };

    private static final int[][] WHEEL_AWARDS_WEIGHT = new int[][]{
            {9440, 400, 100, 60},
            {7255, 2225, 420, 100},
            {6145, 2735, 1000, 120},
            {3135, 5525, 1200, 140},
            {850, 7470, 1500, 180},
            {465, 7235, 2000, 300},
            {1405, 5335, 2900, 360},
            {745, 5455, 3300, 500},
            {1110, 4570, 3500, 820},
            {1400, 3600, 4000, 1000},
    };


    @Override
    protected int getTriggerSymbolNumber() {
        return 14;
    }

    @Override
    protected int[][] getWheelAwardWeight() {
        return WHEEL_AWARDS_WEIGHT;
    }
}
