package com.gcs.game.engine.math.model20260201;


import com.gcs.game.engine.slots.bonus.BaseChoiceMatchBonus;

public class Model20260201Bonus extends BaseChoiceMatchBonus {

    /**
     * pick animation reward.
     */
    private static final long[] PICK_AWARDS = new long[]{
            8, 26, 66, 100
    };
    private static final int[] PICK_MUL = new int[]{
            1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2
    };

    private static final int[] PICK_AWARDS_WEIGHT = new int[]{
            2220, 1500, 550, 1000, 150, 100, 380, 2200, 800, 150, 500, 200, 200, 50
    };


    protected int getDisplayCharactersCount() {
        return 12;
    }

    protected int getCharactersCount() {
        return 4;
    }

    protected int getTriggerSymbolNumber() {
        return 12;
    }

    protected long[] getCharactersAwards(int payback, int hitSymbolCount) {
        return PICK_AWARDS.clone();
    }

    protected int getWildCharacter() {
        return 4;
    }

    protected int[] getAllCharacters() {
        return new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
    }

    protected int[] getBonusMultiplier() {
        return PICK_MUL;
    }

    protected int[] getPickAwardWeight() {
        return PICK_AWARDS_WEIGHT;
    }

}
