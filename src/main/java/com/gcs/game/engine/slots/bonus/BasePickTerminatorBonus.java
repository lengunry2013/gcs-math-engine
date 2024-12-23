package com.gcs.game.engine.slots.bonus;

import com.gcs.game.engine.slots.vo.SlotPickTerminatorBonusResult;
import com.gcs.game.engine.slots.vo.SlotBonusResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;

public abstract class BasePickTerminatorBonus extends BaseBonus {

    protected abstract int[][] getAwardValues();

    protected abstract int[][] getAwardWeight();

    protected abstract int[][] getMultiplierValues();

    protected abstract int[][] getMultiplierWeight();

    protected abstract int[] getPickCount();

    protected abstract int[][] getPickItems(int index);

    protected abstract int maxPickCount();

    protected boolean multiplierIconIsTerminator() {
        return false;
    }

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameLogicBean, int payback) {
        SlotPickTerminatorBonusResult result = new SlotPickTerminatorBonusResult();
        int hitSymbolCount = getHitSymbolCount(gameLogicBean.getSlotSpinResult());
        int index = 0;
        if (hitSymbolCount >= 3 && hitSymbolCount <= 5) {
            index = hitSymbolCount - 3;
        }

        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_START;

        int[] awardValues = getAwardValues()[index];
        int[] awardWeights = getAwardWeight()[index];
        int[] multipliers = getMultiplierValues()[index];
        int[] multiplierWeights = getMultiplierWeight()[index];
        int[] pickCounts = getPickCount();

        int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(awardWeights);
        long award = awardValues[randomIndex];
        int pickCount = pickCounts[randomIndex];
        int[][] pickItems = getPickItems(randomIndex);
        long[] pickAwards = getPickAwardArray(award, pickCount, pickItems);

        int pickMultiplier = RandomUtil.getRandomFromArrayWithWeight(multipliers, multiplierWeights);
        pickAwards = computePickAwardWithMultiplier(pickMultiplier, pickAwards, pickCount);

        long[] pickCharacters = new long[pickAwards.length];
        long[] savePickAwards = new long[pickAwards.length];
        if (multiplierIconIsTerminator()) {
            int[] randomIndexs = RandomUtil.getRandomIndex(pickAwards.length - 1);
            for (int i = 0; i < randomIndexs.length; i++) {
                pickCharacters[i] = pickAwards[randomIndexs[i]];
                savePickAwards[i] = pickAwards[randomIndexs[i]];
            }
            pickCharacters[pickAwards.length - 1] = pickAwards[pickAwards.length - 1];
            savePickAwards[pickAwards.length - 1] = 0;
        } else {
            int[] randomIndexs = RandomUtil.getRandomIndex(pickAwards.length);
            for (int i = 0; i < randomIndexs.length; i++) {
                pickCharacters[i] = pickAwards[randomIndexs[i]];
                if (pickAwards[randomIndexs[i]] > 1000) {
                    savePickAwards[i] = 0;
                } else {
                    savePickAwards[i] = pickAwards[randomIndexs[i]];
                }
            }
        }

        int maxPickCount = maxPickCount();
        long[] displayCharacters4Reveal = new long[maxPickCount];
        for (int i = 0; i < maxPickCount; i++) {
            displayCharacters4Reveal[i] = RandomUtil.getRandomFromArrayWithWeight(
                    new int[]{2, 3, 4, 5, 6, 7},
                    new int[]{21, 25, 25, 16, 2, 2});
        }

        int[] pickIndexs = null;
        long totalPay = 0;
        long payForPick = 0;

        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setPickCharacters(pickCharacters);
        result.setDisplayCharacters4Reveal(displayCharacters4Reveal);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        result.setPickPays(savePickAwards);
        return result;
    }

    protected long[] computePickAwardWithMultiplier(int pickMultiplier, long[] pickAwards, int pickCount) {
        if (pickMultiplier > 1 && pickAwards != null) {
            long[] pickAwardsOld = pickAwards.clone();
            pickAwards = new long[pickCount + 1];
            for (int i = 0; i < pickCount + 1; i++) {
                if (i == pickCount) {
                    pickAwards[i] = 1000L + pickMultiplier;
                } else {
                    pickAwards[i] = pickAwardsOld[i];
                }
            }
        }
        return pickAwards;
    }

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameLogicBean, int payback, InputInfo input) {
        return computeBonusStart(gameLogicBean, payback); // TODO support
    }

    protected long[] getPickAwardArray(long totalAward, int pickCount, int[][] pickItems) {
        long[] result = new long[pickCount];
        int amount = 0;
        for (int i = 0; i < pickCount - 1; i++) {
            int[] picks = pickItems[i];
            int pick = picks[RandomUtil.getRandomInt(picks.length)];
            result[i] = pick;
            amount += pick;
        }
        if (totalAward > amount) {
            result[pickCount - 1] = totalAward - amount;
        } else {
        }
        return result;
    }

    public SlotBonusResult computeBonusPick(SlotGameLogicBean gameSessionBean, PlayerInputInfo playerInfo, SlotBonusResult bonus) {
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_PICK;
        int[] reqPickIndex = null;
        if (playerInfo != null) {
            reqPickIndex = playerInfo.getBonusPickInfos();
        }
        SlotPickTerminatorBonusResult result = null;
        long[] pickCharacters;
        int[] pickIndexs;
        long[] pickAwards;
        long[] displayCharacters4Reveal;
        long totalPay = 0;
        long payForPick = 0;
        if (bonus != null) {
            SlotPickTerminatorBonusResult basePickTerminatorBonusResult = (SlotPickTerminatorBonusResult) bonus;
            pickAwards = basePickTerminatorBonusResult.getPickPays();
            pickIndexs = basePickTerminatorBonusResult.getPickIndexInfos();
            pickCharacters = basePickTerminatorBonusResult.getPickCharacters();
            displayCharacters4Reveal = basePickTerminatorBonusResult.getDisplayCharacters4Reveal();

            if (reqPickIndex != null && reqPickIndex.length >= 1) {
                int pickCount = 0;
                if (pickIndexs != null) {
                    pickCount = pickIndexs.length;
                }
                if (reqPickIndex != null && reqPickIndex.length > pickCharacters.length) {
                    long totalBet = gameSessionBean.getSumBetCredit();
                    bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                    for (long pay : pickAwards) {
                        totalPay += pay * totalBet;
                    }
                    payForPick = totalPay;
                } else if (reqPickIndex != null && reqPickIndex.length > pickCount) {
                    int count = reqPickIndex.length;
                    int pickIndex = reqPickIndex[reqPickIndex.length - 1];
                    long character = pickCharacters[count - 1];

                    // for bonus reveal
                    displayCharacters4Reveal[pickIndex] = character;

                    if (character > 1000) {
                        long multiplier = character % 1000;
                        for (int i = 0; i < pickAwards.length; i++) {
                            pickAwards[i] = pickAwards[i] * multiplier;
                        }
                    }
                    int maxPickCount = maxPickCount();
                    if (reqPickIndex.length > pickCharacters.length || reqPickIndex.length >= maxPickCount) {
                        long totalBet = gameSessionBean.getSumBetCredit();
                        bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                        for (long pay : pickAwards) {
                            totalPay += pay * totalBet;
                        }
                        payForPick = totalPay;
                    }
                }
            }
            result = new SlotPickTerminatorBonusResult();
            result.setBonusPlayStatus(bonusStatus);
            result.setPickIndexInfos(reqPickIndex);
            result.setPickCharacters(pickCharacters);
            result.setPickPays(pickAwards);
            result.setDisplayCharacters4Reveal(displayCharacters4Reveal);
            result.setTotalPay(totalPay);
            result.setPayForPickIndex(payForPick);
        }
        return result;
    }

    public void checkInput4BonusPick(SlotGameLogicBean gameLogicBean, PlayerInputInfo playerInfo, SlotBonusResult bonus) throws InvalidPlayerInputException {
        int[] reqPickIndex = null;
        if (playerInfo != null) {
            reqPickIndex = playerInfo.getBonusPickInfos();
        }
        if (reqPickIndex == null || reqPickIndex.length == 0) {
            throw new InvalidPlayerInputException();
        }
        SlotPickTerminatorBonusResult bonusTemp = (SlotPickTerminatorBonusResult) bonus;
        int count = bonusTemp.getDisplayCharacters4Reveal().length;

        for (int i = 0; i < reqPickIndex.length; i++) {
            if (reqPickIndex[i] < 0 || reqPickIndex[i] >= count) {
                throw new InvalidPlayerInputException();
            }
        }
    }

}
