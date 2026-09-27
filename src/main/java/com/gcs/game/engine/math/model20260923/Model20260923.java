package com.gcs.game.engine.math.model20260923;

import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoResult;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bird Of A KindKeno
 */
@Slf4j
public class Model20260923 extends BaseKenoModel {

    public static final int[] SET_TIMES = new int[]{
            20, 7, 4
    };
    public static final int[] SET_MUL = new int[]{5, 3, 2};

    public static final int[] FS_EXTRA_DRAW = new int[]{2, 3, 4, 5, 7, 10, 12};
    public static final int[][] EXTRA_DRAW_WEIGHT = new int[][]{
            {8, 11, 15, 18, 18, 19, 11},   //mark 2
            {10, 15, 19, 18, 18, 10, 10},  //mark 3
            {12, 19, 19, 19, 12, 11, 8},   //mark 4
            {14, 18, 18, 19, 17, 9, 5},    //mark 5
            {21, 22, 19, 21, 9, 5, 3},     //mark 6
            {19, 20, 19, 20, 12, 7, 3},    //mark 7
            {20, 20, 20, 22, 11, 4, 3},    //mark 8
            {19, 22, 21, 20, 11, 4, 3},    //mark 9
            {19, 21, 21, 20, 12, 4, 3},    //mark 10
    };
    public static final int[] FS_MUL = new int[]{2, 3, 4, 5, 6, 8, 10};
    public static final int[][] FS_MUL_WEIGHT = new int[][]{
            {8, 11, 19, 18, 16, 16, 12},   //mark 2
            {14, 18, 19, 20, 11, 10, 8},  //mark 3
            {18, 20, 21, 20, 11, 6, 4},   //mark 4
            {19, 21, 21, 20, 11, 5, 3},    //mark 5
            {22, 22, 23, 20, 8, 3, 2},     //mark 6
            {20, 22, 22, 21, 10, 3, 2},    //mark 7
            {20, 22, 22, 21, 10, 3, 2},    //mark 8
            {21, 22, 22, 22, 8, 3, 2},    //mark 9
            {21, 22, 22, 22, 8, 3, 2},    //mark 10
    };

    public static final int[][] FS_EXTRA_TIMES = new int[][]{
            {0, 1, 2, 3, 5},
            {90, 2, 4, 3, 1}
    };


