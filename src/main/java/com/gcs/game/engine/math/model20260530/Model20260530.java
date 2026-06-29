package com.gcs.game.engine.math.model20260530;


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

public class Model20260530 extends BaseSlotModel {

    private static final int WILD_SYMBOL = 1;
    public static final int SCATTER_SYMBOL = 12;
    public static final int FS_SCATTER_SYMBOL = 13;
    public static final int L1_SYMBOL = 7;
    public static final int FS_TIMES = 10;
    public static final int[] SC_WEIGHT = new int[]{
            500, 500
    };
    public static final int[] BASE_SC_WEIGHT = new int[]{
            790, 210
    };
    public static final int[] BASE_WL_WEIGHT = new int[]{
            820, 180
    };
    public static final int[] BASE_WIN_WEIGHT = new int[]{
            600, 400
    };
    public static final int[][] FS_SC_PRIZE = new int[][]{
            {1, 2, 3, 5},
            {700, 100, 100, 100}
    };
    public static final int[] FS_SC_WEIGHT = new int[]{
            860, 140
    };
    public static final int[] FS_WL_WEIGHT = new int[]{
            600, 400
    };
    public static final int[][] FS_WL_MUL_WEIGHT = new int[][]{
            {1, 2, 3, 5},
            {5000, 1000, 3000, 1000}
    };
    public static final int[][] FS_WL_ADD_WEIGHT = new int[][]{
            {1, 2, 3, 4, 5, 6},
            {500, 1000, 1500, 4000, 2000, 1000}
    };
    public static final int[] FS_WIN_WEIGHT = new int[]{
            800, 200
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
        //SC1 pick bonus OR SC2 Fs Random
        SlotBonusSymbol symbol12 = new SlotBonusSymbol();
        symbol12.setSymbolNumber(12);
        symbol12.setMinHitCount(3);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BONUS);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 0, 0, 0});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol12.setHitFsCounts(new int[]{0, 0, 0, 0, 0});
        symbol12.setBonusAsset("bonus");
        symbols.add(symbol12);

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

    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = getReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getReelsWeight(modelFeatureBean, gameLogicBean);
            if (reels == null) {
                reels = modelFeatureBean.getSlotReels();
            }
            if (reelsWeight == null) {
                reelsWeight = modelFeatureBean.getSlotReelsWeight();
            }
            int[] stopPosition = randomReelStopPosition(reelsWeight);
            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = true;
            int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
            displaySymbols = getScChangeDisplaySymbols(displaySymbols);
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
        }
        return baseSpinResult;
    }

    private int[] getScChangeDisplaySymbols(int[] displaySymbols) {
        int[] newDisplaySymbols = displaySymbols.clone();
        int scatterCount = getSymbolCount(displaySymbols, SCATTER_SYMBOL);
        int scatterSymbol = SCATTER_SYMBOL;
        if (scatterCount > 0) {
            RandomWeightUntil randomWeightUntil = new RandomWeightUntil(SC_WEIGHT);
            int randomIndex = randomWeightUntil.getRandomResult();
            if (randomIndex == 1) {
                scatterSymbol = FS_SCATTER_SYMBOL;
            }
            for (int i = 0; i < newDisplaySymbols.length; i++) {
                if (newDisplaySymbols[i] == SCATTER_SYMBOL) {
                    newDisplaySymbols[i] = scatterSymbol;
                }
            }
        }
        return newDisplaySymbols;
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
        boolean isHitScatter = computeTriggerBonusOrFs(hitList);
        //trigger bonus or fs not all feature
        if (!isHitScatter) {
            hitList = computeFeature(hitList, gameLogicBean, displaySymbols, payLinesMap, isSlot, result);
        }
        //fs scatter prize
        if (!isSlot) {
            for (int i = 0; i < displaySymbols.length; i++) {
                if (displaySymbols[i] == FS_SCATTER_SYMBOL) {
                    RandomWeightUntil randomWeightUntil = new RandomWeightUntil(FS_SC_PRIZE[0], FS_SC_PRIZE[1]);
                    int scPrize = randomWeightUntil.getRandomResult();
                    SlotSymbolHitResult hit = setSc2Prize(scPrize, gameLogicBean, i + 1);
                    hitList.add(hit);
                }
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

    private boolean computeTriggerBonusOrFs(List<SlotSymbolHitResult> hitList) {
        if (!hitList.isEmpty()) {
            for (SlotSymbolHitResult hitResult : hitList) {
                if (hitResult.isTriggerBonus() || hitResult.isTriggerFs()) {
                    return true;
                }
            }
        }
        return false;
    }


    protected List<SlotSymbolHitResult> computeFeature(List<SlotSymbolHitResult> hitList, SlotGameLogicBean gameLogicBean, int[] displaySymbols, Map<Integer, int[]> payLinesMap, boolean isSlot, Model20260530SpinResult spinResult) {
        List<SlotSymbolHitResult> result = new ArrayList<>(hitList);
        boolean isLineWinFlag = false;
        long totalPay = computeTotalPay(hitList);
        if (totalPay > 0) {
            isLineWinFlag = true;
        }
        if (isSlot) {
            //feature 1 SC feature
            //boolean isScFlag = false;
            boolean isScFlag = computeScFeature(displaySymbols, result, spinResult, isLineWinFlag, isSlot, BASE_SC_WEIGHT);
            //feature 2 WL feature
            boolean isWildFlag = false;
            if (!isScFlag) {
                int wildCount = getSymbolCount(displaySymbols, WILD_SYMBOL);
                if (wildCount > 0) {
                    isWildFlag = computeWlExpand(gameLogicBean, displaySymbols, result, spinResult, totalPay, payLinesMap, isSlot, BASE_WL_WEIGHT);
                }
            }
            //feature 3：Win feature
            if (!isScFlag && !isWildFlag) {
                result = computeWinFeature(gameLogicBean, hitList, displaySymbols, payLinesMap, isSlot, spinResult, result, totalPay, BASE_WIN_WEIGHT);
            }
        } else {
            //Fs feature 1 SC feature
            boolean isScFlag = false;
            if (!isLineWinFlag) {
                int sc2Count = getSymbolCount(displaySymbols, FS_SCATTER_SYMBOL);
                if (sc2Count == 2) {
                    RandomWeightUntil randomWeightUntil = new RandomWeightUntil(FS_SC_WEIGHT);
                    int randomIndex = randomWeightUntil.getRandomResult();
                    spinResult.setFsScRandomIndex(randomIndex);
                    if (randomIndex == 1) {
                        int[] hitPosition = getSymbolPosition(displaySymbols, FS_SCATTER_SYMBOL);
                        setSc2Scatter(result, sc2Count + 1, hitPosition);
                        isScFlag = true;
                        spinResult.setFeatureType(1);
                    }
                }
            }
            //Fs feature 2 WL feature
            boolean isWildFlag = false;
            if (!isScFlag) {
                RandomWeightUntil randomWeightUntil = new RandomWeightUntil(FS_WL_WEIGHT);
                //int randomIndex = 1;
                int randomIndex = randomWeightUntil.getRandomResult();
                spinResult.setFsWlRandomIndex(randomIndex);
                //feature_WL_expand_decide` = 1
                if (randomIndex == 1) {
                    int wildCount = getSymbolCount(displaySymbols, WILD_SYMBOL);
                    //branch A：if wildnum > 0
                    if (wildCount > 0) {
                        //**Wild Expand**
                        isWildFlag = computeFsWlExpand(gameLogicBean, displaySymbols, result, spinResult, totalPay, payLinesMap, isSlot);
                    }
                    // branch B：`win_before > 0`
                    if (!isWildFlag && totalPay > 0) {
                        RandomWeightUntil randomWeightUntil1 = new RandomWeightUntil(FS_WL_MUL_WEIGHT[0], FS_WL_MUL_WEIGHT[1]);
                        int wildMul = randomWeightUntil1.getRandomResult();
                        spinResult.setWildMul(wildMul);
                        if (wildMul > 1) {
                            isWildFlag = true;
                            computeWildMul(result, wildMul);
                            spinResult.setFeatureType(3);
                        }
                    }
                    //branch C：multiplier = 1, or win_before = 0
                    if (!isWildFlag) {
                        RandomWeightUntil randomWeightUntil2 = new RandomWeightUntil(FS_WL_ADD_WEIGHT[0], FS_WL_ADD_WEIGHT[1]);
                        int wildAdd = randomWeightUntil2.getRandomResult();
                        //int wildAdd = 6;
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
                                isWildFlag = true;
                                result.clear();
                                result.addAll(hitResults);
                                spinResult.setSlotWildPositions(wildPositionArray);
                                spinResult.setFeatureType(4);
                            }
                        }
                    }
                }
            }
            //Fs feature 3：Win feature
            if (!isScFlag && !isWildFlag) {
                result = computeWinFeature(gameLogicBean, hitList, displaySymbols, payLinesMap, isSlot, spinResult, result, totalPay, FS_WIN_WEIGHT);
            }
        }
        return result;
    }

    private boolean computeFsWlExpand(SlotGameLogicBean gameLogicBean, int[] displaySymbols, List<SlotSymbolHitResult> result, Model20260530SpinResult spinResult, long totalPay, Map<Integer, int[]> payLinesMap, boolean isSlot) {
        boolean isWildFlag = false;
        int[] wildReels = getWildReels(displaySymbols, isSlot);
        int[] symbols = displaySymbols.clone();
        coverDisplaySymbolsByReels(symbols, wildReels, WILD_SYMBOL);
        List<SlotSymbolHitResult> hitResults = computeSymbols(gameLogicBean, symbols, payLinesMap, isSlot);
        hitResults = filterLineHit(hitResults);
        long wildAfterTotalPay = computeTotalPay(hitResults);
        if (wildAfterTotalPay > totalPay) {
            isWildFlag = true;
            result.clear();
            result.addAll(hitResults);
            spinResult.setSlotWildReels(wildReels);
            spinResult.setFeatureType(2);
        }
        return isWildFlag;
    }

    private void computeWildMul(List<SlotSymbolHitResult> result, int wildMul) {
        if (!result.isEmpty()) {
            for (SlotSymbolHitResult hit : result) {
                int symbolNumber = hit.getHitSymbol();
                if (symbolNumber < SCATTER_SYMBOL) {
                    hit.setHitPay(hit.getHitPay() * wildMul);
                    hit.setHitMul(wildMul);
                }
            }
        }
    }

    private boolean computeWlExpand(SlotGameLogicBean gameLogicBean, int[] displaySymbols, List<SlotSymbolHitResult> result, Model20260530SpinResult spinResult, long totalPay, Map<Integer, int[]> payLinesMap, boolean isSlot, int[] weight) {
        boolean isWildFlag = false;
        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(weight);
        int randomIndex = randomWeightUntil.getRandomResult();
        if (randomIndex == 1) {
            int[] wildReels = getWildReels(displaySymbols, isSlot);
            int[] symbols = displaySymbols.clone();
            coverDisplaySymbolsByReels(symbols, wildReels, WILD_SYMBOL);
            List<SlotSymbolHitResult> hitResults = computeSymbols(gameLogicBean, symbols, payLinesMap, isSlot);
            hitResults = filterLineHit(hitResults);
            long wildAfterTotalPay = computeTotalPay(hitResults);
            if (wildAfterTotalPay > totalPay) {
                isWildFlag = true;
                result.clear();
                result.addAll(hitResults);
                spinResult.setSlotWildReels(wildReels);
                spinResult.setFeatureType(2);
            }
        }
        if (isSlot) {
            spinResult.setBaseWlRandomIndex(randomIndex);
        } else {
            spinResult.setFsWlRandomIndex(randomIndex);
        }
        return isWildFlag;
    }

    private List<SlotSymbolHitResult> computeWinFeature(SlotGameLogicBean gameLogicBean, List<SlotSymbolHitResult> hitList, int[] displaySymbols, Map<Integer, int[]> payLinesMap, boolean isSlot, Model20260530SpinResult spinResult, List<SlotSymbolHitResult> result, long totalPay, int[] weight) {
        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(weight);
        int randomIndex = randomWeightUntil.getRandomResult();
        if (isSlot) {
            spinResult.setBaseWinRandomIndex(randomIndex);
        } else {
            spinResult.setFsWinRandomIndex(randomIndex);
        }
        if (randomIndex == 1) {
            long betPerLine = gameLogicBean.getBet();
            long lines = gameLogicBean.getLines();
            List<SlotSymbolHitResult> allSubSymbolHits = new ArrayList<>();
            for (SlotSymbol symbol : symbols) {
                //计算所有可扩展赢的集合
                if (symbol.getSymbolNumber() >= L1_SYMBOL && symbol.getSymbolNumber() < SCATTER_SYMBOL) {
                    List<SlotSymbolHitResult> tempList = computeLineSymbol2Left2Right(gameLogicBean, totalPay, symbol, displaySymbols, payLinesMap, betPerLine, lines, isSlot);
                    if (!tempList.isEmpty()) {
                        allSubSymbolHits.addAll(tempList);
                    }
                }
            }
            if (!allSubSymbolHits.isEmpty()) {
                int randomSubSymbolIndex = RandomUtil.getRandomInt(allSubSymbolHits.size());
                SlotSymbolHitResult hitResult = allSubSymbolHits.get(randomSubSymbolIndex);
                int line = hitResult.getHitLine();
                int symbolNumber = hitResult.getHitSymbol();
                int[] linePosition = payLinesMap.get(line);
                int hitCount = hitResult.getHitCount();
                int subSymbolPosition = linePosition[hitCount];
                int[] symbols = displaySymbols.clone();
                symbols[subSymbolPosition - 1] = symbolNumber;
                List<SlotSymbolHitResult> hitResults = computeSymbols(gameLogicBean, symbols, payLinesMap, isSlot);
                hitResults = filterLineHit(hitResults);
                long winAfterTotalPay = computeTotalPay(hitResults);
                if (winAfterTotalPay > totalPay) {
                    result.clear();
                    result.addAll(hitResults);
                    spinResult.setSubSymbol(symbolNumber);
                    spinResult.setSubSymbolPosition(subSymbolPosition);
                    if (isSlot) {
                        spinResult.setFeatureType(3);
                    } else {
                        spinResult.setFeatureType(5);
                    }

                }
            }
        }
        return result;

    }

    private boolean computeScFeature(int[] displaySymbols, List<SlotSymbolHitResult> result, Model20260530SpinResult spinResult, boolean isWinFlag, boolean isSlot, int[] weight) {
        boolean isScFlag = false;
        int featureType = -1;
        if (!isWinFlag) {
            int sc1Count = getSymbolCount(displaySymbols, SCATTER_SYMBOL);
            int sc2Count = getSymbolCount(displaySymbols, FS_SCATTER_SYMBOL);
            RandomWeightUntil randomWeightUntil = new RandomWeightUntil(weight);
            if (sc1Count == 2) {
                int randomIndex = randomWeightUntil.getRandomResult();
                if (randomIndex == 1) {
                    int[] hitPosition = getSymbolPosition(displaySymbols, SCATTER_SYMBOL);
                    setSc1Scatter(result, sc1Count + 1, hitPosition);
                    isScFlag = true;
                    featureType = 1;
                }
                spinResult.setBaseScRandomIndex(randomIndex);
            } else if (sc2Count == 2) {
                int randomIndex = randomWeightUntil.getRandomResult();
                if (randomIndex == 1) {
                    int[] hitPosition = getSymbolPosition(displaySymbols, FS_SCATTER_SYMBOL);
                    setSc2Scatter(result, sc2Count + 1, hitPosition);
                    isScFlag = true;
                    featureType = 1;
                }
                spinResult.setBaseScRandomIndex(randomIndex);
            }
        }
        spinResult.setFeatureType(featureType);
        return isScFlag;
    }

    protected List<SlotSymbolHitResult> computeLineSymbol2Left2Right(SlotGameLogicBean gameLogicBean, long beforeTotalWin, SlotSymbol symbol, int[] displaySymbols, Map<Integer, int[]> payLinesMap, long betPerLine, long lines, boolean isSlot) {
        List<SlotSymbolHitResult> resultList = new ArrayList<>();
        int symbolNumber = symbol.getSymbolNumber();
        int[] wildSymbols = symbol.getWildSymbols();
        for (int i = 0; i < lines; i++) {
            int line = i + 1;
            int[] linePosition = payLinesMap.get(line);
            int[] hitPosition = new int[linePosition.length];
            int hitCount = 0;
            for (int j = 0; j < linePosition.length; j++) {
                //baseGame and fs row different
                if (linePosition[j] > displaySymbols.length) {
                    break;
                }
                int tempSymbol = displaySymbols[linePosition[j] - 1];
                if (tempSymbol == symbolNumber) {
                    hitPosition[j] = linePosition[j];
                    hitCount += 1;
                } else if (wildSymbols != null && StringUtil.contains(wildSymbols, tempSymbol)) {
                    hitPosition[j] = linePosition[j];
                    hitCount += 1;
                } else {
                    break;
                }
            }
            if (hitCount >= 2 && hitCount <= 4) {
                SlotSymbolHitResult hitResult = setHitResult(gameLogicBean, symbol, symbolNumber, line, betPerLine, hitPosition, hitCount, isSlot);
                int reelPosition = linePosition[hitCount];
                boolean isAddHit = true;
                int[] symbols = displaySymbols.clone();
                symbols[reelPosition - 1] = symbolNumber;
                List<SlotSymbolHitResult> hitResults = computeSymbols(gameLogicBean, symbols, payLinesMap, isSlot);
                hitResults = filterLineHit(hitResults);
                long winAfterTotalPay = computeTotalPay(hitResults);
                //扩展替换图标后赢的结果小于扩展前的不能添加
                if (winAfterTotalPay <= beforeTotalWin) {
                    isAddHit = false;
                }
                //fs scatter symbol不能覆盖
                if (!isSlot && displaySymbols[reelPosition - 1] == FS_SCATTER_SYMBOL) {
                    isAddHit = false;
                }
                if (isAddHit) {
                    resultList.add(hitResult);
                }
            }
        }
        return resultList;
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

    private int[] getWildReels(int[] displaySymbols, boolean isSlot) {
        List<Integer> wildReels = new ArrayList<>();
        for (int i = 0; i < reelsCount(); i++) {
            for (int j = 0; j < rowsCount(); j++) {
                if (displaySymbols[i + j * reelsCount()] == WILD_SYMBOL) {
                    wildReels.add(i);
                }
            }
        }
        return StringUtil.ListToIntegerArray(wildReels);
    }

    private void setSc2Scatter(List<SlotSymbolHitResult> result, int scatterCount, int[] hitPosition) {
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(FS_SCATTER_SYMBOL);
        hitResult.setHitSymbolSound(FS_SCATTER_SYMBOL);
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitCount(scatterCount);
        hitResult.setHitPay(0);
        hitResult.setHitPosition(hitPosition);
        hitResult.setTriggerFs(true);
        hitResult.setTriggerFsCounts(FS_TIMES);
        result.add(hitResult);
    }

    protected int[] getSymbolPosition(int[] symbols, int symbolNo) {
        int[] hitPosition = new int[reelsCount()];
        int index = 0;
        if (symbols != null) {
            for (int i = 0; i < symbols.length; i++) {
                if (symbols[i] == symbolNo) {
                    hitPosition[index] = i + 1;
                    index++;
                }
            }
        }
        return hitPosition;
    }

    private void setSc1Scatter(List<SlotSymbolHitResult> result, int scatterCount, int[] hitPosition) {
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(SCATTER_SYMBOL);
        hitResult.setHitSymbolSound(SCATTER_SYMBOL);
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitCount(scatterCount);
        hitResult.setHitPay(0);
        hitResult.setHitPosition(hitPosition);
        hitResult.setTriggerBonus(true);
        hitResult.setBonusAsset("bonus");
        result.add(hitResult);
    }


}
