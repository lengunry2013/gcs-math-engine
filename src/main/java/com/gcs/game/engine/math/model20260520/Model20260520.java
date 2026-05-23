package com.gcs.game.engine.math.model20260520;

import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;

import java.util.*;

/**
 * @author: alice
 * @date: 2025/9/11 10:47
 * @description:
 */

public class Model20260520 extends BaseSlotModel {

    public static final String BASE_GAME_MYSTERY_REELS_KEY = "WR";
    public static final String FREE_SPIN_REELS2_KEY = "freespin_SW";
    public static final String FREE_SPIN_REELS3_KEY = "freespin_RW";
    public static final int FREE_SPIN_TYPE_STICKY_WILD = 1;
    public static final int FREE_SPIN_TYPE_REELS_WILD = 2;
    public static final int FREE_SPIN_TYPE_POSITIONS_WILD = 3;

    public static final int WILD_SYMBOL = 1;
    public static final int SCATTER_SYMBOL = 11;
    public static int freespinType = -1;
    public static int freespinRandomIndex = -1;
    private static RandomWeightUntil fsRandom = null;
    private static RandomWeightUntil fsStickyRandom = null;
    private static RandomWeightUntil fsShiftingRandom = null;

    @Override
    protected int reelsCount() {
        return 5;
    }

    @Override
    protected int rowsCount() {
        return 3;
    }

    @Override
    public long[][] getPayTable() {
        return new long[][]{
                {0, 0, 0, 0, 0},      // 1
                {0, 0, 300, 1000, 10000},  // 2
                {0, 0, 200, 600, 1200},  // 3
                {0, 0, 150, 300, 800},  // 4
                {0, 0, 100, 200, 500},  // 5

                {0, 0, 50, 100, 200},    // 6
                {0, 0, 25, 50, 100},    // 7
                {0, 0, 15, 30, 60},    // 8
                {0, 0, 10, 20, 40},     // 9
                {0, 0, 6, 10, 20},     // 10

                {0, 0, 0, 0, 0},      // 11
                {0, 0, 0, 0, 0},      // 12
                {0, 0, 0, 0, 0},      // 13
                {0, 0, 0, 0, 0},      // 14
                {0, 0, 0, 0, 0}       // 15
        };
    }

