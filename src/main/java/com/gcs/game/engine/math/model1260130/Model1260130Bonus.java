package com.gcs.game.engine.math.model1260130;

import com.gcs.game.engine.slots.bonus.BaseChoice2FsOrPickBonus;

public class Model1260130Bonus extends BaseChoice2FsOrPickBonus {

    @Override
    protected int[][] getFreeSpinTimes() {
        return new int[][]{{8}};
    }

    @Override
    protected int[][] getAwardValues() {
        return new int[][]{
                {15, 18, 20, 22, 25, 28, 30, 40, 50},
        };
    }

    @Override
    protected int[][] getAwardWeight() {
        return new int[][]{
                {50, 60, 32, 19, 15, 9, 5, 5, 5},   //3 scatter symbol
        };
    }

    @Override
    protected int[][] getMultiplierValues() {
        return new int[][]{
                {2, 3, 4, 5, 6, 7, 8},
        };
    }

    @Override
    protected int[][] getMultiplierWeight() {
        return new int[][]{
                {515, 257, 135, 75, 10, 5, 3},    //3 scatter symbol
        };
    }

    @Override
    protected int[] getPickCount() {
        return new int[]{4, 4, 5, 5, 6, 6, 7, 7, 7};
    }

    @Override
    protected int getTriggerSymbolNumber() {
        return 12;
    }

    protected int[][] getPickItems(int index) {
        switch (index) {
            case 0:
                return new int[][]{{3, 4, 5}, {3, 4}, {3, 4}};
            case 1:
                return new int[][]{{4, 5}, {3, 4, 5}, {3, 4, 5}};
            case 2:
                return new int[][]{{3, 4}, {3, 4}, {3, 4, 5}, {4, 5}};
            case 3:
                return new int[][]{{3, 4, 5}, {3, 4, 5}, {3, 4, 5}, {3, 4, 5}};
            case 4:
                return new int[][]{{3, 4}, {3, 4}, {3, 4, 5}, {3, 4, 5}, {3, 4, 5}};
            case 5:
                return new int[][]{{3, 4, 5}, {3, 4, 5}, {3, 4, 5}, {3, 4, 5}, {3, 4, 5}};
            case 6:
                return new int[][]{{3, 4, 5}, {3, 4, 5}, {3, 4}, {3, 4}, {3, 4}, {3, 4}};
            case 7:
                return new int[][]{{3, 4, 5}, {3, 4, 5}, {4, 5, 6}, {4, 5, 6}, {4, 5, 6}, {4, 5, 6}};
            case 8:
                return new int[][]{{5, 6, 8}, {5, 6, 8}, {5, 6, 8}, {5, 6, 8}, {5, 6, 8}, {5, 6, 8}};
            default:
                return new int[][]{{3, 4, 5}, {3, 4, 5}, {3, 4, 5}};
        }
    }

    protected int maxPickCount() {
        return 8;
    }

    protected boolean multiplierIconIsTerminator() {
        return true;
    }

}
