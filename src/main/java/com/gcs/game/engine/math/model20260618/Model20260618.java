package com.gcs.game.engine.math.model20260618;


import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomWeightUntil;

import java.util.List;
import java.util.Map;

public class Model20260618 extends BaseSlotModel {

    protected static final int WILD_SYMBOL = 1;
    public static final int WR_FS = 1;
    public static final int SUPER_FS = 2;
    public static final String FREE_SPIN_REELS2_KEY = "freespin_SUPER";
    public static final int SCATTER_SYMBOL = 12;
    public static final int FS_TIME = 10;

    @Override
    protected int reelsCount() {
        return 5;
    }

    @Override
    protected int rowsCount() {
        return 4;
    }

    @Override
    protected long[][] getPayTable() {
        return new long[][]{
                {0, 10, 100, 500, 1000}, // 1
                {0, 0, 60, 200, 400},  // 2
                {0, 0, 50, 100, 300},  // 3
                {0, 0, 40, 100, 200},   // 4
                {0, 0, 30, 100, 120},   // 5

                {0, 0, 20, 40, 80},    // 6
                {0, 0, 16, 30, 60},    // 7
                {0, 0, 16, 30, 60},    // 8
                {0, 0, 10, 20, 40},     // 9
                {0, 0, 10, 20, 40},     // 10

                {0, 0, 0, 0, 0},      // 11
                {0, 0, 0, 0, 0},       // 12
                {0, 0, 0, 0, 0},       // 13
                {0, 0, 0, 0, 0},       // 14
                {0, 0, 0, 0, 0}        // 15;
        };
    }

