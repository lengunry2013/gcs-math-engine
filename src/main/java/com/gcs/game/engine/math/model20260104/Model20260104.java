package com.gcs.game.engine.math.model20260104;


import com.gcs.game.engine.math.model20260103.Model20260103SpinResult;
import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IWildPositionsChange;
import com.gcs.game.engine.slots.model.IWildReelsChange;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.RecoverInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model20260104 extends BaseSlotModel implements IWildPositionsChange {

    private static final int WILD_SYMBOL = 1;

    public static final String BASE_REELS_KEY = "MY";

    public static final String FREE_SPIN_REELS_KEY = "freespin_JP";

    public static final int SC1_SYMBOL = 12;

    public static final int SC2_SYMBOL = 13;

    public static final int SC3_SYMBOL = 14;
    public static final int MY_SYMBOL = 15;

    public static final int FS_TIME = 8;
    public static final int FS_EXTEND_REELS = 1;
    public static final int FS_JACKPOT_BONUS = 2;
    public static final int FS_SUPER_BONUS = 3;


    @Override
    protected int reelsCount() {
        return 5;
    }

    @Override
    protected int rowsCount() {
        return 5;
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


    @Override
    protected String getPayLinesFileName() {
        return null;
    }

    @Override
    protected long[][] getPayTable() {
        return new long[][]{
                {0, 0, 0, 0, 0},     // 1
                {0, 0, 40, 80, 200}, // 2
                {0, 0, 20, 40, 100},  // 3
                {0, 0, 20, 40, 100},  // 4
                {0, 0, 15, 30, 60},  // 5

                {0, 0, 15, 30, 60},   //6
                {0, 0, 10, 20, 40},   //7
                {0, 0, 8, 15, 30},   //8
                {0, 0, 8, 15, 30},    //9
                {0, 0, 5, 10, 20},    //10

                {0, 0, 5, 10, 20},     //11
                {0, 0, 0, 0, 0},     //12
                {0, 0, 0, 0, 0},     //13
                {0, 0, 0, 0, 0},     //14
                {0, 0, 0, 0, 0}      // 15
        };
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
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_ADJACENT_LEFT2RIGHT);
    }

    private static final int[] MY_WEIGHT = new int[]{1000, 800, 600, 900, 700, 1200, 1200, 1200, 1200, 1200};

    private static final int[] MY_SYMBOLS = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    protected int[] getMyWeight() {
        return MY_WEIGHT;
    }

    private static RandomWeightUntil myRandom = null;

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        SlotSpinResult baseSpinResult;
        Map<Integer, int[]> payLinesMap = getPayLines();
        //displaySymbols = getScChangeDisplaySymbols(displaySymbols, isSlot, gameLogicBean);
        int[] oldDisplaySymbols = null;
        int[] wildReels = null;
        if (this instanceof IWildReelsChange) {
            oldDisplaySymbols = displaySymbols.clone();
            wildReels = ((IWildReelsChange) this).computeWildReels(gameLogicBean, displaySymbols, isSlot);
            int wildSymbolNo = ((IWildReelsChange) this).wildSymbolNo();
            coverDisplaySymbolsByReels(displaySymbols, wildReels, wildSymbolNo);
        }
        int[] wildPositions = null;
        int wildSymbolNo = 2;
        if (this instanceof IWildPositionsChange) {
            oldDisplaySymbols = displaySymbols.clone();
            wildPositions = ((IWildPositionsChange) this).computeWildPositions(gameLogicBean, displaySymbols, isSlot);
            if (isSlot) {
                wildSymbolNo = ((IWildPositionsChange) this).wildSymbolNo();
                coverDisplaySymbolsByPositions(displaySymbols, wildPositions, wildSymbolNo);
            }
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


    @Override
    public int wildSymbolNo() {
        if (myRandom == null) {
            myRandom = new RandomWeightUntil(getMyWeight());
        }
        return MY_SYMBOLS[myRandom.getRandomResult()];
    }

    @Override
    public int[] computeWildPositions(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot) {
        if (isSlot) {
            List<Integer> wildPositions = new ArrayList<>();
            for (int i = 0; i < displaySymbols.length; i++) {
                if (displaySymbols[i] == MY_SYMBOL) {
                    wildPositions.add(i);
                }
            }
            return StringUtil.list2Array(wildPositions);
        }
        return null;
    }

    @Override
    public int[] computeWildPositions(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, RecoverInfo recoverInfo) {
        return new int[0];
    }
}
