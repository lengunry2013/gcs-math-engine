package com.gcs.game.engine.math.model20260201;

import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IWildPositionsChange;
import com.gcs.game.engine.slots.model.IWildReelsChange;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.CompressUtil;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.RecoverInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * model 20260201.
 * math doc: 20260201_Miss_Spooky_88%.xlsm
 */
public class Model20260201 extends BaseSlotModel implements IWildReelsChange, IWildPositionsChange {

    private static final int WILD_HIGH_SYMBOL = 1;

    public Model20260201() {
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

    protected long[][] getPayTable() {
        return new long[][]{{
                0, 10, 100, 300, 1000}, {
                0, 0, 75, 150, 300}, {
                0, 0, 75, 150, 300}, {
                0, 0, 75, 150, 300}, {
                0, 0, 40, 80, 150}, {

                0, 0, 20, 40, 80}, {
                0, 0, 10, 20, 40}, {
                0, 0, 10, 20, 40}, {
                0, 0, 5, 10, 20}, {
                0, 0, 5, 10, 20}, {

                0, 0, 0, 0, 0}, {
                0, 0, 0, 0, 0}, {
                0, 0, 0, 0, 0}, {
                0, 0, 0, 0, 0}, {
                0, 0, 0, 0, 0},};
    }

    protected String getPayLinesFileName() {
        return "G3_default_5x3X25.properties";
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

                0}, {
                0}, {
                0}, {
                0}, {
                0}};
    }

    protected void initGameSymbols() {
        // init base symbols
        initBaseSymbols(10, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        // init free spin  bonus symbol
        SlotBonusSymbol symbol11 = new SlotBonusSymbol();
        symbol11.setSymbolNumber(11);
        symbol11.setMinHitCount(3);
        symbol11.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BONUSINBG_FSINFS);
        symbol11.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol11.setWildSymbols(null);
        symbol11.setPay(new long[]{0, 0, 0, 0, 0});
        symbol11.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol11.setHitFsCounts(new int[]{0, 0, 10, 0, 0});
        symbol11.setBonusAsset("bonus1"); // different bonus set different asset.
        symbols.add(symbol11);

        // init match bonus symbol
        SlotBonusSymbol symbol12 = new SlotBonusSymbol();
        symbol12.setSymbolNumber(12);
        symbol12.setMinHitCount(3);
        symbol12.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BONUS);
        symbol12.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol12.setWildSymbols(null);
        symbol12.setPay(new long[]{0, 0, 0, 0, 0});
        symbol12.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol12.setHitFsCounts(new int[]{0, 0, 0, 0, 0});
        symbol12.setBonusAsset("bonus2"); // different bonus set different asset.
        symbols.add(symbol12);

    }

    public static final int[] BASE_MYSTERY_MUL = new int[]{
            1, 2, 3, 5, 10
    };

    protected int[] wildMysteryMultiplierWeight(int payback) {
        int[] result = new int[]{500, 250, 126, 124, 0};
        switch (payback) {
            case 8804:
                result = new int[]{500, 250, 126, 124, 0};
                break;
            default:
                break;

        }
        return result;
    }

    private static RandomWeightUntil baseGameMultiplierRandom = null;

    @Override
    protected int getBaseGameMultiplier(int[] displaySymbols, SlotGameLogicBean gameSessionBean) {
        if (baseGameMultiplierRandom == null) {
            int[] weight = wildMysteryMultiplierWeight(gameSessionBean.getPercentage());
            baseGameMultiplierRandom = new RandomWeightUntil(BASE_MYSTERY_MUL, weight);
        }
        return baseGameMultiplierRandom.getRandomResult();
    }

    public static final String FREE_SPIN_REELS2_KEY = "freespin_W";
    public static final String FREE_SPIN_REELS3_KEY = "freespin_G";

    @Override
    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameSessionBean) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = modelFeatureBean.getSlotFsReels();
            ;
            int[][] reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
            if (gameSessionBean.getSlotBonusResult() != null) {
                SlotChoiceFSBonusResult bonus = (SlotChoiceFSBonusResult) gameSessionBean.getSlotBonusResult();
                int freespinType = bonus.getFsType();
                if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_PERSISTENT_WILD) {
                    reels = modelFeatureBean.getSlotFsReels();
                    reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
                } else if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_TWO_WILD_REELS) {
                    reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS2_KEY);
                    reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS2_KEY);
                } else if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_THREE_SHIFTING_WILD) {
                    reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS3_KEY);
                    reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS3_KEY);
                }
            }
            int[] stopPosition = randomReelStopPosition(reelsWeight);

            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = false;
            int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot);
        }
        return baseSpinResult;
    }

    @Override
    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean, InputInfo inputFeedBean, RecoverInfo recoverInfo) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = modelFeatureBean.getSlotFsReels();
            int[][] reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
            if (gameLogicBean.getSlotBonusResult() != null) {
                SlotChoiceFSBonusResult bonus = (SlotChoiceFSBonusResult) gameLogicBean.getSlotBonusResult();
                int freespinType = bonus.getFsType();
                if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_PERSISTENT_WILD) {
                    reels = modelFeatureBean.getSlotFsReels();
                    reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
                } else if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_TWO_WILD_REELS) {
                    reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS2_KEY);
                    reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS2_KEY);
                } else if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_THREE_SHIFTING_WILD) {
                    reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS3_KEY);
                    reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS3_KEY);
                }
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
            if (recoverInfo != null) {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot, recoverInfo);
            } else {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
            }
        }
        return baseSpinResult;
    }


    private static final int[] WIND_FREESPIN_WILD_REEL_WEIGHT = new int[]{
            836, 800, 800, 300, 200, 100, 10, 10, 11, 23, 10,
            800, 500, 400, 200};

    protected int[] getFsWildReelsWeight() {
        return WIND_FREESPIN_WILD_REEL_WEIGHT;
    }

    private static final int[][] WIND_FREESPIN_WILD_REEL = new int[][]{
            {4, 5}, {3, 5}, {2, 5}, {1, 5}, {3, 4}, {2, 4},
            {1, 4}, {2, 3}, {1, 3}, {1, 2}, {5}, {4},
            {3}, {2}, {1}};

    private static RandomWeightUntil fsWildReelsRandom = null;

    @Override
    public int[] computeWildReels(SlotGameLogicBean gameSessionBean, int[] displaySymbols, boolean isSlot) {
        int[] result = null;
        if (!isSlot) {
            if (gameSessionBean.getSlotBonusResult() != null) {
                SlotChoiceFSBonusResult bonus = (SlotChoiceFSBonusResult) gameSessionBean.getSlotBonusResult();
                int freespinType = bonus.getFsType();
                if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_TWO_WILD_REELS) {
                    if (fsWildReelsRandom == null) {
                        fsWildReelsRandom = new RandomWeightUntil(getFsWildReelsWeight());
                    }
                    int randomIndex = fsWildReelsRandom.getRandomResult();
                    result = new int[WIND_FREESPIN_WILD_REEL[randomIndex].length];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = WIND_FREESPIN_WILD_REEL[randomIndex][i] - 1;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int[] computeWildReels(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, RecoverInfo recoverInfo) {
        int[] result = null;
        if (!isSlot) {
            if (recoverInfo != null) {
                if (gameLogicBean.getSlotBonusResult() != null) {
                    SlotChoiceFSBonusResult bonus = (SlotChoiceFSBonusResult) gameLogicBean.getSlotBonusResult();
                    int freespinType = bonus.getFsType();
                    if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_TWO_WILD_REELS) {
                        result = fsRecover(recoverInfo);
                    }
                }
            } else {
                result = computeWildReels(gameLogicBean, displaySymbols, isSlot);
            }
        }
        return result;
    }

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameSessionBean, boolean isSlot) {
        SlotSpinResult baseSpinResult = super.computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot);
        fsCompute(gameSessionBean, baseSpinResult, stopPosition, isSlot);
        return baseSpinResult;
    }

    private void fsCompute(SlotGameLogicBean gameSessionBean, SlotSpinResult baseSpinResult, int[] stopPosition, boolean isSlot) {
        long recoverData = -1;
        if (!isSlot) {
            if (gameSessionBean.getSlotBonusResult() != null) {
                SlotChoiceFSBonusResult bonus = (SlotChoiceFSBonusResult) gameSessionBean.getSlotBonusResult();
                int freespinType = bonus.getFsType();
                if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_TWO_WILD_REELS) {
                    int[] wildReels = baseSpinResult.getSlotWildReels();
                    if (wildReels != null && wildReels.length == 1) {
                        int wildReel = wildReels[0] + 1;
                        int[] hitSymbolCount = baseSpinResult.getHitSlotSymbolCount();
                        int[] hitSymbol = baseSpinResult.getHitSlotSymbols();
                        int[] hitMultiplier = baseSpinResult.getHitSlotMuls();
                        long[] hitAmount = baseSpinResult.getHitSlotPays();
                        long payAmount = 0;
                        if (hitSymbolCount != null) {
                            for (int i = 0; i < hitSymbolCount.length; i++) {
                                int count = hitSymbolCount[i];
                                if (count >= wildReel && hitSymbol[i] <= 10) {
                                    hitMultiplier[i] = 2; // MULTIPLIER
                                    hitAmount[i] *= 2;
                                }
                                payAmount += hitAmount[i];
                            }
                        }
                        baseSpinResult.setSlotPay(payAmount);
                    }
                    recoverData = CompressUtil.compressWith4Bits(stopPosition, StringUtil.array2IntegerList(wildReels));
                } else {
                    recoverData = CompressUtil.compressWith4Bits(stopPosition, StringUtil.array2IntegerList(baseSpinResult.getSlotWildPositions()));
                }
            }
        } else {
            //recovery/recall param compress zip INT64(long)
            recoverData = CompressUtil.compressToLong(stopPosition, baseSpinResult.getBaseGameMul());
        }
        baseSpinResult.setRecoverData(String.valueOf(recoverData));
    }

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameSessionBean, boolean isSlot, RecoverInfo recoverInfo) {
        SlotSpinResult baseSpinResult = super.computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot, recoverInfo);
        fsCompute(gameSessionBean, baseSpinResult, stopPosition, isSlot);
        return baseSpinResult;
    }


    private int[][] GROUND_FREESPIN_SCRIPT = new int[][]{
            // Script 1
            {0, 0, 3, 0, 0},
            {0, 0, 1, 1, 1},
            {0, 0, 0, 2, 1},
            {0, 0, 1, 0, 2},
            {0, 0, 1, 2, 0},
            {0, 0, 0, 3, 0},
            {0, 0, 2, 1, 0},
            {0, 1, 1, 1, 0},
            {1, 1, 1, 0, 0},
            {0, 1, 2, 0, 0},
            // Script 2
            {0, 0, 3, 0, 0},
            {0, 1, 1, 1, 0},
            {1, 1, 0, 0, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 2, 0, 1},
            {0, 1, 1, 0, 1},
            {1, 1, 0, 1, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 2, 1, 0, 0},
            // Script 3
            {0, 0, 3, 0, 0},
            {0, 2, 0, 1, 0},
            {0, 0, 3, 0, 0},
            {0, 2, 0, 1, 0},
            {1, 1, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 1, 1},
            {0, 1, 1, 0, 1},
            {0, 0, 1, 1, 1},
            {0, 1, 1, 1, 0},
            // Script 4
            {0, 0, 3, 0, 0},
            {0, 2, 1, 0, 0},
            {2, 1, 0, 0, 0},
            {1, 1, 1, 0, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 0, 1, 1},
            {0, 0, 1, 2, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 2, 0},
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
            {1, 1, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 1, 1},
            {0, 1, 1, 1, 0},
            // Script7
            {0, 0, 3, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 1, 1, 0, 1},
            {0, 2, 0, 1, 0},
            {1, 1, 1, 0, 0},
            {2, 0, 1, 0, 0},
            {1, 2, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 2, 1, 0},
            {0, 2, 1, 0, 0},
            // Script 8
            {0, 0, 3, 0, 0},
            {0, 1, 0, 2, 0},
            {0, 0, 1, 1, 1},
            {0, 2, 1, 0, 0},
            {2, 0, 1, 0, 0},
            {1, 2, 0, 0, 0},
            {0, 1, 1, 1, 0},
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
            {1, 1, 0, 1, 0},
            {1, 1, 1, 0, 0},
            {0, 2, 0, 1, 0},
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

    public static final int[] GROUND_FREESPIN_SCRIPT_WEIGHT = new int[]{
            284, 150, 25, 480, 10, 16, 200, 250, 435, 150};

    private static final int[][] LIGHTNING_FREESPIN_SCRIPT = new int[][]{
            {5, 4, 5, 3, 4, 4, 1, 3, 4, 3},
            {5, 2, 1, 5, 4, 4, 5, 4, 2, 2},
            {1, 3, 5, 5, 4, 4, 5, 2, 2, 3},
            {4, 3, 1, 5, 4, 1, 5, 2, 2, 5},
            {4, 2, 5, 2, 4, 2, 1, 2, 1, 2},
            {4, 2, 1, 4, 1, 2, 5, 2, 2, 3},
            {3, 2, 5, 1, 1, 2, 4, 4, 2, 4},
            {2, 1, 3, 3, 5, 5, 2, 4, 2, 4},
            {3, 2, 2, 1, 1, 4, 4, 4, 2, 2},
            {2, 2, 3, 3, 1, 5, 4, 4, 2, 2},
    };

    private static final int LIGHTNING_FREESPIN_WILD_SYMBOL_MAX_REMAIN_TURN = 5;

    public static final int[] LIGHTNING_FREESPIN_SCRIPT_WEIGHT = new int[]{
            528, 300, 100, 120, 150, 62, 60, 60, 60, 60};

    @Override
    public int[] computeWildPositions(SlotGameLogicBean gameSessionBean, int[] displaySymbols, boolean isSlot) {
        int[] result = null;
        if (!isSlot) {
            if (gameSessionBean.getSlotBonusResult() != null) {
                SlotChoiceFSBonusResult bonus = (SlotChoiceFSBonusResult) gameSessionBean.getSlotBonusResult();
                int freespinType = bonus.getFsType();
                int randomIndex = bonus.getRandomIndex4FS();
                int fsSize = 0;
                List<SlotSpinResult> fsList = gameSessionBean.getSlotFsSpinResults();
                if (fsList != null && !fsList.isEmpty()) {
                    fsSize = fsList.size();
                    while (fsSize >= 10) {
                        fsSize -= 10;
                    }
                }
                if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_THREE_SHIFTING_WILD) {
                    int[] reelsWildCount = GROUND_FREESPIN_SCRIPT[randomIndex * 10 + fsSize];
                    List<Integer> wildPositions = new ArrayList<>();
                    int rowCount = rowsCount();
                    int reelsCount = reelsCount();
                    for (int i = 0; i < reelsWildCount.length; i++) {
                        if (reelsWildCount[i] > 0) {
                            int[] index = RandomUtil.getRandomIndex(rowCount, reelsWildCount[i]);
                            for (int j = 0; j < index.length; j++) {
                                int position = index[j] * reelsCount + i;
                                wildPositions.add(position);
                            }
                        }
                    }
                    result = StringUtil.list2Array(wildPositions);
                    Arrays.sort(result);

                } else if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_PERSISTENT_WILD) {
                    int[] lightingScript = LIGHTNING_FREESPIN_SCRIPT[randomIndex];
                    int[] wildPositions = null;
                    if (fsList != null && !fsList.isEmpty()) {
                        wildPositions = fsList.get(fsList.size() - 1).getSlotWildPositions();
                    }
                    List<Integer> wildPositionList = new ArrayList<>();
                    if (wildPositions != null) {
                        for (int position : wildPositions) {
                            wildPositionList.add(position);
                        }
                    }
                    if (fsList != null && fsList.size() >= LIGHTNING_FREESPIN_WILD_SYMBOL_MAX_REMAIN_TURN) {
                        wildPositionList.remove(0);
                    }
                    int wildReelNo = lightingScript[fsSize];
                    int rowCount = rowsCount();
                    int reelCount = reelsCount();

                    int[] randomRows = RandomUtil.getRandomIndex(rowCount, rowCount);
                    for (int random : randomRows) {
                        int position = random * reelCount + wildReelNo - 1;
                        if (!wildPositionList.contains(position)) {
                            wildPositionList.add(position);
                            break;
                        }
                    }
                    result = StringUtil.list2Array(wildPositionList);
                }
            }
        }
        return result;
    }

    @Override
    public int[] computeWildPositions(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, RecoverInfo recoverInfo) {
        int[] result = null;
        if (!isSlot) {
            if (recoverInfo != null) {
                if (gameLogicBean.getSlotBonusResult() != null) {
                    SlotChoiceFSBonusResult bonus = (SlotChoiceFSBonusResult) gameLogicBean.getSlotBonusResult();
                    int freespinType = bonus.getFsType();
                    if (freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_THREE_SHIFTING_WILD ||
                            freespinType == Model20260201FSBonus.FREE_SPIN_TYPE_PERSISTENT_WILD) {
                        result = fsRecover(recoverInfo);
                    }
                }
            } else {
                result = computeWildPositions(gameLogicBean, displaySymbols, isSlot);
            }
        }
        return result;
    }

    protected int[] fsRecover(RecoverInfo recoverInfo) {
        if (recoverInfo != null) {
            long fsRecoverData = Long.parseLong(recoverInfo.getRecoverData());
            if (fsRecoverData > 0) {
                int[] fsPosition = new int[reelsCount()];
                List<Integer> wildPositions = new ArrayList<>();
                CompressUtil.decompressWith4Bits(fsRecoverData, fsPosition, wildPositions);
                return StringUtil.list2Array(wildPositions);
            }
        }
        return null;
    }

    public int wildSymbolNo() {
        return WILD_HIGH_SYMBOL;
    }

    protected SlotSymbolHitResult setHitResult(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int symbolNumber, long line, long betPerLine, int[] hitPosition, int hitCount, boolean inSlot) {
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(symbolNumber);
        hitResult.setHitSymbolSound(symbolNumber);
        hitResult.setHitLine((int) line);
        hitResult.setHitMul(1);
        hitResult.setHitPosition(hitPosition);
        hitResult.setHitCount(hitCount);
        //compute bet=2,pay=1
        if (inSlot) {
            hitResult.setHitPay(symbol.getPay()[hitCount - 1] * betPerLine / 2);
        } else {
            hitResult.setHitPay(symbol.getPayInFreeSpin()[hitCount - 1] * betPerLine / 2);
        }
        computeUnNormalSymbol(gameLogicBean, symbol, hitCount, hitResult, inSlot);
        return hitResult;
    }

}
