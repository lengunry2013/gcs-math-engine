package com.gcs.game.engine.slots.model;

import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.utils.paylines.PayLinesBean;
import com.gcs.game.engine.slots.utils.paylines.PayLinesCachePool;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;

import java.util.*;

public abstract class BaseSlotModel {

    protected List<SlotSymbol> symbols = new ArrayList<>();

    protected int[][] currentReels = null;

    protected int[][] currentReelsWeight = null;

    protected int[] currentStopPosition = null;

    protected abstract int reelsCount();

    protected abstract int rowsCount();

    protected abstract long[][] getPayTable();

    protected abstract String getPayLinesFileName();

    protected abstract int[][] getWildSymbols();

    protected abstract void initGameSymbols();

    public BaseSlotModel() {
        initGameSymbols();
    }

    public abstract long minBetPerLine();

    public abstract long maxBetPerLine();

    public abstract long minLines();

    public abstract long maxLines();

    public abstract long totalBet(long lines, long betPerLine);

    public int getCardinalLineNumber4R2L() {
        return 0;
    }

    protected static Map<String, RandomWeightUntil> randomWeightUtilsMap = new HashMap<>();

    // protected static Object syncObj = new Object();

    protected RandomWeightUntil getRandomWeightUtil(String key, int[] array, int[] weight) {
        RandomWeightUntil random;
        // synchronized (syncObj) {
        random = randomWeightUtilsMap.get(key);
        if (random == null) {
            if (array == null) {
                random = new RandomWeightUntil(weight);
            } else {
                random = new RandomWeightUntil(array, weight);
            }
            randomWeightUtilsMap.put(key, random);
        }
        // }
        return random;
    }

    /**
     * get base game multiplier.
     *
     * @return
     */
    protected int getBaseGameMultiplier(int[] displaySymbols, SlotGameLogicBean gameLogicBean) {
        return 1;
    }

    /**
     * get free spin multiplier.
     *
     * @return
     */
    protected int getFreeSpinMultiplier(int[] displaySymbols, SlotGameLogicBean gameLogicBean) {
        return 1;
    }

