package com.gcs.game.engine.math.model20261021;

import cn.hutool.core.util.ObjectUtil;
import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.keno.utils.KenoGameConstant;
import com.gcs.game.engine.keno.utils.numbers.KenoShapeDrawUtil;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoResult;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * CHEDDAR QUEST KENO
 * CHEDDAR_QUEST_KENO_20261021.xlsx
 */
@Slf4j
public class Model20261021 extends BaseKenoModel {

    public static final int[][] SET_TIMES = new int[][]{
            {5, 6, 8, 12},
            {1, 1, 1, 1}
    };
    public static final int[][] BASE_9MIX_SET_TIMES = new int[][]{
            {40, 45, 50, 55, 60, 75},
            {1, 1, 1, 1, 1, 1}
    };

    public static final int[][] BASE_8MIX_SET_TIMES = new int[][]{
            {15, 17, 20, 25, 30, 40},
            {1, 1, 1, 1, 1, 1}
    };
    public static final int[][] FS_12MIX_SET_TIMES = new int[][]{
            {40, 45, 50, 55, 60, 65, 70, 75},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };
    public static final int[][] FS_11MIX_SET_TIMES = new int[][]{
            {15, 17, 20, 22, 25, 30, 35, 40},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };

    public static final int MIX_HIT_MUL = 3;

    public static final int MAX_PRIZE = 80000;

    public static final int[][] SETD_TIMES = new int[][]{
            {3, 4, 7},
            {1, 1, 1}
    };


    @Override
    public long[][] getPayTable(KenoGameLogicBean gameLogicBean) {
        long[][] payTables = new long[][]{
                {0, 0, 175},
                {0, 0, 50, 300},
                {0, 0, 25, 75, 875},
                {0, 0, 0, 50, 300, 4300},
                {0, 0, 0, 25, 125, 800, 9500},
                {0, 0, 0, 25, 75, 125, 1325, 17700},
                {0, 0, 0, 0, 50, 125, 1000, 5700, 75000},
                {0, 0, 0, 0, 25, 100, 300, 2400, 16500, 80000},
                {0, 0, 0, 0, 25, 75, 100, 575, 3500, 26600, 80000},    //1bet
                {0, 0, 375},
                {0, 0, 100, 700},
                {0, 0, 50, 150, 2000},
                {0, 0, 0, 100, 650, 9100},
                {0, 0, 0, 50, 250, 1800, 20000},
                {0, 0, 0, 50, 150, 300, 3000, 38000},
                {0, 0, 0, 0, 100, 300, 2000, 13300, 80000},
                {0, 0, 0, 0, 50, 200, 700, 5200, 35100, 80000},
                {0, 0, 0, 0, 50, 150, 250, 1200, 7500, 62600, 80000},    //2bet
                {0, 0, 575},
                {0, 0, 150, 1050},
                {0, 0, 75, 225, 3150},
                {0, 0, 0, 150, 1000, 14400},
                {0, 0, 0, 75, 375, 2700, 37200},
                {0, 0, 0, 75, 225, 450, 5250, 65100},
                {0, 0, 0, 0, 150, 450, 3300, 21000, 80000},
                {0, 0, 0, 0, 75, 300, 1125, 8550, 57600, 80000},
                {0, 0, 0, 0, 75, 225, 375, 2025, 13200, 80000, 80000},    //3bet
                {0, 0, 800},
                {0, 0, 200, 1500},
                {0, 0, 100, 300, 4600},
                {0, 0, 0, 200, 1400, 19600},
                {0, 0, 0, 100, 500, 4000, 51000},
                {0, 0, 0, 100, 300, 700, 7400, 80000},
                {0, 0, 0, 0, 200, 600, 4700, 30500, 80000},
                {0, 0, 0, 0, 100, 400, 1600, 12100, 80000, 80000},
                {0, 0, 0, 0, 100, 300, 500, 3200, 18200, 80000, 80000},    //4bet
                {0, 0, 1000},
                {0, 0, 250, 1925},
                {0, 0, 125, 375, 5875},
                {0, 0, 0, 250, 1800, 25000},
                {0, 0, 0, 125, 650, 5000, 68000},
                {0, 0, 0, 125, 375, 1000, 9000, 80000},
                {0, 0, 0, 0, 250, 750, 6250, 39000, 80000},
                {0, 0, 0, 0, 125, 500, 2200, 15100, 80000, 80000},
                {0, 0, 0, 0, 125, 375, 625, 4375, 23625, 80000, 80000},    //5bet
                {0, 0, 1250},
                {0, 0, 300, 2350},
                {0, 0, 150, 450, 7375},
                {0, 0, 0, 300, 2250, 30000},
                {0, 0, 0, 150, 800, 6300, 80000},
                {0, 0, 0, 150, 450, 1250, 11700, 80000},
                {0, 0, 0, 0, 300, 900, 8000, 48300, 80000},
                {0, 0, 0, 0, 150, 600, 2700, 19800, 80000, 80000},
                {0, 0, 0, 0, 150, 450, 750, 5700, 30000, 80000, 80000},    //6bet
                {0, 0, 1475},
                {0, 0, 350, 2825},
                {0, 0, 175, 525, 8900},
                {0, 0, 0, 350, 2650, 37100},
                {0, 0, 0, 175, 1050, 7000, 80000},
                {0, 0, 0, 175, 525, 1550, 14400, 80000},
                {0, 0, 0, 0, 350, 1075, 9600, 56000, 80000},
                {0, 0, 0, 0, 175, 700, 3325, 23700, 80000, 80000},
                {0, 0, 0, 0, 175, 525, 875, 7200, 35000, 80000, 80000},    //7bet
                {0, 0, 1700},
                {0, 0, 400, 3300},
                {0, 0, 200, 600, 10500},
                {0, 0, 0, 400, 3000, 47300},
                {0, 0, 0, 200, 1200, 8800, 80000},
                {0, 0, 0, 200, 600, 1800, 17400, 80000},
                {0, 0, 0, 0, 400, 1200, 11800, 65800, 80000},
                {0, 0, 0, 0, 200, 800, 3800, 30000, 80000, 80000},
                {0, 0, 0, 0, 200, 600, 1000, 8200, 47500, 80000, 80000},    //8bet
        };
        return computePayTables(payTables, gameLogicBean);
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
        return 4;
    }

