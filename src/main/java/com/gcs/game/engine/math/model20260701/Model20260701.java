package com.gcs.game.engine.math.model20260701;

import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IWildReelsChange;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.SlotFsSymbol;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.engine.slots.vo.SlotSpinResult;
import com.gcs.game.engine.slots.vo.SlotSymbolHitResult;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.RecoverInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * GoldRing Circus Game
 */
public class Model20260701 extends BaseSlotModel implements IWildReelsChange {

    public static final int WILD_SYMBOL = 1;

    public static final int SCATTER_SYMBOL = 12;

    public static final int SW_SYMBOL = 13;

    public static final int TRIGGER_SW = 6;
    public static final int RESPIN_TIMES = 3;

    public Model20260701() {
        super();
    }

    public long minBetPerLine() {
        return 1;
    }

    public long maxBetPerLine() {
        return 10;
    }

    public long minLines() {
        return 25;
    }

    public long maxLines() {
        return 25;
    }

    public long totalBet(long lines, long betPerLine) {
        return 50 * betPerLine;
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

    public int getCardinalLineNumber4R2L() {
        return 25;
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
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_RIGHT2LEFT);

        // init free spin symbol
        SlotFsSymbol symbol12 = new SlotFsSymbol();
        symbol12.setSymbolNumber(SCATTER_SYMBOL);
        symbol12.setMinHitCount(3);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_FREE_SPIN);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 2, 10, 30});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 2, 10, 30});
        symbol12.setHitFsCounts(new int[]{0, 0, 12, 24, 36});
        symbols.add(symbol12);
    }

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260701SpinResult result = new Model20260701SpinResult();

        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);

        hitList = filterLineHit(hitList);
        computeTriggerSw(gameLogicBean, result, hitList, displaySymbols);

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = (Model20260701SpinResult) transferHitList(result, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            result.setBaseGameMul(baseGameMultiplier);
        }
        if (!isSlot) {
            result.setFsMul(freeSpinMultiplier);
        }
        return result;
    }

    private void computeTriggerSw(SlotGameLogicBean gameLogicBean, Model20260701SpinResult result, List<SlotSymbolHitResult> hitList, int[] displaySymbols) {
        List<Integer> swPosition = new ArrayList<>();
        int[] swPositionArray = new int[displaySymbols.length];
        for (int i = 0; i < displaySymbols.length; i++) {
            if (displaySymbols[i] == SW_SYMBOL) {
                swPosition.add(i + 1);
                swPositionArray[i] = SW_SYMBOL;
            }
        }
        if (!swPosition.isEmpty() && swPosition.size() >= TRIGGER_SW) {
            int swSize = swPosition.size();
            long swTotalPay = computeSwResult(result, swPositionArray, swSize);
            SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
            hitResult.setHitSymbol(SW_SYMBOL);
            hitResult.setHitSymbolSound(SW_SYMBOL);
            hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
            hitResult.setHitMul(1);
            hitResult.setHitPosition(StringUtil.list2Array(swPosition));
            hitResult.setHitCount(swSize);
            hitResult.setHitPay(swTotalPay * totalBet(gameLogicBean.getLines(), gameLogicBean.getBet()));
            hitList.add(hitResult);
        }
    }

    public static int[][] LINK_BALL_WEIGHT = new int[][]{
            {1, 9999}, {40, 500}, {40, 500}, {50, 500}, {50, 500}, {50, 500},
            {50, 500}, {50, 500}, {50, 500}
    };

    public static int[][] LINK_BALL_AWARDS = new int[][]{
            {1, 2, 3, 5, 10, 15, 20, 30, 50, 100, 5, 20, 100},
            {6541, 1500, 1006, 200, 200, 100, 80, 80, 10, 15, 200, 53, 15}
    };
    public static int[][] COLLECT_WEIGHT = new int[][]{
            {3, 4, 5, 6, 7, 8, 9, 10},
            {6000, 1000, 1000, 800, 700, 400, 90, 10}
    };
    public static int[] ADD_COL_WEIGHT = new int[]{890, 110};


    public static int GRAND_AWARDS = 1000;

    private static RandomWeightUntil linkAwardRandom = null;

    private long computeSwResult(Model20260701SpinResult result, int[] swPositionArray, int swSize) {
        int linkBalls = swSize;
        int respinTimes = RESPIN_TIMES;
        int[] swPays = computeSwPay(swPositionArray, result);
        int collectAward = 0;
        int totalRespinTimes = 0;

        while (respinTimes > 0 && linkBalls < swPositionArray.length) {
            int[][] weight = getLinkWeight(linkBalls, swPositionArray);
            int newBalls = randomLinkBalls(weight, linkBalls, swPositionArray, swPays, result);
            int remainBalls = swPositionArray.length - newBalls;
            int colReward = 0;
            if (remainBalls > 0) {
                RandomWeightUntil colRandomWeight = new RandomWeightUntil(ADD_COL_WEIGHT);
                int colAddRandom = colRandomWeight.getRandomResult();
                if (colAddRandom == 1) {
                    colReward = computeCollectAward(swPays, newBalls);
                    collectAward += colReward;
                }
            }
            if (newBalls > linkBalls || colReward > 0) {
                linkBalls = newBalls;
                respinTimes = 3; // 升级后重置次数
            } else {
                respinTimes--;
            }
            totalRespinTimes++;
        }
        long totalPay = collectAward;
        for (int pay : swPays) {
            totalPay += pay;
        }
        if (linkBalls == swPositionArray.length) {
            totalPay += GRAND_AWARDS;
            result.setGrandWin(GRAND_AWARDS);
        }
        result.setColWin(collectAward);
        result.setLinkBonusSwPays(swPays);
        result.setRespinTimes(totalRespinTimes);
        return totalPay;
    }

    private int computeCollectAward(int[] swPays, int newBalls) {
        int colAward = 0;
        if (newBalls < swPays.length) {
            RandomWeightUntil randomWeightUntil = new RandomWeightUntil(COLLECT_WEIGHT[0], COLLECT_WEIGHT[1]);
            int collectCount = randomWeightUntil.getRandomResult();
            int finalCol = Math.min(collectCount, newBalls);
            int[] swPayArray = new int[newBalls];
            int index = 0;
            for (int pay : swPays) {
                if (pay > 0) {
                    swPayArray[index] = pay;
                    index++;
                }
            }
            int[] randomPayIndex = RandomUtil.getRandomIndex(swPayArray.length, finalCol);
            if (randomPayIndex != null) {
                for (int randomIndex : randomPayIndex) {
                    colAward += swPayArray[randomIndex];
                }
            }
        }
        return colAward;
    }

    private int[] computeSwPay(int[] swPositionArray, Model20260701SpinResult result) {
        int[] swPays = new int[swPositionArray.length];
        int[] linkBonusRandomPayIndex = new int[swPositionArray.length];
        Arrays.fill(linkBonusRandomPayIndex, -1);
        if (linkAwardRandom == null) {
            linkAwardRandom = new RandomWeightUntil(LINK_BALL_AWARDS[1]);
        }
        for (int i = 0; i < swPositionArray.length; i++) {
            if (swPositionArray[i] == SW_SYMBOL) {
                int randomIndex = linkAwardRandom.getRandomResult();
                swPays[i] = LINK_BALL_AWARDS[0][randomIndex];
                linkBonusRandomPayIndex[i] = randomIndex;
            }
        }
        result.setLinkBonusRandomPayIndexs(linkBonusRandomPayIndex);
        return swPays;
    }

    private int randomLinkBalls(int[][] ballsWeight, int currentBalls, int[] swPositionArray, int[] swPays, Model20260701SpinResult result) {
        if (ballsWeight == null) {
            return currentBalls;
        }
        if (result.getLinkBonusRandomPayIndexs() == null) {
            int[] randomPayIndex = new int[swPositionArray.length];
            Arrays.fill(randomPayIndex, -1);
            result.setLinkBonusRandomPayIndexs(randomPayIndex);
        }
        for (int[] linKBallsWeight : ballsWeight) {
            RandomWeightUntil ru = new RandomWeightUntil(linKBallsWeight);
            int randomIndex = ru.getRandomResult();
            if (randomIndex == 0) {
                if (linkAwardRandom == null) {
                    linkAwardRandom = new RandomWeightUntil(LINK_BALL_AWARDS[1]);
                }
                currentBalls++;
                for (int i = 0; i < swPositionArray.length; i++) {
                    if (swPositionArray[i] == 0) {
                        swPositionArray[i] = SW_SYMBOL;
                        break;
                    }
                }
                for (int i = 0; i < swPays.length; i++) {
                    if (swPays[i] == 0) {
                        int index = linkAwardRandom.getRandomResult();
                        swPays[i] = LINK_BALL_AWARDS[0][index];
                        result.getLinkBonusRandomPayIndexs()[i] = index;
                        break;
                    }
                }

            }
        }
        return currentBalls;
    }

    private int[][] getLinkWeight(int linkBalls, int[] swPositionArray) {
        if (linkBalls == TRIGGER_SW) {
            return LINK_BALL_WEIGHT;
        } else {
            int len = swPositionArray.length - linkBalls;
            int[][] weight = new int[len][];
            System.arraycopy(LINK_BALL_WEIGHT, 0, weight, 0, len);
            return weight;
        }
    }


    @Override
    public int wildSymbolNo() {
        return WILD_SYMBOL;
    }

    @Override
    public int[] computeWildReels(SlotGameLogicBean gameSessionBean, int[] displaySymbols, boolean isSlot) {
        if (!isSlot) {
            int rowCount = displaySymbols.length / reelsCount();
            List<Integer> wildReels = new ArrayList<>();
            for (int i = 0; i < reelsCount(); i++) {
                for (int j = 0; j < rowCount; j++) {
                    if (displaySymbols[i + j * reelsCount()] == WILD_SYMBOL) {
                        wildReels.add(i);
                        break;
                    }
                }
            }
            return StringUtil.list2Array(wildReels);
        }
        return null;
    }

    @Override
    public int[] computeWildReels(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, RecoverInfo recoverInfo) {
        return new int[0];
    }

}
