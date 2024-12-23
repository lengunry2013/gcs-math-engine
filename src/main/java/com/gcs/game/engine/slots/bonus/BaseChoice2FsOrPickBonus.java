package com.gcs.game.engine.slots.bonus;


import com.gcs.game.engine.slots.vo.SlotChoice2FsOrPickBonusResult;
import com.gcs.game.engine.slots.vo.SlotBonusResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChoice2FsOrPickBonus extends BasePickTerminatorBonus {

    protected abstract int[][] getFreeSpinTimes();

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameLogicBean, int payback) {
        SlotChoice2FsOrPickBonusResult result = new SlotChoice2FsOrPickBonusResult();
        int hitSymbolCount = getHitSymbolCount(gameLogicBean.getSlotSpinResult());
        int index = 0;
        if (hitSymbolCount >= 3 && hitSymbolCount <= 5) {
            index = hitSymbolCount - 3;
        }

        int[][] freespinTimes = getFreeSpinTimes();
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

        if (freespinTimes.length == 1) {
            result.setFsOrBonusPick(new int[]{0, freespinTimes[0][index]});
        } else if (freespinTimes.length == 2) {
            result.setFsOrBonusPick(new int[]{0, freespinTimes[0][index], freespinTimes[1][index]});
        }
        // TODO 3 or more free spin.

        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setPickCharacters(pickCharacters);
        result.setDisplayCharacters4Reveal(displayCharacters4Reveal);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        result.setPickPays(savePickAwards);
        return result;
    }

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameLogicBean, int payback, InputInfo input) {
        return computeBonusStart(gameLogicBean, payback); // TODO support
    }

    public SlotBonusResult computeBonusPick(SlotGameLogicBean gameLogicBean, PlayerInputInfo playerInfo, SlotBonusResult bonus) {
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_PICK;
        int[] reqPickIndex = null;
        if (playerInfo != null) {
            reqPickIndex = playerInfo.getBonusPickInfos();
        }
        SlotChoice2FsOrPickBonusResult result = null;
        int[] freeSpinOrBonusPick = null;
        long[] pickCharacters = null;
        int[] pickIndexs = null;
        long[] pickAwards = null;
        long[] displayCharacters4Reveal = null;
        int freespinType = -1;
        long totalPay = 0;
        long payForPick = 0;
        if (bonus != null) {
            SlotChoice2FsOrPickBonusResult choiceFsOrPickBonusResult = (SlotChoice2FsOrPickBonusResult) bonus;
            freeSpinOrBonusPick = choiceFsOrPickBonusResult.getFsOrBonusPick();
            pickAwards = choiceFsOrPickBonusResult.getPickPays();
            pickIndexs = choiceFsOrPickBonusResult.getPickIndexInfos();
            pickCharacters = choiceFsOrPickBonusResult.getPickCharacters();
            freespinType = choiceFsOrPickBonusResult.getFsType();
            displayCharacters4Reveal = choiceFsOrPickBonusResult.getDisplayCharacters4Reveal();

            if (reqPickIndex != null && reqPickIndex.length == 1) {
                int pickIndex = reqPickIndex[0];
                int pickResult = freeSpinOrBonusPick[pickIndex];
                if (pickIndex > 0) {
                    if (pickIndex == 1) {
                        freespinType = SlotChoice2FsOrPickBonusResult.FREE_SPIN_TYPE_TWO_STACKS_WILDS;
                    } else if (pickIndex == 2) {
                        freespinType = SlotChoice2FsOrPickBonusResult.FREE_SPIN_TYPE_FOUR_ROAMING_WILDS;
                    }
                    bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                    List<String> nextScenes = gameLogicBean.getHitSceneLeftList();
                    if (nextScenes == null) {
                        nextScenes = new ArrayList<>();
                        gameLogicBean.setHitSceneLeftList(nextScenes);
                    }
                    nextScenes.add("freeSpin");
                    int freeSpinMultiplier = 1;
                    gameLogicBean.getSlotSpinResult().setTriggerFsCounts(pickResult);
                    gameLogicBean.getSlotSpinResult().setFsMul(freeSpinMultiplier);
                }
            } else if (reqPickIndex != null && reqPickIndex.length > 1) {
                int pickCount = 0;
                if (pickIndexs != null) {
                    pickCount = pickIndexs.length;
                }
                if (reqPickIndex != null && reqPickIndex.length > (pickCharacters.length + 1)) {
                    long totalBet = gameLogicBean.getSumBetCredit();
                    bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                    for (long pay : pickAwards) {
                        totalPay += pay * totalBet;
                    }
                    payForPick = totalPay;
                } else if (reqPickIndex != null && reqPickIndex.length > pickCount) {
                    int count = reqPickIndex.length - 1;
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
                    //TODO pick Multipler end bonus,character > 1000
                    if ((multiplierIconIsTerminator() && character > 1000) ||
                            reqPickIndex.length > (pickCharacters.length + 1) || reqPickIndex.length >= (maxPickCount + 1)) {
                        long totalBet = gameLogicBean.getSumBetCredit();
                        bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                        for (long pay : pickAwards) {
                            totalPay += pay * totalBet;
                        }
                        payForPick = totalPay;
                    }
                }
            }
            result = new SlotChoice2FsOrPickBonusResult();
            result.setFsOrBonusPick(freeSpinOrBonusPick);
            result.setFsType(freespinType);
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
        SlotChoice2FsOrPickBonusResult bonusTemp = (SlotChoice2FsOrPickBonusResult) bonus;
        int[] freeSpinOrBonusPick = bonusTemp.getFsOrBonusPick();
        long[] pickCharacters = bonusTemp.getDisplayCharacters4Reveal();

        if (reqPickIndex.length == 1 && (reqPickIndex[0] < 0 || reqPickIndex[0] >= freeSpinOrBonusPick.length)) {
            throw new InvalidPlayerInputException();
        } else if (reqPickIndex.length > 1) {
            for (int i = 1; i < reqPickIndex.length; i++) {
                if (reqPickIndex[i] < 0 || reqPickIndex[i] >= pickCharacters.length) {
                    throw new InvalidPlayerInputException();
                }
            }
        }
    }

}