    /**
     * init normal symbols.
     *
     * @param baseSymbolCount
     */
    protected void initBaseSymbols(int baseSymbolCount, int baseSymbolHitType) {
        int[][] wildSymbols = getWildSymbols();
        long[][] payTables = getPayTable();
        for (int i = 0; i < baseSymbolCount; i++) {
            int minHitCount = getMinHitCount4BaseSymbol(payTables[i]);
            SlotSymbol symbol = new SlotSymbol();
            symbol.setSymbolNumber(i + 1);
            symbol.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BASE);
            symbol.setSymbolHitType(baseSymbolHitType);
            if (wildSymbols != null) {
                symbol.setWildSymbols(wildSymbols[i]);
            }
            symbol.setPay(payTables[i]);
            symbol.setPayInFreeSpin(payTables[i]);
            symbol.setMinHitCount(minHitCount);
            symbols.add(symbol);
        }

    }

    /**
     * get min hit count.
     *
     * @param pay
     * @return
     */
    protected int getMinHitCount4BaseSymbol(long[] pay) {
        int count = -1;
        if (pay != null) {
            for (int i = 0; i < pay.length; i++) {
                if (pay[i] > 0) {
                    count = i + 1;
                    break;
                }
            }
        }
        return count;
    }

    /**
     * spin in slot.
     *
     * @param modelFeatureBean
     * @param gameLogicBean
     * @return
     */
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
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
        }
        return baseSpinResult;
    }

    /**
     * spin in slot with input.
     *
     * @param modelFeatureBean
     * @param gameLogicBean
     * @param inputFeedBean
     * @return
     */
    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean, InputInfo inputFeedBean) {
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
            int[] stopPosition = null;
            if (inputFeedBean != null && inputFeedBean.getInputPosition() != null && inputFeedBean.getInputPosition().size() > 0) {
                stopPosition = inputFeedBean.getInputPosition().get(0);
            }
            if (stopPosition == null || stopPosition.length <= 0) {
                stopPosition = randomReelStopPosition(reelsWeight);
            }
            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = true;
            int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
        }
        return baseSpinResult;
    }

    protected int[][] getReels(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reels = modelFeatureBean.getSlotReels();
        return reels;
    }

    protected int[][] getReelsWeight(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reelsWeight = modelFeatureBean.getSlotReelsWeight();
        return reelsWeight;
    }

    /**
     * spin in free spin.
     *
     * @param modelFeatureBean
     * @param gameLogicBean
     * @return
     */
    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {

            int[][] reels = getFSReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getFSReelsWeight(modelFeatureBean, gameLogicBean);
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

            boolean isSlot = false;
            int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
        }
        return baseSpinResult;
    }

    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean, InputInfo inputFeedBean) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = getFSReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getFSReelsWeight(modelFeatureBean, gameLogicBean);
            if (reels == null) {
                reels = modelFeatureBean.getSlotReels();
            }
            if (reelsWeight == null) {
                reelsWeight = modelFeatureBean.getSlotReelsWeight();
            }

            int[] stopPosition = null;
            if (inputFeedBean != null && inputFeedBean.getInputPosition() != null && inputFeedBean.getInputPosition().size() > 0) {
                stopPosition = inputFeedBean.getInputPosition().get(0);
            }
            if (stopPosition == null || stopPosition.length <= 0) {
                stopPosition = randomReelStopPosition(reelsWeight);
            }

            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = false;
            int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
        }
        return baseSpinResult;
    }

    protected int[][] getFSReels(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reels = modelFeatureBean.getSlotFsReels();
        return reels;
    }

    protected int[][] getFSReelsWeight(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
        return reelsWeight;
    }

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        SlotSpinResult baseSpinResult;
        Map<Integer, int[]> payLinesMap = getPayLines();

        int[] oldDisplaySymbols = null;
        int[] wildReels = null;
        if (this instanceof IWildReelsChange) {
            oldDisplaySymbols = displaySymbols.clone();
            wildReels = ((IWildReelsChange) this).computeWildReels(gameLogicBean, displaySymbols, isSlot);
            int wildSymbolNo = ((IWildReelsChange) this).wildSymbolNo();
            coverDisplaySymbolsByReels(displaySymbols, wildReels, wildSymbolNo);
        }
        int[] wildPositions = null;
        if (this instanceof IWildPositionsChange) {
            oldDisplaySymbols = displaySymbols.clone();
            wildPositions = ((IWildPositionsChange) this).computeWildPositions(gameLogicBean, displaySymbols, isSlot);
            int wildSymbolNo = ((IWildPositionsChange) this).wildSymbolNo();
            coverDisplaySymbolsByPositions(displaySymbols, wildPositions, wildSymbolNo);
        }

        baseSpinResult = computeSpinResult(stopPosition, displaySymbols, payLinesMap, gameLogicBean, isSlot);
        if (baseSpinResult != null && wildReels != null) {
            baseSpinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            baseSpinResult.setSlotWildReels(wildReels);
        }
        if (baseSpinResult != null && wildPositions != null) {
            baseSpinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            baseSpinResult.setSlotWildPositions(wildPositions);
        }
        return baseSpinResult;
    }

    protected int[] coverDisplaySymbolsByReels(int[] displaySymbols, int[] reelsIndex, int coverSymbol) {
        if (reelsIndex != null) {
            int rowsCount = rowsCount();
            int reelsCount = reelsCount();
            for (int i = 0; i < reelsIndex.length; i++) {
                int wildReelIndex = reelsIndex[i];
                for (int j = 0; j < rowsCount; j++) {
                    displaySymbols[wildReelIndex + j * reelsCount] = coverSymbol;
                }
            }
        }
        return displaySymbols;
    }

    protected int[] coverDisplaySymbolsByPositions(int[] displaySymbols, int[] positionsIndex, int coverSymbol) {
        if (positionsIndex != null) {
            for (int i = 0; i < positionsIndex.length; i++) {
                displaySymbols[positionsIndex[i]] = coverSymbol;
            }
        }
        return displaySymbols;
    }

    /**
     * random reels stop position.
     *
     * @param weight
     * @return
     */
    protected int[] randomReelStopPosition(int[][] weight) {
        int[] position = null;
        if (weight != null) {
            position = new int[weight.length];
            for (int i = 0; i < weight.length; i++) {
                int index = RandomUtil.getRandomIndexFromArrayWithWeight(weight[i]);
                position[i] = index;
            }
        }
        return position;
    }

    /**
     * get display symbols by random position.
     *
     * @param reels
     * @param position
     * @return
     */
    public int[] getDisplaySymbols(int[][] reels, int[] position) {
        int rowsCount = rowsCount();
        int reelsCount = reelsCount();
        return getDisplaySymbols(reels, position, reelsCount, rowsCount);
    }

    /**
     * get display symbols by random position.
     *
     * @param reels
     * @param position
     * @return
     */
    public int[] getDisplaySymbols(int[][] reels, int[] position, int reelsCount, int rowsCount) {
        int[] symbols = new int[reelsCount * rowsCount];
        int temp = rowsCount / 2;
        for (int i = 0; i < reelsCount; i++) {
            int positionIndex = position[i];
            for (int j = 0; j < rowsCount; j++) {
                int index = positionIndex + j - temp;
                while (index < 0) {
                    index += reels[i].length;
                }
                while (index >= reels[i].length) {
                    index -= reels[i].length;
                }
                symbols[i + j * reelsCount] = reels[i][index];
            }
        }
        return symbols;
    }

    /**
     * read pay lines.
     *
     * @return
     */
    protected Map<Integer, int[]> getPayLines() {
        String fileName = getPayLinesFileName();
        PayLinesBean payLinesBean = PayLinesCachePool.getPayLines(fileName);
        if (payLinesBean != null) {
            return payLinesBean.getPaylinesMap();
        }
        return null;
    }

    protected Map<Integer, int[]> getPayLines(String fileName) {
        PayLinesBean payLinesBean = PayLinesCachePool.getPayLines(fileName);
        if (payLinesBean != null) {
            return payLinesBean.getPaylinesMap();
        }
        return null;
    }

    /**
     * compute spin result.
     *
     * @param stopPosition
     * @param displaySymbols
     * @param payLinesMap
     * @param gameLogicBean
     * @param isSlot
     * @return
     */
    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        SlotSpinResult result = new SlotSpinResult();
        if (this instanceof IRespin && gameLogicBean.isRespin()) {
            IRespin respin = (IRespin) this;
            return respin.respin(gameLogicBean, displaySymbols, stopPosition, isSlot);
        }

        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);

        hitList = filterLineHit(hitList);
        computeLineMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = transferHitList(result, hitList, displaySymbols, stopPosition);
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

    protected void computeRespin(SlotSpinResult result, int respinTimes) {
        if (respinTimes > 0) {
            result.setTriggerRespin(true);
            result.setTriggerRespinCounts(respinTimes);
            List<String> nextScenes = result.getNextScenes();
            if (nextScenes == null) {
                nextScenes = new ArrayList<>();
            }
            nextScenes.add("freeSpin");
            result.setNextScenes(nextScenes);
        }
    }

    protected List<SlotSymbolHitResult> computeSymbols(SlotGameLogicBean gameLogicBean, int[] displaySymbols, Map<Integer, int[]> payLinesMap, boolean isSlot) {
        long betPerLine = gameLogicBean.getBet();
        long lines = gameLogicBean.getLines();
        long totalBet = gameLogicBean.getSumBetCredit();
        List<SlotSymbolHitResult> hitList = new ArrayList<>();
        for (SlotSymbol symbol : symbols) {
            if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT) {
                List<SlotSymbolHitResult> tempList = computeLineSymbolLeft2Right(gameLogicBean, symbol, displaySymbols, payLinesMap, betPerLine, lines, isSlot);
                if (tempList != null && !tempList.isEmpty()) {
                    hitList.addAll(tempList);
                }
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_RIGHT2LEFT) {
                List<SlotSymbolHitResult> tempList = computeLineSymbolRight2Left(gameLogicBean, symbol, displaySymbols, payLinesMap, betPerLine, lines, isSlot);
                if (tempList != null && !tempList.isEmpty()) {
                    hitList.addAll(tempList);
                }
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_ADJACENT_LEFT2RIGHT) {
                List<SlotSymbolHitResult> tempList = computeAdjacentSymbolLeft2Right(gameLogicBean, symbol, displaySymbols, betPerLine, isSlot);
                if (tempList != null && !tempList.isEmpty()) {
                    hitList.addAll(tempList);
                }
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_ADJACENT_RIGHT2LEFT) {
                List<SlotSymbolHitResult> tempList = computeAdjacentSymbolRight2Left(gameLogicBean, symbol, displaySymbols, betPerLine, isSlot);
                if (tempList != null && !tempList.isEmpty()) {
                    hitList.addAll(tempList);
                }
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER) {
                SlotSymbolHitResult hitResult = computeScatterSymbol(gameLogicBean, symbol, displaySymbols, totalBet, isSlot, false);
                if (hitResult != null) {
                    hitList.add(hitResult);
                }
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_ADJACENT_SCATTER) {
                List<SlotSymbolHitResult> hitResults = computeAdjacentScatterSymbol(symbol, displaySymbols, betPerLine, isSlot);
                if (hitResults != null) {
                    hitList.addAll(hitResults);
                }
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_ADJACENT_REELS_SCATTER) {
                SlotSymbolHitResult hitResult = computeScatterSymbol(gameLogicBean, symbol, displaySymbols, totalBet, isSlot, true);
                if (hitResult != null) {
                    hitList.add(hitResult);
                }
            }
        }
        return hitList;
    }

    /**
     * compute line multiplier.
     *
     * @param hitList
     * @param isSlot
     */
    protected void computeLineMultiplier(int[] displaySymbols, List<SlotSymbolHitResult> hitList, boolean isSlot, SlotGameLogicBean gameLogicBean) {
        // TODO
    }

    /**
     * compute base game multiplier.
     *
     * @param displaySymbols
     * @param hitList
     * @param isSlot
     * @return
     */
    protected int computeBaseGameMultiplier(int[] displaySymbols, List<SlotSymbolHitResult> hitList, boolean isSlot, SlotGameLogicBean gameLogicBean) {
        int baseGameMultiplier = 1;
        if (isSlot) {
            baseGameMultiplier = getBaseGameMultiplier(displaySymbols, gameLogicBean);
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult result : hitList) {
                    long hitPay = result.getHitPay();
                    result.setHitPay(hitPay * baseGameMultiplier);
                }
            }
        }
        return baseGameMultiplier;
    }

    /**
     * compute free spin multiplier.
     *
     * @param displaySymbols
     * @param hitList
     * @param isSlot
     * @return
     */
    protected int computeFreeSpinMultiplier(int[] displaySymbols, List<SlotSymbolHitResult> hitList, boolean isSlot, SlotGameLogicBean gameLogicBean) {
        int freeSpinMultiplier = 1;
        if (!isSlot) {
            freeSpinMultiplier = getFreeSpinMultiplier(displaySymbols, gameLogicBean);
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult result : hitList) {
                    long hitPay = result.getHitPay();
                    result.setHitPay(hitPay * freeSpinMultiplier);
                }
            }
        }
        return freeSpinMultiplier;
    }

    protected void computeMultiplier(List<SlotSymbolHitResult> hitList, boolean isSlot, int baseGameMultiplier, int freeSpinMultiplier) {
        if (hitList != null && !hitList.isEmpty()) {
            for (SlotSymbolHitResult result : hitList) {
                long hitPay = result.getHitPay();
                if (isSlot) {
                    result.setHitPay(hitPay * baseGameMultiplier);
                } else {
                    result.setHitPay(hitPay * freeSpinMultiplier);
                }
            }
        }
    }

    /**
     * compute line symbol from left to right.
     *
     * @param symbol
     * @param displaySymbols
     * @param payLinesMap
     * @param betPerLine
     * @param lines
     * @param inSlot
     * @return
     */
    protected List<SlotSymbolHitResult> computeLineSymbolLeft2Right(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, Map<Integer, int[]> payLinesMap, long betPerLine, long lines, boolean inSlot) {
        List<SlotSymbolHitResult> resultList = new ArrayList<>();
        int minHitCount = symbol.getMinHitCount();
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
            if (hitCount >= minHitCount && minHitCount > 0) {
                SlotSymbolHitResult hitResult = setHitResult(gameLogicBean, symbol, symbolNumber, line, betPerLine, hitPosition, hitCount, inSlot);
                resultList.add(hitResult);
            }
        }
        return resultList;
    }

    protected List<SlotSymbolHitResult> computeLineSymbolRight2Left(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, Map<Integer, int[]> payLinesMap, long betPerLine, long lines, boolean inSlot) {
        List<SlotSymbolHitResult> resultList = new ArrayList<>();
        int minHitCount = symbol.getMinHitCount();
        int symbolNumber = symbol.getSymbolNumber();
        int[] wildSymbols = symbol.getWildSymbols();
        int cardinalLine = getCardinalLineNumber4R2L();
        for (int i = 0; i < lines; i++) {
            int line = i + 1;
            int[] linePosition = payLinesMap.get(line);
            int[] hitPosition = new int[linePosition.length];
            int hitCount = 0;
            int index = 0;
            for (int j = linePosition.length - 1; j >= 0; j--) {
                //baseGame and fs row different
                if (linePosition[j] > displaySymbols.length) {
                    break;
                }
                int tempSymbol = displaySymbols[linePosition[j] - 1];
                if (tempSymbol == symbolNumber) {
                    hitPosition[index] = linePosition[j];
                    index++;
                    hitCount += 1;
                } else if (wildSymbols != null && StringUtil.contains(wildSymbols, tempSymbol)) {
                    hitPosition[index] = linePosition[j];
                    index++;
                    hitCount += 1;
                } else {
                    break;
                }
            }
            if (hitCount >= minHitCount && minHitCount > 0) {
                if (cardinalLine > 0) {
                    line += cardinalLine;
                }
                SlotSymbolHitResult hitResult = setHitResult(gameLogicBean, symbol, symbolNumber, line, betPerLine, hitPosition, hitCount, inSlot);
                resultList.add(hitResult);
            }
        }
        return resultList;
    }

    protected SlotSymbolHitResult setHitResult(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int symbolNumber, long line, long betPerLine, int[] hitPosition, int hitCount, boolean inSlot) {
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(symbolNumber);
        hitResult.setHitSymbolSound(symbolNumber);
        hitResult.setHitLine((int) line);
        hitResult.setHitMul(1);
        hitResult.setHitPosition(hitPosition);
        hitResult.setHitCount(hitCount);
        if (inSlot) {
            hitResult.setHitPay(symbol.getPay()[hitCount - 1] * betPerLine);
        } else {
            hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1] * betPerLine);
        }
        computeUnNormalSymbol(gameLogicBean, symbol, hitCount, hitResult, inSlot);
        return hitResult;
    }

    /**
     * compute bonus or free spin.
     *
     * @param gameLogicBean
     * @param symbol
     * @param hitCount
     * @param hitResult
     */
    protected void computeUnNormalSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int hitCount, SlotSymbolHitResult hitResult, boolean inSlot) {
        if (symbol.getSymbolType() == SlotEngineConstant.SYMBOL_TYPE_BONUS) {
            SlotBonusSymbol bonusSymbol = (SlotBonusSymbol) symbol;
            hitResult.setTriggerBonus(true);
            hitResult.setBonusAsset(bonusSymbol.getBonusAsset());
        } else if (symbol.getSymbolType() == SlotEngineConstant.SYMBOL_TYPE_FREE_SPIN) {
            SlotFsSymbol fsSymbol = (SlotFsSymbol) symbol;
            hitResult.setTriggerFs(true);
            hitResult.setTriggerFsCounts(fsSymbol.getHitFsCounts()[hitCount - 1]);
        } else if (symbol.getSymbolType() == SlotEngineConstant.SYMBOL_TYPE_BONUSINBG_FSINFS) {
            if (inSlot) {
                SlotBonusSymbol bonusSymbol = (SlotBonusSymbol) symbol;
                hitResult.setTriggerBonus(true);
                hitResult.setBonusAsset(bonusSymbol.getBonusAsset());
            } else {
                SlotBonusSymbol fsSymbol = (SlotBonusSymbol) symbol;
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(fsSymbol.getHitFsCounts()[hitCount - 1]);
            }
        } else if (symbol.getSymbolType() == SlotEngineConstant.SYMBOL_TYPE_BONUSINBGANDBGRESPIN_FSINFS) {
            boolean baseGameRespin = isRespinInBaseGame(gameLogicBean);
            if (inSlot || baseGameRespin) {
                SlotBonusSymbol bonusSymbol = (SlotBonusSymbol) symbol;
                hitResult.setTriggerBonus(true);
                hitResult.setBonusAsset(bonusSymbol.getBonusAsset());
            } else {
                SlotBonusSymbol fsSymbol = (SlotBonusSymbol) symbol;
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(fsSymbol.getHitFsCounts()[hitCount - 1]);
            }
        } else if (symbol.getSymbolType() == SlotEngineConstant.SYMBOL_TYPE_FREE_SPIN_ONLY_IN_BG) {
            if (inSlot) {
                SlotFsSymbol fsSymbol = (SlotFsSymbol) symbol;
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(fsSymbol.getHitFsCounts()[hitCount - 1]);
            }
        }
    }

    protected List<SlotSymbolHitResult> computeAdjacentSymbolLeft2Right(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, boolean inSlot) {
        int reelsCount = reelsCount();
        int rowsCount = rowsCount();
        return computeAdjacentSymbolLeft2Right(gameLogicBean, symbol, displaySymbols, betPerLine, inSlot, reelsCount, rowsCount);
    }

    /**
     * compute adjacent symbol from left to right.
     *
     * @param symbol
     * @param displaySymbols
     * @param betPerLine
     * @param inSlot
     * @return
     */
    protected List<SlotSymbolHitResult> computeAdjacentSymbolLeft2Right(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, boolean inSlot, int reelsCount, int rowsCount) {
        int symbolNumber = symbol.getSymbolNumber();
        int[] wildSymbols = symbol.getWildSymbols();

        List<List<Integer>> symbolPositionsOnReels = new ArrayList<>();
        for (int i = 0; i < reelsCount; i++) {
            boolean contains = false;
            List<Integer> symbolPositions = new ArrayList<>();
            for (int j = 0; j < rowsCount; j++) {
                int tempSymbol = displaySymbols[i + j * reelsCount];
                if (tempSymbol == symbolNumber || (wildSymbols != null && StringUtil.contains(wildSymbols, tempSymbol))) {
                    contains = true;
                    int position = i + j * reelsCount + 1;
                    symbolPositions.add(position);
                }
            }
            if (symbolPositions.size() > 0) {
                symbolPositionsOnReels.add(symbolPositions);
            }
            if (!contains) {
                break;
            }
        }
        return computeAdjacentSymbol(gameLogicBean, symbol, displaySymbols, betPerLine, inSlot, symbolPositionsOnReels);
    }

    protected List<SlotSymbolHitResult> computeAdjacentSymbolRight2Left(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, boolean inSlot) {
        int reelsCount = reelsCount();
        int rowsCount = rowsCount();
        return computeAdjacentSymbolRight2Left(gameLogicBean, symbol, displaySymbols, betPerLine, inSlot, reelsCount, rowsCount);
    }

    protected List<SlotSymbolHitResult> computeAdjacentSymbolRight2Left(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, boolean inSlot, int reelsCount, int rowsCount) {
        int symbolNumber = symbol.getSymbolNumber();
        int[] wildSymbols = symbol.getWildSymbols();

        List<List<Integer>> symbolPositionsOnReels = new ArrayList<>();
        for (int i = reelsCount - 1; i >= 0; i--) {
            boolean contains = false;
            List<Integer> symbolPositions = new ArrayList<>();
            for (int j = 0; j < rowsCount; j++) {
                int tempSymbol = displaySymbols[i + j * reelsCount];
                if (tempSymbol == symbolNumber || (wildSymbols != null && StringUtil.contains(wildSymbols, tempSymbol))) {
                    contains = true;
                    int position = i + j * reelsCount + 1;
                    symbolPositions.add(position);
                }
            }
            if (symbolPositions.size() > 0) {
                symbolPositionsOnReels.add(symbolPositions);
            }
            if (!contains) {
                break;
            }
        }
        return computeAdjacentSymbol(gameLogicBean, symbol, displaySymbols, betPerLine, inSlot, symbolPositionsOnReels);
    }

    protected List<SlotSymbolHitResult> computeAdjacentSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, boolean inSlot, List<List<Integer>> symbolPositionsOnReels) {
        int minHitCount = symbol.getMinHitCount();
        List<SlotSymbolHitResult> resultList = new ArrayList<>();
        if (symbolPositionsOnReels.size() >= minHitCount && minHitCount > 0) {
            int hitCount = symbolPositionsOnReels.size();
            int[] wildSymbols = symbol.getWildSymbols();

            int length = 1;
            for (int i = 0; i < symbolPositionsOnReels.size(); i++) {
                length *= symbolPositionsOnReels.get(i).size();
            }
            for (int i = 0; i < length; i++) {
                int[] hitPosition = new int[reelsCount()];
                int temp = 1;
                for (int j = 0; j < symbolPositionsOnReels.size(); j++) {
                    List<Integer> vector = symbolPositionsOnReels.get(j);
                    if (vector != null && !vector.isEmpty()) {
                        temp *= vector.size();
                        int tempIndex = i / (length / temp) % vector.size();
                        hitPosition[j] = vector.get(tempIndex);
                    }
                }
                // filter all hit symbol is wild
                boolean allWild = true;
                for (int m = 0; m < reelsCount(); m++) {
                    if (hitPosition[m] > 0) {
                        int tempSymbol = displaySymbols[hitPosition[m] - 1];
                        if (!StringUtil.contains(wildSymbols, tempSymbol)) {
                            allWild = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (allWild && !StringUtil.contains(wildSymbols, symbol.getSymbolNumber())) {
                    continue;
                }

                SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
                hitResult.setHitSymbol(symbol.getSymbolNumber());
                hitResult.setHitSymbolSound(symbol.getSymbolNumber());
                hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
                hitResult.setHitMul(1);
                hitResult.setHitPosition(hitPosition);
                hitResult.setHitCount(hitCount);
                if (inSlot) {
                    hitResult.setHitPay(symbol.getPay()[hitCount - 1] * betPerLine);
                } else {
                    hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1] * betPerLine);
                }
                computeUnNormalSymbol(gameLogicBean, symbol, hitCount, hitResult, inSlot);
                resultList.add(hitResult);
            }
        }
        return resultList;
    }

    protected SlotSymbolHitResult computeScatterSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long totalBet, boolean inSlot, boolean isAdjacent, int reelsCount, int rowsCount) {
        int minHitCount = symbol.getMinHitCount();
        int symbolNumber = symbol.getSymbolNumber();
        int[] wildSymbols = symbol.getWildSymbols();

        int hitCount = 0;
        int[] hitPosition = new int[reelsCount];
        int startPosition4Adjust = 0;
        while (startPosition4Adjust < reelsCount && (reelsCount - startPosition4Adjust) >= minHitCount) {
            int[] tempPosition = new int[reelsCount];
            int tempHitCount = 0;
            for (int i = startPosition4Adjust; i < reelsCount; i++) {
                boolean contains = false;
                for (int j = 0; j < rowsCount; j++) {
                    int tempSymbol = displaySymbols[i + j * reelsCount];
                    if (tempSymbol == symbolNumber || (wildSymbols != null && StringUtil.contains(wildSymbols, tempSymbol))) {
                        tempPosition[i] = i + j * reelsCount + 1;
                        contains = true;
                        break;
                    }
                }
                if (contains) {
                    tempHitCount++;
                    if (isAdjacent) {
                        startPosition4Adjust = i + 1;
                    }
                } else if (!contains && isAdjacent) {
                    startPosition4Adjust = i + 1;
                    break;
                }
            }
            if (!isAdjacent || (tempHitCount > hitCount)) {
                hitCount = tempHitCount;
                hitPosition = tempPosition.clone();
            }
            if (!isAdjacent) {
                break;
            }
        }

        if (hitCount >= minHitCount && minHitCount > 0) {
            SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
            hitResult.setHitSymbol(symbolNumber);
            hitResult.setHitSymbolSound(symbolNumber);
            hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
            hitResult.setHitMul(1);
            hitResult.setHitPosition(hitPosition);
            hitResult.setHitCount(hitCount);
            if (inSlot) {
                hitResult.setHitPay(symbol.getPay()[hitCount - 1] * totalBet);
            } else {
                hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1] * totalBet);
            }
            computeUnNormalSymbol(gameLogicBean, symbol, hitCount, hitResult, inSlot);
            return hitResult;
        }
        return null;
    }

    /**
     * compute scatter symbol.
     *
     * @param symbol
     * @param displaySymbols
     * @param totalBet
     * @param inSlot
     * @param isAdjacent
     * @return
     */
    protected SlotSymbolHitResult computeScatterSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long totalBet, boolean inSlot, boolean isAdjacent) {
        int reelsCount = reelsCount();
        int rowsCount = rowsCount();
        return computeScatterSymbol(gameLogicBean, symbol, displaySymbols, totalBet, inSlot, isAdjacent, reelsCount, rowsCount);
    }

    protected SlotSymbolHitResult computeAreaScatterSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, long lines, boolean inSlot, int reelsCount, int rowsCount) {
        int minHitCount = symbol.getMinHitCount();
        int symbolNumber = symbol.getSymbolNumber();
        int[] wildSymbols = symbol.getWildSymbols();

        int hitCount = 0;
        int tempIndex = 0;
        int[] hitPosition = new int[reelsCount * rowsCount];
        for (int i = 0; i < displaySymbols.length; i++) {
            if (symbolNumber == displaySymbols[i] || StringUtil.contains(wildSymbols, displaySymbols[i])) {
                hitCount++;
                hitPosition[tempIndex] = i + 1;
                tempIndex++;
            }
        }

        if (hitCount >= minHitCount && minHitCount > 0) {
            SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
            hitResult.setHitSymbol(symbolNumber);
            hitResult.setHitSymbolSound(symbolNumber);
            hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
            hitResult.setHitMul(1);
            hitResult.setHitPosition(hitPosition);
            hitResult.setHitCount(hitCount);
            if (inSlot) {
                hitResult.setHitPay(symbol.getPay()[hitCount - 1] * betPerLine);
            } else {
                hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1] * betPerLine);
            }
            return hitResult;
        }
        return null;
    }

    /**
     * compute scatter in scream.
     *
     * @param gameLogicBean
     * @param symbol
     * @param displaySymbols
     * @param betPerLine
     * @param lines
     * @param inSlot
     * @return
     */
    protected SlotSymbolHitResult computeAreaScatterSymbol(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, long lines, boolean inSlot) {
        int reelsCount = reelsCount();
        int rowsCount = rowsCount();
        return computeAreaScatterSymbol(gameLogicBean, symbol, displaySymbols, betPerLine, lines, inSlot, reelsCount, rowsCount);
    }

    protected List<SlotSymbolHitResult> computeAdjacentScatterSymbol(SlotSymbol symbol, int[] displaySymbols, long betPerLine, boolean inSlot) {
        List<SlotSymbolHitResult> hitList = new ArrayList<>();

        int minHitCount = symbol.getMinHitCount();
        int symbolNumber = symbol.getSymbolNumber();
        int[] wildSymbols = symbol.getWildSymbols();
        List<List<Integer>> adjacentPositionsList = computeAdjacentPosition(displaySymbols, symbolNumber, wildSymbols);
        if (adjacentPositionsList != null) {
            for (List<Integer> adjacentPositions : adjacentPositionsList) {
                if (adjacentPositions != null) {
                    int hitCount = adjacentPositions.size();
                    int[] hitPosition = StringUtil.ListToIntegerArray(adjacentPositions);
                    if (hitCount >= minHitCount && minHitCount > 0) {
                        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
                        hitResult.setHitSymbol(symbolNumber);
                        hitResult.setHitSymbolSound(symbolNumber);
                        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
                        hitResult.setHitMul(1);
                        hitResult.setHitPosition(hitPosition);
                        hitResult.setHitCount(hitCount);
                        if (inSlot) {
                            hitResult.setHitPay(symbol.getPay()[hitCount - 1] * betPerLine);
                        } else {
                            hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1] * betPerLine);
                        }
                        hitList.add(hitResult);
                    }
                }
            }
        }
        return hitList;

    }

    private List<List<Integer>> computeAdjacentPosition(int[] displaySymbols, int symbolNo, int[] wildSymbols) {
        List<List<Integer>> winPositionList = new ArrayList<>();
        int reelsCount = reelsCount();
        for (int i = 0; i < displaySymbols.length; i++) {
            int position = i + 1;
            boolean flag = contains(winPositionList, position);
            if (!flag) {
                List<Integer> positionList = new ArrayList<>();
                if (displaySymbols[i] == symbolNo || StringUtil.contains(wildSymbols, displaySymbols[i])) {
                    positionList.add(position);
                    computeSideKindOfSymbol(displaySymbols, positionList, symbolNo, i / reelsCount, i % reelsCount, wildSymbols);
                }
                if (positionList != null && !positionList.isEmpty()) {
                    sortList(positionList);
                    winPositionList.add(positionList);
                }
            }
        }
        return winPositionList;
    }

    private void sortList(List<Integer> positionList) {
        int size = positionList.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                int left = positionList.get(i);
                int right = positionList.get(j);
                if (left > right) {
                    int position = positionList.get(i);
                    positionList.set(i, positionList.get(j));
                    positionList.set(j, position);
                }
            }
        }
    }

    protected boolean contains(List<List<Integer>> array, int value) {
        boolean flag = false;
        if (array != null && !array.isEmpty()) {
            for (List<Integer> temp : array) {
                if (temp.contains(value)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private void computeSideKindOfSymbol(int[] displaySymbols, List<Integer> positionList, int symbolNo, int rowIndex, int columnIndex, int[] wildSymbols) {
        int rowCount = rowsCount();
        int reelCount = reelsCount();
        //row
        if (rowIndex == 0 || (rowIndex > 0 && rowIndex < rowCount - 1)) {
            int nextRowSymbol = displaySymbols[(rowIndex + 1) * reelCount + columnIndex];
            if (nextRowSymbol == symbolNo || StringUtil.contains(wildSymbols, nextRowSymbol)) {
                int position = (rowIndex + 1) * reelCount + columnIndex + 1;
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    computeSideKindOfSymbol(displaySymbols, positionList, symbolNo, rowIndex
                            + 1, columnIndex, wildSymbols);
                }
            }
        }
        if (rowIndex == rowCount - 1 || ((rowIndex > 0 && rowIndex < rowCount - 1))) {
            int lastRowSymbol = displaySymbols[(rowIndex - 1) * reelCount + columnIndex];
            if (lastRowSymbol == symbolNo || StringUtil.contains(wildSymbols, lastRowSymbol)) {
                int position = (rowIndex - 1) * reelCount + columnIndex + 1;
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    computeSideKindOfSymbol(displaySymbols, positionList, symbolNo, rowIndex - 1, columnIndex, wildSymbols);
                }
            }
        }
        //col
        if (columnIndex == 0 || (columnIndex > 0 && columnIndex < reelCount - 1)) {
            int nextColSymbol = displaySymbols[rowIndex * reelCount + columnIndex + 1];
            if (nextColSymbol == symbolNo || StringUtil.contains(wildSymbols, nextColSymbol)) {
                int position = rowIndex * reelCount + columnIndex + 2;
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    computeSideKindOfSymbol(displaySymbols, positionList, symbolNo, rowIndex, columnIndex + 1, wildSymbols);
                }
            }
        }
        if (columnIndex == reelCount - 1 || (columnIndex > 0 && columnIndex < reelCount - 1)) {
            int lastColSymbol = displaySymbols[rowIndex * reelCount + columnIndex - 1];
            if (lastColSymbol == symbolNo || StringUtil.contains(wildSymbols, lastColSymbol)) {
                int position = rowIndex * reelCount + columnIndex;
                if (!positionList.contains(position)) {
                    positionList.add(position);
                    computeSideKindOfSymbol(displaySymbols, positionList, symbolNo, rowIndex, columnIndex - 1, wildSymbols);
                }
            }
        }

    }

    /**
     * filter line hit.
     *
     * @param hitList
     * @return
     */
    protected List<SlotSymbolHitResult> filterLineHit(List<SlotSymbolHitResult> hitList) {
        return filterLineHit(hitList, SlotEngineConstant.SCATTER_HIT_LINE);
    }

    /**
     * filter line hit.
     *
     * @param hitList
     * @return
     */
    protected List<SlotSymbolHitResult> filterLineHit(List<SlotSymbolHitResult> hitList, int maxLine) {
        List<SlotSymbolHitResult> resultList = new ArrayList<>();
        if (hitList != null && !hitList.isEmpty()) {
            for (SlotSymbolHitResult hitResult : hitList) {
                if (hitResult != null) {
                    if (hitResult.isTriggerBonus() || hitResult.isTriggerFs() || hitResult.getHitLine() >= maxLine) {
                        resultList.add(hitResult);
                    } else {
                        int hitLine = hitResult.getHitLine();
                        SlotSymbolHitResult oldHit = findInList(resultList, hitLine);
                        if (oldHit == null) {
                            resultList.add(hitResult);
                        } else if (oldHit.getHitPay() < hitResult.getHitPay()) {
                            resultList.remove(oldHit);
                            resultList.add(hitResult);
                        }
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * find line hit in list.
     *
     * @param list
     * @param line
     * @return
     */
    protected SlotSymbolHitResult findInList(List<SlotSymbolHitResult> list, int line) {
        SlotSymbolHitResult result = null;
        if (list != null && !list.isEmpty()) {
            for (SlotSymbolHitResult hitResult : list) {
                if (hitResult != null && hitResult.getHitLine() == line) {
                    result = hitResult;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * transfer hit list to spin result.
     *
     * @param hitList
     * @param displaySymbols
     * @param reelStopPosition
     * @return
     */
    protected SlotSpinResult transferHitList(SlotSpinResult result, List<SlotSymbolHitResult> hitList, int[] displaySymbols, int[] reelStopPosition) {
        if (result != null) {
            result.setSlotReelStopPosition(reelStopPosition);
            result.setSlotDisplaySymbols(displaySymbols);

            if (hitList != null && !hitList.isEmpty()) {
                int size = hitList.size();

                int[] hitLines = new int[size];
                int[] hitSymbols = new int[size];
                int[] hitSymbolsSound = new int[size];
                int[] hitSymbolCount = new int[size];
                long[] hitPays = new long[size];
                int[] hitMultipliers = new int[size];
                int[][] hitPositions = new int[size][];
                int payAmount = 0;
                boolean triggerFreespin = false;
                int triggerFreespinTimes = 0;
                boolean triggerBonus = false;
                boolean triggerRespin = false;
                int triggerRespinTimes = 0;
                List<String> nextScenes = new ArrayList<>();

                int index = 0;
                for (SlotSymbolHitResult hitResult : hitList) {
                    if (hitResult != null) {
                        hitLines[index] = hitResult.getHitLine();
                        hitSymbols[index] = hitResult.getHitSymbol();
                        hitSymbolsSound[index] = hitResult.getHitSymbolSound();
                        hitPays[index] = hitResult.getHitPay();
                        hitSymbolCount[index] = hitResult.getHitCount();
                        hitMultipliers[index] = hitResult.getHitMul();
                        hitPositions[index] = hitResult.getHitPosition();
                        payAmount += hitResult.getHitPay();
                        if (hitResult.isTriggerBonus()) {
                            triggerBonus = true;
                            nextScenes.add(hitResult.getBonusAsset());
                            if (hitResult.getTriggerFsCounts() > 0) {
                                triggerFreespinTimes = hitResult.getTriggerFsCounts();
                            }
                        } else if (hitResult.isTriggerRespin()) {
                            triggerRespin = true;
                            triggerRespinTimes = hitResult.getTriggerRespinCounts();
                            nextScenes.add("freeSpin");
                        } else if (hitResult.isTriggerFs()) {
                            triggerFreespin = true;
                            triggerFreespinTimes = hitResult.getTriggerFsCounts();
                            nextScenes.add("freeSpin");
                        }
                        index++;
                    }
                }
                result.setHitSlotSymbols(hitSymbols);
                result.setHitSlotSymbolsSound(hitSymbolsSound);
                result.setHitSlotSymbolCount(hitSymbolCount);
                result.setHitSlotLines(hitLines);
                result.setHitSlotPays(hitPays);
                result.setHitSlotMuls(hitMultipliers);
                result.setHitSlotPositions(hitPositions);
                result.setSlotPay(payAmount);
                result.setTriggerBonus(triggerBonus);
                result.setTriggerFs(triggerFreespin);
                result.setTriggerFsCounts(triggerFreespinTimes);
                result.setNextScenes(nextScenes);
                result.setTriggerRespin(triggerRespin);
                result.setTriggerRespinCounts(triggerRespinTimes);
            }
        }
        return result;
    }

    protected int getSymbolCount(int[] symbols, int symbolNo) {
        int count = 0;
        if (symbols != null) {
            for (int symbol : symbols) {
                if (symbol == symbolNo) {
                    count++;
                }
            }
        }
        return count;
    }

    protected int getSymbolReelCount(int[] symbols, int symbolNo, int reelIndex) {
        int count = 0;
        int rowsCount = rowsCount();
        int reelsCount = reelsCount();
        for (int i = 0; i < rowsCount; i++) {
            if (symbols[reelsCount * i + reelIndex] == symbolNo) {
                count++;
            }
        }
        return count;
    }

    protected int getSymbolReelIndex(int[] symbols, int symbolNo, int reelIndex) {
        int index = -1;
        int rowsCount = rowsCount();
        int reelsCount = reelsCount();
        for (int i = 0; i < rowsCount; i++) {
            if (symbols[reelsCount * i + reelIndex] == symbolNo) {
                index = i;
                break;
            }
        }
        return index;
    }

    protected int getSymbolIndex(int[] symbols, int symbolNo) {
        int index = -1;
        if (symbols != null) {
            for (int i = 0; i < symbols.length; i++) {
                if (symbols[i] == symbolNo) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    protected int[] getSymbolIndexs(int[] symbols, int symbolNo) {
        List<Integer> index = new ArrayList<>();
        if (symbols != null) {
            for (int i = 0; i < symbols.length; i++) {
                if (symbols[i] == symbolNo) {
                    index.add(i);
                }
            }
        }
        return StringUtil.ListToIntegerArray(index);
    }

    protected int[] getNoWin2WonPosition(long lines, Map<Integer, int[]> payLinesMap, int[] displaySymbols) {
        List<Integer> wildPositions = new ArrayList<>();
        for (SlotSymbol symbol : symbols) {
            int symbolNumber = symbol.getSymbolNumber();
            int[] wildSymbols = symbol.getWildSymbols();
            // left to right, right to left
            for (int i = 0; i < lines; i++) {
                int line = i + 1;
                int[] linePosition = payLinesMap.get(line);
                int hitCount = 0;
                for (int j = 0; j < linePosition.length; j++) {
                    int tempSymbol = displaySymbols[linePosition[j] - 1];
                    if (tempSymbol == symbolNumber) {
                        hitCount += 1;
                    } else if (wildSymbols != null && StringUtil.contains(wildSymbols, tempSymbol)) {
                        hitCount += 1;
                    } else {
                        break;
                    }
                }
                if (hitCount == 2 && !wildPositions.contains(linePosition[2] - 1)) {
                    wildPositions.add(linePosition[2] - 1);
                }
            }
        }
        return StringUtil.ListToIntegerArray(wildPositions);
    }

    public static long getAverageBet(List<Long> collectBetCentList, int count) {
        long totalBet = 0L;
        List<Long> tmpList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            long item = collectBetCentList.get(i);
            totalBet += item;
            if (tmpList.indexOf(item * count) < 0) {
                tmpList.add(item * count);
            }
        }
        if (tmpList.size() == 1) {
            return collectBetCentList.get(0);
        }
        Collections.sort(tmpList);
        int index = 1;
        long minBet = tmpList.get(index - 1);
        long maxBet = tmpList.get(index);
        while (totalBet > maxBet) {
            index++;
            minBet = tmpList.get(index - 1);
            maxBet = tmpList.get(index);
        }
        if (totalBet == minBet || totalBet == maxBet) {
            return totalBet / count;
        }
        long[] weight = {totalBet - minBet, maxBet - totalBet};
        int result = RandomUtil.getRandomIndexFromArrayWithWeight(weight);
        if (result == 1) {
            return minBet / count;
        } else {
            return maxBet / count;
        }
    }

    protected int getNextLevelMultiplier(int[] multiplierLevels, int currentMultiplier) {
        int result;
        if (currentMultiplier <= 1) {
            result = multiplierLevels[0];
        } else {
            result = currentMultiplier;
            for (int i = 0; i < multiplierLevels.length; i++) {
                if (currentMultiplier < multiplierLevels[i]) {
                    result = multiplierLevels[i];
                    break;
                }
            }
        }
        return result;
    }

    public boolean isRespinInBaseGame(SlotGameLogicBean gameLogicBean) {
        boolean baseGameRespin = false;
        if (gameLogicBean.isRespin()) {
            if (gameLogicBean.getSlotFsSpinResults() == null || gameLogicBean.getSlotFsSpinResults().isEmpty()) {
                baseGameRespin = true;
            } else {
                SlotSpinResult lastSpin = gameLogicBean.getSlotFsSpinResults().get(gameLogicBean.getSlotFsSpinResults().size() - 1);
                if (lastSpin != null && lastSpin.getSpinType() == SlotEngineConstant.SPIN_TYPE_RESPIN_IN_BASE_GAME) {
                    baseGameRespin = true;
                }
            }
        }
        return baseGameRespin;
    }

    public boolean isRespinInFreeSpin(SlotGameLogicBean gameSessionBean) {
        boolean baseGameRespin = false;
        if (gameSessionBean.isRespin()) {
            if (gameSessionBean.getSlotFsSpinResults() == null || gameSessionBean.getSlotFsSpinResults().isEmpty()) {
                baseGameRespin = false;
            } else {
                SlotSpinResult lastSpin = gameSessionBean.getSlotFsSpinResults().get(gameSessionBean.getSlotFsSpinResults().size() - 1);
                if (lastSpin != null
                        && (lastSpin.getSpinType() == SlotEngineConstant.SPIN_TYPE_SPIN_IN_FREE_SPIN
                        || lastSpin.getSpinType() == SlotEngineConstant.SPIN_TYPE_RESPIN_IN_FREE_SPIN)) {
                    baseGameRespin = true;
                }
            }
        }
        return baseGameRespin;
    }

    protected int getLineSymbolCount(int[] displaySymbols, int[] hitPositions, int symbolNo) {
        int count = 0;
        if (hitPositions != null && displaySymbols != null) {
            for (int position : hitPositions) {
                if (position > 0 && displaySymbols[position - 1] == symbolNo) {
                    count++;
                }
            }
        }
        return count;
    }

}
