package com.gcs.game.engine.math.model20260825;

import cn.hutool.core.util.ObjectUtil;
import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IFsLinkBonusComputer;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;

import java.util.*;

/**
 * GoldRing Circus Game
 */
public class Model20260825 extends BaseSlotModel implements IFsLinkBonusComputer {

    public static final int WILD_SYMBOL = 1;

    public static final int SCATTER_SYMBOL = 12;

    public static final int SW1_SYMBOL = 13;
    public static final int SW2_SYMBOL = 14;
    public static final int SW3_SYMBOL = 15;
    private static RandomWeightUntil swRandom = null;
    public static final int[] SW_WEIGHT = new int[]{1, 1, 1};

    public static final int TRIGGER_SW = 1;
    public static final int RESPIN_TIMES = 3;
    public static final int SW_A = 1;
    public static final int SW_B = 2;
    public static final int SW_C = 3;
    public static final int SW_AB = 4;
    public static final int SW_AC = 5;
    public static final int SW_BC = 6;
    public static final int SW_ABC = 7;

    public static int[][] LINK_BALL_WEIGHT = new int[][]{
            {40, 500}, {1, 9999}, {40, 500}, {40, 500}, {40, 500}, {40, 500},
            {40, 500}, {40, 500}, {40, 500}, {40, 500}, {40, 500}, {40, 500},
            {40, 500}, {40, 500}
    };

    public static int[][] LINK_BALL_AWARDS = new int[][]{
            {1, 2, 3, 5, 10, 15, 20, 30, 50, 100},
            {6829, 2000, 1000, 100, 50, 10, 5, 3, 2, 1}
    };
    public static int[][] MULTI_WEIGHT = new int[][]{
            {2, 3, 5},
            {700, 200, 100}
    };
    public static int[] ADD_COL_WEIGHT = new int[]{920, 80};

    private static RandomWeightUntil linkAwardRandom = null;

    private Map<Integer, Integer> positionWeightMap = new HashMap<>();

    // ==================== static cache ====================
    private static Map<Integer, RandomWeightUntil> CACHED_LINK_RANDOM = new HashMap<>();

    static {
        // init link random
        for (int i = 0; i < LINK_BALL_WEIGHT.length; i++) {
            CACHED_LINK_RANDOM.put(i, new RandomWeightUntil(LINK_BALL_WEIGHT[i]));
        }
    }

    public Model20260825() {
        super();
    }

    public long minBetPerLine() {
        return 2;
    }

    public long maxBetPerLine() {
        return 20;
    }

    public long minLines() {
        return 25;
    }

    public long maxLines() {
        return 25;
    }

    public long totalBet(long lines, long betPerLine) {
        return lines * betPerLine;
    }

    protected int reelsCount() {
        return 5;
    }

    protected int rowsCount() {
        return 3;
    }

    public long[][] getPayTable() {
        return new long[][]{
                {0, 0, 0, 0, 0},  // 1
                {0, 0, 50, 150, 500},  // 2
                {0, 0, 40, 100, 400},  // 3
                {0, 0, 30, 75, 300},  // 4
                {0, 0, 20, 50, 200},    // 5

                {0, 0, 15, 30, 100},    // 6
                {0, 0, 10, 20, 40},    // 7
                {0, 0, 8, 15, 30},     // 8
                {0, 0, 8, 15, 30},     // 9
                {0, 0, 5, 10, 25},     // 10

                {0, 0, 5, 10, 25},      // 11
                {0, 0, 0, 0, 0},      // 12
                {0, 0, 0, 0, 0},      // 13
                {0, 0, 0, 0, 0},      // 14
                {0, 0, 0, 0, 0}       // 15
        };
    }

    protected String getPayLinesFileName() {
        return "G3_default_5x3X25_2.properties";
    }

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

