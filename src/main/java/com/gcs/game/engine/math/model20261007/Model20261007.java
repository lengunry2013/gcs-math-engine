package com.gcs.game.engine.math.model20261007;

import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoResult;
import com.gcs.game.engine.math.model20260923.Model20260923KenoResult;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Atomic Ball Keno
 * ATOMIC_BALL_KENO_20261007.xlsx
 */
@Slf4j
public class Model20261007 extends BaseKenoModel {

    @Override
    public long[][] getPayTable(KenoGameLogicBean gameLogicBean) {
        long[][] payTables = new long[][]{
                {0, 0, 275},
                {0, 0, 50, 650},
                {0, 0, 25, 75, 2175},
                {0, 0, 0, 75, 450, 3400},
                {0, 0, 0, 50, 125, 900, 6675},
                {0, 0, 0, 25, 75, 300, 2525, 19800},
                {0, 0, 0, 0, 50, 200, 1150, 7775, 40000},
                {0, 0, 0, 0, 50, 100, 275, 1800, 11400, 50000},
                {0, 0, 0, 0, 25, 75, 150, 1000, 4900, 15000, 50000},    //1bet
                {0, 0, 550},
                {0, 0, 100, 1300},
                {0, 0, 50, 150, 4500},
                {0, 0, 0, 150, 900, 7450},
                {0, 0, 0, 100, 250, 1900, 14200},
                {0, 0, 0, 50, 150, 600, 6050, 40000},
                {0, 0, 0, 0, 100, 400, 2500, 15550, 80000},
                {0, 0, 0, 0, 100, 200, 600, 3900, 24000, 80000},
                {0, 0, 0, 0, 50, 150, 300, 2250, 9900, 25000, 80000},    //2bet
                {0, 0, 825},
                {0, 0, 150, 2025},
                {0, 0, 75, 225, 6825},
                {0, 0, 0, 225, 1350, 11550},
                {0, 0, 0, 150, 375, 3000, 19500},
                {0, 0, 0, 75, 225, 900, 9600, 61500},
                {0, 0, 0, 0, 150, 600, 4050, 23100, 80000},
                {0, 0, 0, 0, 150, 300, 900, 6600, 37200, 80000},
                {0, 0, 0, 0, 75, 225, 450, 3525, 14850, 32100, 80000},    //3bet
                {0, 0, 1100},
                {0, 0, 200, 2700},
                {0, 0, 100, 300, 9300},
                {0, 0, 0, 300, 1800, 16100},
                {0, 0, 0, 200, 500, 4300, 28000},
                {0, 0, 0, 100, 300, 1200, 13600, 80000},
                {0, 0, 0, 0, 200, 800, 5800, 36700, 80000},
                {0, 0, 0, 0, 200, 400, 1300, 9000, 43000, 80000},
                {0, 0, 0, 0, 100, 300, 600, 4900, 23300, 35000, 80000},    //4bet
                {0, 0, 1375},
                {0, 0, 250, 3500},
                {0, 0, 125, 375, 11750},
                {0, 0, 0, 375, 2250, 21250},
                {0, 0, 0, 250, 625, 5500, 40000},
                {0, 0, 0, 125, 375, 1500, 17625, 80000},
                {0, 0, 0, 0, 250, 1000, 7750, 40000, 80000},
                {0, 0, 0, 0, 250, 500, 1625, 11875, 47500, 80000},
                {0, 0, 0, 0, 125, 375, 750, 6625, 28000, 43750, 80000},    //5bet
                {0, 0, 1650},
                {0, 0, 300, 4200},
                {0, 0, 150, 450, 14250},
                {0, 0, 0, 450, 2850, 26100},
                {0, 0, 0, 300, 750, 6900, 45300},
                {0, 0, 0, 150, 450, 1950, 21000, 80000},
                {0, 0, 0, 0, 300, 1200, 9600, 51000, 80000},
                {0, 0, 0, 0, 300, 600, 2100, 13800, 50100, 80000},
                {0, 0, 0, 0, 150, 450, 900, 8250, 41400, 53100, 80000},    //6bet
                {0, 0, 1925},
                {0, 0, 350, 4900},
                {0, 0, 175, 525, 16625},
                {0, 0, 0, 525, 3500, 29575},
                {0, 0, 0, 350, 875, 8225, 55650},
                {0, 0, 0, 175, 525, 2450, 26950, 80000},
                {0, 0, 0, 0, 350, 1400, 11550, 58625, 80000},
                {0, 0, 0, 0, 350, 700, 2450, 16800, 60200, 80000},
                {0, 0, 0, 0, 175, 525, 1050, 10150, 44625, 65100, 80000},    //7bet
                {0, 0, 2400},
                {0, 0, 400, 5600},
                {0, 0, 200, 600, 19200},
                {0, 0, 0, 600, 4200, 30600},
                {0, 0, 0, 400, 1000, 9600, 61400},
                {0, 0, 0, 200, 600, 3000, 31200, 80000},
                {0, 0, 0, 0, 400, 1600, 13400, 69200, 80000},
                {0, 0, 0, 0, 400, 800, 2800, 19800, 66000, 80000},
                {0, 0, 0, 0, 200, 600, 1200, 12000, 48600, 68000, 80000},    //8bet
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
    protected int baseSetNumbersCount() {
        return 0;
    }

    @Override
    protected int fsSetNumbersCount() {
        return 0;
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
            long winPay = 0;
            int baseWinMul = 1;
            int lastDrawBall = randomNumbers.get(randomNumbers.size() - 1);
            int matchCount = computeMatchCount(randomNumbers, selectNumbers);
            boolean isHitLastBall = false;
            if (selectNumbers.contains(lastDrawBall)) {
                isHitLastBall = true;
            }
            long[][] payTable = getPayTable(gameLogicCache);
            winPay = payTable[selectNumbers.size() - minSelectNumbersCount()][matchCount];
            if (winPay > 0) {
                if (isHitLastBall) {
                    baseWinMul = 4;
                    winPay *= baseWinMul;
                }
            } else {
                if (isHitLastBall) {
                    winPay = totalBet(gameLogicCache.getLines(), gameLogicCache.getBet());
                }
            }
            //The max prize is $800
            if (maxTotalPay() > 0 && winPay >= maxTotalPay()) {
                winPay = maxTotalPay();
            }
            kenoResult.setRandomNumbers(randomNumbers);
            kenoResult.setBaseMul(baseWinMul);
            kenoResult.setMatchCount(matchCount);
            kenoResult.setKenoPay(winPay);
        }
        return kenoResult;
    }

}