    @Override
    protected String getPayLinesFileName() {
        return "G3_default_5x4x20.properties";
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
        initBaseSymbols(10, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        // init free spin symbol
        SlotBonusSymbol symbol12 = new SlotBonusSymbol();
        symbol12.setSymbolNumber(12);
        symbol12.setMinHitCount(3);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BASE);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 0, 0, 0});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol12.setHitFsCounts(new int[]{0, 0, 10, 10, 0});
        //symbol12.setBonusAsset("bonus");
        symbols.add(symbol12);
    }

    @Override
    public long minBetPerLine() {
        return 1;
    }

    @Override
    public long maxBetPerLine() {
        return 10;
    }

    @Override
    public long minLines() {
        return 20;
    }

    @Override
    public long maxLines() {
        return 20;
    }

    @Override
    public long totalBet(long lines, long betPerLine) {
        return 40 * betPerLine;
    }

    protected static int[] FS_INCREMENT_MUL = new int[]{2, 3, 4, 5, 10};
    public static final int[] WHEEL_BONUS_WEIGHT = new int[]{
            1138, 500, 200, 150, 100, 100, 100, 80, 30, 2, 1250, 1350
    };
    public static final int[] WHEEL_BONUS_AWARD = new int[]{
            5, 8, 10, 15, 20, 30, 25, 50, 200, 1000, 0, 0};
    public static final int[][] SCATTER_AWARD_WEIGHT = new int[][]{
            {5, 6, 8, 10, 12, 15},
            {1140, 500, 200, 100, 50, 10},
            {695, 600, 300, 250, 100, 55}
    };

    protected int[] getWheelBonusWeight() {
        return WHEEL_BONUS_WEIGHT;
    }

    protected int[][] getScatterAwardWeight() {
        return SCATTER_AWARD_WEIGHT;
    }

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260618SpinResult result = new Model20260618SpinResult();
        for (SlotSymbol symbol : symbols) {
            if (symbol.getSymbolNumber() == SCATTER_SYMBOL) {
                if (isSlot) {
                    symbol.setMinHitCount(3);
                } else {
                    symbol.setMinHitCount(2);
                }
            }
        }
        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);
        hitList = filterLineHit(hitList);

        int baseGameMultiplier = 1;
        int freeSpinMultiplier = 1;
        int fsType = -1;
        if (!isSlot) {
            fsType = ((Model20260618SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
            int scatterCount = 0;
            if (fsType == WR_FS) {
                freeSpinMultiplier = FS_INCREMENT_MUL[0];
                computeScatterPrize(hitList, gameLogicBean, fsType);
            } else if (fsType == SUPER_FS) {
                if (gameLogicBean.getSlotFsSpinResults() != null && !gameLogicBean.getSlotFsSpinResults().isEmpty()) {
                    SlotSpinResult lastFS = gameLogicBean.getSlotFsSpinResults().get(gameLogicBean.getSlotFsSpinResults().size() - 1);
                    freeSpinMultiplier = lastFS.getFsMul();
                } else {
                    freeSpinMultiplier = FS_INCREMENT_MUL[0];
                }
                scatterCount = computeScatterPrize(hitList, gameLogicBean, fsType);
                if (scatterCount <= 0) {
                    scatterCount = getSymbolCount(displaySymbols, SCATTER_SYMBOL);
                }
                if (scatterCount > 0) {
                    freeSpinMultiplier = getNextLevelMultiplier(FS_INCREMENT_MUL, freeSpinMultiplier);
                }
                result.setFsMul(freeSpinMultiplier);
            }
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult temp : hitList) {
                    int hitSymbol = temp.getHitSymbol();
                    long hitAmount = temp.getHitPay();
                    if (hitSymbol != SCATTER_SYMBOL) {
                        temp.setHitPay(hitAmount * freeSpinMultiplier);
                    }
                }
            }
        } else {
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult temp : hitList) {
                    int symbolNumber = temp.getHitSymbol();
                    if (symbolNumber == SCATTER_SYMBOL) {
                        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(getWheelBonusWeight());
                        int randomIndex = randomWeightUntil.getRandomResult();
                        long bonusWin = WHEEL_BONUS_AWARD[randomIndex] * totalBet(gameLogicBean.getLines(), gameLogicBean.getBet());
                        temp.setHitPay(bonusWin);
                        if (randomIndex == 10) {
                            fsType = WR_FS;
                            temp.setTriggerFs(true);
                            temp.setTriggerFsCounts(FS_TIME);
                        } else if (randomIndex == 11) {
                            fsType = SUPER_FS;
                            temp.setTriggerFs(true);
                            temp.setTriggerFsCounts(FS_TIME);
                        }
                        break;
                    }
                }
                result.setFsType(fsType);
            }
        }

        transferHitList(result, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            result.setBaseGameMul(baseGameMultiplier);
        } else {
            result.setFsMul(freeSpinMultiplier);
        }
        return result;
    }

    private int computeScatterPrize(List<SlotSymbolHitResult> hitList, SlotGameLogicBean gameLogicBean, int fsType) {
        int scatterCount = 0;
        if (hitList != null && !hitList.isEmpty()) {
            for (SlotSymbolHitResult hit : hitList) {
                int hitSymbol = hit.getHitSymbol();
                int hitCount = hit.getHitCount();
                if (hitSymbol == SCATTER_SYMBOL && hitCount == 2) {
                    scatterCount = hitCount;
                    if (fsType == WR_FS) {
                        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(getScatterAwardWeight()[0], getScatterAwardWeight()[1]);
                        int prize = randomWeightUntil.getRandomResult();
                        long scatterWin = prize * totalBet(gameLogicBean.getLines(), gameLogicBean.getBet());
                        hit.setHitPay(scatterWin);
                    } else if (fsType == SUPER_FS) {
                        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(getScatterAwardWeight()[0], getScatterAwardWeight()[2]);
                        int prize = randomWeightUntil.getRandomResult();
                        long scatterWin = prize * totalBet(gameLogicBean.getLines(), gameLogicBean.getBet());
                        hit.setHitPay(scatterWin);
                    }
                    hit.setTriggerFs(true);
                    hit.setTriggerFsCounts(FS_TIME);
                    break;
                }
            }
        }
        return scatterCount;
    }

    protected int[][] getFSReels(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reels = modelFeatureBean.getSlotFsReels();
        int fsType = ((Model20260618SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
        if (fsType == SUPER_FS) {
            reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS2_KEY);
        }
        return reels;
    }

    protected int[][] getFSReelsWeight(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
        int fsType = ((Model20260618SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
        if (fsType == SUPER_FS) {
            reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS2_KEY);
        }
        return reelsWeight;
    }

}