    @Override
    public String getPayLinesFileName() {
        return "G3_default_5x3X25.properties";
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
        // init base symbols
        initBaseSymbols(10, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        // init free spin symbol
        SlotFsSymbol symbol11 = new SlotFsSymbol();
        symbol11.setSymbolNumber(SCATTER_SYMBOL);
        symbol11.setMinHitCount(3);
        symbol11.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_FREE_SPIN);
        symbol11.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol11.setWildSymbols(null);
        symbol11.setPay(new long[]{0, 0, 0, 0, 0});
        symbol11.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol11.setHitFsCounts(new int[]{0, 0, 10, 0, 0});
        //symbol11.setBonusAsset("bonus");
        symbols.add(symbol11);
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

    public long maxTotalPay() {
        return 80000;
    }

    @Override
    public long totalBet(long lines, long betPerLine) {
        return 50 * betPerLine;
    }

    protected static int[] BASE_MYSTERY_TYPE_WEIGHT = new int[]{
            943, 57};

    protected static int[] BASE_MYSTERY_FEATURE_WEIGHT = new int[]{
            3605, 6395, 0
    };

    protected static int[][] WILD_REELS = new int[][]{
            {3, 4}, {2, 4}, {1, 4}, {2, 3}, {1, 3}, {1, 2}, {4}, {3}, {2}, {1}
    };

    protected static int[] WILD_MUL = new int[]{
            1, 1, 1, 1, 1, 1, 2, 2, 2, 2
    };

    protected static int[] BASE_WILD_REELS_WEIGHT = new int[]{
            367,
            450,
            450,
            14,
            14,
            5,
            50,
            300,
            200,
            150,
    };

    protected static RandomWeightUntil baseStickyWildReelRandomUntil = null;

    protected static int[][] BASE_STICKY_WILD = new int[][]{
            {0, 0, 0, 0, 1}, {0, 0, 0, 0, 2}, {0, 0, 0, 0, 3}, {0, 0, 0, 1, 0}, {0, 0, 0, 1, 1},
            {0, 0, 0, 1, 2}, {0, 0, 0, 1, 3}, {0, 0, 0, 2, 0}, {0, 0, 0, 2, 1}, {0, 0, 0, 2, 2},
            {0, 0, 0, 2, 3}, {0, 0, 0, 3, 0}, {0, 0, 0, 3, 1}, {0, 0, 0, 3, 2}, {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 1}, {0, 0, 1, 0, 2}, {0, 0, 1, 0, 3}, {0, 0, 1, 1, 0}, {0, 0, 1, 1, 1},
            {0, 0, 1, 1, 2}, {0, 0, 1, 1, 3}, {0, 0, 1, 2, 0}, {0, 0, 1, 2, 1}, {0, 0, 1, 2, 2},
            {0, 0, 1, 3, 0}, {0, 0, 1, 3, 1}, {0, 0, 2, 0, 0}, {0, 0, 2, 0, 1}, {0, 0, 2, 0, 2},
            {0, 0, 2, 0, 3}, {0, 0, 2, 1, 0}, {0, 0, 2, 1, 1}, {0, 0, 2, 1, 2}, {0, 0, 2, 2, 0},
            {0, 0, 2, 2, 1}, {0, 0, 2, 3, 0}, {0, 0, 3, 0, 0}, {0, 0, 3, 0, 1}, {0, 0, 3, 0, 2},
            {0, 0, 3, 1, 0}, {0, 0, 3, 1, 1}, {0, 0, 3, 2, 0}, {0, 1, 0, 0, 0}, {0, 1, 0, 0, 1},
            {0, 1, 0, 0, 2}, {0, 1, 0, 0, 3}, {0, 1, 0, 1, 0}, {0, 1, 0, 1, 1}, {0, 1, 0, 1, 2},
            {0, 1, 0, 1, 3}, {0, 1, 0, 2, 0}, {0, 1, 0, 2, 1}, {0, 1, 0, 2, 2}, {0, 1, 0, 3, 0},
            {0, 1, 0, 3, 1}, {0, 1, 1, 0, 0}, {0, 1, 1, 0, 1}, {0, 1, 1, 0, 2}, {0, 1, 1, 0, 3},
            {0, 1, 1, 1, 0}, {0, 1, 1, 1, 1}, {0, 1, 1, 1, 2}, {0, 1, 1, 2, 0}, {0, 1, 1, 2, 1},
            {0, 1, 1, 3, 0}, {0, 1, 2, 0, 0}, {0, 1, 2, 0, 1}, {0, 1, 2, 0, 2}, {0, 1, 2, 1, 0},
            {0, 1, 2, 1, 1}, {0, 1, 2, 2, 0}, {0, 1, 3, 0, 0}, {0, 1, 3, 0, 1}, {0, 1, 3, 1, 0},
            {0, 2, 0, 0, 0}, {0, 2, 0, 0, 1}, {0, 2, 0, 0, 2}, {0, 2, 0, 0, 3}, {0, 2, 0, 1, 0},
            {0, 2, 0, 1, 1}, {0, 2, 0, 1, 2}, {0, 2, 0, 2, 0}, {0, 2, 0, 2, 1}, {0, 2, 0, 3, 0},
            {0, 2, 1, 0, 0}, {0, 2, 1, 0, 1}, {0, 2, 1, 0, 2}, {0, 2, 1, 1, 0}, {0, 2, 1, 1, 1},
            {0, 2, 1, 2, 0}, {0, 2, 2, 0, 0}, {0, 2, 2, 0, 1}, {0, 2, 2, 1, 0}, {0, 2, 3, 0, 0},
            {0, 3, 0, 0, 0}, {0, 3, 0, 0, 1}, {0, 3, 0, 0, 2}, {0, 3, 0, 1, 0}, {0, 3, 0, 1, 1},
            {0, 3, 0, 2, 0}, {0, 3, 1, 0, 0}, {0, 3, 1, 0, 1}, {0, 3, 1, 1, 0}, {0, 3, 2, 0, 0},
    };

    protected static int[] BASE_STICKY_WILD_WEIGHT = new int[]{
            245,
            350,
            500,
            500,
            500,
            500,
            500,
            500,
            300,
            300,
            400,
            300,
            200,
            200,
            500,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            200,
            100,
            100,
            100,
            100,
            100,
            100,
            100,
            100,
            5,
            5,
            50,
            5,
            5,
            50,
            50,
            50,
            50,
            50,
            5,
            50,
            50,
            5,
            20,
            20,
            20,
            20,
            20,
            20,
            20,
            20,
            20,
            20,
            20,
            20,
            20,
            5,
            5,
            5,
            5,
            5,
            5,
            5,
            10,
            10,
            10,
            10,
            10,
            10,
            5,
            5,
            5,
            5,
    };

    protected static RandomWeightUntil baseStickyWildRandomUntil = null;

    protected static int[] FS_WEIGHT = new int[]{
            200, 200, 200
    };

    protected int[] getFreeSpinWeight() {
        return FS_WEIGHT;
    }

    protected static int[] FS_WR_WEIGHT = new int[]{
            864,
            800,
            800,
            350,
            133,
            50,
            50,
            903,
            750,
            300,
    };

    protected int[] getFreeSpinWRWeight() {
        return FS_WR_WEIGHT;
    }

    protected static int[] FS_STICKY_WILD_WEIGHT = new int[]{
            237, 87, 165, 175, 65, 154, 160, 200, 15, 242
    };

    protected static int[] FS_RANDOM_WILD_WEIGHT = new int[]{
            318, 150, 128, 116, 330, 390, 348, 150, 40, 30
    };

    protected int[] getFreeSpinStickyWeight() {
        return FS_STICKY_WILD_WEIGHT;
    }

    protected int[] getFreeSpinRandomWeight() {
        return FS_RANDOM_WILD_WEIGHT;
    }

    protected static RandomWeightUntil freeSpinWRRandomUntil = null;

    protected static int[][] FS_STICKY_SCRIPT = new int[][]{
            {5, 4, 5, 5, 4, 4, 2, 3, 4, 3},
            {5, 2, 5, 5, 4, 4, 5, 4, 2, 2},
            {5, 3, 5, 5, 4, 4, 5, 2, 2, 3},
            {4, 3, 5, 5, 4, 2, 5, 2, 2, 5},
            {4, 2, 5, 5, 4, 3, 5, 2, 2, 2},
            {4, 3, 5, 4, 5, 2, 5, 2, 2, 3},
            {3, 2, 5, 5, 5, 2, 4, 4, 2, 4},
            {3, 4, 5, 5, 5, 2, 4, 4, 2, 4},
            {3, 2, 5, 5, 5, 4, 4, 4, 3, 2},
            {2, 2, 5, 4, 5, 5, 4, 4, 3, 2},
    };

    protected static int FS_STICKY_WILD_MAX_REMAIN_TURN = 5;

    protected int[][] FS_RANDOM_SCRIPT = new int[][]{
            // Script 1
            {0, 0, 3, 0, 0},
            {0, 0, 1, 1, 1},
            {0, 0, 0, 2, 1},
            {0, 0, 1, 0, 2},
            {0, 0, 1, 2, 0},
            {0, 0, 0, 3, 0},
            {0, 0, 2, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 2, 0, 0},
            // Script 2
            {0, 0, 3, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 1, 0, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 2, 0, 1},
            {0, 1, 1, 0, 1},
            {0, 1, 1, 1, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 2, 1, 0, 0},
            // Script 3
            {0, 0, 3, 0, 0},
            {0, 2, 0, 1, 0},
            {0, 0, 3, 0, 0},
            {0, 2, 0, 1, 0},
            {0, 1, 1, 0, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 1, 1},
            {0, 1, 1, 0, 1},
            {0, 0, 1, 1, 1},
            {0, 1, 1, 1, 0},
            // Script 4
            {0, 0, 3, 0, 0},
            {0, 2, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 0, 1, 1},
            {0, 0, 1, 2, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 2, 0, 0},
            // Script 5
            {0, 0, 3, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 0, 3, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 2, 0, 1},
            {0, 1, 1, 0, 1},
            {0, 2, 0, 1, 0},
            {0, 1, 2, 0, 0},
            // Script 6
            {0, 0, 3, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 0, 1, 1, 1},
            {0, 0, 2, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 2, 0, 1, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 1, 1},
            {0, 0, 2, 1, 0},
            // Script7
            {0, 0, 3, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 1, 1, 0, 1},
            {0, 2, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 3, 0},
            {0, 2, 0, 0, 1},
            {0, 1, 1, 0, 1},
            {0, 0, 2, 1, 0},
            {0, 2, 1, 0, 0},
            // Script 8
            {0, 0, 3, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 0, 1, 1, 1},
            {0, 2, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 2, 1},
            {0, 0, 1, 1, 1},
            {0, 1, 0, 2, 0},
            {0, 2, 1, 0, 0},
            // Script 9
            {0, 0, 3, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 2, 0, 0},
            {0, 3, 0, 0, 0},
            {0, 2, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 1, 0, 1},
            {0, 2, 0, 0, 1},
            {0, 1, 1, 0, 1},
            {0, 1, 1, 1, 0},
            // Script 10
            {0, 0, 3, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 2, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 1, 0, 1},
            {0, 0, 1, 2, 0},
            {0, 2, 1, 0, 0},
            {0, 1, 2, 0, 0},
            {0, 0, 1, 2, 0},
            {0, 1, 0, 2, 0},

    };

    protected int[] getBaseMysteryFeatureWeight(SlotGameLogicBean gameSessionBean) {
        return BASE_MYSTERY_TYPE_WEIGHT;
    }

    protected static Map<String, RandomWeightUntil> mysteryRandomMap = new HashMap<>();

    protected int randomBaseGameMysteryType(SlotGameLogicBean gameSessionBean) {
        int baseGameMysteryType = -1;
        RandomWeightUntil random = mysteryRandomMap.get(gameSessionBean.getMmID() + "_" + gameSessionBean.getPercentage());
        if (random == null) {
            random = new RandomWeightUntil(getBaseMysteryFeatureWeight(gameSessionBean));
            mysteryRandomMap.put(gameSessionBean.getMmID() + "_" + gameSessionBean.getPercentage(), random);
        }
        int temp = random.getRandomResult();
        if (temp == 1) {
            baseGameMysteryType = RandomUtil.getRandomIndexFromArrayWithWeight(BASE_MYSTERY_FEATURE_WEIGHT) + 1;
        }
        return baseGameMysteryType;
    }

    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameSessionBean) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = modelFeatureBean.getSlotReels();
            int[][] reelsWeight = modelFeatureBean.getSlotReelsWeight();

