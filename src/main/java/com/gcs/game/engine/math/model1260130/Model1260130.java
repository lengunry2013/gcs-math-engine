package com.gcs.game.engine.math.model1260130;


import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.SlotBonusSymbol;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.engine.slots.vo.SlotSpinResult;
import com.gcs.game.engine.slots.vo.SlotSymbolHitResult;
import com.gcs.game.utils.RandomWeightUntil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model1260130 extends BaseSlotModel {

    protected static final int WILD_SYMBOL = 1;

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
                {0, 0, 200, 500, 1000}, // 1
                {0, 0, 100, 200, 400},  // 2
                {0, 0, 100, 200, 400},  // 3
                {0, 0, 50, 100, 200},   // 4
                {0, 0, 50, 100, 200},   // 5

                {0, 0, 40, 80, 160},    // 6
                {0, 0, 30, 60, 120},    // 7
                {0, 0, 20, 40, 80},    // 8
                {0, 0, 10, 30, 60},     // 9
                {0, 0, 10, 20, 40},     // 10

                {0, 0, 6, 10, 20},      // 11
                {0, 0, 0, 0, 0},       // 12
                {0, 0, 0, 0, 0},       // 13
                {0, 0, 0, 0, 0},       // 14
                {0, 0, 0, 0, 0}        // 15;
        };
    }

    @Override
    protected String getPayLinesFileName() {
        return "G3_default_5x4x108.properties";
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

                1}, {
                0}, {
                0}, {
                0}, {
                0}};
    }

    @Override
    protected void initGameSymbols() {
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        // init free spin symbol
        SlotBonusSymbol symbol12 = new SlotBonusSymbol();
        symbol12.setSymbolNumber(12);
        symbol12.setMinHitCount(3);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BONUSINBG_FSINFS);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 0, 0, 0});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol12.setHitFsCounts(new int[]{0, 0, 8, 0, 0});
        symbol12.setBonusAsset("bonus");
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
        return 88;
    }

    @Override
    public long maxLines() {
        return 88;
    }

    @Override
    public long totalBet(long lines, long betPerLine) {
        return lines * betPerLine;
    }

    protected static int[] BASE_GAME_MULTIPLIER = new int[]{1, 2, 3, 4, 5, 6, 7, 8};

    protected int[] getBaseGameMultiplierWeight(int payback) {
        int[] result = new int[]{1317, 283, 110, 80, 70, 60, 50, 30};
        switch (payback) {
            case 9550:
                result = new int[]{1317, 283, 110, 80, 70, 60, 50, 30};
                break;
            case 9600:
                result = new int[]{1296, 294, 120, 80, 70, 60, 50, 30};
                break;
            case 9650:
                result = new int[]{1276, 303, 131, 80, 70, 60, 50, 30};
                break;
        }
        return result;
    }

    protected static Map<String, RandomWeightUntil> baseGameMultiWeightUntilMap = new HashMap<>();

    protected static Object syncObj4Map = new Object();

    protected int getBaseGameMultiplier(int[] displaySymbols, SlotGameLogicBean gameLogicBean) {
        int randomMultiplier;
        synchronized (syncObj4Map) {
            RandomWeightUntil random = baseGameMultiWeightUntilMap.get(gameLogicBean.getMmID() + "_" + gameLogicBean.getPercentage());
            if (random == null) {
                random = new RandomWeightUntil(this.BASE_GAME_MULTIPLIER, getBaseGameMultiplierWeight(gameLogicBean.getPercentage()));
                baseGameMultiWeightUntilMap.put(gameLogicBean.getMmID() + "_" + gameLogicBean.getPercentage(), random);
            }
            randomMultiplier = random.getRandomResult();
        }
        return randomMultiplier;
    }

    protected static int[] FS_INCREMENT_MUL = new int[]{2, 3, 4, 5, 6, 7, 8};

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model1260130SpinResult result = new Model1260130SpinResult();

        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);
        hitList = filterLineHit(hitList);

        if (hitList != null) {
            for (SlotSymbolHitResult hitResult : hitList) {
                if (hitResult != null && hitResult.getHitSymbol() == 12) {
                    hitResult.setHitLine(999); // TODO
                }
            }
        }

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = 1;
        int nextFreeSpinMultiplier = 1;
        if (!isSlot) {
            if (gameLogicBean.getSlotFsSpinResults() != null && gameLogicBean.getSlotFsSpinResults().size() > 0) {
                SlotSpinResult lastFS = gameLogicBean.getSlotFsSpinResults().get(gameLogicBean.getSlotFsSpinResults().size() - 1);
                freeSpinMultiplier = ((Model1260130SpinResult) lastFS).getNextFsMul();
            } else {
                freeSpinMultiplier = FS_INCREMENT_MUL[0];
            }
            nextFreeSpinMultiplier = freeSpinMultiplier;
            if (hitList != null && hitList.size() > 0) {
                nextFreeSpinMultiplier = getNextLevelMultiplier(FS_INCREMENT_MUL, freeSpinMultiplier);
            }
            result.setFsMul(freeSpinMultiplier);
            result.setNextFsMul(nextFreeSpinMultiplier);
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult temp : hitList) {
                    long hitAmount = temp.getHitPay();
                    temp.setHitPay(hitAmount * freeSpinMultiplier);
                }
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

    protected List<SlotSymbolHitResult> filterLineHit(List<SlotSymbolHitResult> hitList) {
        return filterLineHit(hitList, ((int) maxLines() + 1));
    }

}
