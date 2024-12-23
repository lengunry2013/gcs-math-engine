package com.gcs.game.engine.math.model5070530;

import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.utils.RandomUtil;

public class Model5070530 extends BaseKenoModel {
    public static final int[][] FS_TIMES = new int[][]{
            {5, 6, 8, 12},
            {3, 4, 7}
    };
    public static final int[][] FS_WEIGHT = new int[][]{
            {1, 1, 1, 1},
            {1, 1, 1}
    };
    public static final int[][] FS_3SETS_TIMES = new int[][]{
            {15, 17, 20, 25, 30, 40},
            {40, 45, 50, 55, 60, 75}
    };
    public static final int[][] FS_3SETS_WEIGHT = new int[][]{
            {1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1}
    };
    public static final int[][] FS_4SETS_TIMES = new int[][]{
            {15, 17, 20, 22, 25, 30, 35, 40},
            {40, 45, 50, 55, 60, 65, 70, 75}
    };
    public static final int[][] FS_4SETS_WEIGHT = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };

    @Override
    public long[][] getPayTable(KenoGameLogicBean gameLogicBean) {
        double[][] payTables = new double[][]{
                {0, 0, 7.4},
                {0, 0, 2, 12.4},
                {0, 0, 1, 3, 33.6},
                {0, 0, 0, 2, 12, 168},
                {0, 0, 0, 1, 5, 31, 375},
                {0, 0, 0, 1, 3, 5, 50, 708},
                {0, 0, 0, 0, 2, 5, 40, 224, 3000},
                {0, 0, 0, 0, 1, 4, 12, 96, 632, 3200},
                {0, 0, 0, 0, 1, 3, 4, 22, 140, 1060, 3200},
        };
        long[][] payTableResult = computePayTables(payTables, gameLogicBean);
        return payTableResult;
    }

    protected long[][] computePayTables(double[][] payTables, KenoGameLogicBean gameLogicBean) {
        long[][] payTableResult = new long[payTables.length][];
        int index = 0;
        long totalBet = totalBet(gameLogicBean.getLines(), gameLogicBean.getBet());
        for (double[] tempPay : payTables) {
            payTableResult[index] = new long[tempPay.length];
            for (int i = 0; i < tempPay.length; i++) {
                payTableResult[index][i] = (long) (tempPay[i] * totalBet);
            }
            index++;
        }
        return payTableResult;
    }

    @Override
    public long minLines() {
        return 25;
    }

    @Override
    public long minBet() {
        return 1;
    }

    @Override
    public long maxLines() {
        return 25;
    }

    @Override
    public long maxBet() {
        return 8;
    }

    @Override
    public long totalBet(long lines, long bet) {
        return lines * bet;
    }

    @Override
    public int[] getAllRandomDigits() {
        return new int[]{
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
                44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
                58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
                72, 73, 74, 75, 76, 77, 78, 79, 80
        };
    }

    @Override
    protected int getRandomCount() {
        return 20;
    }

    @Override
    public int minSelectNumbersCount() {
        return 2;
    }

    @Override
    public int maxSelectNumbersCount() {
        return 10;
    }

    @Override
    protected int baseSetCount() {
        return 3;
    }

    @Override
    protected int fsSetCount() {
        return 1;
    }

    @Override
    protected int baseSetNumbersCount() {
        return 4;
    }

    @Override
    protected int fsSetNumbersCount() {
        return 3;
    }

    protected int[][] getFsTimes() {
        return FS_TIMES;
    }

    protected int[][] getFsWeight() {
        return FS_WEIGHT;
    }

    protected int[][] getFs3setsTimes() {
        return FS_3SETS_TIMES;
    }

    protected int[][] getFs3setsWeight() {
        return FS_3SETS_WEIGHT;
    }

    protected int[][] getFs4setsTimes() {
        return FS_4SETS_TIMES;
    }

    protected int[][] getFs4setsWeight() {
        return FS_4SETS_WEIGHT;
    }

    protected int getSetFsTimes(boolean isFsSet) {
        int fsTimes = 0;
        //fs all set4 fs times
        if (isFsSet) {
            fsTimes = RandomUtil.getRandomFromArrayWithWeight(getFsTimes()[1], getFsWeight()[1]);
        }  //base all set3 fs times
        else {
            fsTimes = RandomUtil.getRandomFromArrayWithWeight(getFsTimes()[0], getFsWeight()[0]);
        }
        return fsTimes;
    }

    @Override
    public int[][] mixHitOnAll3Sets() {
        int fs1 = RandomUtil.getRandomFromArrayWithWeight(getFs3setsTimes()[0], getFs3setsWeight()[0]);
        int fs2 = RandomUtil.getRandomFromArrayWithWeight(getFs3setsTimes()[1], getFs3setsWeight()[1]);
        return new int[][]{
                {0, 6, 7, 8, 9, 10},  //Hits
                {3, 4, 5, 0, 0, 0},  //Win Multiplier
                {0, 0, 0, fs1, fs2, 0},  //Free Games
                {0, 0, 0, 0, 0, 80000},  //Award
        };
    }

    @Override
    public int[][] mixHitOnAll4Sets() {
        int fs1 = RandomUtil.getRandomFromArrayWithWeight(getFs4setsTimes()[0], getFs4setsWeight()[0]);
        int fs2 = RandomUtil.getRandomFromArrayWithWeight(getFs4setsTimes()[1], getFs4setsWeight()[1]);
        return new int[][]{
                {0, 9, 10, 11, 12, 13},  //Hits
                {3, 4, 10, 0, 0, 0},  //Win Multiplier
                {0, 0, 0, fs1, fs2, 0},  //Free Games
                {0, 0, 0, 0, 0, 80000},  //Award
        };
    }

    @Override
    public long maxTotalPay() {
        return 80000;
    }

    @Override
    public int hitSetDefaultMul() {
        return 2;
    }


}
