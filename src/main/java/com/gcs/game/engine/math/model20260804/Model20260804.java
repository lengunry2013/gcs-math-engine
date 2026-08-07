package com.gcs.game.engine.math.model20260804;


import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.model.IWildPositionsChange;
import com.gcs.game.engine.slots.model.IWildReelsChange;
import com.gcs.game.engine.slots.utils.SlotEngineConstant;
import com.gcs.game.engine.slots.utils.paylines.PayLinesBean;
import com.gcs.game.engine.slots.utils.paylines.PayLinesCachePool;
import com.gcs.game.engine.slots.vo.*;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.RecoverInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * model 20260804.
 * Game WitchKit chen
 * math doc: 20260804_WITCH_KITCHEN_88%.xlsx
 * Date:20260805
 */
public class Model20260804 extends BaseSlotModel implements IWildReelsChange {

    public static final int WILD_SYMBOL = 1;

    public static final String FREE_SPIN_REELS2_KEY = "freespin_JP";
    public static final String FREE_SPIN_REELS3_KEY = "freespin_SP";

    public static final int SC1_SYMBOL = 12;

    public static final int SC2_SYMBOL = 13;

    public static final int FS_TIME = 10;
    public static final int FS_EXTEND_REELS = 1;
    public static final int FS_JACKPOT_BONUS = 2;
    public static final int FS_SUPER_BONUS = 3;


    @Override
    protected int reelsCount() {
        return 5;
    }

    @Override
    protected int rowsCount() {
        return 4;
    }

    @Override
    public long minBetPerLine() {
        return 1;
    }

    @Override
    public long maxBetPerLine() {
        return 20;
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
        return 6;
    }


    @Override
    protected String getPayLinesFileName() {
        return "G3_default_5x4x50.properties";
    }

    protected String getPayLinesFsFileName() {
        return "G3_default_5x6x75.properties";
    }