    @Override
    protected int fsSetCount() {
        return 3;
    }

    @Override
    public int baseSetNumbersCount() {
        return 4;
    }

    @Override
    public int fsSetNumbersCount() {
        return 3;
    }

    public int[] getABCSet() {
        int fsTimes = RandomUtil.getRandomFromArrayWithWeight(SET_TIMES[0], SET_TIMES[1]);  //fs times
        return new int[]{2, fsTimes};

    }

    public int[] getMixSetBaseInfo() {
        int hit8FsTimes = RandomUtil.getRandomFromArrayWithWeight(BASE_8MIX_SET_TIMES[0], BASE_8MIX_SET_TIMES[1]);  //fs times
        int hit9FsTimes = RandomUtil.getRandomFromArrayWithWeight(BASE_9MIX_SET_TIMES[0], BASE_9MIX_SET_TIMES[1]);  //fs times
        return new int[]{
                4, 5, hit8FsTimes, hit9FsTimes, MAX_PRIZE
        };
    }

    public int[] getMixSetFsInfo() {
        int hit11FsTimes = RandomUtil.getRandomFromArrayWithWeight(FS_11MIX_SET_TIMES[0], FS_11MIX_SET_TIMES[1]);  //fs times
        int hit12FsTimes = RandomUtil.getRandomFromArrayWithWeight(FS_12MIX_SET_TIMES[0], FS_12MIX_SET_TIMES[1]);  //fs times
        return new int[]{
                4, 10, hit11FsTimes, hit12FsTimes, MAX_PRIZE
        };
    }

