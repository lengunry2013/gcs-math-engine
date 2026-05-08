package com.gcs.game.engine.math.model20260103;


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

public class Model20260103 extends BaseSlotModel implements IWildPositionsChange {

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

    protected int reelsCountInFreeSpin() {
        return 5;
    }

    protected int rowCountInFreeSpin() {
        return 5;
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

    private static RandomWeightUntil baseReelsRandom = null;
    private static RandomWeightUntil fsReelsRandom = null;

    protected int[] getBaseReelsWeight(int payBack) {
        int[] baseReelsWeight = new int[]{899, 101};
        switch (payBack) {
            case 8816:
                baseReelsWeight = new int[]{899, 101};
                break;
            case 9016:
                baseReelsWeight = new int[]{840, 160};
                break;
            default:
                break;
        }
        return baseReelsWeight;
    }

    protected int[] getFsReelsWeight(int payBack) {
        int[] result = new int[]{300, 700};
        switch (payBack) {
            case 8816:
                result = new int[]{300, 700};
                break;
            case 9016:
                result = new int[]{300, 700};
                break;
            default:
                break;
        }
        return result;
    }

    public SlotSpinResult spin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        Model20260103SpinResult baseSpinResult = null;
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
            baseSpinResult = (Model20260103SpinResult) computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
            if (randomIndex == 1) {
                baseSpinResult.setReelsType(2);
            }
        }
        return baseSpinResult;
    }

    protected List<SlotSymbolHitResult> computeAdjacentSymbolLeft2Right(SlotGameLogicBean gameLogicBean, SlotSymbol symbol, int[] displaySymbols, long betPerLine, boolean inSlot) {
        int reelsCount = reelsCount();
        int rowsCount = rowsCount();
        if (!inSlot) {
            Model20260103SpinResult spinResult = (Model20260103SpinResult) gameLogicBean.getSlotSpinResult();
            int fsType = spinResult.getFsType();
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                reelsCount = reelsCountInFreeSpin();
                rowsCount = rowCountInFreeSpin();
            } else if (fsType == FS_JACKPOT_BONUS) {
                reelsCount = reelsCount();
                rowsCount = rowsCount();
            }
        }
        return computeAdjacentSymbolLeft2Right(gameLogicBean, symbol, displaySymbols, betPerLine, inSlot, reelsCount, rowsCount);
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
            //TODO Recover
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
            if (recoverInfo != null) {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot, recoverInfo);
            } else {
                baseSpinResult = computeSpin(displaySymbols, stopPosition, gameLogicBean, isSlot);
            }
        }
        return baseSpinResult;
    }

    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameSessionBean) {
        Model20260103SpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = getFSReels(modelFeatureBean, gameSessionBean);
            int[][] reelsWeight = getFSReelsWeight(modelFeatureBean, gameSessionBean);
            if (fsReelsRandom == null) {
                fsReelsRandom = new RandomWeightUntil(getFsReelsWeight(gameSessionBean.getPercentage()));
            }
            int randomIndex = fsReelsRandom.getRandomResult();
            if (randomIndex == 1) {
                reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS_KEY);
                reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS_KEY);
            }
            int[] stopPosition = randomReelStopPosition(reelsWeight);

            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = false;
            int[] displaySymbols = null;
            Model20260103SpinResult spinResult = (Model20260103SpinResult) gameSessionBean.getSlotSpinResult();
            int fsType = spinResult.getFsType();
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                displaySymbols = getDisplaySymbols(reels, stopPosition, reelsCountInFreeSpin(), rowCountInFreeSpin());
            } else {
                displaySymbols = getDisplaySymbols(reels, stopPosition);
            }
            baseSpinResult = (Model20260103SpinResult) computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot);
            if (randomIndex == 1) {
                baseSpinResult.setReelsType(2);
            }
        }
        return baseSpinResult;
    }

    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean, InputInfo inputFeedBean, RecoverInfo recoverInfo) {
        SlotSpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = getFSReels(modelFeatureBean, gameLogicBean);
            int[][] reelsWeight = getFSReelsWeight(modelFeatureBean, gameLogicBean);
            //TODO recover
            if (reels == null) {
                reels = modelFeatureBean.getSlotFsReels();
            }
            if (reelsWeight == null) {
                reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
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
            Model20260103SpinResult spinResult = (Model20260103SpinResult) gameLogicBean.getSlotSpinResult();
            int fsType = spinResult.getFsType();
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

    private static final int[] SC_WEIGHT = new int[]{1, 1};

    protected int[] getScWeight() {
        return SC_WEIGHT;
    }

    public static final int[] SC1_TRIGGER_WEIGHT = new int[]{953, 47};
    public static final int[] SC2_TRIGGER_WEIGHT = new int[]{953, 47};
    public static final int[] SC12_TRIGGER_WEIGHT = new int[]{890, 20, 20, 70};

    private static final int[] MY_WEIGHT = new int[]{1000, 800, 600, 900, 700, 1200, 1200, 1200, 1200, 1200};

    private static final int[] MY_SYMBOLS = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    protected int[] getMyWeight() {
        return MY_WEIGHT;
    }


    private static RandomWeightUntil scRandom = null;
    private static RandomWeightUntil myRandom = null;

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260103SpinResult baseSpinResult;
        Map<Integer, int[]> payLinesMap = getPayLines();
        displaySymbols = getScChangeDisplaySymbols(displaySymbols, isSlot, gameLogicBean);
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

        baseSpinResult = (Model20260103SpinResult) computeSpinResult(stopPosition, displaySymbols, payLinesMap, gameLogicBean, isSlot);
        if (baseSpinResult != null && wildReels != null) {
            baseSpinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            baseSpinResult.setSlotWildReels(wildReels);
        }
        if (baseSpinResult != null && wildPositions != null) {
            baseSpinResult.setSlotDisplaySymbols(oldDisplaySymbols); // symbols before over.
            baseSpinResult.setSlotWildPositions(wildPositions);
            baseSpinResult.setMysterySymbol(wildSymbolNo);
        }
        return baseSpinResult;
    }

    private static RandomWeightUntil fs1Random = null;
    private static RandomWeightUntil fs2Random = null;
    private static RandomWeightUntil fs3Random = null;

    public static final int[] JP_BONUS_INIT_WEIGHT = new int[]{
            10000, 8000, 3000, 800, 10000, 8000, 1000, 500, 200,
            10000, 8000, 1000, 200, 100, 10000, 8000, 1000, 200, 1
    };
    public static int[] JP_BONUS_WEIGHT = new int[]{
            10000, 8000, 3000, 800, 10000, 8000, 1000, 500, 200,
            10000, 8000, 1000, 200, 100, 10000, 8000, 1000, 200, 1
    };

    public static final int[][] JP_BONUS_LETTER_WEIGHT = new int[][]{
            {10000, 8000, 3000, 800},
            {10000, 8000, 1000, 500, 200},
            {10000, 8000, 1000, 200, 100},
            {10000, 8000, 1000, 200, 1}
    };

    public static final int[] JP_BONUS_REWARD = new int[]{10, 30, 100, 1000};

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260103SpinResult result = new Model20260103SpinResult();
        List<SlotSymbolHitResult> hitList = computeSymbols(gameLogicBean, displaySymbols, payLinesMap, isSlot);

        hitList = filterLineHit(hitList);
        computeLineMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        //baseGame Trigger Fs
        if (isSlot) {
            JP_BONUS_WEIGHT = JP_BONUS_INIT_WEIGHT.clone();
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
                    setHitScatter(SC3_SYMBOL, scatterCount, displaySymbols, fsType, hitList);
                }
            } else if (sc1Count > 0) {
                if (fs1Random == null) {
                    fs1Random = new RandomWeightUntil(SC1_TRIGGER_WEIGHT);
                }
                int randomIndex = fs1Random.getRandomResult();
                if (randomIndex == 1) {
                    fsType = FS_EXTEND_REELS;
                    setHitScatter(SC1_SYMBOL, sc1Count, displaySymbols, fsType, hitList);
                }
            } else if (sc2Count > 0) {
                if (fs2Random == null) {
                    fs2Random = new RandomWeightUntil(SC2_TRIGGER_WEIGHT);
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
            Model20260103SpinResult baseSpinResult = (Model20260103SpinResult) gameLogicBean.getSlotSpinResult();
            int fsType = baseSpinResult.getFsType();
            int scSymbol = SC1_SYMBOL;
            if (fsType == FS_JACKPOT_BONUS) {
                scSymbol = SC2_SYMBOL;
            } else if (fsType == FS_SUPER_BONUS) {
                scSymbol = SC3_SYMBOL;
            }
            List<Integer> scPositions = computeFsScPosition(displaySymbols, scSymbol);
            //每次上fs一局的JP Bonus 字母显示保留
            int[] jpBonusLevel = getJPBonusLevelCount(gameLogicBean);
            if (!scPositions.isEmpty()) {
                if (fsType == FS_EXTEND_REELS) {
                    if (sc1AwardRandom == null) {
                        sc1AwardRandom = new RandomWeightUntil(SC_AWARD_WEIGHT[fsType - 1]);
                    }
                    for (int position : scPositions) {
                        int scAwardIndex = sc1AwardRandom.getRandomResult();
                        jpBonusLevel = setFsHitScatter(gameLogicBean, scAwardIndex, position, scSymbol, hitList, result, jpBonusLevel);
                    }
                } else if (fsType == FS_JACKPOT_BONUS) {
                    if (sc2AwardRandom == null) {
                        sc2AwardRandom = new RandomWeightUntil(SC_AWARD_WEIGHT[fsType - 1]);
                    }
                    for (int position : scPositions) {
                        int scAwardIndex = sc2AwardRandom.getRandomResult();
                        jpBonusLevel = setFsHitScatter(gameLogicBean, scAwardIndex, position, scSymbol, hitList, result, jpBonusLevel);
                    }
                } else if (fsType == FS_SUPER_BONUS) {
                    if (sc3AwardRandom == null) {
                        sc3AwardRandom = new RandomWeightUntil(SC_AWARD_WEIGHT[fsType - 1]);
                    }
                    for (int position : scPositions) {
                        int scAwardIndex = sc2AwardRandom.getRandomResult();
                        jpBonusLevel = setFsHitScatter(gameLogicBean, scAwardIndex, position, scSymbol, hitList, result, jpBonusLevel);
                    }
                }
            }
            result.setJpBonusLevelsCount(jpBonusLevel);
        }

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = (Model20260103SpinResult) transferHitList(result, hitList, displaySymbols, stopPosition);
        if (isSlot) {
            result.setBaseGameMul(baseGameMultiplier);
        }
        if (!isSlot) {
            result.setFsMul(freeSpinMultiplier);
        }
        return result;
    }

    //scatter symbol hit
    private int[] setFsHitScatter(SlotGameLogicBean gameLogicBean, int scAwardIndex, int position, int scatterSymbol, List<SlotSymbolHitResult> hitList, Model20260103SpinResult result, int[] jpBonusLevel) {
        int hitLevel = -1;
        int[] jpBonusLevelCount = jpBonusLevel.clone();
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitLine(SlotEngineConstant.SCATTER_HIT_LINE);
        hitResult.setHitMul(1);
        hitResult.setHitCount(1);
        hitResult.setHitPosition(new int[]{position, 0, 0, 0, 0});
        if (scAwardIndex < 10) {
            hitResult.setHitSymbol(scatterSymbol);
            hitResult.setHitSymbolSound(scatterSymbol);
            hitResult.setHitPay(SC_AWARD[scAwardIndex] * gameLogicBean.getSumBetCredit());
        } else if (scAwardIndex < 12) {
            //distinguish different free spin counts +1 FREE->1001 or +2 FREE 1002
            hitResult.setHitSymbol(scAwardIndex - 9 + 1000);
            hitResult.setHitSymbolSound(scAwardIndex - 9 + 1000);
            hitResult.setHitPay(0);
            SlotSymbolHitResult existTriggerFsSymbol = getExistTriggerFs(hitList);
            if (existTriggerFsSymbol != null) {
                int fsTimes = SC_AWARD[scAwardIndex] + existTriggerFsSymbol.getTriggerFsCounts();
                existTriggerFsSymbol.setTriggerFsCounts(fsTimes);
            } else {
                hitResult.setTriggerFs(true);
                hitResult.setTriggerFsCounts(SC_AWARD[scAwardIndex]);
            }
        } else {
            int[] hitLevels = result.getHitLevels();
            int jpIndex = RandomUtil.getRandomIndexFromArrayWithWeight(JP_BONUS_WEIGHT);
            int hitSymbol = jpIndex + 100;
            //compute JP Bonus number of letters,lettersM~D---100~118
            setJpBonusLetters(hitSymbol, jpBonusLevelCount);
            hitResult.setHitSymbol(hitSymbol);
            hitResult.setHitSymbolSound(hitSymbol);
            //JPBonus hit level
            for (int i = 0; i < jpBonusLevelCount.length; i++) {
                if (i == 0 && jpBonusLevelCount[i] == 4) {
                    hitLevel = i + 1;
                    jpBonusLevelCount[i] = 0;
                    hitLevels[i] = hitLevel;
                    break;
                } else if (jpBonusLevelCount[i] == 5) {
                    hitLevel = i + 1;
                    jpBonusLevelCount[i] = 0;
                    hitLevels[i] = hitLevel;
                    break;
                }
            }
            if (hitLevel > 0) {
                resetJpBonusWeight(hitLevel);
                hitResult.setHitPay(JP_BONUS_REWARD[hitLevel - 1] * gameLogicBean.getSumBetCredit());
            } else {
                hitResult.setHitPay(0);
                JP_BONUS_WEIGHT[jpIndex] = 0;
            }
            result.setHitLevels(hitLevels);
        }
        hitList.add(hitResult);
        return jpBonusLevelCount;
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

    private void resetJpBonusWeight(int hitLevel) {
        int[] resetWeight = JP_BONUS_LETTER_WEIGHT[hitLevel - 1];
        //for mini 4 letters
        if (hitLevel == 1) {
            for (int i = 0; i < resetWeight.length; i++) {
                JP_BONUS_WEIGHT[i] = resetWeight[i];
            }
        } else {
            //hitlevel>2 minor,major,grand 5 letter
            for (int i = 0; i < resetWeight.length; i++) {
                JP_BONUS_WEIGHT[i + 4 + (hitLevel - 2) * 5] = resetWeight[i];
            }
        }

    }

    private int[] getJPBonusLevelCount(SlotGameLogicBean gameLogicBean) {
        //compute pre all freespin hit JP bonus letter
        int[] jpBonusLevel = new int[4];
        List<SlotSpinResult> fsSpinResultList = gameLogicBean.getSlotFsSpinResults();
        if (fsSpinResultList != null && !fsSpinResultList.isEmpty()) {
            Model20260103SpinResult spinResult = (Model20260103SpinResult) fsSpinResultList.get(fsSpinResultList.size() - 1);
            jpBonusLevel = spinResult.getJpBonusLevelsCount();
        }
        return jpBonusLevel;
    }

    private void setJpBonusLetters(int hitSymbol, int[] jpBonusLevel) {
        //compute JP bonus number of letters
        if (hitSymbol >= 100) {
            if (hitSymbol < 104) {
                jpBonusLevel[0]++;
            } else if (hitSymbol < 109) {
                jpBonusLevel[1]++;
            } else if (hitSymbol < 114) {
                jpBonusLevel[2]++;
            } else if (hitSymbol < 119) {
                jpBonusLevel[3]++;
            }
        }

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

    public static final int[][] SC_ADD_WEIGHT = new int[][]{
            {33886, 10000, 20000, 15000, 15001, 5000, 1000, 100, 10, 2, 1},
            {9903, 10000, 20000, 29994, 15000, 10000, 5000, 100, 2, 1},
            {8986, 10000, 10000, 15000, 20001, 20000, 15000, 1000, 10, 2, 1}
    };
    public static final int[] SC_AWARD = new int[]{
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 15
    };
    public static final int[][] SC_AWARD_WEIGHT = new int[][]{
            {7732, 1000, 500, 200, 100, 50, 10, 5, 2, 1, 200, 200},
            {3312, 800, 300, 100, 50, 20, 10, 5, 2, 1, 200, 200, 5000},
            {3312, 800, 300, 100, 50, 20, 10, 5, 2, 1, 200, 200, 5000}
    };
    public static RandomWeightUntil sc1AddRandom = null;
    public static RandomWeightUntil sc2AddRandom = null;
    public static RandomWeightUntil sc3AddRandom = null;
    public static RandomWeightUntil sc1AwardRandom = null;
    public static RandomWeightUntil sc2AwardRandom = null;
    public static RandomWeightUntil sc3AwardRandom = null;

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

        } else {
            //freespin Add SC
            Model20260103SpinResult baseSpinResult = (Model20260103SpinResult) gameLogicBean.getSlotSpinResult();
            int fsType = baseSpinResult.getFsType();
            if (fsType == FS_EXTEND_REELS) {
                if (sc1AddRandom == null) {
                    sc1AddRandom = new RandomWeightUntil(SC_ADD_WEIGHT[fsType - 1]);
                }
                int scAddIndex = sc1AddRandom.getRandomResult();
                if (scAddIndex > 0) {
                    List<Integer> noWildPosition = new ArrayList<>();
                    for (int i = 0; i < displaySymbols.length; i++) {
                        if (displaySymbols[i] != WILD_SYMBOL) {
                            noWildPosition.add(i);
                        }
                    }
                    int[] randomIndex = RandomUtil.getRandomIndex(noWildPosition.size());
                    for (int i = 0; i < scAddIndex; i++) {
                        newDisplaySymbols[randomIndex[i]] = SC1_SYMBOL;
                    }
                }
            } else if (fsType == FS_JACKPOT_BONUS) {
                if (sc2AddRandom == null) {
                    sc2AddRandom = new RandomWeightUntil(SC_ADD_WEIGHT[fsType - 1]);
                }
                int scAddIndex = sc2AddRandom.getRandomResult();
                if (scAddIndex > 0) {
                    List<Integer> noWildPosition = new ArrayList<>();
                    for (int i = 0; i < displaySymbols.length; i++) {
                        if (displaySymbols[i] != WILD_SYMBOL) {
                            noWildPosition.add(i);
                        }
                    }
                    int[] randomIndex = RandomUtil.getRandomIndex(noWildPosition.size());
                    for (int i = 0; i < scAddIndex; i++) {
                        newDisplaySymbols[randomIndex[i]] = SC2_SYMBOL;
                    }
                }
            } else if (fsType == FS_SUPER_BONUS) {
                if (sc3AddRandom == null) {
                    sc3AddRandom = new RandomWeightUntil(SC_ADD_WEIGHT[fsType - 1]);
                }
                int scAddIndex = sc3AddRandom.getRandomResult();
                if (scAddIndex > 0) {
                    List<Integer> noWildPosition = new ArrayList<>();
                    for (int i = 0; i < displaySymbols.length; i++) {
                        if (displaySymbols[i] != WILD_SYMBOL) {
                            noWildPosition.add(i);
                        }
                    }
                    int[] randomIndex = RandomUtil.getRandomIndex(noWildPosition.size());
                    for (int i = 0; i < scAddIndex; i++) {
                        newDisplaySymbols[randomIndex[i]] = SC3_SYMBOL;
                    }
                }
            }
        }
        return newDisplaySymbols;
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
