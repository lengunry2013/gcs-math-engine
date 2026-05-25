package com.gcs.game.engine.math.model20260508;


import com.gcs.game.engine.math.model20260507.Model20260507SpinResult;
import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IRespin;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Model20260508 extends BaseSlotModel {

    private static final int WILD_SYMBOL = 1;
    private static final int WILD_X3_SYMBOL = 2;
    public static final int SCATTER_SYMBOL = 12;
    public static final int SW_SYMBOL = 13;
    public static final int LINK_BONUS_SYMBOL = 15;

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
                {0, 0, 0, 0, 0},    // 2
                {0, 0, 20, 200, 500},  // 3
                {0, 0, 15, 80, 200},  // 4
                {0, 0, 15, 80, 200},  // 5

                {0, 0, 10, 40, 100},  //6
                {0, 0, 10, 40, 100},   //7
                {0, 0, 5, 25, 50},   //8
                {0, 0, 5, 20, 40},    //9
                {0, 0, 3, 15, 30},    //10

                {0, 0, 3, 10, 20},     //11
                {0, 0, 0, 0, 0},     //12
                {0, 0, 0, 0, 0},     //13
                {0, 0, 0, 0, 0},     //14
                {0, 0, 0, 0, 0}      // 15
        };
    }

    @Override
    protected String getPayLinesFileName() {
        return "G3_default_5x3X25_2.properties";
    }

    @Override
    protected int[][] getWildSymbols() {
        return new int[][]{
                {},
                {},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {WILD_SYMBOL, WILD_X3_SYMBOL},
                {0}, {0}, {0}, {0}};
    }

    @Override
    protected void initGameSymbols() {
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

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

    public int[] fsTimes() {
        return new int[]{10, 15, 20};
    }

    protected int linkBonusRowCount() {
        return 6;
    }

    protected int linkBonusReelsCount() {
        return 5;
    }

    private static final int WILD_X3 = 3;
    public static final int MIN_TRIGGER_LINK_BONUS = 5;
    public static final int[] SW_WEIGHT = new int[]{10000, 2500, 1200, 800, 200, 100, 200, 20, 10, 2};
    public static final int[] SW_AWARD = new int[]{1, 2, 3, 5, 10, 15, 20, 30, 50, 100};
    public static RandomWeightUntil levelNoRandom = null;
    public static RandomWeightUntil levelLv2Random = null;
    public static RandomWeightUntil levelLv1Random = null;
    public static RandomWeightUntil levelLv0Random = null;
    private static final int TRIGGER_LEVEL2_SW_COUNT = 7;
    private static final int TRIGGER_LEVEL1_W_COUNT = 11;
    private static final int TRIGGER_LEVEL0_W_COUNT = 15;
    private static final int RESET_SPIN_COUNT = 3;
    public static final int GRANT_AWARD = 1000;
    public static final int[] LEVEL_NO_UNLOCK = new int[]{930, 70};
    public static final int[] LEVEL_LV2_UNLOCK = new int[]{950, 50};
    public static final int[] LEVEL_LV1_UNLOCK = new int[]{950, 50};
    public static final int[] LEVEL_LV0_UNLOCK = new int[]{980, 20};

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260507SpinResult result = new Model20260507SpinResult();
        if (this instanceof IRespin && gameLogicBean.isRespin()) {
            IRespin respin = (IRespin) this;
            return respin.respin(gameLogicBean, displaySymbols, stopPosition, isSlot);
        }

        //link bonus and fs same trigger, first link bonus,second fs
        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);
        //List<Integer> swPosition = computeSwHit(gameLogicBean, displaySymbols, hitList, result);

        //WL_02 WILD_X3
        computeLineMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        hitList = filterLineHit(hitList);

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = (Model20260507SpinResult) transferHitList(result, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            result.setBaseGameMul(baseGameMultiplier);
        }
        if (!isSlot) {
            result.setFsMul(freeSpinMultiplier);
        }
        //result.setSwPosition(swPosition);
        int respinTimes = 0;
        if (this instanceof IRespin) {
            IRespin respin = (IRespin) this;
            respinTimes = respin.computeRespin(gameLogicBean, displaySymbols, isSlot, result);
        }
        computeRespin(result, respinTimes);
        return result;
    }

    private List<Integer> computeSwHit(SlotGameLogicBean gameLogicBean, int[] displaySymbols, List<SlotSymbolHitResult> hitList, Model20260507SpinResult result) {
        List<Integer> swPosition = new ArrayList<>();
        List<Integer> swHitPosition = new ArrayList<>();
        for (int i = 0; i < displaySymbols.length; i++) {
            if (displaySymbols[i] == SW_SYMBOL) {
                swPosition.add(15 + i + 1);
                swHitPosition.add(i + 1);
            }
        }
        if (!swHitPosition.isEmpty()) {
            if (swHitPosition.size() >= MIN_TRIGGER_LINK_BONUS) {
                computeHitLinkBonus(hitList, swHitPosition, swPosition, gameLogicBean, result);
            }
        }
        return swPosition;
    }

    private void computeHitLinkBonus(List<SlotSymbolHitResult> hitList, List<Integer> swHitPosition, List<Integer> swPosition, SlotGameLogicBean gameLogicBean, Model20260507SpinResult result) {
        long linkBonusAward = computeLinkBonusReward(gameLogicBean, result, swPosition);
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(LINK_BONUS_SYMBOL);
        hitResult.setHitSymbolSound(LINK_BONUS_SYMBOL);
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitCount(swHitPosition.size());
        hitResult.setHitPay(linkBonusAward);
        hitResult.setHitPosition(StringUtil.list2Array(swHitPosition));
        hitList.add(hitResult);
    }

    private long computeLinkBonusReward(SlotGameLogicBean gameLogicBean, Model20260507SpinResult result, List<Integer> swPosition) {
        int swCount = swPosition.size();
        result.setTriggerSwCount(swCount);
        int triggerActiveLevel = getActiveLevel(swCount);

        result.setTriggerActiveLevel(triggerActiveLevel);
        int linkBonusLen = linkBonusRowCount() * linkBonusReelsCount();
        int[] linkBonusDisplaySymbol = new int[linkBonusLen];
        for (int position : swPosition) {
            linkBonusDisplaySymbol[position - 1] = Model20260508.SW_SYMBOL;
        }
        int respinTimes = RESET_SPIN_COUNT;
        if (levelNoRandom == null) {
            levelNoRandom = new RandomWeightUntil(LEVEL_NO_UNLOCK);
        }
        if (levelLv0Random == null) {
            levelLv0Random = new RandomWeightUntil(LEVEL_LV0_UNLOCK);
        }
        if (levelLv1Random == null) {
            levelLv1Random = new RandomWeightUntil(LEVEL_LV1_UNLOCK);
        }
        if (levelLv2Random == null) {
            levelLv2Random = new RandomWeightUntil(LEVEL_LV2_UNLOCK);
        }
        int activeFirstPosition = getActiveFirstPosition(triggerActiveLevel);
        int randomIndex = 0;
        int activeSwCount = swCount;
        while (respinTimes > 0 && activeSwCount < linkBonusLen) {
            for (int i = 0; i < linkBonusDisplaySymbol.length; i++) {
                if (linkBonusDisplaySymbol[i] == 0) {
                    switch (triggerActiveLevel) {
                        case -1:
                            randomIndex = levelNoRandom.getRandomResult();
                            break;
                        case 0:
                            randomIndex = levelLv0Random.getRandomResult();
                            break;
                        case 1:
                            randomIndex = levelLv1Random.getRandomResult();
                            break;
                        case 2:
                            randomIndex = levelLv2Random.getRandomResult();
                            break;
                        default:
                            break;
                    }
                    if (randomIndex == 1) {
                        linkBonusDisplaySymbol[i] = SW_SYMBOL;
                        if (i >= activeFirstPosition - 1) {
                            activeSwCount++;
                        }
                    }
                }
            }
            triggerActiveLevel = getActiveLevel(activeSwCount);
            if (activeSwCount > swCount) {
                respinTimes = RESET_SPIN_COUNT;
                activeFirstPosition = getActiveFirstPosition(triggerActiveLevel);
                activeSwCount = 0;
                for (int i = activeFirstPosition - 1; i < linkBonusDisplaySymbol.length; i++) {
                    if (linkBonusDisplaySymbol[i] == SW_SYMBOL) {
                        activeSwCount++;
                    }
                }
                swCount = activeSwCount;
            } else {
                respinTimes--;
            }
        }
        long totalPay = computeBonusAward(gameLogicBean, triggerActiveLevel, linkBonusDisplaySymbol, activeSwCount, result);
        result.setEndActiveLevel(triggerActiveLevel);
        result.setEndSwCount(activeSwCount);
        result.setLinkBonusDisplaySymbol(linkBonusDisplaySymbol);
        return totalPay;
    }


    private long computeBonusAward(SlotGameLogicBean gameLogicBean, int triggerActiveLevel, int[] linkBonusDisplaySymbol, int activeSwCount, Model20260507SpinResult result) {
        long totalPay = 0;
        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(SW_WEIGHT);
        int startPos = getActiveFirstPosition(triggerActiveLevel) - 1;
        List<Long> swSymbolsWin = new ArrayList<>();
        for (int i = startPos; i < linkBonusDisplaySymbol.length; i++) {
            if (linkBonusDisplaySymbol[i] == Model20260508.SW_SYMBOL) {
                int randomIndex = randomWeightUntil.getRandomResult();
                long swWin = SW_AWARD[randomIndex] * gameLogicBean.getSumBetCredit();
                totalPay += swWin;
                swSymbolsWin.add(swWin);
            }
        }
        result.setSwSymbolsWin(swSymbolsWin);
        //hit GRAND
        if (activeSwCount == linkBonusDisplaySymbol.length) {
            long grandWin = GRANT_AWARD * gameLogicBean.getSumBetCredit();
            totalPay += grandWin;
            result.setGrantWin(grandWin);
        }
        return totalPay;
    }

    private int getActiveLevel(int swCount) {
        int triggerActiveLevel = -1;
        if (swCount >= TRIGGER_LEVEL0_W_COUNT) {
            triggerActiveLevel = 0;
        } else if (swCount >= TRIGGER_LEVEL1_W_COUNT) {
            triggerActiveLevel = 1;
        } else if (swCount >= TRIGGER_LEVEL2_SW_COUNT) {
            triggerActiveLevel = 2;
        }
        return triggerActiveLevel;
    }

    private int getActiveFirstPosition(int triggerActiveLevel) {
        switch (triggerActiveLevel) {
            case -1:  // no active 3 row
                return 16;
            case 0:  //level 0 active
                return 1;
            case 1:  //level 1 active
                return 6;
            case 2:  //level 2 active
                return 11;
            default:
                break;
        }
        return 16;
    }

    protected void computeLineMultiplier(int[] displaySymbols, List<SlotSymbolHitResult> hitList, boolean isSlot, SlotGameLogicBean gameLogicBean) {
        if (!hitList.isEmpty()) {
            for (SlotSymbolHitResult hitResult : hitList) {
                int[] hitPosition = hitResult.getHitPosition();
                int winMul = 1;
                for (int position : hitPosition) {
                    if (position > 0 && displaySymbols[position - 1] == WILD_X3_SYMBOL) {
                        winMul *= WILD_X3;
                    }
                }
                hitResult.setHitMul(winMul);
                hitResult.setHitPay(winMul * hitResult.getHitPay());
            }
        }
    }


}
