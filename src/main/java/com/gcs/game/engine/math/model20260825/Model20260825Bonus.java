package com.gcs.game.engine.math.model20260825;


import com.gcs.game.engine.slots.bonus.BaseWheelBonus;

public class Model20260825Bonus extends BaseWheelBonus {

    /**
     * Wheel animation reward.
     */
    public static final long[] WHEEL_AWARDS = new long[]{
            500, 2500, 12500, 25000
    };

    private static final int[][] WHEEL_AWARDS_WEIGHT = new int[][]{
            {9540, 300, 100, 60},
            {9540, 300, 100, 60},
            {7455, 2025, 420, 100},
            {7455, 2025, 420, 100},
            {6445, 2435, 1000, 120},
            {6445, 2435, 1000, 120},
            {3535, 5125, 1200, 140},
            {3535, 5125, 1200, 140},
            {1350, 6970, 1500, 180},
            {1350, 6970, 1500, 180},
            {1065, 6635, 2000, 300},
            {1065, 6635, 2000, 300},
            {2105, 4635, 2900, 360},
            {2105, 4635, 2900, 360},
            {1545, 4655, 3300, 500},
            {1545, 4655, 3300, 500},
            {2010, 3670, 3500, 820},
            {2010, 3670, 3500, 820},
            {2400, 2600, 4000, 1000},
            {2400, 2600, 4000, 1000},
    };


    @Override
    protected int getTriggerSymbolNumber() {
        return 12;
    }

    @Override
    protected int[][] getWheelAwardWeight() {
        return WHEEL_AWARDS_WEIGHT;
    }
}