    @Override
    public long[][] getPayTable(KenoGameLogicBean gameLogicBean) {
        long[][] payTables = new long[][]{
                {0, 0, 150},
                {0, 0, 50, 225},
                {0, 0, 25, 75, 625},
                {0, 0, 0, 50, 200, 4250},
                {0, 0, 0, 25, 125, 800, 9325},
                {0, 0, 0, 25, 75, 125, 1050, 22000},
                {0, 0, 0, 0, 50, 125, 925, 5525, 43500},
                {0, 0, 0, 0, 25, 100, 300, 2200, 11000, 80000},
                {0, 0, 0, 0, 25, 75, 125, 400, 3000, 27000, 80000},    //1bet
                {0, 0, 300},
                {0, 0, 100, 450},
                {0, 0, 50, 150, 1250},
                {0, 0, 0, 100, 450, 8250},
                {0, 0, 0, 50, 250, 1650, 21300},
                {0, 0, 0, 50, 150, 250, 2475, 50000},
                {0, 0, 0, 0, 100, 250, 2000, 11150, 80000},
                {0, 0, 0, 0, 50, 200, 600, 4775, 30000, 80000},
                {0, 0, 0, 0, 50, 125, 250, 1175, 8475, 80000, 80000},    //2bet
                {0, 0, 450},
                {0, 0, 150, 675},
                {0, 0, 75, 225, 1875},
                {0, 0, 0, 150, 700, 13200},
                {0, 0, 0, 75, 300, 3200, 35150},
                {0, 0, 0, 75, 200, 425, 4750, 80000},
                {0, 0, 0, 0, 150, 350, 3100, 22650, 80000},
                {0, 0, 0, 0, 75, 300, 900, 7900, 50000, 80000},
                {0, 0, 0, 0, 75, 225, 325, 1450, 13100, 80000, 80000},    //3bet
                {0, 0, 625},
                {0, 0, 200, 1000},
                {0, 0, 100, 300, 2975},
                {0, 0, 0, 200, 1000, 21400},
                {0, 0, 0, 100, 500, 4000, 56500},
                {0, 0, 0, 100, 300, 600, 6650, 80000},
                {0, 0, 0, 0, 200, 500, 4800, 31300, 80000},
                {0, 0, 0, 0, 100, 400, 1400, 11800, 80000, 80000},
                {0, 0, 0, 0, 100, 300, 500, 2400, 18400, 80000, 80000},    //4bet
                {0, 0, 775},
                {0, 0, 250, 1250},
                {0, 0, 125, 375, 3775},
                {0, 0, 0, 250, 1325, 28000},
                {0, 0, 0, 125, 625, 5125, 77200},
                {0, 0, 0, 125, 375, 800, 8475, 80000},
                {0, 0, 0, 0, 250, 650, 6125, 43000, 80000},
                {0, 0, 0, 0, 125, 500, 1750, 16700, 80000, 80000},
                {0, 0, 0, 0, 125, 375, 625, 3125, 26400, 80000, 80000},    //5bet
                {0, 0, 925},
                {0, 0, 300, 1500},
                {0, 0, 150, 450, 4600},
                {0, 0, 0, 300, 1650, 35900},
                {0, 0, 0, 150, 750, 6625, 80000},
                {0, 0, 0, 150, 450, 1000, 10500, 80000},
                {0, 0, 0, 0, 300, 825, 7400, 53700, 80000},
                {0, 0, 0, 0, 150, 600, 2100, 23300, 80000, 80000},
                {0, 0, 0, 0, 150, 450, 750, 4050, 33000, 80000, 80000},    //6bet
                {0, 0, 1100},
                {0, 0, 350, 1750},
                {0, 0, 175, 525, 5475},
                {0, 0, 0, 350, 2025, 42400},
                {0, 0, 0, 175, 875, 8275, 80000},
                {0, 0, 0, 175, 525, 1200, 12650, 80000},
                {0, 0, 0, 0, 350, 1000, 8750, 64500, 80000},
                {0, 0, 0, 0, 175, 800, 2400, 24000, 80000, 80000},
                {0, 0, 0, 0, 175, 525, 875, 5000, 40000, 80000, 80000},    //7bet
                {0, 0, 1250},
                {0, 0, 400, 2000},
                {0, 0, 200, 600, 6400},
                {0, 0, 0, 400, 2400, 50000},
                {0, 0, 0, 200, 1000, 10000, 80000},
                {0, 0, 0, 200, 600, 1400, 15000, 80000},
                {0, 0, 0, 0, 400, 1200, 10000, 74000, 80000},
                {0, 0, 0, 0, 200, 800, 3200, 33000, 80000, 80000},
                {0, 0, 0, 0, 200, 600, 1000, 6000, 50000, 80000, 80000},    //8bet
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
        return 3;
    }

    @Override
    protected int fsSetCount() {
        return 3;
    }

    @Override
    public int baseSetNumbersCount() {
        return 2;
    }

    @Override
    protected int fsSetNumbersCount() {
        return 2;
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
        Model20260923KenoResult kenoResult = null;
        if (gameLogicCache.getKenoResult() instanceof Model20260923KenoResult) {
            kenoResult = (Model20260923KenoResult) gameLogicCache.getKenoResult();
        } else {
            kenoResult = new Model20260923KenoResult();
            kenoResult.setSelectNumbers(gameLogicCache.getKenoResult().getSelectNumbers());
        }
        if (kenoResult != null) {
            List<Integer> selectNumbers = kenoResult.getSelectNumbers();
            int[] allNumbers = getAllRandomDigits();
            List<Integer> randomNumbers = getRandomNumbers(allNumbers, getRandomCount());
            if (input != null && input.getInputHandsCards() != null && input.getInputHandsCards().size() > 1) {
                selectNumbers = input.getInputHandsCards().get(0);
                kenoResult.setSelectNumbers(selectNumbers);
                randomNumbers = input.getInputHandsCards().get(1);
            }
            List<Integer> leftNumbers = getLeftNumbers(allNumbers, selectNumbers);
            List<List<Integer>> additionsSetsNumbers = new ArrayList<>();
            int fsTotalTimes = 0;
            List<Integer> winMul = new ArrayList<>();
            List<Integer> fsCounts = new ArrayList<>();
            List<Integer> setsMatchCount = new ArrayList<>();
            long winPay = 0;
            int baseWinMul = 1;
            //SetA numbers
            List<Integer> setANumbers = getExtraDrawNumbers(leftNumbers, baseSetNumbersCount());
            additionsSetsNumbers.add(setANumbers);
            //SetB numbers
            List<Integer> setBNumbers = getExtraDrawNumbers(leftNumbers, baseSetNumbersCount());
            additionsSetsNumbers.add(setBNumbers);
            //SetC numbers
            List<Integer> setCNumbers = getExtraDrawNumbers(leftNumbers, baseSetNumbersCount());
            additionsSetsNumbers.add(setCNumbers);
            //match SetA numbers
            int matchCountA = computeMatchCount(randomNumbers, setANumbers);
            setsMatchCount.add(matchCountA);
            //match SetB numbers
            int matchCountB = computeMatchCount(randomNumbers, setBNumbers);
            setsMatchCount.add(matchCountB);
            //match SetC numbers
            int matchCountC = computeMatchCount(randomNumbers, setCNumbers);
            setsMatchCount.add(matchCountC);

            int pairCount = 0;
            int halfPairCount = 0;
            for (int match : setsMatchCount) {
                if (match == 2) {
                    pairCount++;
                } else if (match == 1) {
                    halfPairCount++;
                }
            }
            int prizeType = computePrizeType(pairCount, halfPairCount);
            if (prizeType > 0 && prizeType < 4) {
                int fsTime = SET_TIMES[prizeType - 1];
                fsTotalTimes += fsTime;
                fsCounts.add(fsTime);
            }
            if (prizeType > 3 && prizeType <= 6) {
                baseWinMul = SET_MUL[prizeType - 4];
                winMul.add(baseWinMul);
            }
            int matchCount = computeMatchCount(randomNumbers, selectNumbers);
            long[][] payTable = getPayTable(gameLogicCache);
            winPay = payTable[selectNumbers.size() - minSelectNumbersCount()][matchCount];
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
            kenoResult.setPairsType(prizeType);
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

    private int computePrizeType(int pairCount, int halfPairCount) {
        //all 3 pairs
        if (pairCount == 3) {
            return 1;
        }
        //any 2 pairs+half pair
        if (pairCount == 2 && halfPairCount == 1) {
            return 2;
        }
        //any 2 pairs
        if (pairCount == 2 && halfPairCount == 0) {
            return 3;
        }
        //1 pair+2half pair
        if (pairCount == 1 && halfPairCount == 2) {
            return 4;
        }
        //1 pair+1half pair
        if (pairCount == 1 && halfPairCount == 1) {
            return 5;
        }
        //1 pair
        if (pairCount == 1 && halfPairCount == 0) {
            return 6;
        }
        return 0;
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


    public KenoResult spinInFs(KenoGameLogicBean gameLogicCache, InputInfo input) {
        Model20260923KenoResult kenoResult = new Model20260923KenoResult();
        //selectNumbers same baseGame in fs
        kenoResult.setSelectNumbers(gameLogicCache.getKenoResult().getSelectNumbers());
        int[] allNumbers = getAllRandomDigits();
        List<Integer> randomNumbers = getRandomNumbers(allNumbers, getRandomCount());
        List<List<Integer>> baseAdditionsSetsNumbers = gameLogicCache.getKenoResult().getAdditionsSetsNumbers();
        List<List<Integer>> additionsSetsNumbers = baseAdditionsSetsNumbers.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
        int fsTotalTimes = 0;
        List<Integer> winMul = new ArrayList<>();
        List<Integer> fsCounts = new ArrayList<>();
        List<Integer> setsMatchCount = new ArrayList<>();
        long winPay = 0;
        int fsWinMul = 1;
        int markSize = kenoResult.getSelectNumbers().size() - 2;
        //feature 1 extra draw
        RandomWeightUntil randomWeightUntil = new RandomWeightUntil(EXTRA_DRAW_WEIGHT[markSize]);
        int extraIndex = randomWeightUntil.getRandomResult();
        int extraCount = FS_EXTRA_DRAW[extraIndex];
        //random extra DrawNumber
        List<Integer> leftRandomNumbers = getLeftNumbers(allNumbers, randomNumbers);
        List<Integer> extraDrawNumberList = getRandomNumbers(StringUtil.ListToIntegerArray(leftRandomNumbers), extraCount);
        //feature 2 Multiplier
        int mulIndex = RandomUtil.getRandomIndexFromArrayWithWeight(FS_MUL_WEIGHT[markSize]);
        int fsMul = FS_MUL[mulIndex];
        fsWinMul *= fsMul;
        winMul.add(fsMul);
        //feature 3 extra free games
        RandomWeightUntil fsRandWeight = new RandomWeightUntil(FS_EXTRA_TIMES[0], FS_EXTRA_TIMES[1]);
        int fsTimes = fsRandWeight.getRandomResult();
        fsTotalTimes += fsTimes;
        fsCounts.add(fsTimes);
        List<Integer> totalRandomNumbers = new ArrayList<>();
        totalRandomNumbers.addAll(randomNumbers);
        totalRandomNumbers.addAll(extraDrawNumberList);
        //Set A number and match count
        List<Integer> setANumbers = baseAdditionsSetsNumbers.get(0);
        int matchACount = computeMatchCount(totalRandomNumbers, setANumbers);
        //Set B number and match count
        List<Integer> setBNumbers = baseAdditionsSetsNumbers.get(1);
        int matchBCount = computeMatchCount(totalRandomNumbers, setBNumbers);
        //Set C number and match count
        List<Integer> setCNumbers = baseAdditionsSetsNumbers.get(2);
        int matchCCount = computeMatchCount(totalRandomNumbers, setCNumbers);
        //compute SetA,B,C
        setsMatchCount.add(matchACount);
        setsMatchCount.add(matchBCount);
        setsMatchCount.add(matchCCount);
        int pairCount = 0;
        int halfPairCount = 0;
        for (int match : setsMatchCount) {
            if (match == 2) {
                pairCount++;
            } else if (match == 1) {
                halfPairCount++;
            }
        }
        int prizeType = computePrizeType(pairCount, halfPairCount);
        if (prizeType > 0 && prizeType < 4) {
            int fsTime = SET_TIMES[prizeType - 1];
            fsTotalTimes += fsTime;
            fsCounts.add(fsTime);
        }
        if (prizeType > 3 && prizeType <= 6) {
            fsMul = SET_MUL[prizeType - 4];
            fsWinMul *= fsMul;
            winMul.add(fsMul);
        }
        int matchCount = computeMatchCount(totalRandomNumbers, kenoResult.getSelectNumbers());
        long[][] payTable = getPayTable(gameLogicCache);
        winPay = payTable[kenoResult.getSelectNumbers().size() - minSelectNumbersCount()][matchCount];
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
        kenoResult.setPairsType(prizeType);
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