                1}, {
                0}, {
                0}, {
                0}, {
                0}};
    }

    protected void initGameSymbols() {
        // init base symbols
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        // init Wheel Bonus
        SlotBonusSymbol symbol12 = new SlotBonusSymbol();
        symbol12.setSymbolNumber(SCATTER_SYMBOL);
        symbol12.setMinHitCount(3);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BONUS);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 0, 0, 0});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol12.setHitFsCounts(new int[]{0, 0, 0, 0, 0});
        symbol12.setBonusAsset("bonus");
        symbols.add(symbol12);
    }

    protected int[] getSw1Weight(int payBack) {
        int[] sw1TriggerWeight = new int[]{970, 30};
        switch (payBack) {
            case 8807:
                sw1TriggerWeight = new int[]{970, 30};
                break;
            case 9025:
                sw1TriggerWeight = new int[]{968, 32};
                break;
            default:
                break;
        }
        return sw1TriggerWeight;
    }

    protected int[] getSw2Weight(int payBack) {
        int[] sw2TriggerWeight = new int[]{923, 0, 0, 77};
        switch (payBack) {
            case 8807:
                sw2TriggerWeight = new int[]{923, 0, 0, 77};
                break;
            case 9025:
                sw2TriggerWeight = new int[]{920, 0, 0, 80};
                break;
            default:
                break;
        }
        return sw2TriggerWeight;
    }

    protected int[] getSw3Weight(int payBack) {
        int[] sw3TriggerWeight = new int[]{755, 0, 0, 0, 0, 0, 0, 245};
        switch (payBack) {
            case 8807:
                sw3TriggerWeight = new int[]{755, 0, 0, 0, 0, 0, 0, 245};
                break;
            case 9025:
                sw3TriggerWeight = new int[]{750, 0, 0, 0, 0, 0, 0, 250};
                break;
            default:
                break;
        }
        return sw3TriggerWeight;
    }

    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        Model20260825SpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = getReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getReelsWeight(modelFeatureBean, gameLogicBean);
            int[] stopPosition = null;
            boolean isSlot = true;
            //progresive jackpot Grand Daddy
            if (gameLogicBean.isHitGrandDaddy() && gameLogicBean.getHitJackpotLevel() == 5) {
                do {
                    stopPosition = randomReelStopPosition(reelsWeight);
                    int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
                    displaySymbols = transformSwSymbols(displaySymbols, isSlot, gameLogicBean);
                    baseSpinResult = (Model20260825SpinResult) computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
                } while (shouldContinueSpin(baseSpinResult));
            } else if (gameLogicBean.getHitJackpotLevel() > 0) {
                //progresive jackpot Mini,Minor,Major,BigDaddy
                do {
                    stopPosition = randomReelStopPosition(reelsWeight);
                    int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
                    displaySymbols = transformSwSymbols(displaySymbols, isSlot, gameLogicBean);
                    baseSpinResult = (Model20260825SpinResult) computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
                } while (!baseSpinResult.isTriggerBonus());
            } else {
                stopPosition = randomReelStopPosition(reelsWeight);
                int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
                displaySymbols = transformSwSymbols(displaySymbols, isSlot, gameLogicBean);
                baseSpinResult = (Model20260825SpinResult) computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
            }

            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;
        }
        return baseSpinResult;
    }

    private boolean shouldContinueSpin(Model20260825SpinResult result) {
        return result.getSlotPay() != 0
                || result.isTriggerFs()
                || !result.isTriggerBonus();
    }

    private int[] transformSwSymbols(int[] displaySymbols, boolean isSlot, SlotGameLogicBean gameLogicBean) {
        int[] newDisplaySymbols = displaySymbols.clone();
        if (isSlot) {
            boolean isSwSymbol = false;
            for (int symbol : displaySymbols) {
                if (symbol == SW1_SYMBOL) {
                    isSwSymbol = true;
                    break;
                }
            }
            //SW random SW change to SW1 or SW2 or SW3
            if (isSwSymbol) {
                if (swRandom == null) {
                    swRandom = new RandomWeightUntil(SW_WEIGHT);
                }
                for (int i = 0; i < displaySymbols.length; i++) {
                    if (displaySymbols[i] == SW1_SYMBOL) {
                        int swIndex = swRandom.getRandomResult();
                        if (swIndex == 1) {
                            newDisplaySymbols[i] = SW2_SYMBOL;
                        } else if (swIndex == 2) {
                            newDisplaySymbols[i] = SW3_SYMBOL;
                        }
                    }
                }
            }

        }
        return newDisplaySymbols;
    }

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260825SpinResult result = new Model20260825SpinResult();
        //link bonus only compute sw pays
        if (!isSlot) {
            return computeFsSwPay(gameLogicBean);
        }
        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);

        hitList = filterLineHit(hitList);
        if (isSlot) {
            computeTriggerSw(gameLogicBean, result, hitList, displaySymbols);
        }

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = (Model20260825SpinResult) transferHitList(result, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            result.setBaseGameMul(baseGameMultiplier);
        }
        if (!isSlot) {
            result.setFsMul(freeSpinMultiplier);
        }
        return result;
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
            hitResult.setHitPay(symbol.getPay()[hitCount - 1] * betPerLine / 2);
        } else {
            hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1] * betPerLine / 2);
        }
        computeUnNormalSymbol(gameLogicBean, symbol, hitCount, hitResult, inSlot);
        return hitResult;
    }

    /**
     * compute each SW fs result
     *
     * @param gameLogicBean
     * @return
     */
    private Model20260825SpinResult computeFsSwPay(SlotGameLogicBean gameLogicBean) {
        Model20260825SpinResult result = new Model20260825SpinResult();
        //The initial fs uses the spinResult of baseGame
        Model20260825SpinResult spinResult = (Model20260825SpinResult) gameLogicBean.getSlotSpinResult();
        //get base init weight
        initPositionWeightMap(spinResult.getLinkBonusSwPays().get(0));
        //get last fs spin result
        List<SlotSpinResult> fsSpinResult = gameLogicBean.getSlotFsSpinResults();
        if (fsSpinResult != null && !fsSpinResult.isEmpty()) {
            spinResult = (Model20260825SpinResult) fsSpinResult.get(fsSpinResult.size() - 1);
        }
        int swType = spinResult.getSwType();
        List<long[]> swPayList = StringUtil.deepCopyLongArrayList(spinResult.getLinkBonusSwPays());
        List<int[]> swMultis = StringUtil.deepCopyIntArrayList(spinResult.getSwMultis());
        List<Long> colPays = new ArrayList<>(spinResult.getColPays());
        List<Long> colTotalPays = new ArrayList<>(spinResult.getColTotalPays());
        List<int[]> colIcons = null;
        List<Boolean> isFullBalls = new ArrayList<>(spinResult.getIsFullBalls());
        List<Boolean> isGridTriggerFs = new ArrayList<>(spinResult.getIsGridTriggerFs());
        List<Integer> gridFsLeftCounts = new ArrayList<>(spinResult.getGridFsLeftCounts());
        boolean isTriggerFs = false;
        System.out.println("gridFsLeftCounts: " + gridFsLeftCounts);
        System.out.println("isFullBalls: " + isFullBalls);
        System.out.println("isTriggerFs: " + isTriggerFs);
        if (ObjectUtil.isNotEmpty(swPayList)) {
            colIcons = resetColIcons(swPayList);
            int swMul = 1;
            //contain SW_A
            if (containsSwA(swType)) {
                RandomWeightUntil randomWeightUntil = new RandomWeightUntil(MULTI_WEIGHT[0], MULTI_WEIGHT[1]);
                swMul = randomWeightUntil.getRandomResult();
            }
            int index = 0;
            for (long[] swPays : swPayList) {
                //tow grid one grid full and other grid not full
                boolean isFullBall = isFullBalls.get(index);
                int fsLeftTimes = gridFsLeftCounts.get(index);
                if (isFullBall || fsLeftTimes <= 0) {
                    index++;
                    continue;
                }
                colPays.set(index, 0L);
                int[] swMulti = swMultis.get(index);
                int currentBalls = computeBalls(swPays);
                int newBalls = randomLinkBalls(currentBalls, swType, swPays, swMulti, swMul, gameLogicBean);
                //contain SW_C compute Collect
                long colReward = 0;
                if (swType == SW_C || swType == SW_AC || swType == SW_BC || swType == SW_ABC) {
                    int remainBalls = swPays.length - newBalls;
                    if (remainBalls > 0) {
                        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(ADD_COL_WEIGHT);
                        int randomIndex = randomWeightUntil.getRandomResult();
                        if (randomIndex == 1) {
                            colReward = getColReward(swPays);
                            colPays.set(index, colReward);
                            colTotalPays.set(index, colTotalPays.get(index) + colReward);
                        }
                    }
                }
                //trigger collect
                if (colReward > 0) {
                    int[] colIcon = computeColIconInfo(swPays);
                    colIcons.set(index, colIcon);
                }
                //full grid
                if (newBalls == swPays.length) {
                    isFullBalls.set(index, true);
                    isFullBall = true;
                }
                if (!isFullBall) {
                    if (newBalls > currentBalls || colReward > 0) {
                        isGridTriggerFs.set(index, true);
                        gridFsLeftCounts.set(index, RESPIN_TIMES);
                        isTriggerFs = true;
                    } else {
                        int fsTimes = gridFsLeftCounts.get(index) - 1;
                        gridFsLeftCounts.set(index, fsTimes);
                        isGridTriggerFs.set(index, false);
                    }
                } else {
                    isGridTriggerFs.set(index, false);
                    gridFsLeftCounts.set(index, 0);
                }
                index++;
            }
            result.setSwType(swType);
            result.setSwMultis(swMultis);
            result.setLinkBonusSwPays(swPayList);
            result.setColIcons(colIcons);
            result.setColPays(colPays);
            result.setColTotalPays(colTotalPays);
            result.setIsFullBalls(isFullBalls);
            result.setIsGridTriggerFs(isGridTriggerFs);
            result.setGridFsLeftCounts(gridFsLeftCounts);
            //new balls or collect is trigger
            if (isTriggerFs) {
                result.setTriggerFs(true);
                result.setTriggerFsCounts(RESPIN_TIMES);
            }
        }
        return result;
    }

    //each fs spin reset collect icon
    private List<int[]> resetColIcons(List<long[]> swPayList) {
        List<int[]> colIcons = new ArrayList<>();
        for (long[] swPays : swPayList) {
            int[] colIcon = new int[swPays.length];
            colIcons.add(colIcon);
        }
        return colIcons;
    }

    private int[] computeColIconInfo(long[] swPays) {
        //0 is not collect,1 is collect icon
        int[] colIcon = new int[swPays.length];
        List<Integer> swPosition = new ArrayList<>();
        for (int i = 0; i < swPays.length; i++) {
            if (swPays[i] == 0) {
                swPosition.add(i);
            }
        }
        if (!swPosition.isEmpty()) {
            int randomIndex = RandomUtil.getRandomInt(swPosition.size());
            colIcon[swPosition.get(randomIndex)] = 1;
        }
        return colIcon;
    }

    private long getColReward(long[] swPays) {
        long colReward = 0;
        for (long pays : swPays) {
            if (pays > 0) {
                colReward += pays;
            }
        }
        return colReward;
    }

    private boolean containsSwA(int swType) {
        return swType == SW_A || swType == SW_AB || swType == SW_AC || swType == SW_ABC;
    }


    private boolean containsSwB(int swType) {
        return swType == SW_B || swType == SW_AB || swType == SW_BC || swType == SW_ABC;
    }

    private void initPositionWeightMap(long[] swPays) {
        positionWeightMap.clear();
        int weightIndex = 0;
        for (int i = 0; i < swPays.length; i++) {
            if (swPays[i] == 0) {
                // init position weight
                positionWeightMap.put(i, weightIndex % LINK_BALL_WEIGHT.length);
                weightIndex++;
            }
        }
    }

    private int randomLinkBalls(int currentBalls, int swType, long[] swPays, int[] swMulti, int swMul, SlotGameLogicBean gameLogicBean) {
        List<Integer> swPositions = new ArrayList<>();
        for (int i = 0; i < swPays.length; i++) {
            if (swPays[i] == 0) {
                Integer weightIndex = positionWeightMap.get(i);
                if (weightIndex == null) {
                    weightIndex = i % LINK_BALL_WEIGHT.length;
                    positionWeightMap.put(i, weightIndex);
                }
                RandomWeightUntil random = CACHED_LINK_RANDOM.get(weightIndex);
                // random=null handle
                if (random == null) {
                    random = new RandomWeightUntil(LINK_BALL_WEIGHT[weightIndex]);
                    CACHED_LINK_RANDOM.put(weightIndex, random);
                }
                int randomResult = random.getRandomResult();
                if (randomResult == 0) {
                    currentBalls++;
                    int awardIndex = linkAwardRandom.getRandomResult();
                    swPays[i] = LINK_BALL_AWARDS[0][awardIndex] * gameLogicBean.getSumBetCredit();
                    if (containsSwA(swType)) {
                        swPositions.add(i);
                    }
                }
            }
        }
        if (containsSwA(swType) && !swPositions.isEmpty()) {
            int swMulIndex = RandomUtil.getRandomInt(swPositions.size());
            int swPosition = swPositions.get(swMulIndex);
            swPays[swPosition] *= swMul;
            swMulti[swPosition] = swMul;
        }
        return currentBalls;
    }

    /**
     * compute current balls count
     *
     * @param swPays
     * @return
     */
    private int computeBalls(long[] swPays) {
        int count = 0;
        if (swPays != null) {
            for (long pay : swPays) {
                if (pay > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public boolean computeFsCountLeftWhileLinkBonus(SlotGameLogicBean gameLogicCache, SlotSpinResult spinResult) {
        boolean freeSpinComplete = false;
        Model20260825SpinResult fsSpinResult = (Model20260825SpinResult) spinResult;
        List<Integer> fsCountLeftList = gameLogicCache.getFsCountLeftList();
        if (fsCountLeftList == null || fsCountLeftList.isEmpty()) {
            fsCountLeftList = new ArrayList<>(fsSpinResult.getGridFsLeftCounts());
        } else {
            fsCountLeftList.clear();
            fsCountLeftList.addAll(fsSpinResult.getGridFsLeftCounts());
        }
        int freeSpinLeft = 0;
        for (int fsCountLeft : fsCountLeftList) {
            freeSpinLeft = Math.max(fsCountLeft, freeSpinLeft);
        }
        if (fsSpinResult.isTriggerFs()) {
            int[] freeSpinHitTimes = gameLogicCache.getFsHitCounts();
            int[] newFreeSpinHitTimes = new int[freeSpinHitTimes.length + 1];
            for (int i = 0; i < freeSpinHitTimes.length; i++) {
                newFreeSpinHitTimes[i] = freeSpinHitTimes[i];
            }
            newFreeSpinHitTimes[freeSpinHitTimes.length] = spinResult.getTriggerFsCounts();
            gameLogicCache.setFsHitCounts(newFreeSpinHitTimes);
        }
        if (freeSpinLeft <= 0) {
            freeSpinComplete = true;
        }
        System.out.println("freeSpinLeft: " + freeSpinLeft);
        System.out.println("freeSpinComplete: " + freeSpinComplete);
        gameLogicCache.setFsCountLeft(freeSpinLeft);
        gameLogicCache.setFsCountLeftList(fsCountLeftList);
        return freeSpinComplete;
    }

    @Override
    public void computeTotalPays(SlotGameLogicBean gameLogicCache, SlotSpinResult spinResult, Boolean freeSpinComplete) {
        //link bonus freespin complete
        if (freeSpinComplete) {
            Model20260825SpinResult fsSpinResult = (Model20260825SpinResult) spinResult;
            List<long[]> winPays = fsSpinResult.getLinkBonusSwPays();
            List<Long> colTotalPays = fsSpinResult.getColTotalPays();
            long winCredit = 0;
            //add all linkbonus pays
            if (ObjectUtil.isNotEmpty(winPays)) {
                for (long[] winPay : winPays) {
                    for (long pay : winPay) {
                        winCredit += pay;
                    }
                }
            }
            //add collect all pays
            if (ObjectUtil.isNotEmpty(colTotalPays)) {
                for (long colPay : colTotalPays) {
                    winCredit += colPay;
                }
            }
            long denom = gameLogicCache.getDenom();
            long winBalance = winCredit * denom;
            gameLogicCache.setSumWinCredit(gameLogicCache.getSumWinCredit() + winCredit);
            gameLogicCache.setSumWinBalance(gameLogicCache.getSumWinBalance() + winBalance);
            gameLogicCache.setPayForCurrentStep(winCredit);
        }
    }

    /**
     * Sw Symbol info
     */
    private static class SwSymbolInfo {
        boolean hasSwSymbol = false;
        int swCount = 0;
        int sw1Count = 0;
        int sw2Count = 0;
        int sw3Count = 0;
        int[] swPositions = new int[5]; // ReelsCount=5
        int[] swDisplaySymbols = new int[15];  //5X3
    }

    private void computeTriggerSw(SlotGameLogicBean gameLogicBean, Model20260825SpinResult result, List<SlotSymbolHitResult> hitList, int[] displaySymbols) {
        SwSymbolInfo swInfo = analyzeSwSymbols(displaySymbols);
        if (!swInfo.hasSwSymbol) {
            return;
        }
        if (linkAwardRandom == null) {
            linkAwardRandom = new RandomWeightUntil(LINK_BALL_AWARDS[1]);
        }
        // compute sw pay
        int swType = determineSwType(swInfo);
        // compute sw pay and multiplier
        calculateSwPays(gameLogicBean, displaySymbols, swType, swInfo, result, hitList);
    }


    private int determineSwType(SwSymbolInfo info) {
        if (info.sw1Count > 0 && info.sw2Count > 0 && info.sw3Count > 0) {
            return SW_ABC;
        } else if (info.sw2Count > 0 && info.sw3Count > 0) {
            return SW_BC;
        } else if (info.sw1Count > 0 && info.sw3Count > 0) {
            return SW_AC;
        } else if (info.sw1Count > 0 && info.sw2Count > 0) {
            return SW_AB;
        } else if (info.sw3Count > 0) {
            return SW_C;
        } else if (info.sw2Count > 0) {
            return SW_B;
        } else if (info.sw1Count > 0) {
            return SW_A;
        }
        return 0;
    }

    private void addSwTriggerResult(List<SlotSymbolHitResult> hitList, SwSymbolInfo info) {
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(SW1_SYMBOL);
        hitResult.setHitSymbolSound(SW1_SYMBOL);
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitPosition(info.swPositions);
        hitResult.setHitCount(info.swCount);
        hitResult.setHitPay(0);
        hitResult.setTriggerFs(true);
        hitResult.setTriggerFsCounts(RESPIN_TIMES);
        hitList.add(hitResult);
    }

    /**
     * analyze sw symbols
     */
    private SwSymbolInfo analyzeSwSymbols(int[] displaySymbols) {
        SwSymbolInfo info = new SwSymbolInfo();
        int reelsCount = reelsCount();
        int rowsCount = rowsCount();

        for (int i = 0; i < reelsCount; i++) {
            for (int j = 0; j < rowsCount; j++) {
                int index = i + j * reelsCount;
                int symbol = displaySymbols[index];

                if (symbol == SW1_SYMBOL) {
                    info.swPositions[i] = index + 1;
                    info.swDisplaySymbols[index] = SW1_SYMBOL;
                    info.hasSwSymbol = true;
                    info.swCount++;
                    info.sw1Count++;
                } else if (symbol == SW2_SYMBOL) {
                    info.swPositions[i] = index + 1;
                    info.swDisplaySymbols[index] = SW2_SYMBOL;
                    info.hasSwSymbol = true;
                    info.swCount++;
                    info.sw2Count++;
                } else if (symbol == SW3_SYMBOL) {
                    info.swPositions[i] = index + 1;
                    info.swDisplaySymbols[index] = SW3_SYMBOL;
                    info.hasSwSymbol = true;
                    info.swCount++;
                    info.sw3Count++;
                }
            }
        }
        return info;
    }

    /**
     * calculate sw pays
     */
    private void calculateSwPays(SlotGameLogicBean gameLogicBean,
                                 int[] displaySymbols,
                                 int swType,
                                 SwSymbolInfo info,
                                 Model20260825SpinResult result, List<SlotSymbolHitResult> hitList) {
        // sw ABC
        if (swType == SW_ABC) {
            // ABC
            RandomWeightUntil random = new RandomWeightUntil(getSw3Weight(gameLogicBean.getPercentage()));
            if (random.getRandomResult() == swType) {
                computeSwPay(info.swDisplaySymbols, swType, result, gameLogicBean);
                // add trigger result
                addSwTriggerResult(hitList, info);
                result.setSwType(swType);
            }
        } else if (swType == SW_BC || swType == SW_AC || swType == SW_AB) {
            // AB,AC,BC
            getSwPaysWithWeight(gameLogicBean, displaySymbols, swType, result, hitList, info, 2);
        } else if (swType == SW_C || swType == SW_B || swType == SW_A) {
            // A,B,C
            getSwPaysWithWeight(gameLogicBean, displaySymbols, swType, result, hitList, info, 1);
        }

    }

    /**
     * get sw Pays weight
     */
    private void getSwPaysWithWeight(SlotGameLogicBean gameLogicBean,
                                     int[] displaySymbols,
                                     int swType,
                                     Model20260825SpinResult result,
                                     List<SlotSymbolHitResult> hitList, SwSymbolInfo info, int swLevel) {
        int[] weight;
        int triggerIndex;

        if (swLevel == 1) {
            weight = getSw1Weight(gameLogicBean.getPercentage());
            triggerIndex = 1;
        } else {
            weight = getSw2Weight(gameLogicBean.getPercentage());
            triggerIndex = 3;
        }
        //random trigger A,B,C
        RandomWeightUntil random = new RandomWeightUntil(weight);
        if (random.getRandomResult() == triggerIndex) {
            computeSwPay(info.swDisplaySymbols, swType, result, gameLogicBean);
            // add trigger result
            addSwTriggerResult(hitList, info);
            result.setSwType(swType);
        }
    }

    private void computeSwPay(int[] swDisplaySymbols, int swType, Model20260825SpinResult result, SlotGameLogicBean gameLogicBean) {
        long[] swPays = new long[swDisplaySymbols.length];
        int[] swMultis = new int[swDisplaySymbols.length];
        Arrays.fill(swMultis, 1);
        int swMul = 1;
        if (containsSwA(swType)) {
            RandomWeightUntil randomWeightUntil = new RandomWeightUntil(MULTI_WEIGHT[0], MULTI_WEIGHT[1]);
            swMul = randomWeightUntil.getRandomResult();
        }
        List<Integer> swPositions = new ArrayList<>();
        for (int i = 0; i < swDisplaySymbols.length; i++) {
            if (swDisplaySymbols[i] > 0) {
                int randomIndex = linkAwardRandom.getRandomResult();
                swPays[i] = LINK_BALL_AWARDS[0][randomIndex] * gameLogicBean.getSumBetCredit();
                if (containsSwA(swType)) {
                    swPositions.add(i);
                }
            }
        }
        //SW prizes is selected at random and multiplied.
        if (containsSwA(swType) && !swPositions.isEmpty()) {
            int randomMulIndex = RandomUtil.getRandomInt(swPositions.size());
            int swPosition = swPositions.get(randomMulIndex);
            swPays[swPosition] *= swMul;
            swMultis[swPosition] = swMul;
        }
        //base game init entry fs parameter
        List<long[]> swPaysList = new ArrayList<>();
        swPaysList.add(swPays);
        List<Boolean> isFullBalls = new ArrayList<>();
        isFullBalls.add(false);
        List<Boolean> isGridTriggerFs = new ArrayList<>();
        isGridTriggerFs.add(false);
        List<Integer> gridFsLeftCounts = new ArrayList<>();
        gridFsLeftCounts.add(RESPIN_TIMES);
        List<int[]> colIcons = new ArrayList<>();
        int[] colIcon = new int[swDisplaySymbols.length];
        colIcons.add(colIcon);
        List<Long> colPays = new ArrayList<>();
        colPays.add(0L);
        List<Long> colTotalPays = new ArrayList<>();
        colTotalPays.add(0L);
        List<int[]> swMultiList = new ArrayList<>();
        swMultiList.add(swMultis);
        //contain SW_B plays two grids
        if (containsSwB(swType)) {
            swPaysList.add(swPays.clone());
            swMultiList.add(swMultis.clone());
            gridFsLeftCounts.add(RESPIN_TIMES);
            isGridTriggerFs.add(false);
            isFullBalls.add(false);
            colIcons.add(colIcon.clone());
            colPays.add(0L);
            colTotalPays.add(0L);
        }
        result.setLinkBonusSwPays(swPaysList);
        result.setSwMultis(swMultiList);
        result.setGridFsLeftCounts(gridFsLeftCounts);
        result.setIsFullBalls(isFullBalls);
        result.setIsGridTriggerFs(isGridTriggerFs);
        result.setColIcons(colIcons);
        result.setColPays(colPays);
        result.setColTotalPays(colTotalPays);
        //global fs counts
        gameLogicBean.setFsCountLeftList(new ArrayList<>(gridFsLeftCounts));
    }


}
