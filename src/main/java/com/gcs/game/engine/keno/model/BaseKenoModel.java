package com.gcs.game.engine.keno.model;

import com.gcs.game.engine.keno.utils.KenoGameConstant;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoResult;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class BaseKenoModel {

    public abstract long[][] getPayTable(KenoGameLogicBean gameLogicBean);

    public abstract long minLines();

    public abstract long minBet();

    public abstract long maxLines();

    public abstract long maxBet();

    public abstract long totalBet(long lines, long bet);

    public abstract int[] getAllRandomDigits();

    protected abstract int getRandomCount();

    public abstract int minSelectNumbersCount();

    public abstract int maxSelectNumbersCount();

    protected abstract int baseSetCount();

    protected abstract int fsSetCount();

    protected abstract int baseSetNumbersCount();

    protected abstract int fsSetNumbersCount();

    public abstract int[][] mixHitOnAll3Sets();

    public abstract int[][] mixHitOnAll4Sets();

    public abstract long maxTotalPay();

    public abstract int hitSetDefaultMul();

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
            List<Integer> leftNumbers = getLeftNumbers(allNumbers, selectNumbers);
            int baseAdditionsLen = baseSetCount() * baseSetNumbersCount();
            int[] additionsSetsNumsIndex = RandomUtil.getRandomIndex(leftNumbers.size(), baseAdditionsLen);
            List<List<Integer>> additionsSetsNumbers = new ArrayList<>();
            int totalMatchCount = 0;
            int fsTotalTimes = 0;
            List<Integer> winMul = new ArrayList<>();
            List<Integer> fsCounts = new ArrayList<>();
            List<Integer> setsMatchCount = new ArrayList<>();
            long winPay = 0;
            int baseWinMul = 1;
            for (int setIndex = 0; setIndex < baseSetCount(); setIndex++) {
                List<Integer> setNumbers = new ArrayList<>();
                int setCounts = setIndex * baseSetNumbersCount();
                for (int numIndex = 0; numIndex < baseSetNumbersCount(); numIndex++) {
                    int index = setCounts + numIndex;
                    setNumbers.add(leftNumbers.get(additionsSetsNumsIndex[index]));
                }
                if (input != null && input.getInputHandsCards() != null && input.getInputHandsCards().size() > 2 + setIndex) {
                    setNumbers = input.getInputHandsCards().get(2 + setIndex);
                }
                int matchCount = computeMatchCount(randomNumbers, setNumbers);
                setsMatchCount.add(matchCount);
                totalMatchCount += matchCount;
                if (matchCount == KenoGameConstant.BASE_SET_MAX_SPOTS) {
                    int fsTime = getSetFsTimes(false);
                    fsTotalTimes += fsTime;
                    fsCounts.add(fsTime);
                } else if (matchCount == KenoGameConstant.BASE_SET_MUL_SPOTS) {
                    winMul.add(hitSetDefaultMul());
                    baseWinMul *= hitSetDefaultMul();
                }
                additionsSetsNumbers.add(setNumbers);
            }
            int[][] mixHitOnAllSet = mixHitOnAll3Sets();
            boolean isMaxTotalPay = false;
            //MIX HIT on all 3 sets
            for (int index = 0; index < mixHitOnAllSet[0].length; index++) {
                if (index == mixHitOnAllSet[0].length - 1) {
                    if (totalMatchCount >= mixHitOnAllSet[0][index]) {
                        //Award
                        if (mixHitOnAllSet[3][index] > 0) {
                            isMaxTotalPay = true;
                            winPay = mixHitOnAllSet[3][index];
                        }
                        break;
                    }
                } else {
                    if (totalMatchCount == mixHitOnAllSet[0][index]) {
                        //win Mul
                        if (mixHitOnAllSet[1][index] > 0) {
                            winMul.add(mixHitOnAllSet[1][index]);
                            baseWinMul *= mixHitOnAllSet[1][index];
                        }
                        //freeSpin Games
                        if (mixHitOnAllSet[2][index] > 0) {
                            fsTotalTimes += mixHitOnAllSet[2][index];
                            fsCounts.add(mixHitOnAllSet[2][index]);
                        }
                        break;
                    }
                }
            }
            int matchCount = computeMatchCount(randomNumbers, selectNumbers);
            long[][] payTable = getPayTable(gameLogicCache);
            //The max prize is $800
            if (!isMaxTotalPay) {
                winPay = payTable[selectNumbers.size() - 2][matchCount];
                winPay *= baseWinMul;
            }
            if (maxTotalPay() > 0 && winPay >= maxTotalPay()) {
                winPay = maxTotalPay();
            }
            kenoResult.setRandomNumbers(randomNumbers);
            kenoResult.setAdditionsSetsNumbers(additionsSetsNumbers);
            kenoResult.setBaseMul(baseWinMul);
            kenoResult.setMixHitMatchCount(totalMatchCount);
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

    public List<Integer> getRandomNumbers(int[] allNumbers, int count) {
        if (count > 0) {
            int[] randomIndex = RandomUtil.getRandomIndex(allNumbers.length, count);
            List<Integer> randomNumbers = new ArrayList<>();
            for (int index = 0; index < randomIndex.length; index++) {
                randomNumbers.add(allNumbers[randomIndex[index]]);
            }
            return randomNumbers;
        }
        return null;
    }

    private int computeMatchCount(List<Integer> randomNumbers, List<Integer> setNumbers) {
        AtomicInteger matchCount = new AtomicInteger();
        setNumbers.forEach(number -> {
            if (randomNumbers.contains(number)) {
                matchCount.getAndIncrement();
            }
        });
        return matchCount.intValue();
    }

    protected int getSetFsTimes(boolean isFsSet) {
        return 1;
    }

    private List<Integer> getLeftNumbers(int[] allNumbers, List<Integer> numberList) {
        List<Integer> leftNumbers = new ArrayList<>();
        for (int number : allNumbers) {
            if (!numberList.contains(number)) {
                leftNumbers.add(number);
            }
        }
        return leftNumbers;
    }

    public KenoResult spinInFs(KenoGameLogicBean gameLogicCache, InputInfo input) {
        KenoResult kenoResult = new KenoResult();
        //selectNumbers same baseGame in fs
        kenoResult.setSelectNumbers(gameLogicCache.getKenoResult().getSelectNumbers());
        int[] allNumbers = getAllRandomDigits();
        List<Integer> randomNumbers = getRandomNumbers(allNumbers, getRandomCount());
        List<Integer> leftNumbers = getLeftNumbers(allNumbers, kenoResult.getSelectNumbers());
        int fsAdditionsLen = baseSetCount() * baseSetNumbersCount() + fsSetNumbersCount();
        int[] setsNumsIndex = RandomUtil.getRandomIndex(leftNumbers.size(), fsAdditionsLen);
        List<List<Integer>> additionsSetsNumbers = new ArrayList<>();
        int totalMatchCount = 0;
        int fsTotalTimes = 0;
        List<Integer> winMul = new ArrayList<>();
        List<Integer> fsCounts = new ArrayList<>();
        List<Integer> setsMatchCount = new ArrayList<>();
        long winPay = 0;
        int fsWinMul = 1;
        for (int setIndex = 0; setIndex < baseSetCount(); setIndex++) {
            List<Integer> setNumbers = new ArrayList<>();
            int setCounts = setIndex * baseSetNumbersCount();
            for (int numIndex = 0; numIndex < baseSetNumbersCount(); numIndex++) {
                int index = setCounts + numIndex;
                setNumbers.add(leftNumbers.get(setsNumsIndex[index]));
            }
            int matchCount = computeMatchCount(randomNumbers, setNumbers);
            setsMatchCount.add(matchCount);
            totalMatchCount += matchCount;
            if (matchCount == KenoGameConstant.BASE_SET_MAX_SPOTS) {
                int fsTime = getSetFsTimes(false);
                fsTotalTimes += fsTime;
                fsCounts.add(fsTime);
            } else if (matchCount == KenoGameConstant.BASE_SET_MUL_SPOTS) {
                winMul.add(hitSetDefaultMul());
                fsWinMul *= hitSetDefaultMul();
            }
            additionsSetsNumbers.add(setNumbers);
        }
        //set4 numbers And match count
        List<Integer> setNumbers = new ArrayList<>();
        int setCounts = baseSetCount() * baseSetNumbersCount();
        for (int numIndex = 0; numIndex < fsSetNumbersCount(); numIndex++) {
            int index = setCounts + numIndex;
            setNumbers.add(leftNumbers.get(setsNumsIndex[index]));
        }
        int fsSetMatchCount = computeMatchCount(randomNumbers, setNumbers);
        setsMatchCount.add(fsSetMatchCount);
        totalMatchCount += fsSetMatchCount;
        if (fsSetMatchCount == KenoGameConstant.FS_SET_MAX_SPOTS) {
            int fsTime = getSetFsTimes(true);
            fsTotalTimes += fsTime;
            fsCounts.add(fsTime);
        } else if (fsSetMatchCount == KenoGameConstant.FS_SET_MUL_SPOTS) {
            winMul.add(hitSetDefaultMul());
            fsWinMul *= hitSetDefaultMul();
        }
        additionsSetsNumbers.add(setNumbers);

        int[][] mixHitOnAllSet = mixHitOnAll4Sets();
        boolean isMaxTotalPay = false;
        //MIX HIT on all 4 sets
        for (int index = 0; index < mixHitOnAllSet[0].length; index++) {
            if (index == mixHitOnAllSet[0].length - 1) {
                if (totalMatchCount >= mixHitOnAllSet[0][index]) {
                    //Award
                    if (mixHitOnAllSet[3][index] > 0) {
                        isMaxTotalPay = true;
                        winPay = mixHitOnAllSet[3][index];
                    }
                    break;
                }
            } else {
                if (totalMatchCount == mixHitOnAllSet[0][index]) {
                    //win Mul
                    if (mixHitOnAllSet[1][index] > 0) {
                        winMul.add(mixHitOnAllSet[1][index]);
                        fsWinMul *= mixHitOnAllSet[1][index];
                    }
                    //freeSpin Games
                    if (mixHitOnAllSet[2][index] > 0) {
                        fsTotalTimes += mixHitOnAllSet[2][index];
                        fsCounts.add(mixHitOnAllSet[2][index]);
                    }
                    break;
                }
            }
        }
        List<Integer> leftRandomNumbers = getLeftNumbers(allNumbers, randomNumbers);
        List<Integer> extraDrawNumber = getRandomNumbers(StringUtil.ListToIntegerArray(leftRandomNumbers), totalMatchCount);
        int matchCount = computeMatchCount(randomNumbers, kenoResult.getSelectNumbers());
        if (extraDrawNumber != null && totalMatchCount > 0) {
            matchCount += computeMatchCount(extraDrawNumber, kenoResult.getSelectNumbers());
        }
        long[][] payTable = getPayTable(gameLogicCache);
        //The max prize is $800
        if (!isMaxTotalPay) {
            winPay = payTable[kenoResult.getSelectNumbers().size() - 2][matchCount];
            winPay *= fsWinMul;
        }
        if (maxTotalPay() > 0 && winPay >= maxTotalPay()) {
            winPay = maxTotalPay();
        }
        kenoResult.setRandomNumbers(randomNumbers);
        kenoResult.setAdditionsSetsNumbers(additionsSetsNumbers);
        kenoResult.setExtraDrawNumbers(extraDrawNumber);
        kenoResult.setMixHitMatchCount(totalMatchCount);
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
