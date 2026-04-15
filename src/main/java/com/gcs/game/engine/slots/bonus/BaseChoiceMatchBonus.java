package com.gcs.game.engine.slots.bonus;


import com.gcs.game.engine.slots.vo.SlotBonusResult;
import com.gcs.game.engine.slots.vo.SlotChoiceBonusResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.utils.BonusCharactersUtil;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.vo.RecoverInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseChoiceMatchBonus extends BaseBonus {

    protected abstract int getDisplayCharactersCount();

    protected abstract int getCharactersCount();

    protected abstract long[] getCharactersAwards(int payback, int hitSymbolCount);

    protected abstract int getWildCharacter();

    protected abstract int[] getAllCharacters();

    protected abstract int[] getBonusMultiplier();

    protected abstract int[][] getPickAwardWeight();

    /**
     * bonus start.
     *
     * @param gameSessionBean
     * @param payback
     * @return
     */
    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameSessionBean, int payback) {
        SlotChoiceBonusResult result = new SlotChoiceBonusResult();
        int count = getCharactersCount();
        int displayCharCount = getDisplayCharactersCount();
        int hitSymbolCount = getHitSymbolCount(gameSessionBean.getSlotSpinResult());
        long[] charactersAwards = getCharactersAwards(payback, hitSymbolCount);
        /*if (charactersAwards != null) {
            for (int i = 0; i < charactersAwards.length; i++) {
                charactersAwards[i] *= gameSessionBean.getSumBetCredit();
            }
        }*/
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_START;
        int[] pickIndexs = null;
        int[] pickCharacters = new int[displayCharCount];
        int[] charactersCount = new int[count];
        int[] charactersCountWithWild = new int[count];
        int[] hitCharacters = null;
        long[] hitCharactersPay = null;
        int[] displayCharacters4Reveal = new int[displayCharCount];
        long totalPay = 0;
        long payForPick = 0;
        String bonusWinPattern = "";

        int[] allCharacters = getAllCharacters();
        /*int[] randomIndex = RandomUtil.getRandomIndex(displayCharCount);
        for (int i = 0; i < displayCharCount; i++) {
            pickCharacters[i] = allCharacters[randomIndex[i]];
        }*/
        //int wildCharacter = getWildCharacter();
        int betLevel = (int) (gameSessionBean.getBet() - 1);
        int[] awardWeights = getPickAwardWeight()[betLevel];
        int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(awardWeights);
        int bonusMultiplier = getBonusMultiplier()[randomIndex];
        bonusWinPattern = BonusCharactersUtil.getBonusWinPattern(randomIndex, bonusMultiplier);
        pickCharacters = BonusCharactersUtil.getCharactersResult(bonusWinPattern, allCharacters);
        System.out.println("bonus start pick characters: " + Arrays.toString(pickCharacters));
        result.setCharactersRewards(charactersAwards);
        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setPickCharacters(pickCharacters);
        result.setCharactersCount(charactersCount);
        result.setCharactersCountWithWild(charactersCountWithWild);
        result.setHitCharacters(hitCharacters);
        result.setHitCharactersPay(hitCharactersPay);
        result.setDisplayCharacters4Reveal(displayCharacters4Reveal);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        result.setBonusMul(bonusMultiplier);
        result.setBonusWinPattern(bonusWinPattern);
        return result;
    }

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameSessionBean, int payback, InputInfo input, RecoverInfo recoverInfo) {
        SlotChoiceBonusResult result = new SlotChoiceBonusResult();
        int count = getCharactersCount();
        int displayCharCount = getDisplayCharactersCount();
        int hitSymbolCount = getHitSymbolCount(gameSessionBean.getSlotSpinResult());
        long[] charactersAwards = getCharactersAwards(payback, hitSymbolCount);
        /*if (charactersAwards != null) {
            for (int i = 0; i < charactersAwards.length; i++) {
                charactersAwards[i] *= gameSessionBean.getSumBetCredit();
            }
        }*/
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_START;
        int[] pickIndexs = null;
        int[] pickCharacters = new int[displayCharCount];
        int[] charactersCount = new int[count];
        int[] charactersCountWithWild = new int[count];
        int[] hitCharacters = null;
        long[] hitCharactersPay = null;
        int[] displayCharacters4Reveal = new int[displayCharCount];
        long totalPay = 0;
        long payForPick = 0;
        String bonusWinPattern = "";

        int bonusMultiplier = 1;
        int[] allCharacters = getAllCharacters();
        if (recoverInfo != null) {
            bonusWinPattern = recoverInfo.getRecoverData();
            input = new InputInfo();
            input.setPickCharacters(BonusCharactersUtil.getCharactersResult(bonusWinPattern, allCharacters));
        }
        if (input != null && input.getPickCharacters() != null && input.getPickCharacters().length == displayCharCount) {
            pickCharacters = input.getPickCharacters();
            for (int pickChar : pickCharacters) {
                if (pickChar > 1000) {
                    int mul = pickChar / 1000;
                    bonusMultiplier *= mul;
                }
            }
        } else {
           /* int[] randomIndex = RandomUtil.getRandomIndex(displayCharCount);
            for (int i = 0; i < displayCharCount; i++) {
                pickCharacters[i] = allCharacters[randomIndex[i]];
            }*/
            int wildCharacter = getWildCharacter();
            int betLevel = (int) (gameSessionBean.getBet() - 1);
            int[] awardWeights = getPickAwardWeight()[betLevel];
            int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(awardWeights);
            bonusMultiplier = getBonusMultiplier()[randomIndex];
            bonusWinPattern = BonusCharactersUtil.getBonusWinPattern(randomIndex, bonusMultiplier);
            pickCharacters = BonusCharactersUtil.getCharactersResult(bonusWinPattern, allCharacters);
        }

        result.setCharactersRewards(charactersAwards);
        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setPickCharacters(pickCharacters);
        result.setCharactersCount(charactersCount);
        result.setCharactersCountWithWild(charactersCountWithWild);
        result.setHitCharacters(hitCharacters);
        result.setHitCharactersPay(hitCharactersPay);
        result.setDisplayCharacters4Reveal(displayCharacters4Reveal);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        result.setBonusMul(bonusMultiplier);
        result.setBonusWinPattern(bonusWinPattern);
        return result;

    }

    public SlotBonusResult computeBonusPick(SlotGameLogicBean gameSessionBean, PlayerInputInfo playerInfo, SlotBonusResult bonus, RecoverInfo recoverInfo) {
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_PICK;
        int[] reqPickIndex = null;
        if (playerInfo != null) {
            reqPickIndex = playerInfo.getBonusPickInfos();
        }
        if (recoverInfo != null) {
            int recoverData = Integer.parseInt(recoverInfo.getRecoverData());
            if (reqPickIndex != null) {
                int[] tempPickIndex = new int[reqPickIndex.length + 1];
                System.arraycopy(reqPickIndex, 0, tempPickIndex, 0, reqPickIndex.length);
                tempPickIndex[reqPickIndex.length] = recoverData;
                reqPickIndex = tempPickIndex.clone();
            } else {
                reqPickIndex = new int[]{recoverData};
            }
        }
        SlotChoiceBonusResult result = null;
        long[] charactersAwards = null;
        int[] pickIndexs = null;
        int[] pickCharacters = null;

        int[] charactersCount = null;
        int[] charactersCountWithWild = null;
        int[] hitCharacters = null;
        long[] hitCharactersPay = null;
        int[] displayCharacters4Reveal = null;
        long totalPay = 0;
        long payForPick = 0;
        int bonusMultiplier = 1;
        String bonusWinPattern = "";
        int hitLevel = -1;
        if (bonus != null) {
            SlotChoiceBonusResult choiceBonusResult = (SlotChoiceBonusResult) bonus;
            charactersAwards = choiceBonusResult.getCharactersRewards();
            pickIndexs = choiceBonusResult.getPickIndexInfos();
            pickCharacters = choiceBonusResult.getPickCharacters();
            charactersCount = choiceBonusResult.getCharactersCount();
            charactersCountWithWild = choiceBonusResult.getCharactersCountWithWild();
            hitCharactersPay = choiceBonusResult.getHitCharactersPay();
            hitCharacters = choiceBonusResult.getHitCharacters();
            displayCharacters4Reveal = choiceBonusResult.getDisplayCharacters4Reveal();
            bonusMultiplier = choiceBonusResult.getBonusMul();
            bonusWinPattern = choiceBonusResult.getBonusWinPattern();
            hitLevel = choiceBonusResult.getHitLevel();
            int pickCount = 0;
            if (pickIndexs != null) {
                pickCount = pickIndexs.length;
            }
            if (reqPickIndex != null && reqPickIndex.length > pickCount) {

                int count = reqPickIndex.length;
                int pickIndex = reqPickIndex[count - 1];
                int character = pickCharacters[count - 1];
                charactersCount[character % 1000 - 1]++;
                charactersCountWithWild[character % 1000 - 1]++;

                int wildCharacter = getWildCharacter();
                if (wildCharacter > 0 && character % 1000 == wildCharacter) {
                    for (int i = 0; i < charactersCountWithWild.length; i++) {
                        if ((i + 1) != wildCharacter) {
                            charactersCountWithWild[i]++;
                        }
                    }
                }

                // for bonus reveal
                displayCharacters4Reveal[pickIndex] = character;

                int wildCharacterIndex = wildCharacter - 1;
                int charCount = getCharactersCount();
                if (wildCharacter > 0) {
                    // there is wild char
                    for (int i = 0; i < (charCount - 1); i++) {
                        // bonus end while there are 3 matching items
                        if (charactersCount[i] + charactersCount[wildCharacterIndex] >= 3) {
                            bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                            if (charactersCount[wildCharacterIndex] >= 3) {
                                payForPick = charactersAwards[wildCharacterIndex];
                                totalPay = payForPick;
                                hitCharacters = new int[]{wildCharacter};
                                hitCharactersPay = new long[]{payForPick};
                            } else {
                                List<Integer> tempHitChars = new ArrayList<>();
                                List<Long> tempHitCharsPay = new ArrayList<>();
                                for (int m = 0; m < (charCount - 1); m++) {
                                    // compute total win
                                    if (charactersCount[m] + charactersCount[wildCharacterIndex] >= 3) {
                                        long tempPay = charactersAwards[m];
                                        payForPick += tempPay;
                                        totalPay = payForPick;
                                        tempHitChars.add(m + 1);
                                        tempHitCharsPay.add(tempPay);
                                    }
                                }
                                hitCharacters = StringUtil.list2Array(tempHitChars);
                                hitCharactersPay = StringUtil.list2LongArray(tempHitCharsPay);
                            }
                            if (bonusMultiplier > 1) {
                                int multi = 1;
                                for (i = 0; i < count; i++) {
                                    int temp = pickCharacters[i] / 1000;
                                    if (temp > 0) {
                                        multi *= temp;
                                    }
                                }
                                bonusMultiplier = multi;
                                if (multi > 1) {
                                    // trigger wild bonus multiplier
                                    payForPick *= bonusMultiplier;
                                    totalPay = payForPick;
                                }

                            }
                            break;
                        }
                    }
                } else {
                    // no wild
                    for (int i = 0; i < charCount; i++) {
                        if (charactersCount[i] >= 3) {
                            bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                            //payForPick = charactersAwards[i];
                            //totalPay = payForPick;
                            hitCharacters = new int[]{i + 1};
                            hitLevel = i + 1;
                            hitCharactersPay = new long[]{payForPick};
                            break;
                        }
                    }
                }
                if (totalPay > 0) {
                    // bonus end for bonus reveal
                    int tempIndex = count;
                    if (tempIndex < displayCharacters4Reveal.length) {
                        for (int i = 0; i < displayCharacters4Reveal.length; i++) {
                            if (displayCharacters4Reveal[i] == 0) {
                                displayCharacters4Reveal[i] = pickCharacters[tempIndex];
                                tempIndex++;
                                if (tempIndex >= pickCharacters.length) {
                                    break;
                                }
                            }
                        }
                    }
                }
                result = new SlotChoiceBonusResult();
                result.setCharactersRewards(charactersAwards);
                result.setBonusPlayStatus(bonusStatus);
                result.setPickIndexInfos(reqPickIndex);
                result.setPickCharacters(pickCharacters);
                result.setCharactersCount(charactersCount);
                result.setCharactersCountWithWild(charactersCountWithWild);
                result.setHitCharacters(hitCharacters);
                result.setHitCharactersPay(hitCharactersPay);
                result.setDisplayCharacters4Reveal(displayCharacters4Reveal);
                result.setTotalPay(totalPay);
                result.setPayForPickIndex(payForPick);
                result.setBonusMul(bonusMultiplier);
                result.setBonusWinPattern(bonusWinPattern);
                result.setHitLevel(hitLevel);
            }
        }
        return result;
    }

    public void checkInput4BonusPick(SlotGameLogicBean gameSessionBean, PlayerInputInfo playerInfo, SlotBonusResult bonus, RecoverInfo recoverInfo) throws InvalidPlayerInputException {
        if (recoverInfo == null) {
            int[] reqPickIndex = null;
            if (playerInfo != null) {
                reqPickIndex = playerInfo.getBonusPickInfos();
            }
            if (reqPickIndex == null || reqPickIndex.length == 0) {
                throw new InvalidPlayerInputException();
            }
            int count = getDisplayCharactersCount();
            for (int i = 0; i < reqPickIndex.length; i++) {
                if (reqPickIndex[i] < 0 || reqPickIndex[i] >= count) {
                    throw new InvalidPlayerInputException();
                }
            }
        }
    }

}
