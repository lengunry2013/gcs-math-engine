package com.gcs.game.engine.math.model20260201;


import com.gcs.game.engine.slots.bonus.BaseChoiceMatchBonus;

public class Model20260201Bonus extends BaseChoiceMatchBonus {

    /**
     * pick animation reward.
     */
    private static final long[] PICK_AWARDS = new long[]{
            500, 2500, 12500, 25000
    };
    private static final int[] PICK_MUL = new int[]{
            1, 1, 1, 1
    };

    private static final int[][] PICK_AWARDS_WEIGHT = new int[][]{
            {8860, 650, 400, 90},
            {8860, 650, 400, 90},
            {7420, 1300, 1000, 280},
            {7420, 1300, 1000, 280},
            {5905, 2000, 1650, 445},
            {5905, 2000, 1650, 445},
            {2960, 4400, 2040, 600},
            {2960, 4400, 2040, 600},
            {2950, 3450, 2700, 900},
            {2950, 3450, 2700, 900},
            {3000, 2200, 3800, 1000},
            {3000, 2200, 3800, 1000},
            {3150, 2000, 2800, 2050},
            {3150, 2000, 2800, 2050},
            {2300, 2200, 3000, 2500},
            {2300, 2200, 3000, 2500},
            {1700, 2200, 3100, 3000},
            {1700, 2200, 3100, 3000},
            {1300, 2500, 2200, 4000},
            {1300, 2500, 2200, 4000},
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
        return 0;
    }

    protected int[] getAllCharacters() {
        return new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
    }

    protected int[] getBonusMultiplier() {
        return PICK_MUL;
    }

    protected int[][] getPickAwardWeight() {
        return PICK_AWARDS_WEIGHT;
    }

}
