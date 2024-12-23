package com.gcs.game.engine.math.model6080630;

import com.gcs.game.engine.poker.bonus.PokerROrBBonus;

public class Model6080630Bonus extends PokerROrBBonus {
    public static final int[] ROUND_REWARD = new int[]{
            5, 10, 15, 25, 50, 100
    };

    @Override
    protected int[] getAward() {
        return ROUND_REWARD;
    }

    @Override
    protected int maxRound() {
        return 6;
    }
}
