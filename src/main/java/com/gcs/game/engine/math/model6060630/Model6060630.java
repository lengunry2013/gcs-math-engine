package com.gcs.game.engine.math.model6060630;

import com.gcs.game.engine.math.model6080630.Model6080630;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.utils.RandomWeightUntil;

public class Model6060630 extends Model6080630 {
    protected static final int[] GOLD_CARD_BONUS_WEIGHT = new int[]{
            20, 57, 23
    };

    public static final int[][] FS_TIMES_WEIGHT = new int[][]{
            {5, 10, 15},
            {50, 100, 45}
    };

    public static final int[][] INSTANT_CASH_PAY_WEIGHT = new int[][]{
            {5, 15, 25, 50, 100},
            {100, 25, 10, 4, 1}
    };

    public static final int[][] FS_MUL_WEIGHT = new int[][]{
            {2, 3, 5, 10},
            {50, 100, 50, 25}
    };

    protected long[] getPayTable(PokerGameLogicBean gameLogicBean) {
        long bet = gameLogicBean.getBet();
        long maxPay = 20000 * bet;
        if (maxPay > maxTotalPay()) {
            maxPay = 80000;
        }
        long[] payTable = new long[]{0, maxPay, 2000, 1000, 250, 200, 125, 50, 25, 0};
        for (int i = 2; i < payTable.length; i++) {
            payTable[i] *= bet;
        }
        return payTable;
    }

    protected int[] fsPokers() {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
                39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
        };
    }

    protected int[][] getFsMulWeight() {
        return FS_MUL_WEIGHT;
    }

    protected int getFsMultiplier(PokerGameLogicBean gameLogicCache) {
        int[][] fsMulWeight = getFsMulWeight();
        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(fsMulWeight[0], fsMulWeight[1]);
        int fsMul = randomWeightUntil.getRandomResult();
        return fsMul;
    }

}
