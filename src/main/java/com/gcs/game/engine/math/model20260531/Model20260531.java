package com.gcs.game.engine.math.model20260531;


import com.gcs.game.engine.math.model20260530.Model20260530SpinResult;
import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IRespin;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;

import java.util.*;

/**
 * Rabbit game
 */

public class Model20260531 extends BaseSlotModel {

    private static final int WILD_SYMBOL = 1;
    public static final int SCATTER_SYMBOL = 12;
    public static final int FS_SCATTER_SYMBOL = 13;
    public static final int FS_TIMES = 10;
    public static final int[][] FS_SC_PRIZE = new int[][]{
            {1, 2, 3, 5},
            {700, 100, 100, 100}
    };
    public static final int[][] FS_WL_ADD_WEIGHT = new int[][]{
            {1, 2, 3, 4, 5, 6},
            {500, 1000, 1500, 4000, 2000, 1000}
    };

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
                {0, 0, 0, 0, 0},    // 1
                {0, 0, 150, 300, 1000},  // 2
                {0, 0, 100, 250, 500},  // 3
                {0, 0, 60, 150, 300},  // 4
                {0, 0, 30, 75, 150},  // 5

                {0, 0, 20, 40, 80},  //6
                {0, 0, 20, 40, 80},   //7
                {0, 0, 15, 30, 60},   //8
                {0, 0, 15, 30, 60},    //9
                {0, 0, 10, 20, 40},    //10

                {0, 0, 10, 20, 40},     //11
                {0, 0, 0, 0, 0},     //12
                {0, 0, 0, 0, 0},     //13
                {0, 0, 0, 0, 0},     //14
                {0, 0, 0, 0, 0}      // 15
        };
    }

    @Override
    protected String getPayLinesFileName() {
        return "G3_default_5x3X25.properties";
    }

    @Override
    protected int[][] getWildSymbols() {
        return new int[][]{
                {},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {WILD_SYMBOL},
                {0}, {0}, {0}, {0}};
    }

    @Override
    protected void initGameSymbols() {
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        SlotFsSymbol symbol13 = new SlotFsSymbol();
        symbol13.setSymbolNumber(13);
        symbol13.setMinHitCount(3);
        symbol13.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_FREE_SPIN);
        symbol13.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol13.setWildSymbols(null);
        symbol13.setPay(new long[]{0, 0, 0, 0, 0});
        symbol13.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol13.setHitFsCounts(new int[]{0, 0, FS_TIMES, 0, 0});
        symbols.add(symbol13);
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
        return 25;
    }

    @Override
    public long maxLines() {
        return 25;
    }

    @Override
    public long totalBet(long lines, long betPerLine) {
        return 50 * betPerLine;
    }


    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260530SpinResult result = new Model20260530SpinResult();
        if (this instanceof IRespin && gameLogicBean.isRespin()) {
            IRespin respin = (IRespin) this;
            return respin.respin(gameLogicBean, displaySymbols, stopPosition, isSlot);
        }

        //link bonus and fs same trigger, first link bonus,second fs
        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);
        computeLineMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        hitList = filterLineHit(hitList);
        hitList = computeFeature(hitList, gameLogicBean, displaySymbols, payLinesMap, isSlot, result);
        //fs scatter prize
        for (int i = 0; i < displaySymbols.length; i++) {
            if (displaySymbols[i] == FS_SCATTER_SYMBOL) {
                RandomWeightUntil randomWeightUntil = new RandomWeightUntil(FS_SC_PRIZE[0], FS_SC_PRIZE[1]);
                int scPrize = randomWeightUntil.getRandomResult();
                SlotSymbolHitResult hit = setSc2Prize(scPrize, gameLogicBean, i + 1);
                hitList.add(hit);
            }
        }
        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = (Model20260530SpinResult) transferHitList(result, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            result.setBaseGameMul(baseGameMultiplier);
        }
        if (!isSlot) {
            result.setFsMul(freeSpinMultiplier);
        }
        int respinTimes = 0;
        if (this instanceof IRespin) {
            IRespin respin = (IRespin) this;
            respinTimes = respin.computeRespin(gameLogicBean, displaySymbols, isSlot, result);
        }
        computeRespin(result, respinTimes);
        return result;
    }

    private SlotSymbolHitResult setSc2Prize(int scPrize, SlotGameLogicBean gameLogicBean, int position) {
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(FS_SCATTER_SYMBOL);
        hitResult.setHitSymbolSound(FS_SCATTER_SYMBOL);
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitCount(1);
        hitResult.setHitPay(scPrize * gameLogicBean.getSumBetCredit());
        hitResult.setHitPosition(new int[]{position, 0, 0, 0, 0});
        return hitResult;
    }


    protected List<SlotSymbolHitResult> computeFeature(List<SlotSymbolHitResult> hitList, SlotGameLogicBean gameLogicBean, int[] displaySymbols, Map<Integer, int[]> payLinesMap, boolean isSlot, Model20260530SpinResult spinResult) {
        List<SlotSymbolHitResult> result = new ArrayList<>(hitList);
        long totalPay = computeTotalPay(hitList);
        RandomWeightUntil randomWeightUntil2 = new RandomWeightUntil(FS_WL_ADD_WEIGHT[0], FS_WL_ADD_WEIGHT[1]);
        int wildAdd = randomWeightUntil2.getRandomResult();
        spinResult.setFsWlAddRandomIndex(wildAdd);
        List<Integer> expandPosition = new ArrayList<>();
        //random select a position on reel 2,3,4 to become wild，**only add once**
        for (int i = 1; i < reelsCount() - 1; i++) {
            for (int j = 0; j < rowsCount(); j++) {
                int index = i + j * reelsCount();
                if (displaySymbols[index] != FS_SCATTER_SYMBOL && displaySymbols[index] != WILD_SYMBOL) {
                    expandPosition.add(index);
                }
            }
        }
        if (!expandPosition.isEmpty() && expandPosition.size() > wildAdd) {
            Collections.sort(expandPosition);
            int[] random = RandomUtil.getRandomIndex(expandPosition.size(), wildAdd);
            List<Integer> wildPosition = new ArrayList<>();
            if (random != null) {
                for (int index : random) {
                    wildPosition.add(expandPosition.get(index));
                }
            }
            int[] symbols = displaySymbols.clone();
            int[] wildPositionArray = StringUtil.ListToIntegerArray(wildPosition);
            Arrays.sort(wildPositionArray);
            coverDisplaySymbolsByPositions(symbols, wildPositionArray, WILD_SYMBOL);
            List<SlotSymbolHitResult> hitResults = computeSymbols(gameLogicBean, symbols, payLinesMap, isSlot);
            hitResults = filterLineHit(hitResults);
            long wildAfterTotalPay = computeTotalPay(hitResults);
            if (wildAfterTotalPay > totalPay) {
                result.clear();
                result.addAll(hitResults);
                spinResult.setSlotWildPositions(wildPositionArray);
                spinResult.setFeatureType(4);
            }
        }
        return result;
    }




    private long computeTotalPay(List<SlotSymbolHitResult> hitList) {
        long totalPay = 0;
        if (!hitList.isEmpty()) {
            for (SlotSymbolHitResult hit : hitList) {
                if (hit.getHitSymbol() < SCATTER_SYMBOL) {
                    totalPay += hit.getHitPay();
                }
            }
        }
        return totalPay;
    }

}
