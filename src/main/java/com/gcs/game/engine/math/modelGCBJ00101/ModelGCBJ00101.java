package com.gcs.game.engine.math.modelGCBJ00101;

import com.gcs.game.engine.blackJack.model.BaseBlackJackModel;
import com.gcs.game.engine.blackJack.vo.BlackJackBetInfo;
import com.gcs.game.engine.blackJack.vo.BlackJackResult;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.engine.blackJack.vo.BlackJackGameLogicBean;

import java.util.HashMap;
import java.util.Map;

public class ModelGCBJ00101 extends BaseBlackJackModel {
    public static final int[] JACKPOT_WEIGHT = new int[]{
            1, 10, 100, 33000
    };
    public static final long[] JACKPOT_PAY = new long[]{
            1000000, 100000, 10000, 0
    };
    protected static Map<String, RandomWeightUntil> jackpotWeightUntilMap = new HashMap<>();

    @Override
    public long minBet() {
        return 10;
    }

    @Override
    public long maxBet() {
        return 10000;
    }

    @Override
    public int minHandsCount() {
        return 1;
    }

    @Override
    public int maxHandsCount() {
        return 5;
    }

    @Override
    public int cardDecks() {
        return 8;
    }

    @Override
    protected long[] getPayTable() {
        return JACKPOT_PAY;
    }

    protected int[] getJackpotWeight() {
        return JACKPOT_WEIGHT;
    }


    protected double getBlackJackPay() {
        return 2.5;
    }

    @Override
    protected int getPay() {
        return 2;
    }

    @Override
    protected int getInsurancePay() {
        return 3;
    }

    protected static Object syncObj = new Object();

    @Override
    protected long computeJackpotPay(BlackJackGameLogicBean gameLogicBean, BlackJackResult blackJackResult, BlackJackBetInfo betInfo) {
        RandomWeightUntil randomWeightUntil = null;
        synchronized (syncObj) {
            randomWeightUntil = jackpotWeightUntilMap.get(gameLogicBean.getMmID() + "_" + gameLogicBean.getPercentage());
            if (randomWeightUntil == null) {
                randomWeightUntil = new RandomWeightUntil(getJackpotWeight());
                jackpotWeightUntilMap.put(gameLogicBean.getMmID() + "_" + gameLogicBean.getPercentage(), randomWeightUntil);
            }
        }
        int random = randomWeightUntil.getRandomResult();
        long jackpotPay = getPayTable()[random] / gameLogicBean.getDenom();
        return jackpotPay;
    }

}