    public int[] getDSet() {
        int fsTimes = RandomUtil.getRandomFromArrayWithWeight(SETD_TIMES[0], SETD_TIMES[1]);  //fs times
        return new int[]{2, fsTimes};
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
                setANumbers = getExtraDrawNumbers(leftNumbers, baseSetNumbersCount());
            }
            if (setBNumbers.isEmpty()) {
                setBNumbers = getExtraDrawNumbers(leftNumbers, baseSetNumbersCount());
            }
            if (setCNumbers.isEmpty()) {
                setCNumbers = getExtraDrawNumbers(leftNumbers, baseSetNumbersCount());
            }
            if (additionsSetsNumbers != null) {
                additionsSetsNumbers.clear();
            } else {
                additionsSetsNumbers = new ArrayList<>();
            }
            additionsSetsNumbers.add(setANumbers);
            additionsSetsNumbers.add(setBNumbers);
            additionsSetsNumbers.add(setCNumbers);
            //alone A,B,C match
            int totalMatchCount = 0;
            for (List<Integer> setsNumber : additionsSetsNumbers) {
                int matchCount = computeMatchCount(randomNumbers, setsNumber);
                totalMatchCount += matchCount;
                if (matchCount >= KenoGameConstant.BASE_SET_MUL_SPOTS) {
                    int index = matchCount - KenoGameConstant.BASE_SET_MUL_SPOTS;
                    if (matchCount == KenoGameConstant.BASE_SET_MUL_SPOTS) {
                        int baseMul = getABCSet()[index];
                        if (baseMul > 0) {
                            baseWinMul *= baseMul;
                        }
                        winMul.add(baseMul);
                    } else {
                        int fsTimes = getABCSet()[index];
                        fsTotalTimes += fsTimes;
                        fsCounts.add(fsTimes);
                    }
                }
                setsMatchCount.add(matchCount);
            }
            //mix ABC match
            if (totalMatchCount == 0) {
                baseWinMul *= MIX_HIT_MUL;
                winMul.add(MIX_HIT_MUL);
            } else if (totalMatchCount >= 6) {
                int index = totalMatchCount - 6;
                if (index > 4) {
                    index = 4;
                }
                int result = getMixSetBaseInfo()[index];
                if (totalMatchCount < 8) {
                    if (result > 0) {
                        baseWinMul *= result;
                    }
                    winMul.add(result);
                } else if (totalMatchCount < 10) {
                    fsTotalTimes += result;
                    fsCounts.add(result);
                } else {
                    //The max prize is $800
                    winPay = result;
                }
            }

            int matchCount = computeMatchCount(randomNumbers, selectNumbers);
            if (winPay < maxTotalPay()) {
                long[][] payTable = getPayTable(gameLogicCache);
                winPay = payTable[selectNumbers.size() - 2][matchCount];
                winPay *= baseWinMul;
            }
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
        List<List<Integer>> additionsSetsNumbers = new ArrayList<>();
        additionsSetsNumbers.addAll(gameLogicCache.getKenoResult().getAdditionsSetsNumbers());

