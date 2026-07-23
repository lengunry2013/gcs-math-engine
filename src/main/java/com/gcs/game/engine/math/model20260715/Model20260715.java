package com.gcs.game.engine.math.model20260715;

import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.keno.utils.KenoGameConstant;
import com.gcs.game.engine.keno.utils.numbers.KenoShapeDrawUtil;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoResult;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Model20260715 extends BaseKenoModel {
    public static final int[][] FS_SETA_TIMES = new int[][]{
            {30, 35, 40, 45, 50, 60},
            {5, 6, 7, 9, 12}
    };
    public static final int[][] FS_SETA_WEIGHT = new int[][]{
            {100, 80, 50, 30, 10, 5},
            {1, 1, 1, 1, 1}
    };
    public static final int[] FS_SETB_TIMES = new int[]{
            15, 17, 20, 25, 30,
    };
    public static final int[] FS_SETB_WEIGHT = new int[]{
            100, 75, 50, 25, 5
    };
    public static final int[] FS_SETC_TIMES = new int[]{
            5, 6, 8, 12
    };
    public static final int[] FS_SETC_WEIGHT = new int[]{
            1, 1, 1, 1
    };
    public static final int[] FS_SETD_TIMES = new int[]{
            2, 4, 6
    };
    public static final int[] FS_SETD_WEIGHT = new int[]{
            1, 1, 1
    };


    @Override
    public long[][] getPayTable(KenoGameLogicBean gameLogicBean) {
        long[][] payTables = new long[][]{
                {0, 0, 225},
                {0, 0, 50, 450},
                {0, 0, 25, 100, 1100},
                {0, 0, 0, 50, 400, 4500},
                {0, 0, 0, 25, 150, 1000, 9000},
                {0, 0, 0, 25, 100, 150, 1500, 15000},
                {0, 0, 0, 0, 50, 200, 1000, 5500, 42000},
                {0, 0, 0, 0, 25, 100, 525, 2200, 15300, 80000},
                {0, 0, 0, 0, 25, 75, 200, 600, 3750, 25000, 80000},    //1bet
                {0, 0, 450},
                {0, 0, 100, 900},
                {0, 0, 50, 200, 2150},
                {0, 0, 0, 100, 775, 10000},
                {0, 0, 0, 50, 275, 2250, 18850},
                {0, 0, 0, 50, 175, 375, 3500, 35000},
                {0, 0, 0, 0, 100, 400, 2050, 12900, 80000},
                {0, 0, 0, 0, 50, 250, 750, 5000, 31600, 80000},
                {0, 0, 0, 0, 50, 175, 275, 1350, 7800, 62500, 80000},    //2bet
                {0, 0, 675},
                {0, 0, 175, 1125},
                {0, 0, 75, 275, 3600},
                {0, 0, 0, 150, 1200, 14950},
                {0, 0, 0, 75, 450, 3000, 35300},
                {0, 0, 0, 75, 250, 650, 5250, 61500},
                {0, 0, 0, 0, 150, 550, 3300, 21000, 80000},
                {0, 0, 0, 0, 75, 375, 1100, 8400, 52000, 80000},
                {0, 0, 0, 0, 75, 275, 400, 2050, 12000, 75000, 80000},    //3bet
                {0, 0, 900},
                {0, 0, 225, 1600},
                {0, 0, 100, 350, 5000},
                {0, 0, 0, 200, 1550, 20550},
                {0, 0, 0, 100, 600, 4200, 49000},
                {0, 0, 0, 100, 325, 900, 7500, 80000},
                {0, 0, 0, 0, 200, 700, 4750, 29500, 80000},
                {0, 0, 0, 0, 100, 500, 1450, 12500, 75000, 80000},
                {0, 0, 0, 0, 100, 300, 650, 3500, 17500, 76000, 80000},    //4bet
                {0, 0, 1125},
                {0, 0, 275, 2000},
                {0, 0, 125, 450, 6300},
                {0, 0, 0, 250, 2050, 25800},
                {0, 0, 0, 125, 750, 5425, 61000},
                {0, 0, 0, 125, 450, 1000, 9300, 80000},
                {0, 0, 0, 0, 250, 825, 6500, 40000, 80000},
                {0, 0, 0, 0, 125, 600, 2000, 15000, 76000, 80000},
                {0, 0, 0, 0, 125, 400, 725, 4000, 25000, 77000, 80000},    //5bet
                {0, 0, 1350},
                {0, 0, 300, 2700},
                {0, 0, 150, 500, 8000},
                {0, 0, 0, 300, 2450, 33000},
                {0, 0, 0, 150, 950, 6000, 75000},
                {0, 0, 0, 150, 550, 1300, 10000, 80000},
                {0, 0, 0, 0, 300, 1050, 7500, 45600, 80000},
                {0, 0, 0, 0, 150, 750, 2450, 16800, 77000, 80000},
                {0, 0, 0, 0, 150, 450, 900, 6000, 27900, 78000, 80000},    //6bet
                {0, 0, 1575},
                {0, 0, 350, 3150},
                {0, 0, 175, 575, 9500},
                {0, 0, 0, 350, 3050, 35000},
                {0, 0, 0, 175, 1050, 7700, 80000},
                {0, 0, 0, 175, 600, 1700, 13100, 80000},
                {0, 0, 0, 0, 350, 1225, 9300, 55000, 80000},
                {0, 0, 0, 0, 175, 800, 3000, 24700, 78000, 80000},
                {0, 0, 0, 0, 175, 600, 1000, 6500, 30000, 79000, 80000},    //7bet
                {0, 0, 1800},
                {0, 0, 450, 3200},
                {0, 0, 200, 700, 10800},
                {0, 0, 0, 400, 3400, 42500},
                {0, 0, 0, 200, 1400, 8000, 80000},
                {0, 0, 0, 200, 700, 1900, 15000, 80000},
                {0, 0, 0, 0, 400, 1300, 12000, 62000, 80000},
                {0, 0, 0, 0, 200, 1000, 3600, 25000, 79000, 80000},
                {0, 0, 0, 0, 200, 650, 1200, 8000, 34000, 80000, 80000},    //8bet
        };
        long[][] payTableResult = computePayTables(payTables, gameLogicBean);
        return payTableResult;
    }

    protected long[][] computePayTables(long[][] payTables, KenoGameLogicBean gameLogicBean) {
        long[][] payTableResult = new long[maxSelectNumbersCount() - 1][];
        int betIndex = (int) gameLogicBean.getBet() - 1;
        for (int i = 0; i < payTableResult.length; i++) {
            payTableResult[i] = payTables[i + betIndex * 9].clone();
        }
        return payTableResult;
    }

    @Override
    public long minLines() {
        return 25;
    }

    @Override
    public long minBet() {
        return 1;
    }

    @Override
    public long maxLines() {
        return 25;
    }

    @Override
    public long maxBet() {
        return 8;
    }

    @Override
    public long totalBet(long lines, long bet) {
        return lines * bet;
    }

    @Override
    public int[] getAllRandomDigits() {
        return new int[]{
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
                44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
                58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
                72, 73, 74, 75, 76, 77, 78, 79, 80
        };
    }

    @Override
    protected int getRandomCount() {
        return 20;
    }

    @Override
    public int minSelectNumbersCount() {
        return 2;
    }

    @Override
    public int maxSelectNumbersCount() {
        return 10;
    }

    @Override
    protected int baseSetCount() {
        return 3;
    }

    @Override
    protected int fsSetCount() {
        return 1;
    }

    @Override
    protected int baseSetNumbersCount() {
        return 4;
    }

    @Override
    protected int fsSetNumbersCount() {
        return 3;
    }

    protected int[][] getFsSetATimes() {
        return FS_SETA_TIMES;
    }

    protected int[][] getFsSetAWeight() {
        return FS_SETA_WEIGHT;
    }

    public int[] getSetAFsTimes() {
        int fs1 = RandomUtil.getRandomFromArrayWithWeight(getFsSetATimes()[0], getFsSetAWeight()[0]);
        int fs2 = RandomUtil.getRandomFromArrayWithWeight(getFsSetATimes()[1], getFsSetAWeight()[1]);
        return new int[]{0, fs2, fs1};  //fs times
    }

    public int[] getSetAMul() {
        return new int[]{4, 0, 0};  //Multiplier
    }

    public int[] getSetBFsTimes() {
        int fs1 = RandomUtil.getRandomFromArrayWithWeight(FS_SETB_TIMES, FS_SETB_WEIGHT);
        return new int[]{
                0, 0, fs1  //fs times
        };
    }

    public int[] getSetBMul() {
        return new int[]{2, 5, 0};  //Multiplier
    }

    public int[] getSetCFsTimes() {
        int fs1 = RandomUtil.getRandomFromArrayWithWeight(FS_SETC_TIMES, FS_SETC_WEIGHT);
        return new int[]{0, fs1};
    }

    public int[] getSetCMul() {
        return new int[]{3, 0};  //Multiplier
    }


    public int[] getSetDFsTimes() {
        int fs1 = RandomUtil.getRandomFromArrayWithWeight(FS_SETD_TIMES, FS_SETD_WEIGHT);
        return new int[]{0, fs1};
    }

    public int[] getSetDMul() {
        return new int[]{2, 0};  //Multiplier
    }

    protected int getSetFsTimes(boolean isFsSet) {
        int fsTimes = 1;
        return fsTimes;
    }

    @Override
    public int[][] mixHitOnAll3Sets() {
        return null;
    }

    @Override
    public int[][] mixHitOnAll4Sets() {
        return null;
    }

    @Override
    public long maxTotalPay() {
        return 80000;
    }

    @Override
    public int hitSetDefaultMul() {
        return 1;
    }

    public KenoResult spin(KenoGameLogicBean gameLogicCache, InputInfo input) {
        KenoResult kenoResult = gameLogicCache.getKenoResult();
        if (kenoResult != null) {
            List<Integer> selectNumbers = kenoResult.getSelectNumbers();
            int[] allNumbers = getAllRandomDigits();
            List<Integer> randomNumbers = getRandomNumbers(allNumbers, getRandomCount());
            if (input != null && input.getInputHandsCards() != null && input.getInputHandsCards().size() > 1) {
                selectNumbers = input.getInputHandsCards().get(0);
                kenoResult.setSelectNumbers(selectNumbers);
                randomNumbers = input.getInputHandsCards().get(1);
            }
            List<Integer> leftNumbers = StringUtil.IntegerArrayToList(allNumbers);
            List<List<Integer>> additionsSetsNumbers = kenoResult.getAdditionsSetsNumbers();
            int fsTotalTimes = 0;
            List<Integer> winMul = new ArrayList<>();
            List<Integer> fsCounts = new ArrayList<>();
            List<Integer> setsMatchCount = new ArrayList<>();
            long winPay = 0;
            int baseWinMul = 1;
            //SetA numbers
            List<Integer> setANumbers = new ArrayList<>();
            List<Integer> setBNumbers = new ArrayList<>();
            List<Integer> setCNumbers = new ArrayList<>();
            if (additionsSetsNumbers != null && !additionsSetsNumbers.isEmpty() && additionsSetsNumbers.size() >= 3) {
                setANumbers = additionsSetsNumbers.get(0);
                setBNumbers = additionsSetsNumbers.get(1);
                setCNumbers = additionsSetsNumbers.get(2);
            }
            if (setANumbers.isEmpty()) {
                setANumbers = KenoShapeDrawUtil.drawShapeA(leftNumbers);
                leftNumbers.removeAll(setANumbers);
            }
            if (setBNumbers.isEmpty()) {
                setBNumbers = KenoShapeDrawUtil.drawShapeB(leftNumbers);
                leftNumbers.removeAll(setBNumbers);
            }
            if (setCNumbers.isEmpty()) {
                setCNumbers = KenoShapeDrawUtil.drawShapeC(leftNumbers);
                leftNumbers.removeAll(setCNumbers);
            }
            //SetA numbers
            int matchCount = computeMatchCount(randomNumbers, setANumbers);
            if (matchCount >= KenoGameConstant.BASE_SET_MAX_SPOTS) {
                int index = matchCount - KenoGameConstant.BASE_SET_MAX_SPOTS;
                int fsTimes = getSetAFsTimes()[index];
                int baseMul = getSetAMul()[index];
                fsTotalTimes += fsTimes;
                if (baseMul > 0) {
                    baseWinMul *= baseMul;
                }
                fsCounts.add(fsTimes);
                winMul.add(baseMul);
            }
            setsMatchCount.add(matchCount);
            //SetB numbers
            matchCount = computeMatchCount(randomNumbers, setBNumbers);
            if (matchCount >= KenoGameConstant.BASE_SET_MUL_SPOTS) {
                int index = matchCount - KenoGameConstant.BASE_SET_MUL_SPOTS;
                int fsTimes = getSetBFsTimes()[index];
                int baseMul = getSetBMul()[index];
                fsTotalTimes += fsTimes;
                if (baseMul > 0) {
                    baseWinMul *= baseMul;
                }
                fsCounts.add(fsTimes);
                winMul.add(baseMul);
            }
            setsMatchCount.add(matchCount);
            //SetC number
            matchCount = computeMatchCount(randomNumbers, setCNumbers);
            if (matchCount >= KenoGameConstant.BASE_SET_MUL_SPOTS) {
                int index = matchCount - KenoGameConstant.BASE_SET_MUL_SPOTS;
                int fsTimes = getSetCFsTimes()[index];
                int baseMul = getSetCMul()[index];
                fsTotalTimes += fsTimes;
                if (baseMul > 0) {
                    baseWinMul *= baseMul;
                }
                fsCounts.add(fsTimes);
                winMul.add(baseMul);
            }
            setsMatchCount.add(matchCount);
            if (additionsSetsNumbers != null) {
                additionsSetsNumbers.clear();
            } else {
                additionsSetsNumbers = new ArrayList<>();
            }
            additionsSetsNumbers.add(setANumbers);
            additionsSetsNumbers.add(setBNumbers);
            additionsSetsNumbers.add(setCNumbers);
            matchCount = computeMatchCount(randomNumbers, selectNumbers);
            long[][] payTable = getPayTable(gameLogicCache);
            winPay = payTable[selectNumbers.size() - 2][matchCount];
            winPay *= baseWinMul;
            //The max prize is $800
            if (maxTotalPay() > 0 && winPay >= maxTotalPay()) {
                winPay = maxTotalPay();
            }
            kenoResult.setRandomNumbers(randomNumbers);
            kenoResult.setAdditionsSetsNumbers(additionsSetsNumbers);
            kenoResult.setBaseMul(baseWinMul);
            kenoResult.setSetsMatchCount(setsMatchCount);
            kenoResult.setMatchCount(matchCount);
            kenoResult.setWinMul(winMul);
            kenoResult.setFsCountsList(fsCounts);
            kenoResult.setKenoPay(winPay);
            //The max prize is $800
            if (fsTotalTimes > 0) {
                log.debug("Trigger Freespin");
                List<String> nextScenes = new ArrayList<>();
                kenoResult.setTriggerFs(true);
                kenoResult.setTriggerFsCounts(fsTotalTimes);
                nextScenes.add("freeSpin");
                kenoResult.setNextScenes(nextScenes);
            }
        }
        return kenoResult;
    }


    public KenoResult spinInFs(KenoGameLogicBean gameLogicCache, InputInfo input) {
        KenoResult kenoResult = new KenoResult();
        //selectNumbers same baseGame in fs
        kenoResult.setSelectNumbers(gameLogicCache.getKenoResult().getSelectNumbers());
        int[] allNumbers = getAllRandomDigits();
        List<Integer> randomNumbers = getRandomNumbers(allNumbers, getRandomCount());
        List<Integer> leftNumbers = getLeftNumbers(allNumbers, kenoResult.getSelectNumbers());
        List<List<Integer>> additionsSetsNumbers = gameLogicCache.getKenoResult().getAdditionsSetsNumbers();

        int totalMatchCount = 0;
        int fsTotalTimes = 0;
        List<Integer> winMul = new ArrayList<>();
        List<Integer> fsCounts = new ArrayList<>();
        List<Integer> setsMatchCount = new ArrayList<>();
        long winPay = 0;
        int fsWinMul = 1;
        //Set A number and match count
        List<Integer> setANumbers = additionsSetsNumbers.get(0);
        leftNumbers.removeAll(setANumbers);
        int matchACount = computeMatchCount(randomNumbers, setANumbers);
        totalMatchCount += matchACount;
        //Set B number and match count
        List<Integer> setBNumbers = additionsSetsNumbers.get(1);
        leftNumbers.removeAll(setBNumbers);
        int matchBCount = computeMatchCount(randomNumbers, setBNumbers);
        totalMatchCount += matchBCount;
        //Set C number and match count
        List<Integer> setCNumbers = additionsSetsNumbers.get(2);
        leftNumbers.removeAll(setCNumbers);
        int matchCCount = computeMatchCount(randomNumbers, setCNumbers);
        totalMatchCount += matchCCount;
        //setD numbers And match count
        List<Integer> setDNumbers = KenoShapeDrawUtil.drawShapeD(leftNumbers);
        int matchDCount = computeMatchCount(randomNumbers, setDNumbers);
        totalMatchCount += matchDCount;
        additionsSetsNumbers.add(setDNumbers);
        //random extra DrawNumber
        List<Integer> leftRandomNumbers = getLeftNumbers(allNumbers, randomNumbers);
        List<Integer> extraDrawNumberList = new ArrayList<>();
        while (totalMatchCount > 0) {
            List<Integer> extraDrawNumber = getRandomNumbers(StringUtil.ListToIntegerArray(leftRandomNumbers), totalMatchCount);
            extraDrawNumberList.addAll(extraDrawNumber);
            totalMatchCount = 0;
            int matchCount = computeMatchCount(extraDrawNumber, setANumbers);
            matchACount += matchCount;
            totalMatchCount += matchCount;
            matchCount = computeMatchCount(extraDrawNumber, setBNumbers);
            matchBCount += matchCount;
            totalMatchCount += matchCount;
            matchCount = computeMatchCount(extraDrawNumber, setCNumbers);
            matchCCount += matchCount;
            totalMatchCount += matchCount;
            matchCount = computeMatchCount(extraDrawNumber, setDNumbers);
            matchDCount += matchCount;
            totalMatchCount += matchCount;
            leftRandomNumbers.removeAll(extraDrawNumber);
        }
        //compute SetA,B,C,D
        setsMatchCount.add(matchACount);
        setsMatchCount.add(matchBCount);
        setsMatchCount.add(matchCCount);
        setsMatchCount.add(matchDCount);
        if (matchACount >= KenoGameConstant.BASE_SET_MAX_SPOTS) {
            int index = matchACount - KenoGameConstant.BASE_SET_MAX_SPOTS;
            int fsTimes = getSetAFsTimes()[index];
            int fsMul = getSetAMul()[index];
            fsTotalTimes += fsTimes;
            if (fsMul > 0) {
                fsWinMul *= fsMul;
            }
            fsCounts.add(fsTimes);
            winMul.add(fsMul);
        }
        if (matchBCount >= KenoGameConstant.FS_SET_MAX_SPOTS) {
            int index = matchBCount - KenoGameConstant.FS_SET_MAX_SPOTS;
            int fsTimes = getSetBFsTimes()[index];
            int fsMul = getSetBMul()[index];
            fsTotalTimes += fsTimes;
            if (fsMul > 0) {
                fsWinMul *= fsMul;
            }
            fsCounts.add(fsTimes);
            winMul.add(fsMul);
        }
        if (matchCCount >= KenoGameConstant.FS_SET_MAX_SPOTS) {
            int index = matchCCount - KenoGameConstant.FS_SET_MAX_SPOTS;
            int fsTimes = getSetCFsTimes()[index];
            int fsMul = getSetCMul()[index];
            fsTotalTimes += fsTimes;
            if (fsMul > 0) {
                fsWinMul *= fsMul;
            }
            fsCounts.add(fsTimes);
            winMul.add(fsMul);
        }
        if (matchDCount >= KenoGameConstant.FS_SET_MUL_SPOTS) {
            int index = matchDCount - KenoGameConstant.FS_SET_MUL_SPOTS;
            int fsTimes = getSetDFsTimes()[index];
            int fsMul = getSetDMul()[index];
            fsTotalTimes += fsTimes;
            if (fsMul > 0) {
                fsWinMul *= fsMul;
            }
            fsCounts.add(fsTimes);
            winMul.add(fsMul);
        }
        int matchCount = computeMatchCount(randomNumbers, kenoResult.getSelectNumbers());
        if (!extraDrawNumberList.isEmpty()) {
            matchCount += computeMatchCount(extraDrawNumberList, kenoResult.getSelectNumbers());
        }
        long[][] payTable = getPayTable(gameLogicCache);
        winPay = payTable[kenoResult.getSelectNumbers().size() - 2][matchCount];
        winPay *= fsWinMul;
        //The max prize is $800
        if (maxTotalPay() > 0 && winPay >= maxTotalPay()) {
            winPay = maxTotalPay();
        }
        kenoResult.setRandomNumbers(randomNumbers);
        kenoResult.setAdditionsSetsNumbers(additionsSetsNumbers);
        kenoResult.setExtraDrawNumbers(extraDrawNumberList);
        kenoResult.setSetsMatchCount(setsMatchCount);
        kenoResult.setFsMul(fsWinMul);
        kenoResult.setMatchCount(matchCount);
        kenoResult.setWinMul(winMul);
        kenoResult.setFsCountsList(fsCounts);
        kenoResult.setKenoPay(winPay);
        //The max prize is $800
        if (fsTotalTimes > 0) {
            log.debug("Trigger Freespin");
            List<String> nextScenes = new ArrayList<>();
            kenoResult.setTriggerFs(true);
            kenoResult.setTriggerFsCounts(fsTotalTimes);
            nextScenes.add("freeSpin");
            kenoResult.setNextScenes(nextScenes);
        }
        return kenoResult;
    }


}