    public int getCardinalLineNumber4R2L() {
        return 75;
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
        // init base symbols
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_LEFT2RIGHT);
        initBaseSymbols(11, SlotEngineConstant.SYMBOL_HIT_TYPE_LINE_RIGHT2LEFT);
    }

    protected int[][] getFSReels(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reels = modelFeatureBean.getSlotFsReels();
        int fsType = ((Model20260804SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
        if (fsType == FS_JACKPOT_BONUS) {
            reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS2_KEY);
        } else if (fsType == FS_SUPER_BONUS) {
            reels = modelFeatureBean.getOtherSlotReelsMap().get(FREE_SPIN_REELS3_KEY);
        }
        return reels;
    }

    protected int[][] getFSReelsWeight(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameLogicBean) {
        int[][] reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
        int fsType = ((Model20260804SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
        if (fsType == FS_JACKPOT_BONUS) {
            reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS2_KEY);
        } else if (fsType == FS_SUPER_BONUS) {
            reelsWeight = modelFeatureBean.getOtherSlotReelsWeightMap().get(FREE_SPIN_REELS3_KEY);
        }
        return reelsWeight;
    }


    public SlotSpinResult spinInFreeSpin(SlotGameFeatureVo modelFeatureBean, SlotGameLogicBean gameSessionBean) {
        Model20260804SpinResult baseSpinResult = null;
        if (modelFeatureBean != null) {
            int[][] reels = getFSReels(modelFeatureBean, gameSessionBean);
            int[][] reelsWeight = getFSReelsWeight(modelFeatureBean, gameSessionBean);
            if (reels == null) {
                reels = modelFeatureBean.getSlotFsReels();
            }
            if (reelsWeight == null) {
                reelsWeight = modelFeatureBean.getSlotFsReelsWeight();
            }

            int[] stopPosition = randomReelStopPosition(reelsWeight);

            this.currentReels = reels;
            this.currentReelsWeight = reelsWeight;
            this.currentStopPosition = stopPosition;

            boolean isSlot = false;
            int[] displaySymbols = null;
            Model20260804SpinResult spinResult = (Model20260804SpinResult) gameSessionBean.getSlotSpinResult();
            int fsType = spinResult.getFsType();
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                displaySymbols = getDisplaySymbols(reels, stopPosition, reelsCountInFreeSpin(), rowCountInFreeSpin());
            } else {
                displaySymbols = getDisplaySymbols(reels, stopPosition);
            }
            baseSpinResult = (Model20260804SpinResult) computeSpin(displaySymbols, stopPosition, gameSessionBean, isSlot);
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
    public static final int[] WL_WEIGHT = new int[]{15, 10};

    public static final int[] SC_AWARD = new int[]{
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 15
    };
    public static final int[][] SC_AWARD_WEIGHT = new int[][]{
            {7982, 800, 500, 200, 100, 50, 10, 5, 2, 1, 200, 150},
            {2362, 800, 300, 100, 50, 20, 10, 5, 2, 1, 200, 150, 6000},
            {3764, 800, 500, 200, 100, 50, 20, 10, 5, 1, 200, 150, 4200}
    };
    private static RandomWeightUntil scRandom = null;
    public static RandomWeightUntil sc1AwardRandom = null;
    public static RandomWeightUntil sc2AwardRandom = null;
    public static RandomWeightUntil sc3AwardRandom = null;
    private static RandomWeightUntil fs1Random = null;
    private static RandomWeightUntil fs2Random = null;
    private static RandomWeightUntil fs3Random = null;

    public static final int[] JP_BONUS_INIT_WEIGHT = new int[]{
            2000, 1600, 600, 160, 2000, 1600, 200, 100, 40,
            2000, 1600, 200, 40, 20, 2000, 1600, 200, 40, 1
    };
    public static int[] JP_BONUS_WEIGHT = new int[]{
            2000, 1600, 600, 160, 2000, 1600, 200, 100, 40,
            2000, 1600, 200, 40, 20, 2000, 1600, 200, 40, 1
    };

    public static final int[][] JP_BONUS_LETTER_WEIGHT = new int[][]{
            {2000, 1600, 600, 160},
            {2000, 1600, 200, 100, 40},
            {2000, 1600, 200, 40, 20},
            {2000, 1600, 200, 40, 1}
    };

    public static final int[] JP_BONUS_REWARD = new int[]{10, 30, 100, 1000};

    protected int[] getSc1TriggerWeight(int payBack) {
        int[] result = SC1_TRIGGER_WEIGHT;
        switch (payBack) {
            case 8808:
                result = new int[]{953, 47};
                break;
            case 9049:
                result = new int[]{950, 50};
                break;
            default:
                break;
        }
        return result;
    }

    protected int[] getSc2TriggerWeight(int payBack) {
        int[] result = SC2_TRIGGER_WEIGHT;
        switch (payBack) {
            case 8808:
                result = new int[]{953, 47};
                break;
            case 9049:
                result = new int[]{950, 50};
                break;
            default:
                break;
        }
        return result;
    }

    protected Map<Integer, int[]> getPayLines(SlotGameLogicBean gameLogicBean, boolean isSlot) {
        String fileName = getPayLinesFileName();
        if (!isSlot) {
            int fsType = ((Model20260804SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                fileName = getPayLinesFsFileName();
            }
        }
        PayLinesBean payLinesBean = PayLinesCachePool.getPayLines(fileName);
        if (payLinesBean != null) {
            return payLinesBean.getPaylinesMap();
        }
        return null;
    }

    protected SlotSpinResult computeSpin(int[] displaySymbols, int[] stopPosition, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260804SpinResult baseSpinResult;
        Map<Integer, int[]> payLinesMap = getPayLines(gameLogicBean, isSlot);
        displaySymbols = getScChangeDisplaySymbols(displaySymbols, isSlot, gameLogicBean);
        int[] oldDisplaySymbols = null;
        int[] wildReels = null;
        if (this instanceof IWildReelsChange) {
            oldDisplaySymbols = displaySymbols.clone();
            wildReels = ((IWildReelsChange) this).computeWildReels(gameLogicBean, displaySymbols, isSlot);
            int wildSymbolNo = ((IWildReelsChange) this).wildSymbolNo();
            coverDisplaySymbolsByWildReels(gameLogicBean, displaySymbols, isSlot, wildReels, wildSymbolNo);
        }
        int[] wildPositions = null;
        if (this instanceof IWildPositionsChange) {
            oldDisplaySymbols = displaySymbols.clone();
            wildPositions = ((IWildPositionsChange) this).computeWildPositions(gameLogicBean, displaySymbols, isSlot);
            int wildSymbolNo = ((IWildPositionsChange) this).wildSymbolNo();
            coverDisplaySymbolsByPositions(displaySymbols, wildPositions, wildSymbolNo);
        }

        baseSpinResult = (Model20260804SpinResult) computeSpinResult(stopPosition, displaySymbols, payLinesMap, gameLogicBean, isSlot);
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

    protected void coverDisplaySymbolsByWildReels(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, int[] reelsIndex, int coverSymbol) {
        if (reelsIndex != null) {
            int rowsCount = rowsCount();
            int reelsCount = reelsCount();
            if (!isSlot) {
                int fsType = ((Model20260804SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
                if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                    reelsCount = reelsCountInFreeSpin();
                    rowsCount = rowCountInFreeSpin();
                }
            }
            for (int wildReelIndex : reelsIndex) {
                for (int j = 0; j < rowsCount; j++) {
                    displaySymbols[wildReelIndex + j * reelsCount] = coverSymbol;
                }
            }
        }
    }

    protected List<SlotSymbolHitResult> filterLineHit(List<SlotSymbolHitResult> hitList) {
        return filterLineHit(hitList, (getCardinalLineNumber4R2L() * 2 + 1));
    }

    protected SlotSpinResult computeSpinResult(int[] stopPosition, int[] displaySymbols, Map<Integer, int[]> payLinesMap, SlotGameLogicBean gameLogicBean, boolean isSlot) {
        Model20260804SpinResult result = new Model20260804SpinResult();
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
                    setHitScatter(SC1_SYMBOL, scatterCount, displaySymbols, fsType, hitList);
                }
            } else if (sc1Count > 0) {
                if (fs1Random == null) {
                    fs1Random = new RandomWeightUntil(getSc1TriggerWeight(gameLogicBean.getPercentage()));
                }
                int randomIndex = fs1Random.getRandomResult();
                if (randomIndex == 1) {
                    fsType = FS_EXTEND_REELS;
                    setHitScatter(SC1_SYMBOL, sc1Count, displaySymbols, fsType, hitList);
                }
            } else if (sc2Count > 0) {
                if (fs2Random == null) {
                    fs2Random = new RandomWeightUntil(getSc2TriggerWeight(gameLogicBean.getPercentage()));
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
            Model20260804SpinResult baseSpinResult = (Model20260804SpinResult) gameLogicBean.getSlotSpinResult();
            int fsType = baseSpinResult.getFsType();
            int scSymbol = SC1_SYMBOL;
            if (fsType == FS_JACKPOT_BONUS) {
                scSymbol = SC2_SYMBOL;
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
                        int scAwardIndex = sc3AwardRandom.getRandomResult();
                        jpBonusLevel = setFsHitScatter(gameLogicBean, scAwardIndex, position, scSymbol, hitList, result, jpBonusLevel);
                    }
                }
            }
            result.setJpBonusLevelsCount(jpBonusLevel);
        }

        int baseGameMultiplier = computeBaseGameMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);
        int freeSpinMultiplier = computeFreeSpinMultiplier(displaySymbols, hitList, isSlot, gameLogicBean);

        result = (Model20260804SpinResult) transferHitList(result, hitList, displaySymbols, stopPosition);
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
        long lines = gameLogicBean.getLines();
        long totalBet = gameLogicBean.getSumBetCredit();
        if (!isSlot) {
            int fsType = ((Model20260804SpinResult) gameLogicBean.getSlotSpinResult()).getFsType();
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                lines = 75;
            }
        }
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
            } else if (symbol.getSymbolHitType() == SlotEngineConstant.SYMBOL_HIT_TYPE_SCATTER) {
                SlotSymbolHitResult hitResult = computeScatterSymbol(gameLogicBean, symbol, displaySymbols, totalBet, isSlot, false);
                if (hitResult != null) {
                    hitList.add(hitResult);
                }
            }
        }
        return hitList;
    }

    //scatter symbol hit
    private int[] setFsHitScatter(SlotGameLogicBean gameLogicBean, int scAwardIndex, int position, int scatterSymbol, List<SlotSymbolHitResult> hitList, Model20260804SpinResult result, int[] jpBonusLevel) {
        int hitLevel = -1;
        int[] jpBonusLevelCount = jpBonusLevel.clone();
        SlotSymbolHitResult hitResult = new SlotSymbolHitResult();
        hitResult.setHitLine(2 * getCardinalLineNumber4R2L() + 1);
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
            Model20260804SpinResult spinResult = (Model20260804SpinResult) fsSpinResultList.get(fsSpinResultList.size() - 1);
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

        }
        return newDisplaySymbols;
    }


    @Override
    public int wildSymbolNo() {
        return 1;
    }

    @Override
    public int[] computeWildReels(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot) {
        if (isSlot) {
            int reelsCount = reelsCount();
            int rowCount = rowsCount();
            int wildCount = 0;
            for (int displaySymbol : displaySymbols) {
                if (displaySymbol == WILD_SYMBOL) {
                    wildCount++;
                }
            }
            if (wildCount > 0) {
                List<Integer> wildReels = new ArrayList<>();
                for (int i = 0; i < reelsCount; i++) {
                    for (int j = 0; j < rowCount; j++) {
                        if (displaySymbols[i + j * reelsCount] == WILD_SYMBOL) {
                            RandomWeightUntil randomWeightUntil = new RandomWeightUntil(WL_WEIGHT);
                            int randomIndex = randomWeightUntil.getRandomResult();
                            if (randomIndex == 1) {
                                wildReels.add(i);
                            }
                        }
                    }
                }
                return StringUtil.ListToIntegerArray(wildReels);
            }
        } else {
            int fsType = ((Model20260804SpinResult) gameLogicCache.getSlotSpinResult()).getFsType();
            int reelsCount = reelsCount();
            int rowCount = rowsCount();
            if (fsType == FS_EXTEND_REELS || fsType == FS_SUPER_BONUS) {
                reelsCount = reelsCountInFreeSpin();
                rowCount = rowCountInFreeSpin();
            }
            return getWildReels(reelsCount, rowCount, displaySymbols);
        }
        return null;
    }

    private int[] getWildReels(int reelsCount, int rowCount, int[] displaySymbols) {
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
        return new int[0];
    }
}