        int totalMatchCount = 0;
        int fsTotalTimes = 0;
        List<Integer> winMul = new ArrayList<>();
        List<Integer> fsCounts = new ArrayList<>();
        List<Integer> setsMatchCount = new ArrayList<>();
        long winPay = 0;
        int fsWinMul = 1;
        //alone A,B,C match
        if (ObjectUtil.isNotEmpty(additionsSetsNumbers)) {
            for (List<Integer> setsNumber : additionsSetsNumbers) {
                leftNumbers.removeAll(setsNumber);
                int matchCount = computeMatchCount(randomNumbers, setsNumber);
                setsMatchCount.add(matchCount);
                totalMatchCount += matchCount;
            }
        }
        //setD numbers And match count
        List<Integer> setDNumbers = getExtraDrawNumbers(leftNumbers, fsSetNumbersCount());
        int matchDCount = computeMatchCount(randomNumbers, setDNumbers);
        totalMatchCount += matchDCount;
        additionsSetsNumbers.add(setDNumbers);
        setsMatchCount.add(matchDCount);
        int mixTotalMatchCount = totalMatchCount;
        //random extra DrawNumber
        List<Integer> leftRandomNumbers = getLeftNumbers(allNumbers, randomNumbers);
        List<Integer> extraDrawNumberList = new ArrayList<>();
        while (totalMatchCount > 0) {
            List<Integer> extraDrawNumber = getRandomNumbers(StringUtil.ListToIntegerArray(leftRandomNumbers), totalMatchCount);
            extraDrawNumberList.addAll(extraDrawNumber);
            totalMatchCount = 0;
            for (int i = 0; i < additionsSetsNumbers.size(); i++) {
                List<Integer> setNumbers = additionsSetsNumbers.get(i);
                int matchCount = computeMatchCount(extraDrawNumber, setNumbers);
                totalMatchCount += matchCount;
                mixTotalMatchCount += matchCount;
                setsMatchCount.set(i, setsMatchCount.get(i) + matchCount);
            }
            leftRandomNumbers.removeAll(extraDrawNumber);
        }
        //alone A,B,C,D win
        int setIndex = 0;
        for (int matchCount : setsMatchCount) {
            setIndex++;
            //set D compute
            if (setIndex == 4) {
                if (matchCount >= KenoGameConstant.FS_SET_MUL_SPOTS) {
                    int index = matchCount - KenoGameConstant.FS_SET_MUL_SPOTS;
                    if (matchCount == KenoGameConstant.FS_SET_MUL_SPOTS) {
                        int fsMul = getDSet()[index];
                        if (fsMul > 0) {
                            fsWinMul *= fsMul;
                        }
                        winMul.add(fsMul);
                    } else {
                        int fsTimes = getDSet()[index];
                        fsTotalTimes += fsTimes;
                        fsCounts.add(fsTimes);
                    }
                }
            } else {
                if (matchCount >= KenoGameConstant.BASE_SET_MUL_SPOTS) {
                    int index = matchCount - KenoGameConstant.BASE_SET_MUL_SPOTS;
                    if (matchCount == KenoGameConstant.BASE_SET_MUL_SPOTS) {
                        int fsMul = getABCSet()[index];
                        if (fsMul > 0) {
                            fsWinMul *= fsMul;
                        }
                        winMul.add(fsMul);
                    } else {
                        int fsTimes = getABCSet()[index];
                        fsTotalTimes += fsTimes;
                        fsCounts.add(fsTimes);
                    }
                }
            }
        }
        //mix set match compute win
        if (mixTotalMatchCount == 0) {
            fsWinMul *= MIX_HIT_MUL;
            winMul.add(MIX_HIT_MUL);
        } else if (mixTotalMatchCount >= 9) {
            int index = mixTotalMatchCount - 9;
            if (index > 4) {
                index = 4;
            }
            int result = getMixSetFsInfo()[index];
            if (mixTotalMatchCount < 11) {
                if (result > 0) {
                    fsWinMul *= result;
                }
                winMul.add(result);
            } else if (mixTotalMatchCount < 13) {
                fsTotalTimes += result;
                fsCounts.add(result);
            } else {
                //The max prize is $800
                winPay = result;
            }
        }
        int matchCount = computeMatchCount(randomNumbers, kenoResult.getSelectNumbers());
        if (!extraDrawNumberList.isEmpty()) {
            matchCount += computeMatchCount(extraDrawNumberList, kenoResult.getSelectNumbers());
        }
        //The max prize is $800
        if (winPay < maxTotalPay()) {
            long[][] payTable = getPayTable(gameLogicCache);
            winPay = payTable[kenoResult.getSelectNumbers().size() - 2][matchCount];
            winPay *= fsWinMul;
        }
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

    public List<Integer> getExtraDrawNumbers(List<Integer> leftNumbers, int number) {
        List<Integer> drawNumbers = new ArrayList<>();
        int[] setNumberIndexs = RandomUtil.getRandomIndex(leftNumbers.size(), number);
        if (setNumberIndexs != null) {
            for (int setAIndex : setNumberIndexs) {
                drawNumbers.add(leftNumbers.get(setAIndex));
            }
            leftNumbers.removeAll(drawNumbers);
        }
        return drawNumbers;
    }


}
