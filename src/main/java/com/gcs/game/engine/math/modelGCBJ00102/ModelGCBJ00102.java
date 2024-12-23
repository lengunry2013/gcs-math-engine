package com.gcs.game.engine.math.modelGCBJ00102;

import com.gcs.game.engine.blackJack.model.BaseBlackJackModel;
import com.gcs.game.engine.blackJack.vo.BlackJackBetInfo;
import com.gcs.game.engine.blackJack.vo.BlackJackGameLogicBean;
import com.gcs.game.engine.blackJack.vo.BlackJackResult;

public class ModelGCBJ00102 extends BaseBlackJackModel {
    public static final long[] JACKPOT_PAY = new long[]{
            5, 50, 100, 250, 2500, 5000, 10000
    };

    @Override
    public long minBet() {
        return 1;
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
        return 3;
    }

    @Override
    public int cardDecks() {
        return 8;
    }

    @Override
    protected long[] getPayTable() {
        return JACKPOT_PAY;
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
        int index = computeSideBetIndex(blackJackResult);
        long jackpotPay = 0;
        if (index > 0) {
            jackpotPay = getPayTable()[index - 1] * betInfo.getJackpotBet();
        }
        return jackpotPay;
    }

}
