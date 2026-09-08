package com.gcs.game.engine.math.model20260903;

import cn.hutool.core.util.ObjectUtil;
import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.keno.utils.KenoGameConstant;
import com.gcs.game.engine.keno.utils.numbers.KenoShapeDrawUtil;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoResult;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Rabbit in the Hat Keno
 */
@Slf4j
public class Model20260903 extends BaseKenoModel {

    public static final int[] BASE_SETA_TIMES = new int[]{
            10, 15
    };
    public static final int BASE_SETB_MUL = 3;

    public static final int[] FS_SETA_TIMES = new int[]{
            2, 5, 10
    };
    public static final int[] FS_SETC_MUL = new int[]{2, 3};
    public static final int[] FS_SETC_WEIGHT = new int[]{85, 15};
    public static final int BASE_SET_MUL_SPOTS = 2;


    @Override
    public long[][] getPayTable(KenoGameLogicBean gameLogicBean) {
        long[][] payTables = new long[][]{
                {0, 0, 50, 350},
                {0, 0, 25, 100, 675},
                {0, 0, 0, 50, 500, 1450},
                {0, 0, 0, 25, 150, 900, 5300},
                {0, 0, 0, 25, 50, 200, 2600, 17500},
                {0, 0, 0, 0, 75, 150, 700, 1950, 20000},
                {0, 0, 0, 0, 50, 100, 200, 900, 12000, 80000},
                {0, 0, 0, 0, 25, 75, 200, 400, 2000, 20000, 80000},  //1bet
                {0, 0, 100, 700},
                {0, 0, 50, 200, 1350},
                {0, 0, 0, 100, 750, 7200},
                {0, 0, 0, 100, 175, 1000, 13000},
                {0, 0, 0, 50, 150, 300, 3400, 35000},
                {0, 0, 0, 0, 150, 300, 1000, 9500, 80000},
                {0, 0, 0, 0, 50, 250, 900, 2650, 20000, 80000},
                {0, 0, 0, 0, 50, 150, 300, 1200, 6000, 60000, 80000},    //2bet
                {0, 0, 150, 1050},
                {0, 0, 75, 250, 2800},
                {0, 0, 0, 150, 950, 15000},
                {0, 0, 0, 100, 375, 2000, 31300},
                {0, 0, 0, 75, 150, 600, 8250, 61500},
                {0, 0, 0, 0, 150, 500, 3700, 10000, 80000},
                {0, 0, 0, 0, 75, 400, 1100, 5100, 40000, 80000},
                {0, 0, 0, 0, 75, 200, 550, 1800, 12000, 80000, 80000},    //3bet
                {0, 0, 200, 1550},
                {0, 0, 100, 400, 3300},
                {0, 0, 0, 200, 1350, 23000},
                {0, 0, 0, 100, 600, 3500, 50800},
                {0, 0, 0, 100, 200, 1100, 10500, 80000},
                {0, 0, 0, 0, 200, 800, 4500, 21000, 80000},
                {0, 0, 0, 0, 100, 500, 1500, 11000, 75000, 80000},
                {0, 0, 0, 0, 100, 300, 700, 2700, 17500, 80000, 80000},    //4bet
                {0, 0, 250, 1900},
                {0, 0, 125, 450, 4900},
                {0, 0, 0, 300, 1550, 25000},
                {0, 0, 0, 150, 750, 3650, 60000},
                {0, 0, 0, 125, 300, 1250, 12000, 80000},
                {0, 0, 0, 0, 250, 1000, 5000, 38100, 80000},
                {0, 0, 0, 0, 125, 550, 2200, 16000, 80000, 80000},
                {0, 0, 0, 0, 125, 350, 900, 4200, 20000, 80000, 80000},    //5bet
                {0, 0, 300, 2300},
                {0, 0, 150, 500, 6400},
                {0, 0, 0, 400, 1650, 30500},
                {0, 0, 0, 200, 800, 4750, 70000},
                {0, 0, 0, 150, 350, 1500, 15700, 80000},
                {0, 0, 0, 0, 300, 1300, 5500, 45500, 80000},
                {0, 0, 0, 0, 150, 700, 2450, 18700, 80000, 80000},
                {0, 0, 0, 0, 150, 400, 1000, 4900, 53000, 80000, 80000},    //6bet
                {0, 0, 350, 2700},
                {0, 0, 175, 525, 8350},
                {0, 0, 0, 450, 2200, 33200},
                {0, 0, 0, 250, 900, 5200, 80000},
                {0, 0, 0, 175, 400, 1900, 18500, 80000},
                {0, 0, 0, 0, 350, 1500, 6800, 50000, 80000},
                {0, 0, 0, 0, 200, 800, 2800, 22000, 80000, 80000},
                {0, 0, 0, 0, 175, 450, 1250, 5100, 80000, 80000, 80000},    //7bet
                {0, 0, 400, 3100},
                {0, 0, 200, 550, 10400},
                {0, 0, 0, 500, 2800, 35000},
                {0, 0, 0, 300, 1000, 6300, 80000},
                {0, 0, 0, 200, 500, 2000, 20500, 80000},
                {0, 0, 0, 0, 400, 1800, 7300, 60000, 80000},
                {0, 0, 0, 0, 250, 900, 3000, 24800, 80000, 80000},
                {0, 0, 0, 0, 200, 500, 1500, 6400, 80000, 80000, 80000},    //8bet
        };
        long[][] payTableResult = computePayTables(payTables, gameLogicBean);
        return payTableResult;
    }