            int baseGameMysteryType = randomBaseGameMysteryType(gameSessionBean);
            if (baseGameMysteryType > 0) {
                reels = modelFeatureBean.getOtherSlotReelsMap().get(BASE_GAME_MYSTERY_REELS_KEY);
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(BASE_GAME_MYSTERY_REELS_KEY);
            }
            int[] stopPosition = randomReelStopPosition(reelsWeight);
            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = true;
            int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot, baseGameMysteryType);
        }
        return baseSpinResult;
    }

    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameSessionBean, InputInfo inputFeedBean) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = modelFeatureBean.getSlotReels();
            int[][] reelsWeight = modelFeatureBean.getSlotReelsWeight();

            int baseGameMysteryType = randomBaseGameMysteryType(gameSessionBean);
            if (baseGameMysteryType > 0) {
                reels = modelFeatureBean.getOtherSlotReelsMap().get(BASE_GAME_MYSTERY_REELS_KEY);
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(BASE_GAME_MYSTERY_REELS_KEY);
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
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot, baseGameMysteryType);
        }
        return baseSpinResult;
    }

    protected int[][] getFSReels(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameSessionBean) {
        int[][] reels = null;
        if (freespinType == FREE_SPIN_TYPE_STICKY_WILD) {
            reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS2_KEY);
        } else if (freespinType == FREE_SPIN_TYPE_REELS_WILD) {
            reels = modelFeatureBean.getSlotFsReels();
        } else if (freespinType == FREE_SPIN_TYPE_POSITIONS_WILD) {
            reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS3_KEY);
        }
        return reels;
    }

    protected int[][] getFSReelsWeight(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameSessionBean) {
        int[][] reelsWeight = null;
        if (freespinType == FREE_SPIN_TYPE_STICKY_WILD) {
            reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS2_KEY);
        } else if (freespinType == FREE_SPIN_TYPE_REELS_WILD) {
            reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
        } else if (freespinType == FREE_SPIN_TYPE_POSITIONS_WILD) {
            reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS3_KEY);
        }
        return reelsWeight;
    }

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameSessionBean, boolean isSlot) {
        return computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot, -1);
    }

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameSessionBean, boolean isSlot, int baseGameMysteryType) {
        Model20260520SpinResult spinResult = new Model20260520SpinResult();
        Map<Integer, int[]> payLinesMap = getPayLines();

        int[] oldDisplaySymbols = displaySymbols.clone();

        int[] wildReels = null;
        int[] wildPositions = null;

        int baseGameMultiplier = 1;
        int freeSpinMultiplier = 1;
        if (isSlot) {
            freespinType = -1;
            freespinRandomIndex = -1;
            if (baseGameMysteryType == 1) {
                if (baseStickyWildReelRandomUntil == null) {
                    baseStickyWildReelRandomUntil = new RandomWeightUntil(BASE_WILD_REELS_WEIGHT);
                }
                int randomIndex = baseStickyWildReelRandomUntil.getRandomResult();
                wildReels = WILD_REELS[randomIndex];
                baseGameMultiplier = WILD_MUL[randomIndex];
            } else if (baseGameMysteryType == 2) {
                if (baseStickyWildRandomUntil == null) {
                    baseStickyWildRandomUntil = new RandomWeightUntil(BASE_STICKY_WILD_WEIGHT);
                }
                int randomIndex = baseStickyWildRandomUntil.getRandomResult();
                int[] baseStickyWildCount = BASE_STICKY_WILD[randomIndex];
                wildPositions = getCoverPositionsByCount(baseStickyWildCount);
            }
        } else {
            int fsSize = 0;
            List<SlotSpinResult> fsList = gameSessionBean.getSlotFsSpinResults();
            if (fsList != null && fsList.size() > 0) {
                fsSize = fsList.size();
                while (fsSize >= 10) {
                    fsSize -= 10;
                }
            }

            int reelCount = reelsCount();
            int rowCount = rowsCount();
            if (freespinType == FREE_SPIN_TYPE_STICKY_WILD) {
                int wildReelIndex = FS_STICKY_SCRIPT[freespinRandomIndex][fsSize] - 1;
                if (fsList != null && fsList.size() > 0 && fsList.get(fsList.size() - 1).getSlotWildPositions() != null) {
                    wildPositions = fsList.get(fsList.size() - 1).getSlotWildPositions().clone();
                }
                List<Integer> wildPositionList = new ArrayList<>();
                if (wildPositions != null) {
                    for (int position : wildPositions) {
                        wildPositionList.add(position);
                    }
                }
                if (fsList != null && fsList.size() >= FS_STICKY_WILD_MAX_REMAIN_TURN) {
                    wildPositionList.remove(0);
                }

                int[] randomRows = RandomUtil.getRandomIndex(rowCount, rowCount);
                for (int random : randomRows) {
                    int position = random * reelCount + wildReelIndex;
                    if (!wildPositionList.contains(position)) {
                        wildPositionList.add(position);
                        break;
                    }
                }
                wildPositions = StringUtil.list2Array(wildPositionList);
            } else if (freespinType == FREE_SPIN_TYPE_REELS_WILD) {
                if (freeSpinWRRandomUntil == null) {
                    freeSpinWRRandomUntil = new RandomWeightUntil(getFreeSpinWRWeight());
                }
                int random = freeSpinWRRandomUntil.getRandomResult();
                wildReels = WILD_REELS[random];
                freeSpinMultiplier = WILD_MUL[random];
            } else if (freespinType == FREE_SPIN_TYPE_POSITIONS_WILD) {
                int[] reelsWildCount = FS_RANDOM_SCRIPT[freespinRandomIndex * 10 + fsSize];
                wildPositions = getCoverPositionsByCount(reelsWildCount);
            }
        }
        if (wildReels != null) {
            coverDisplaySymbolsByReels(displaySymbols, wildReels, WILD_SYMBOL);
        }
        if (wildPositions != null) {
            coverDisplaySymbolsByPositions(displaySymbols, wildPositions, WILD_SYMBOL);
        }

        List<SlotSymbolHitResult> hitList = computeSymbols(gameSessionBean, displaySymbols, payLinesMap, isSlot);
        hitList = filterLineHit(hitList);
        if (isSlot) {
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult result : hitList) {
                    long hitAmount = result.getHitPay();
                    result.setHitPay(hitAmount * baseGameMultiplier);
                }
            }
        } else {
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult result : hitList) {
                    long hitAmount = result.getHitPay();
                    result.setHitPay(hitAmount * freeSpinMultiplier);
                }
            }
        }

        transferHitList(spinResult, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            spinResult.setBaseGameMul(baseGameMultiplier);
        } else {
            spinResult.setFsMul(freeSpinMultiplier);
        }

        spinResult.setBaseGameMysteryType(baseGameMysteryType);
        if (spinResult != null && wildReels != null) {
            spinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            spinResult.setSlotWildReels(wildReels);
        }
        if (spinResult != null && wildPositions != null) {
            spinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            spinResult.setSlotWildPositions(wildPositions);
        }
        return spinResult;
    }

    protected void computeUnNormalSymbol(SlotGameLogicBean gameSessionBean, SlotSymbol symbol, int hitCount, SlotSymbolHitResult hitResult, boolean inSlot) {
        super.computeUnNormalSymbol(gameSessionBean, symbol, hitCount, hitResult, inSlot);
        if (inSlot && symbol.getSymbolNumber() == SCATTER_SYMBOL && hitResult.isTriggerFs()) {
            randomFreespinType();
        }
    }

    protected void randomFreespinType() {
        if (fsRandom == null) {
            fsRandom = new RandomWeightUntil(getFreeSpinWeight());
        }
        freespinType = fsRandom.getRandomResult() + 1;
        if (freespinType == FREE_SPIN_TYPE_STICKY_WILD) {
            if (fsStickyRandom == null) {
                fsStickyRandom = new RandomWeightUntil(getFreeSpinStickyWeight());
            }
            freespinRandomIndex = fsStickyRandom.getRandomResult();
        } else if (freespinType == FREE_SPIN_TYPE_POSITIONS_WILD) {
            if (fsShiftingRandom == null) {
                fsShiftingRandom = new RandomWeightUntil(getFreeSpinRandomWeight());
            }
            freespinRandomIndex = fsShiftingRandom.getRandomResult();
        }
    }

    protected int[] getCoverPositionsByCount(int[] reelsWildCount) {
        int rowCount = rowsCount();
        int reelsCount = reelsCount();
        List<Integer> wildPositions = new ArrayList<>();
        for (int i = 0; i < reelsWildCount.length; i++) {
            if (reelsWildCount[i] > 0) {
                int[] index = RandomUtil.getRandomIndex(rowCount, reelsWildCount[i]);
                for (int j = 0; j < index.length; j++) {
                    int position = index[j] * reelsCount + i;
                    wildPositions.add(position);
                }
            }
        }
        int[] result = StringUtil.list2Array(wildPositions);
        Arrays.sort(result);
        return result;
    }

}
