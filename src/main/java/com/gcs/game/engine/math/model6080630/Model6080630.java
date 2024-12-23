package com.gcs.game.engine.math.model6080630;

import com.gcs.game.engine.poker.model.BasePokerModel;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.utils.RandomWeightUntil;

import java.util.HashMap;
import java.util.Map;

public class Model6080630 extends BasePokerModel {
    protected static final int[] GOLD_CARD_WEIGHT = new int[]{59, 1};
    protected static final int[] GOLD_CARD_BONUS_WEIGHT = new int[]{
            5, 70, 25
    };

    public static final int[][] FS_TIMES_WEIGHT = new int[][]{
            {5, 10, 15},
            {100, 25, 5}
    };

    public static final int[][] INSTANT_CASH_PAY_WEIGHT = new int[][]{
            {5, 15, 25, 50, 100},
            {100, 25, 10, 4, 1}
    };


    protected static Map<String, RandomWeightUntil> goldBonusWeightUntilMap = new HashMap<>();

    @Override
    protected long[] getPayTable(PokerGameLogicBean gameLogicBean) {
        long bet = gameLogicBean.getBet();
        long maxPay = 80000;
        if (bet < 7) {
            maxPay = 12500 * bet;
        }
        long[] payTable = new long[]{0, maxPay, 1250, 500, 250, 125, 75, 50, 25, 15};
        for (int i = 2; i < payTable.length; i++) {
            payTable[i] *= bet;
        }
        return payTable;
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
    protected int cardDecks() {
        return 1;
    }

    @Override
    public long maxTotalPay() {
        return 80000;
    }

    @Override
    protected int handPokersCount() {
        return 5;
    }

    protected int[] getGoldCardWeight() {
        return GOLD_CARD_WEIGHT;
    }

    protected int[] getGoldCardBonusWeight() {
        return GOLD_CARD_BONUS_WEIGHT;
    }


    @Override
    protected boolean isGoldCard() {
        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(getGoldCardWeight());
        int randomIndex = randomWeightUntil.getRandomResult();
        return randomIndex == 1 ? true : false;
    }

    @Override
    protected int[] fsPokers() {
        return new int[]{0, 9, 10, 11, 12, 13, 22, 23, 24, 25, 26, 35, 36, 37, 38, 39, 48, 49, 50, 51};
    }

    @Override
    protected int[][] fsTimesWeight() {
        return FS_TIMES_WEIGHT;
    }

    @Override
    protected int[][] instantCashPayWeight() {
        return INSTANT_CASH_PAY_WEIGHT;
    }

    protected static Object syncObj = new Object();

    protected int getGoldBonusType(PokerGameLogicBean gameLogicBean) {
        RandomWeightUntil randomWeightUntil = null;
        synchronized (syncObj) {
            randomWeightUntil = goldBonusWeightUntilMap.get(gameLogicBean.getMmID() + "_" + gameLogicBean.getPercentage());
            if (randomWeightUntil == null) {
                randomWeightUntil = new RandomWeightUntil(getGoldCardBonusWeight());
                goldBonusWeightUntilMap.put(gameLogicBean.getMmID() + "_" + gameLogicBean.getPercentage(), randomWeightUntil);
            }
        }
        int random = randomWeightUntil.getRandomResult();
        return random + 1;
    }

}
