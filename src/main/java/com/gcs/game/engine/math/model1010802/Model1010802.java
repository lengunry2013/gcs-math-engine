package com.gcs.game.engine.math.model1010802;


import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IRespin;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model1010802 extends BaseSlotModel implements IRespin {

    private static final int WILD_SYMBOL = 1;
    public static final int SCATTER_SYMBOL = 12;
    private static final int H1_SYMBOL = 2;
    private static final int MAX_WILD_COUNT = 3;

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
                {0, 0, 0, 0, 0},    // 1->6
                {0, 0, 100, 200, 500},    // 2->5
                {0, 0, 50, 90, 250},  // 3->4
                {0, 0, 40, 70, 140},  // 4->3
                {0, 0, 30, 60, 120},  // 5->2

                {0, 0, 30, 50, 90},  //6->1
                {0, 0, 20, 40, 70},   //7->0
                {0, 0, 0, 0, 0},   //8
                {0, 0, 0, 0, 0},    //9
                {0, 0, 0, 0, 0},    //10

                {0, 0, 0, 0, 0},     //11
                {0, 0, 0, 0, 0},     //12
                {0, 0, 0, 0, 0},     //13
                {0, 0, 0, 0, 0},     //14
                {0, 0, 0, 0, 0}      // 15
        };
    }

    @Override
    protected String getPayLinesFileName() {
        return "G3_default_5x3x50_3.properties";
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
                {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}};
    }

    @Override
    protected void initGameSymbols() {
        initBaseSymbols(7, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        SlotFsSymbol symbol12 = new SlotFsSymbol();
        symbol12.setSymbolNumber(SCATTER_SYMBOL);
        symbol12.setMinHitCount(3);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_FREE_SPIN);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 0, 0, 0});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol12.setHitFsCounts(new int[]{0, 0, 10, 15, 20});
        symbols.add(symbol12);
    }

    @Override
    public long minBetPerLine() {
        return 100;
    }

    @Override
    public long maxBetPerLine() {
        return 100;
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
        return 100;
    }

    public long maxWin() {
        return 10000;
    }

    public int[] fsTimes() {
        return new int[]{10, 15, 20};
    }

    public int[] fsInFsTimes() {
        return new int[]{3, 7, 10, 12};
    }

    public int[] getBaseReelSetWeight() {
        return new int[]{75, 30, 35, 25, 140};
    }

    public int[][] getFsReelSetWeight() {
        return new int[][]{
                {50, 50, 50, 50, 50},   //3scatter
                {50, 50, 50, 50, 50},   //4scatter
                {550, 450, 200, 50, 25}   //5scatter
        };
    }

    public int[] getBaseIncreaseMul() {
        return new int[]{1, 2, 4, 8, 15, 30, 30};
    }

    public int[] getFsIncreaseMul() {
        return new int[]{1, 2, 4, 8, 15, 30, 50};
    }

    private static final int[][] WILD_REMOVE_POSITION = new int[][]{
            {1, 2, 3, 6, 7, 8}, {2, 3, 4, 7, 8, 9}, {3, 4, 5, 8, 9, 10}, {4, 5, 9, 10},  //Top
            {1, 2, 3, 6, 7, 8, 11, 12, 13}, {2, 3, 4, 7, 8, 9, 12, 13, 14}, {3, 4, 5, 8, 9, 10, 13, 14, 15}, {4, 5, 9, 10, 14, 15}, //Middle
            {6, 7, 8, 11, 12, 13}, {7, 8, 9, 12, 13, 14}, {8, 9, 10, 13, 14, 15}, {9, 10, 14, 15}  //Bottom
    };
    public static final String BASE_REELS_KEY = "base_R";
    public static final int DEFAULT_TYPE_REELS = 1;
    public static final String FREE_SPIN_REELS_KEY = "freespin_R";

    protected int[][] getReels(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(getBaseReelSetWeight());
        int reelsType = randomIndex + 1;
        gameLogicBean.setBaseReelsType(reelsType);
        int[][] reels = modelFeatureBean.getSlotReels();
        if (reelsType > DEFAULT_TYPE_REELS) {
            String baseKey = BASE_REELS_KEY + reelsType;
            reels = modelFeatureBean.getOtherSlotReelsMap().get(baseKey);
        }
        return reels;
    }

    protected int[][] getReelsWeight(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reelsWeight = modelFeatureBean.getSlotReelsWeight();
        int reelsType = gameLogicBean.getBaseReelsType();
        if (reelsType > DEFAULT_TYPE_REELS) {
            String baseKey = BASE_REELS_KEY + reelsType;
            reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(baseKey);
        }
        return reelsWeight;
    }

    protected int[][] getFSReels(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicCache) {
        boolean isBaseRepsin = isRespinInBaseGame(gameLogicCache);
        int[][] reels = modelFeatureBean.getSlotFsReels();
        if (isBaseRepsin) {
            int reelsType = gameLogicCache.getBaseReelsType();
            reels = modelFeatureBean.getSlotReels();
            if (reelsType > DEFAULT_TYPE_REELS) {
                String baseKey = BASE_REELS_KEY + reelsType;
                reels = modelFeatureBean.getOtherSlotReelsMap().get(baseKey);
            }
        } else {
            Model1010802SpinResult spinResult = (Model1010802SpinResult) gameLogicCache.getSlotSpinResult();
            int fsType = spinResult.getFsReelsType();
            if (fsType > DEFAULT_TYPE_REELS) {
                String fsKey = FREE_SPIN_REELS_KEY + fsType;
                reels = modelFeatureBean.getOtherSlotReelsMap().get(fsKey);
            }
        }
        return reels;
    }

    protected int[][] getFSReelsWeight(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicCache) {
        int[][] reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
        boolean isBaseRepsin = isRespinInBaseGame(gameLogicCache);
        if (isBaseRepsin) {
            int reelsType = gameLogicCache.getBaseReelsType();
            reelsWeight = modelFeatureBean.getSlotReelsWeight();
            if (reelsType > DEFAULT_TYPE_REELS) {
                String baseKey = BASE_REELS_KEY + reelsType;
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(baseKey);
            }
        } else {
            Model1010802SpinResult spinResult = (Model1010802SpinResult) gameLogicCache.getSlotSpinResult();
            int fsType = spinResult.getFsReelsType();
            if (fsType > DEFAULT_TYPE_REELS) {
                String fsKey = FREE_SPIN_REELS_KEY + fsType;
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(fsKey);
            }
        }
        return reelsWeight;
    }

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model1010802SpinResult result = new Model1010802SpinResult();
        boolean isBaseRespin = isRespinInBaseGame(gameLogicBean);
        setScatterMinHit(symbols, isSlot, isBaseRespin);
        //int[] oldDisplaySymbols = displaySymbols.clone();
        int[] respinPositions = refreshDisplaySymbols(gameLogicBean, displaySymbols);
        result.setSlotDisplaySymbols(displaySymbols);
        result.setRespinPositions(respinPositions);
        int[] finalSymbols = displaySymbols;
        int lastSpinMul = 1;
        int lastFsMul = 1;
        //TODO BaseGame simulation use
        int lastSpinMulLevel = 1;
        List<SlotSpinResult> fsList = gameLogicBean.getSlotFsSpinResults();
        //baseGame和fs中的respin
        if (gameLogicBean.isRespin()) {
            Model1010802SpinResult lastSpinResult = (Model1010802SpinResult) gameLogicBean.getSlotSpinResult();
            if (fsList != null && !fsList.isEmpty()) {
                lastSpinResult = (Model1010802SpinResult) fsList.get(fsList.size() - 1);
            }
            lastSpinMul = lastSpinResult.getRespinNextMul();
            lastSpinMulLevel = lastSpinResult.getRespinNextMulLevel();
            //fs的上一次乘积存在baseGame spin那次
            if (!isBaseRespin) {
                lastFsMul = ((Model1010802SpinResult) gameLogicBean.getSlotSpinResult()).getFsNextMul();
            }
        } else {
            //在fs中spin的乘积
            //compute Fs Next multiplier
            if (!isSlot) {
                Model1010802SpinResult spinResult = (Model1010802SpinResult) gameLogicBean.getSlotSpinResult();
                lastSpinMul = spinResult.getFsNextMul();
                lastFsMul = spinResult.getFsNextMul();
            }
        }
        // compute won
        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, finalSymbols, payLinesMap, isSlot);
        hitList = filterLineHit(hitList);

        boolean hasWin = false;
        if (hitList != null && hitList.size() > 0) {
            for (SlotSymbolHitResult hit : hitList) {
                long hitPay = hit.getHitPay();
                //respin pay都需要乘以乘积
                hit.setHitPay(hitPay * lastSpinMul);
                if (hit.getHitPay() > 0) {
                    hasWin = true;
                }
            }
        }
        if (isSlot) {
            result.setBaseGameMul(lastSpinMul);
        } else {
            result.setFsMul(lastSpinMul);
        }
        //转换hit list
        transferHitList(result, hitList, displaySymbols, stopPosition);
        //判断是否大于maxWin
        long totalWin = gameLogicBean.getSumWinCredit() + result.getSlotPay();
        boolean isMaxWin = false;
        long totalPayCal = maxWin() * totalBet(gameLogicBean.getLines(), gameLogicBean.getBet());
        if (totalWin >= totalPayCal) {
            result.setSlotPay(totalPayCal - gameLogicBean.getSumWinCredit());
            isMaxWin = true;
            //已经达到最大赢将不会触发fs,如果还有剩余画面也清空不会进入，如果有fs次数也清0
            result.setTriggerFs(false);
            result.setTriggerFsCounts(0);
            result.setNextScenes(null);
            gameLogicBean.setHitSceneLeftList(null);
            gameLogicBean.setFsCountLeft(0);
            gameLogicBean.setRespinCountsLeft(0);
        }
        if (!isMaxWin) {
            //compute wild
            List<Integer> wildPositionsOnReel = computeWildPosition(finalSymbols);
            int wildCount = wildPositionsOnReel.size();
            int respinTimes = 0;
            if (hasWin || wildCount > 0) {
                respinTimes = 1;
                lastSpinMul = computeNextMul(lastSpinMul, isSlot, isBaseRespin, lastFsMul, lastSpinMulLevel, result);
            } else {
                result.setRespinNextMulLevel(lastSpinMulLevel);
            }
            result.setRespinNextMul(lastSpinMul);

            //collect wild
            boolean isCollectTriggerFs = false;
            //只有fs才会收集wild
            if (!isSlot && !isBaseRespin) {
                Model1010802SpinResult baseSpinResult = (Model1010802SpinResult) gameLogicBean.getSlotSpinResult();
                long wildCollectCount = baseSpinResult.getCollectWild();
                if (wildCount > 0) {
                    wildCollectCount += wildCount;
                }
                //收集满3个wild触发添加fs一次和mulLevel+1
                if (wildCollectCount >= MAX_WILD_COUNT) {
                    isCollectTriggerFs = true;
                    for (int mul : getFsIncreaseMul()) {
                        if (lastFsMul < mul) {
                            lastFsMul = mul;
                            break;
                        }
                    }
                    //出respin后fs的每次乘积
                    baseSpinResult.setFsNextMul(lastFsMul);
                    wildCollectCount -= MAX_WILD_COUNT;
                }
                baseSpinResult.setCollectWild(wildCollectCount);
            }
            result.setTriggerCollectWild(isCollectTriggerFs);

            //只有在fs中会发生
            if (isCollectTriggerFs) {
                //如果fs in fs发生同时收集满3个wild
                if (result.isTriggerFs()) {
                    result.setTriggerFsCounts(result.getTriggerFsCounts() + 1);
                } else {
                    result.setTriggerFs(true);
                    result.setTriggerFsCounts(1);
                    List<String> nextScenes = new ArrayList<>();
                    nextScenes.add("freeSpin");
                    result.setNextScenes(nextScenes);
                }
            }
            //在baseGame或base Respin的时候触发了fs才会随机进入fs的reels
            if ((isSlot || isBaseRespin) && result.isTriggerFs()) {
                int scatterCount = getScatterCount(result);
                int[] fsReelsWeight = getFsReelSetWeight()[scatterCount - 3];
                int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(fsReelsWeight);
                int fsReelsType = randomIndex + 1;
                if (isSlot) {
                    result.setFsReelsType(fsReelsType);
                    result.setTriggerFsScatterCount(scatterCount);
                } else if (isBaseRespin) {
                    ((Model1010802SpinResult) gameLogicBean.getSlotSpinResult()).setFsReelsType(fsReelsType);
                    ((Model1010802SpinResult) gameLogicBean.getSlotSpinResult()).setTriggerFsScatterCount(scatterCount);
                }
            }

            if (respinTimes > 0) {
                computeRespin(result, respinTimes);
                // compute remain positions
                List<Integer> remainPositions = new ArrayList<>();
                //包含了5X3显示的所有位置
                for (int i = 0; i < reelsCount() * rowsCount(); i++) {
                    remainPositions.add(i);
                }
                if (hitList != null && !hitList.isEmpty()) {
                    for (SlotSymbolHitResult hit : hitList) {
                        int[] hitPositions = hit.getHitPosition();
                        if (hitPositions != null) {
                            for (int position : hitPositions) {
                                //如果赢的位置将被移除，重新替换新的symbol
                                if (remainPositions.contains(position - 1)) {
                                    remainPositions.remove(Integer.valueOf(position - 1));
                                }
                            }
                        }
                    }
                }
                List<Integer> wildRemovePosition = computeWildRemovePosition(wildPositionsOnReel, displaySymbols, hasWin);
                if (wildRemovePosition != null && !wildRemovePosition.isEmpty()) {
                    for (int position : wildRemovePosition) {
                        //如果wild周围的位置将被移除，重新替换新的symbol
                        if (remainPositions.contains(position - 1)) {
                            remainPositions.remove(Integer.valueOf(position - 1));
                        }
                    }
                }
                result.setRemainPositions(remainPositions);
                result.setWildPositionsOnReel(wildPositionsOnReel);
            }
        }

        return result;
    }

    protected int getScatterCount(Model1010802SpinResult result) {
        int scatterCount = 0;
        if (result != null) {
            int[] hitSymbol = result.getHitSlotSymbols();
            int[] hitSymbolCount = result.getHitSlotSymbolCount();
            if (hitSymbol != null && hitSymbol.length > 0) {
                for (int i = 0; i < hitSymbol.length; i++) {
                    if (hitSymbol[i] == SCATTER_SYMBOL) {
                        scatterCount = hitSymbolCount[i];
                        break;
                    }
                }
            }
        }
        return scatterCount;
    }

    protected SlotSymbolHitResult setHitResult(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int symbolNumber, long line, long betPerLine, int[] hitPosition, int hitCount, boolean inSlot) {
        SlotSymbolHitResult hitResult = super.setHitResult(gameLogicBean, symbol, symbolNumber, line, betPerLine, hitPosition, hitCount, inSlot);
        if (inSlot) {
            hitResult.setHitPay(symbol.getPay()[hitCount - 1]);
        } else {
            hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1]);
        }
        return hitResult;
    }

    private List<Integer> computeWildRemovePosition(List<Integer> wildPositionsOnReel, int[] displaySymbols, boolean hasWin) {
        List<Integer> removePositions = new ArrayList<>();
        if (wildPositionsOnReel != null && !wildPositionsOnReel.isEmpty()) {
            for (int position : wildPositionsOnReel) {
                int row = (position - 1) / reelsCount();
                int col = (position - 1) % reelsCount() - 1;  //相当于第2列开始
                int[] resultPosition = WILD_REMOVE_POSITION[row * 4 + col];
                for (int temp : resultPosition) {
                    //scatter位置不会被移除，还有当win=0的时候H1 Symbol也不会被移除
                    boolean isRemove = isRemoveSymbol(displaySymbols[temp - 1], hasWin);
                    if (isRemove) {
                        removePositions.add(temp);
                    }
                }
            }
        }
        return removePositions;
    }

    private boolean isRemoveSymbol(int symbol, boolean hasWin) {
        //scatter位置不会被移除，还有当win=0的时候H1 Symbol也不会被移除
        if (symbol == SCATTER_SYMBOL || (!hasWin && symbol == H1_SYMBOL)) {
            return false;
        }
        return true;
    }

    /**
     * compute respin multiplier
     *
     * @param lastMul
     * @param isSlot
     * @param isBaseRespin
     * @param lastFsMul
     * @param result
     * @return
     */
    private int computeNextMul(int lastMul, boolean isSlot, boolean isBaseRespin, int lastFsMul, int lastSpinMulLevel, Model1010802SpinResult result) {
        if (isSlot || isBaseRespin) {
            for (int i = 0; i < getBaseIncreaseMul().length; i++) {
                //上一次的乘积小于增加的乘积就会取下一个
                if (lastMul < getBaseIncreaseMul()[i]) {
                    lastMul = getBaseIncreaseMul()[i];
                    lastSpinMulLevel++;
                    break;
                } else if (i >= getBaseIncreaseMul().length - 1 && lastSpinMulLevel < getBaseIncreaseMul().length) {
                    lastSpinMulLevel++;
                    break;
                }
            }
            result.setRespinNextMulLevel(lastSpinMulLevel);
        } else {
            int[] fsMul = getFsIncreaseMul();
            for (int i = 0; i < fsMul.length; i++) {
                //multiplier level5解锁后level7才会被激活
                if (lastFsMul >= fsMul[4] && lastMul < fsMul[i]) {
                    lastMul = fsMul[i];
                    break;
                } else {
                    //只能激活到level6
                    if (lastMul < fsMul[i] && i < 6) {
                        lastMul = fsMul[i];
                        break;
                    }
                }
            }
        }
        return lastMul;
    }

    /**
     * refresh respin display symbols
     *
     * @param gameLogicBean
     * @param displaySymbols
     * @return
     */
    private int[] refreshDisplaySymbols(SlotGameLogicBean gameLogicBean, int[] displaySymbols) {
        List<SlotSpinResult> fsList = gameLogicBean.getSlotFsSpinResults();
        if (gameLogicBean.isRespin()) {
            Model1010802SpinResult lastSpinResult = (Model1010802SpinResult) gameLogicBean.getSlotSpinResult();
            if (fsList != null && !fsList.isEmpty()) {
                lastSpinResult = (Model1010802SpinResult) fsList.get(fsList.size() - 1);
            }
            int[] respinPositions;
            if (lastSpinResult.getRespinPositions() != null) {
                respinPositions = lastSpinResult.getRespinPositions().clone();
            } else {
                respinPositions = lastSpinResult.getSlotReelStopPosition().clone();
            }
            int[] lastDisplaySymbols = lastSpinResult.getSlotDisplaySymbols().clone();
            List<Integer> lastRemainPositions = lastSpinResult.getRemainPositions();
            if (lastRemainPositions != null && !lastRemainPositions.isEmpty()) {
                int reelsCount = reelsCount();
                int rowsCount = rowsCount();

                List<List<Integer>> remainSymbols = new ArrayList<>();
                for (int i = 0; i < reelsCount; i++) {
                    List<Integer> remainSymbolsPerReels = new ArrayList<>();
                    for (int j = rowsCount - 1; j >= 0; j--) {
                        if (lastRemainPositions.contains(j * reelsCount + i)) {
                            remainSymbolsPerReels.add(lastDisplaySymbols[j * reelsCount + i]);
                        }
                    }
                    remainSymbols.add(remainSymbolsPerReels);
                }

                for (int i = 0; i < reelsCount; i++) {
                    List<Integer> remainSymbolsPerReels = remainSymbols.get(i);
                    if (remainSymbolsPerReels != null && !remainSymbolsPerReels.isEmpty()) {
                        int tempIndex = 0;
                        for (int j = rowsCount - 1; j >= 0; j--) {
                            if (tempIndex < remainSymbolsPerReels.size()) {
                                int symbol = remainSymbolsPerReels.get(tempIndex);
                                displaySymbols[j * reelsCount + i] = symbol;
                            } else {
                                //空白位置补充的symbols
                                int reelsLength = this.currentReels[i].length;
                                respinPositions[i]--;
                                while (respinPositions[i] < 0) {
                                    respinPositions[i] += reelsLength;
                                }
                                int tempPositionIndex = respinPositions[i] - rowsCount / 2;
                                while (tempPositionIndex < 0) {
                                    tempPositionIndex += reelsLength;
                                }
                                int symbol = this.currentReels[i][tempPositionIndex];
                                displaySymbols[j * reelsCount + i] = symbol;
                            }
                            tempIndex++;
                        }
                    } else {
                        //整列都没有需要重新按照顺序补齐，不是去随机
                        for (int j = rowsCount - 1; j >= 0; j--) {
                            int reelsLength = this.currentReels[i].length;
                            respinPositions[i]--;
                            while (respinPositions[i] < 0) {
                                respinPositions[i] += reelsLength;
                            }
                            int tempPositionIndex = respinPositions[i] - rowsCount / 2;
                            while (tempPositionIndex < 0) {
                                tempPositionIndex += reelsLength;
                            }
                            int symbol = this.currentReels[i][tempPositionIndex];
                            displaySymbols[j * reelsCount + i] = symbol;
                        }
                    }
                }
            }
            return respinPositions;
        }
        return null;
    }

    /**
     * 计算wild symbol的位置
     *
     * @param finalSymbols
     * @return
     */
    protected List<Integer> computeWildPosition(int[] finalSymbols) {
        List<Integer> wildPositions = new ArrayList<>();
        for (int i = 0; i < finalSymbols.length; i++) {
            if (finalSymbols[i] == WILD_SYMBOL) {
                wildPositions.add(i + 1);
            }
        }
        return wildPositions;
    }

    /**
     * 设置scatter的最小hit count
     *
     * @param symbols
     * @param isSlot
     * @param isBaseRespin
     */
    protected void setScatterMinHit(List<SlotSymbol> symbols, boolean isSlot, boolean isBaseRespin) {
        for (SlotSymbol symbol : symbols) {
            if (symbol.getSymbolNumber() == SCATTER_SYMBOL) {
                if (isSlot || isBaseRespin) {
                    symbol.setMinHitCount(3);
                } else {
                    symbol.setMinHitCount(2);
                }
            }
        }
    }

    /**
     * 计算scatter symbol
     *
     * @param gameLogicBean
     * @param symbol
     * @param hitCount
     * @param hitResult
     * @param inSlot
     */
    protected void computeUnNormalSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int hitCount, SlotSymbolHitResult hitResult, boolean inSlot) {
        boolean isBaseRespin = isRespinInBaseGame(gameLogicBean);
        if (symbol.getSymbolNumber() == SCATTER_SYMBOL) {
            if (inSlot && hitCount >= 3) {
                // trigger freespin
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(fsTimes()[hitCount - 3]);
            } else if (!inSlot && hitCount >= 2) {
                //TODO baseGame也触发了Fs,同时BaseGame和respin都触发Fs是否需要另外处理,不清楚是否画面切换是否会出问题
                if (isBaseRespin && hitCount >= 3) {
                    hitResult.setTriggerFs(true);
                    hitResult.setTriggerFsCounts(fsTimes()[hitCount - 3]);
                } else {
                    // re-trigger fs
                    hitResult.setTriggerFs(true);
                    hitResult.setTriggerFsCounts(fsInFsTimes()[hitCount - 2]);
                }
            }
        }
    }

    @Override
    public int computeRespin(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot, SlotSpinResult spinResult) {
        return 0;
    }

    @Override
    public SlotSpinResult respin(SlotGameLogicBean gameLogicCache, int[] displaySymbols, int[] stopPosition, boolean isSlot) {
        return null;
    }

}
