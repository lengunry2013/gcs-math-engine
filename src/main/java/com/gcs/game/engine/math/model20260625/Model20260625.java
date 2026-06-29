package com.gcs.game.engine.math.model20260625;


import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IWildReelsChange;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.RecoverInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BigDaddy Game Bones&Rose
 */
public class Model20260625 extends BaseSlotModel implements IWildReelsChange {

    private static final int WILD_SYMBOL = 1;

    public static final String BASE_REELS_KEY = "R2";

    public static final String FREE_SPIN_REELS_KEY = "freespin_JP";

    public static final int SC1_SYMBOL = 12;

    public static final int SC2_SYMBOL = 13;

    public static final int SC3_SYMBOL = 14;

    public static final int FS_TIME = 8;
    public static final int FS_EXTEND_REELS = 1;
    public static final int FS_JACKPOT_BONUS = 2;
    public static final int FS_SUPER_BONUS = 3;
    public static final int FS_MUL = 2;


    @Override
    protected int reelsCount() {
        return 5;
    }

    @Override
    protected int rowsCount() {
        return 3;
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

    protected int reelsCountInFreeSpin() {
        return 5;
    }

    protected int rowCountInFreeSpin() {
        return 6;
    }


    @Override
    protected String getPayLinesFileName() {
        return "G3_default_5x6x50.properties";
    }

    @Override
    protected long[][] getPayTable() {
        return new long[][]{
                {0, 0, 0, 0, 0},     // 1
                {0, 0, 50, 150, 500}, // 2
                {0, 0, 40, 100, 400},  // 3
                {0, 0, 30, 75, 300},  // 4
                {0, 0, 20, 50, 200},  // 5

                {0, 0, 15, 30, 100},   //6
                {0, 0, 10, 20, 40},   //7
                {0, 0, 8, 15, 30},   //8
                {0, 0, 8, 15, 30},    //9
                {0, 0, 5, 10, 25},    //10

                {0, 0, 5, 10, 25},     //11
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
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);

        SlotBonusSymbol symbol14 = new SlotBonusSymbol();
        symbol14.setSymbolNumber(SC3_SYMBOL);
        symbol14.setMinHitCount(3);
        symbol14.setSymbolType(SlotEngineConstant.SYMBOL_TYPE_BONUS);
        symbol14.setSymbolHitType(SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER);
        symbol14.setWildSymbols(null);
        symbol14.setPay(new long[]{0, 0, 0, 0, 0});
        symbol14.setPayInFreeSpin(new long[]{0, 0, 0, 0, 0});
        symbol14.setHitFsCounts(new int[]{0, 0, 0, 0, 0});
        symbol14.setBonusAsset("bonus");
        symbols.add(symbol14);
    }

    private static RandomWeightUntil baseReelsRandom = null;

    protected int[] getBaseReelsWeight(int payBack) {
        int[] baseReelsWeight = new int[]{820, 180};
        switch (payBack) {
            case 8800:
                baseReelsWeight = new int[]{820, 180};
                break;
            case 9008:
                baseReelsWeight = new int[]{820, 180};
                break;
            default:
                break;
        }
        return baseReelsWeight;
    }

    private static final int[] SC_WEIGHT = new int[]{1, 1};

    protected int[] getScWeight() {
        return SC_WEIGHT;
    }
    protected int[] getSc1Weight(int payBack) {
        int[] sc1TriggerWeight = SC1_TRIGGER_WEIGHT;
        switch (payBack) {
            case 8800:
                sc1TriggerWeight = SC1_TRIGGER_WEIGHT;
                break;
            case 9008:
                sc1TriggerWeight = new int[]{958, 42};
                break;
            default:
                break;
        }
        return sc1TriggerWeight;
    }

    protected int[] getSc2Weight(int payBack) {
        int[] sc2TriggerWeight = SC2_TRIGGER_WEIGHT;
        switch (payBack) {
            case 8800:
                sc2TriggerWeight = SC2_TRIGGER_WEIGHT;
                break;
            case 9008:
                sc2TriggerWeight = new int[]{958, 42};
                break;
            default:
                break;
        }
        return sc2TriggerWeight;
    }

    public static final int[] SC1_TRIGGER_WEIGHT = new int[]{961, 39};
    public static final int[] SC2_TRIGGER_WEIGHT = new int[]{960, 40};
    public static final int[] SC12_TRIGGER_WEIGHT = new int[]{898, 10, 10, 82};
    private static RandomWeightUntil scRandom = null;
    private static RandomWeightUntil fs1Random = null;
    private static RandomWeightUntil fs2Random = null;
    private static RandomWeightUntil fs3Random = null;

    public static final int[] SC_AWARD = new int[]{
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2
    };
    public static final int[][] SC_AWARD_WEIGHT = new int[][]{
            {8110, 500, 300, 200, 100, 100, 100, 80, 60, 50, 300, 100},
            {8110, 500, 300, 200, 100, 100, 100, 80, 60, 50, 300, 100},
            {8110, 500, 300, 200, 100, 100, 100, 80, 60, 50, 300, 100}
    };

    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        Model20260625SpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = getReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getReelsWeight(modelFeatureBean, gameLogicBean);
            if (baseReelsRandom == null) {
                baseReelsRandom = new RandomWeightUntil(getBaseReelsWeight(gameLogicBean.getPercentage()));
            }
            int randomIndex = baseReelsRandom.getRandomResult();
            if (randomIndex == 1) {
                reels = modelFeatureBean.getOtherSlotReelsMap().get(BASE_REELS_KEY);
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(BASE_REELS_KEY);
            }
            int[] stopPosition = randomReelStopPosition(reelsWeight);
            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = true;
            int[] displaySymbols = getDisplaySymbols(reels, stopPosition);
            displaySymbols = getScChangeDisplaySymbols(displaySymbols, isSlot, gameLogicBean);
            baseSpinResult = (Model20260625SpinResult) computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
            gameLogicBean.setBaseReelsType(randomIndex);
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
    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean, InputInfo inputFeedBean, RecoverInfo recoverInfo) {
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
            //TODO Recover baseReelsType
            int[] stopPosition = null;
            if (inputFeedBean != null && inputFeedBean.getInputPosition() != null && !inputFeedBean.getInputPosition().isEmpty()) {
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
            //TODO SC Random displaySymbols
            if (recoverInfo != null) {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot, recoverInfo);
            } else {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
            }
        }
        return baseSpinResult;
    }

    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int fsType = ((Model20260625SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
            int[][] reels = getFSReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getFSReelsWeight(modelFeatureBean, gameLogicBean);
            if (fsType == FS_JACKPOT_BONUS) {
                reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS_KEY);
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS_KEY);
            }
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
            int[] displaySymbols = null;
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                displaySymbols = getDisplaySymbols(reels, stopPosition, reelsCountInFreeSpin(), rowCountInFreeSpin());
            } else {
                displaySymbols = getDisplaySymbols(reels, stopPosition);
            }
            baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
        }
        return baseSpinResult;
    }

    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean, InputInfo inputFeedBean, RecoverInfo recoverInfo) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int fsType = ((Model20260625SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
            int[][] reels = getFSReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getFSReelsWeight(modelFeatureBean, gameLogicBean);
            if (fsType == FS_JACKPOT_BONUS) {
                reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS_KEY);
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS_KEY);
            }
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
            int[] displaySymbols = null;
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                displaySymbols = getDisplaySymbols(reels, stopPosition, reelsCountInFreeSpin(), rowCountInFreeSpin());
            } else {
                displaySymbols = getDisplaySymbols(reels, stopPosition);
            }
            if (recoverInfo != null) {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot, recoverInfo);
            } else {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
            }
        }
        return baseSpinResult;
    }

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260625SpinResult result = new Model20260625SpinResult();
        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);

        hitList = filterLineHit(hitList);
        computeLineMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        //baseGame Trigger Fs
        if (isSlot) {
            int fsType = -1;
            int sc1Count = computeScPosition(displaySymbols, SC1_SYMBOL);
            int sc2Count = computeScPosition(displaySymbols, SC2_SYMBOL);
            if (sc1Count > 0 && sc2Count > 0) {
                if (fs3Random == null) {
                    fs3Random = new RandomWeightUntil(SC12_TRIGGER_WEIGHT);
                }
                int randomIndex = fs3Random.getRandomResult();
                if (randomIndex > 0) {
                    fsType = randomIndex;
                }
                if (randomIndex == FS_EXTEND_REELS) {
                    setHitScatter(SC1_SYMBOL, sc1Count, displaySymbols, fsType, hitList);
                } else if (randomIndex == FS_JACKPOT_BONUS) {
                    setHitScatter(SC2_SYMBOL, sc2Count, displaySymbols, fsType, hitList);
                } else if (randomIndex == FS_SUPER_BONUS) {
                    int scatterCount = sc1Count + sc2Count;
                    setHitScatter(SC1_SYMBOL, scatterCount, displaySymbols, fsType, hitList);
                }
            } else if (sc1Count > 0) {
                if (fs1Random == null) {
                    fs1Random = new RandomWeightUntil(getSc1Weight(gameLogicBean.getPercentage()));
                }
                int randomIndex = fs1Random.getRandomResult();
                if (randomIndex == 1) {
                    fsType = FS_EXTEND_REELS;
                    setHitScatter(SC1_SYMBOL, sc1Count, displaySymbols, fsType, hitList);
                }
            } else if (sc2Count > 0) {
                if (fs2Random == null) {
                    fs2Random = new RandomWeightUntil(getSc2Weight(gameLogicBean.getPercentage()));
                }
                int randomIndex = fs2Random.getRandomResult();
                if (randomIndex == 1) {
                    fsType = FS_JACKPOT_BONUS;
                    setHitScatter(SC2_SYMBOL, sc2Count, displaySymbols, fsType, hitList);
                }
            }
            result.setFsType(fsType);
        } else {
            //Fs random SC feature
            Model20260625SpinResult baseSpinResult = (Model20260625SpinResult) gameLogicBean.getSlotSpinResult();
            int fsType = baseSpinResult.getFsType();
            int scSymbol = SC1_SYMBOL;
            if (fsType == FS_JACKPOT_BONUS) {
                scSymbol = SC2_SYMBOL;
            }
            List<Integer> scPositions = computeFsScPosition(displaySymbols, scSymbol);
            int[] hitScatterPay = new int[displaySymbols.length];
            if (!scPositions.isEmpty()) {
                RandomWeightUntil randomWeightUntil = new RandomWeightUntil(SC_AWARD_WEIGHT[fsType - 1]);
                for (int position : scPositions) {
                    int scAwardIndex = randomWeightUntil.getRandomResult();
                    int scAward = setFsHitScatter(gameLogicBean, scAwardIndex, position, scSymbol, hitList);
                    hitScatterPay[position - 1] = scAward;
                }
            }
            result.setHitSlotScatterPay(hitScatterPay);
        }

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = (Model20260625SpinResult) transferHitList(result, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            result.setBaseGameMul(baseGameMultiplier);
        }
        if (!isSlot) {
            result.setFsMul(freeSpinMultiplier);
        }
        return result;
    }

    protected List<SlotSymbolHitResult> computeSymbols(SlotGameLogicBean gameLogicBean, int[] displaySymbols, Map<Integer, int[]> payLinesMap, boolean isSlot) {
        long betPerLine = gameLogicBean.getBet();
        long lines = 50;
        long totalBet = gameLogicBean.getSumBetCredit();
        List<SlotSymbolHitResult> hitList = new ArrayList<>();
        for (SlotSymbol symbol : symbols) {
            if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT) {
                List<SlotSymbolHitResult> tempList = computeLineSymbolLeft2Right(gameLogicBean, symbol, displaySymbols, payLinesMap, betPerLine, lines, isSlot);
                if (tempList != null && !tempList.isEmpty()) {
                    hitList.addAll(tempList);
                }
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER) {
                SlotSymbolHitResult hitResult = computeScatterSymbol(gameLogicBean, symbol, displaySymbols, totalBet, isSlot, false);
                if (hitResult != null) {
                    hitList.add(hitResult);
                }
            }
        }
        return hitList;
    }


    protected int computeFreeSpinMultiplier(int[] displaySymbols, List<SlotSymbolHitResult> hitList, boolean isSlot, SlotGameLogicBean gameLogicBean) {
        int freeSpinMultiplier = 1;
        if (!isSlot) {
            freeSpinMultiplier = FS_MUL;
            if (hitList != null && !hitList.isEmpty()) {
                for (SlotSymbolHitResult result : hitList) {
                    int hitSymbol = result.getHitSymbol();
                    if (hitSymbol < SC1_SYMBOL) {
                        long hitPay = result.getHitPay();
                        result.setHitPay(hitPay * freeSpinMultiplier);
                    }
                }
            }
        }
        return freeSpinMultiplier;
    }


    //scatter symbol hit
    private int setFsHitScatter(SlotGameLogicBean gameLogicBean, int scAwardIndex, int position, int scatterSymbol, List<SlotSymbolHitResult> hitList) {
        int scAward = 0;
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitCount(1);
        hitResult.setHitPosition(new int[]{position, 0, 0, 0, 0});
        if (scAwardIndex < 10) {
            hitResult.setHitSymbol(scatterSymbol);
            hitResult.setHitSymbolSound(scatterSymbol);
            scAward = (int) (SC_AWARD[scAwardIndex] * gameLogicBean.getSumBetCredit());
            hitResult.setHitPay(scAward);
        } else if (scAwardIndex < 12) {
            //distinguish different free spin counts +1 FREE->1001 or +2 FREE 1002
            hitResult.setHitSymbol(scatterSymbol);
            hitResult.setHitSymbolSound(scAwardIndex - 9 + 1000);
            hitResult.setHitPay(0);
            SlotSymbolHitResult existTriggerFsSymbol = getExistTriggerFs(hitList);
            scAward = SC_AWARD[scAwardIndex];
            if (existTriggerFsSymbol != null) {
                int fsTimes = scAward + existTriggerFsSymbol.getTriggerFsCounts();
                existTriggerFsSymbol.setTriggerFsCounts(fsTimes);
            } else {
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(scAward);
            }
        }
        hitList.add(hitResult);
        return scAward;
    }

    private SlotSymbolHitResult getExistTriggerFs(List<SlotSymbolHitResult> hitList) {
        if (hitList != null && !hitList.isEmpty()) {
            for (SlotSymbolHitResult hitResult : hitList) {
                if (hitResult.isTriggerFs()) {
                    return hitResult;
                }
            }
        }
        return null;
    }


    public List<Integer> computeFsScPosition(int[] displaySymbols, int scSymbol) {
        List<Integer> scPositions = new ArrayList<>();
        for (int i = 0; i < displaySymbols.length; i++) {
            if (displaySymbols[i] == scSymbol) {
                scPositions.add(i + 1);
            }
        }
        return scPositions;
    }

    private void setHitScatter(int scatterSymbol, int scatterCount, int[] displaySymbols, int fsType, List<SlotSymbolHitResult> hitList) {
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitSymbol(scatterSymbol);
        hitResult.setHitSymbolSound(scatterSymbol);
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitCount(scatterCount);
        hitResult.setHitPay(0);
        hitResult.setHitPosition(computeScatterHitPosition(displaySymbols, scatterSymbol, fsType));
        hitResult.setTriggerFs(true);
        hitResult.setTriggerFsCounts(FS_TIME);
        hitList.add(hitResult);
    }

    private int[] computeScatterHitPosition(int[] displaySymbols, int scSymbol, int fsType) {
        int reelsCount = reelsCount();
        int rowCount = rowsCount();
        int[] hitPositions = new int[reelsCount];
        for (int i = 0; i < reelsCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                int tempSymbol = displaySymbols[i + j * reelsCount];
                if (fsType == FS_SUPER_BONUS) {
                    if (tempSymbol == SC1_SYMBOL || tempSymbol == SC2_SYMBOL) {
                        hitPositions[i] = i + j * reelsCount + 1;
                    }
                } else {
                    if (tempSymbol == scSymbol) {
                        hitPositions[i] = i + j * reelsCount + 1;
                    }
                }
            }
        }
        return hitPositions;
    }

    public int computeScPosition(int[] displaySymbols, int scSymbol) {
        int scatterCount = 0;
        for (int symbol : displaySymbols) {
            if (symbol == scSymbol) {
                scatterCount++;
            }
        }
        return scatterCount;
    }

    private int[] getScChangeDisplaySymbols(int[] displaySymbols, boolean isSlot, SlotGameLogicBean gameLogicBean) {
        int[] newDisplaySymbols = displaySymbols.clone();
        if (isSlot) {
            boolean isScatter = false;
            for (int symbol : displaySymbols) {
                if (symbol == SC1_SYMBOL) {
                    isScatter = true;
                    break;
                }
            }
            //scatter random sc change to sc1 or sc2
            //TODO Recover
            if (isScatter) {
                if (scRandom == null) {
                    scRandom = new RandomWeightUntil(getScWeight());
                }
                for (int i = 0; i < displaySymbols.length; i++) {
                    if (displaySymbols[i] == SC1_SYMBOL) {
                        int scIndex = scRandom.getRandomResult();
                        if (scIndex == 1) {
                            newDisplaySymbols[i] = SC2_SYMBOL;
                        }
                    }
                }
            }

        }
        return newDisplaySymbols;
    }

    @Override
    public int wildSymbolNo() {
        return WILD_SYMBOL;
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
            coverDisplaySymbolsByWildReels(gameLogicBean, displaySymbols, wildReels, wildSymbolNo);
        }
        baseSpinResult = computeSpinResult(stopPosition, displaySymbols, payLinesMap, gameLogicBean, isSlot);
        if (baseSpinResult != null && wildReels != null) {
            baseSpinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            baseSpinResult.setSlotWildReels(wildReels);
        }
        return baseSpinResult;
    }

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameLogicBean, boolean isSlot, RecoverInfo recoverInfo) {
        SlotSpinResult baseSpinResult;
        Map<Integer, int[]> payLinesMap = getPayLines();

        int[] oldDisplaySymbols = null;
        int[] wildReels = null;
        if (this instanceof IWildReelsChange) {
            oldDisplaySymbols = displaySymbols.clone();
            if (recoverInfo != null) {
                wildReels = ((IWildReelsChange) this).computeWildReels(gameLogicBean, displaySymbols, isSlot, recoverInfo);
            } else {
                wildReels = ((IWildReelsChange) this).computeWildReels(gameLogicBean, displaySymbols, isSlot);
            }
            int wildSymbolNo = ((IWildReelsChange) this).wildSymbolNo();
            coverDisplaySymbolsByWildReels(gameLogicBean, displaySymbols, wildReels, wildSymbolNo);
        }
        if (recoverInfo != null) {
            baseSpinResult = computeSpinResult(stopPosition, displaySymbols, payLinesMap, gameLogicBean, isSlot, recoverInfo);
        } else {
            baseSpinResult = computeSpinResult(stopPosition, displaySymbols, payLinesMap, gameLogicBean, isSlot);
        }
        if (baseSpinResult != null && wildReels != null) {
            baseSpinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            baseSpinResult.setSlotWildReels(wildReels);
        }
        return baseSpinResult;
    }

    @Override
    public int[] computeWildReels(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot) {
        if (!isSlot) {
            int fsType = ((Model20260625SpinResult) gameLogicCache.getSlotSpinResult()).getFsType();
            if (fsType == FS_JACKPOT_BONUS || fsType == FS_SUPER_BONUS) {
                return getWildReels(fsType, displaySymbols);
            }
        }
        return null;
    }

    private int[] getWildReels(int fsType, int[] displaySymbols) {
        int reelsCount = reelsCount();
        int rowCount = rowsCount();
        if (fsType == FS_SUPER_BONUS) {
            reelsCount = reelsCountInFreeSpin();
            rowCount = rowCountInFreeSpin();
        }
        List<Integer> wildReels = new ArrayList<>();
        for (int i = 0; i < reelsCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                if (displaySymbols[i + j * reelsCount] == WILD_SYMBOL) {
                    wildReels.add(i);
                }
            }
        }
        return StringUtil.ListToIntegerArray(wildReels);
    }

    @Override
    public int[] computeWildReels(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, RecoverInfo recoverInfo) {
        return computeWildReels(gameLogicBean, displaySymbols, isSlot);
    }

    protected void coverDisplaySymbolsByWildReels(SlotGameLogicBean gameLogicBean, int[] displaySymbols, int[] reelsIndex, int coverSymbol) {
        if (reelsIndex != null) {
            int fsType = ((Model20260625SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
            int rowsCount = rowsCount();
            int reelsCount = reelsCount();
            if (fsType == FS_SUPER_BONUS) {
                reelsCount = reelsCountInFreeSpin();
                rowsCount = rowCountInFreeSpin();
            }
            for (int wildReelIndex : reelsIndex) {
                for (int j = 0; j < rowsCount; j++) {
                    displaySymbols[wildReelIndex + j * reelsCount] = coverSymbol;
                }
            }
        }
    }


}
