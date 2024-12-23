package com.gcs.game.engine.math.model8140802;


import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IWildPositionsChange;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;

import java.util.Map;

public class Model8140802 extends BaseSlotModel implements IWildPositionsChange {

    private static final int WILD_SYMBOL = 1;

    @Override
    protected int reelsCount() {
        return 5;
    }

    @Override
    protected int rowsCount() {
        return 3;
    }

    @Override
    protected long[][] getPayTable() {
        return new long[][]{
                {0, 0, 0, 0, 0},     // 1
                {0, 0, 150, 250, 500}, // 2
                {0, 0, 30, 100, 250},  // 3
                {0, 0, 30, 100, 250},  // 4
                {0, 0, 20, 50, 100},  // 5

                {0, 0, 10, 20, 50},   //6
                {0, 0, 10, 20, 40},   //7
                {0, 0, 8, 15, 30},   //8
                {0, 0, 5, 10, 20},    //9
                {0, 0, 3, 5, 10},    //10

                {0, 0, 0, 0, 0},     //11
                {0, 0, 0, 0, 0},     //12
                {0, 0, 0, 0, 0},     //13
                {0, 0, 0, 0, 0},     //14
                {0, 0, 0, 0, 0}      // 15
        };
    }

    @Override
    protected String getPayLinesFileName() {
        return null;
    }

    @Override
    protected int[][] getWildSymbols() {
        return new int[][]{{
        }, {
                1}, {
                1}, {
                1}, {
                1}, {

                1}, {
                1}, {
                1}, {
                1}, {
                1}, {

                0}, {
                0}, {
                0}, {
                0}, {
                0}};
    }

    @Override
    protected void initGameSymbols() {
        initBaseSymbols(10, SlotEngineConstant.SYMBOL_HIT_TYPE_ADJACENT_LEFT2RIGHT);

        SlotFsSymbol symbol12 = new SlotFsSymbol();
        symbol12.setSymbolNumber(12);
        symbol12.setMinHitCount(2);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_FREE_SPIN);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 0, 0, 0});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol12.setHitFsCounts(new int[]{0, 5, 8, 0, 0});
        symbols.add(symbol12);
    }

    @Override
    public long minBetPerLine() {
        return 1;
    }

    @Override
    public long maxBetPerLine() {
        return 20;
    }

    @Override
    public long minLines() {
        return 50;
    }

    @Override
    public long maxLines() {
        return 50;
    }

    @Override
    public long totalBet(long lines, long betPerLine) {
        return lines * betPerLine;
    }

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        for (SlotSymbol symbol : symbols) {
            if (symbol.getSymbolNumber() == 12) {
                if (isSlot) {
                    symbol.setMinHitCount(3);
                } else {
                    symbol.setMinHitCount(2);
                }
            }
        }
        return super.computeSpinResult(stopPosition, displaySymbols, payLinesMap, gameLogicBean, isSlot);
    }


    protected void computeUnNormalSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int hitCount, SlotSymbolHitResult hitResult, boolean inSlot) {
        if (symbol.getSymbolNumber() == 12) {
            if (inSlot && hitCount >= 3) {
                // trigger freespin
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(8);
            } else if (!inSlot && hitCount >= 2) {
                // re-trigger fs
                SlotFsSymbol fsSymbol = (SlotFsSymbol) symbol;
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(fsSymbol.getHitFsCounts()[hitCount - 1]);
            }
        }
    }

    private static final int[][] BASE_LEVEL1_WEIGHT = new int[][]{
            {121, 124, 755},   //Top 95.50%
            {29, 5, 5, 4, 4, 4, 25, 24},  //Middle
            {120, 125, 755},  //Bottom

            {338, 335, 327},   //Top 96.00%
            {15, 10, 10, 11, 15, 10, 15, 14},  //Middle
            {326, 326, 348},  //Bottom

            {132, 137, 731},   //Top 96.50%
            {25, 5, 5, 5, 5, 5, 25, 25},  //Middle
            {128, 127, 745}  //Bottom
    };

    private static final int[][] BASE_WILD_POSITION = new int[][]{
            {3, 2, 4}, {3, 2, 8}, {3, 4, 8},  //Top
            {8, 3, 13}, {8, 7, 9}, {8, 2, 14}, {8, 4, 12}, {8, 7, 13}, {8, 3, 7}, {8, 9, 13}, {8, 3, 9}, //Middle
            {13, 12, 14}, {13, 8, 12}, {13, 8, 14}  //Bottom
    };

    public static final int[] FS_LEVEL_WEIGHT = new int[]{
            105, 55, 55, 55, 55,  //Top
            50, 50, 25, 25, 25, 25, 25, 50, 25, 50, //Middle
            105, 55, 55, 55, 55  //Bottom
    };
    private static final int[][] FS_WILD_POSITION = new int[][]{
            {3, 2, 4}, {3, 7, 8}, {3, 8, 9}, {3, 2, 8}, {3, 4, 8}, //Top
            {8, 3, 7, 9, 13}, {8, 2, 4, 12, 14}, {8, 2, 3, 4, 7}, {8, 2, 3, 4, 9}, {8, 7, 12, 13, 14},     //Middle
            {8, 9, 12, 13, 14}, {8, 2, 3, 7, 12}, {8, 3, 4, 9, 14}, {8, 2, 7, 12, 13}, {8, 4, 9, 13, 14},  //Middle
            {13, 12, 14}, {13, 7, 8}, {13, 8, 9}, {13, 8, 12}, {13, 8, 14}  //Bottom
    };

    private static RandomWeightUntil fsRandom = null;

    private int[] getBaseGameRandomWeight(int payback, int index) {
        int paybackIndex = 0;
        switch (payback) {
            case 9551:
                paybackIndex = 0;
                break;
            case 9601:
                paybackIndex = 1;
                break;
            case 9651:
                paybackIndex = 2;
                break;
            default:
                break;

        }
        return BASE_LEVEL1_WEIGHT[index + paybackIndex * 3];
    }

    @Override
    public int wildSymbolNo() {
        return WILD_SYMBOL;
    }

    @Override
    public int[] computeWildPositions(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot) {
        int[] wildPositions = null;
        if (isSlot) {
            int wildIndexOnMReel = getSymbolReelIndex(displaySymbols, WILD_SYMBOL, 2);
            if (wildIndexOnMReel >= 0) {
                int[] weight = getBaseGameRandomWeight(gameLogicBean.getPercentage(), wildIndexOnMReel);
                int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(weight);
                if (wildIndexOnMReel > 0) {
                    for (int i = 0; i < wildIndexOnMReel; i++) {
                        randomIndex += BASE_LEVEL1_WEIGHT[i].length;
                    }
                }
                wildPositions = BASE_WILD_POSITION[randomIndex].clone();
            }
        } else {
            if (fsRandom == null) {
                fsRandom = new RandomWeightUntil(FS_LEVEL_WEIGHT);
            }
            int randomIndex = fsRandom.getRandomResult();
            wildPositions = FS_WILD_POSITION[randomIndex].clone();

        }
        if (wildPositions != null) {
            for (int i = 0; i < wildPositions.length; i++) {
                wildPositions[i] -= 1;
            }
        }
        return wildPositions;
    }

}