    protected long[][] computePayTables(long[][] payTables, KenoGameLogicBean gameLogicBean) {
        long[][] payTableResult = new long[maxSelectNumbersCount() - 2][];
        int betIndex = (int) gameLogicBean.getBet() - 1;
        for (int i = 0; i < payTableResult.length; i++) {
            payTableResult[i] = payTables[i + betIndex * 8].clone();
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
        return 3;
    }

    @Override
    public int maxSelectNumbersCount() {
        return 10;
    }

    @Override
    protected int baseSetCount() {
        return 2;
    }

    @Override
    protected int fsSetCount() {
        return 2;
    }

    @Override
    public int baseSetNumbersCount() {
        return 5;
    }

    public int baseSetBNumbersCount() {
        return 2;
    }

    @Override
    protected int fsSetNumbersCount() {
        return 4;
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
            if (additionsSetsNumbers != null && !additionsSetsNumbers.isEmpty() && additionsSetsNumbers.size() >= 2) {
                setANumbers = additionsSetsNumbers.get(0);
                setBNumbers = additionsSetsNumbers.get(1);
            }
            if (setANumbers.isEmpty()) {
                setANumbers = getExtraDrawNumbers(leftNumbers, baseSetNumbersCount());
            }
            if (setBNumbers.isEmpty()) {
                setBNumbers = getExtraDrawNumbers(leftNumbers, baseSetBNumbersCount());
            }
            //SetA numbers
            int matchCount = computeMatchCount(randomNumbers, setANumbers);
            if (matchCount >= KenoGameConstant.BASE_SET_MAX_SPOTS) {
                int index = matchCount - KenoGameConstant.BASE_SET_MAX_SPOTS;
                int fsTimes = BASE_SETA_TIMES[index];
                fsTotalTimes += fsTimes;
                fsCounts.add(fsTimes);
                winMul.add(1);
            }
            setsMatchCount.add(matchCount);
            //SetB numbers
            matchCount = computeMatchCount(randomNumbers, setBNumbers);
            if (matchCount >= BASE_SET_MUL_SPOTS) {
                int baseMul = BASE_SETB_MUL;
                baseWinMul *= baseMul;
                fsCounts.add(0);
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
            matchCount = computeMatchCount(randomNumbers, selectNumbers);
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
        KenoResult kenoResult = new KenoResult();
        //selectNumbers same baseGame in fs
        kenoResult.setSelectNumbers(gameLogicCache.getKenoResult().getSelectNumbers());
        int[] allNumbers = getAllRandomDigits();
        List<Integer> randomNumbers = getRandomNumbers(allNumbers, getRandomCount());
        List<Integer> leftNumbers = getLeftNumbers(allNumbers, kenoResult.getSelectNumbers());
        List<List<Integer>> baseAdditionsSetsNumbers = gameLogicCache.getKenoResult().getAdditionsSetsNumbers();
        List<List<Integer>> additionsSetsNumbers = new ArrayList<>();
        List<KenoResult> kenoResultList = gameLogicCache.getKenoFsResult();
        if (ObjectUtil.isNotEmpty(kenoResultList)) {
            KenoResult fsKenoResult = kenoResultList.get(kenoResultList.size() - 1);
            additionsSetsNumbers = fsKenoResult.getAdditionsSetsNumbers();
        } else {
            //init fs random two new number
            for (List<Integer> setsNumber : baseAdditionsSetsNumbers) {
                List<Integer> newSetsNumbers = new ArrayList<>(setsNumber);
                additionsSetsNumbers.add(newSetsNumbers);
                leftNumbers.removeAll(setsNumber);
            }
            List<Integer> setBAddNumbers = getExtraDrawNumbers(leftNumbers, baseSetBNumbersCount());
            additionsSetsNumbers.get(1).addAll(setBAddNumbers);
        }
        int totalMatchCount = 0;
        int fsTotalTimes = 0;
        List<Integer> winMul = new ArrayList<>();
        List<Integer> fsCounts = new ArrayList<>();
        List<Integer> setsMatchCount = new ArrayList<>();
        long winPay = 0;
        int fsWinMul = 1;
        //Set A number and match count
        List<Integer> setANumbers = additionsSetsNumbers.get(0);
        int matchACount = computeMatchCount(randomNumbers, setANumbers);
        totalMatchCount += matchACount;
        //Set B number and match count
        List<Integer> setBNumbers = additionsSetsNumbers.get(1);
        int matchBCount = computeMatchCount(randomNumbers, setBNumbers);
        totalMatchCount += matchBCount;
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
            leftRandomNumbers.removeAll(extraDrawNumber);
        }
        //compute SetA,B,C,D
        setsMatchCount.add(matchACount);
        setsMatchCount.add(matchBCount);
        if (matchACount >= KenoGameConstant.FS_SET_MAX_SPOTS) {
            int index = matchACount - KenoGameConstant.FS_SET_MAX_SPOTS;
            int fsTimes = FS_SETA_TIMES[index];
            fsTotalTimes += fsTimes;
            fsCounts.add(fsTimes);
        }
        if (matchBCount >= 1) {
            RandomWeightUntil randomWeightUntil = new RandomWeightUntil(FS_SETC_WEIGHT);
            for (int i = 1; i <= matchBCount; i++) {
                int randomIndex = randomWeightUntil.getRandomResult();
                int fsMul = FS_SETC_MUL[randomIndex];
                fsWinMul *= fsMul;
                winMul.add(fsMul);
            }
        }
        int matchCount = computeMatchCount(randomNumbers, kenoResult.getSelectNumbers());
        if (!extraDrawNumberList.isEmpty()) {
            matchCount += computeMatchCount(extraDrawNumberList, kenoResult.getSelectNumbers());
        }
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
