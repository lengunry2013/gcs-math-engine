package com.gcs.game.engine.poker.model;

import com.alibaba.fastjson.JSON;
import com.gcs.game.engine.poker.utils.PokerGameConstant;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.engine.poker.vo.PokerResult;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.RandomWeightUntil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BasePokerModel {
    protected abstract long[] getPayTable(PokerGameLogicBean gameLogicBean);

    public abstract long minLines();

    public abstract long minBet();

    public abstract long maxLines();

    public abstract long maxBet();

    public abstract long totalBet(long lines, long bet);

    protected abstract int cardDecks();

    public abstract long maxTotalPay();

    protected abstract int handPokersCount();

    protected abstract boolean isGoldCard();

    protected abstract int[] fsPokers();

    protected abstract int[][] fsTimesWeight();

    protected abstract int[][] instantCashPayWeight();

    public PokerResult deals(PokerGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        PokerResult pokerResult = new PokerResult();
        int[] finalPokers = computeCardNumber(pokerResult, input);
        engineContextMap.put("switchPokers", StringUtil.arrayToHexStr(finalPokers));
        log.debug("switchPokers {}", JSON.toJSONString(finalPokers));
        int initPayType = computePayType(pokerResult);
        long initPay = getPayTable(gameLogicCache)[initPayType];
        pokerResult.setInitPay(initPay);
        pokerResult.setInitPayType(initPayType);
        pokerResult.setPokerPlayStatus(PokerGameConstant.POKER_STATUS_SWITCH_CARD);
        //math model Gold Card feature  is true
        if (isGoldCard()) {
            int goldCard = PokerGameConstant.GOLD_CARD;
            List<Integer> tempGoldPokers = new ArrayList<>();
            tempGoldPokers.addAll(pokerResult.getHandPokers());
            tempGoldPokers.add(goldCard);
            //TODO GOLD POKERS for fronted
            List<Integer> goldHandPokers = new ArrayList<>();
            int[] goldCardIndex = RandomUtil.getRandomIndex(tempGoldPokers.size());
            for (int i = 0; i < goldCardIndex.length; i++) {
                goldHandPokers.add(tempGoldPokers.get(goldCardIndex[i]));
            }
            pokerResult.setGoldHandPokers(goldHandPokers);
        }
        return pokerResult;
    }

    protected int computePayType(PokerResult pokerResult) {
        int payType = 0;
        if (pokerResult != null) {
            List<Integer> handPokers = pokerResult.getHandPokers();
            if (isRoyalFlush(handPokers)) {
                payType = PokerGameConstant.ROYAL_FLUSH_TYPE;
            } else if (isStraightFlush(handPokers)) {
                payType = PokerGameConstant.STRAIGHT_FLUSH_TYPE;
            } else if (isFourOfKind(handPokers)) {
                payType = PokerGameConstant.FOUR_OF_KIND_TYPE;
            } else if (isFullHouse(handPokers)) {
                payType = PokerGameConstant.FULL_HOUSE_TYPE;
            } else if (isFlush(handPokers)) {
                payType = PokerGameConstant.FLUSH_TYPE;
            } else if (isStraight(handPokers)) {
                payType = PokerGameConstant.STRAIGHT_TYPE;
            } else if (isThreeOfKind(handPokers)) {
                payType = PokerGameConstant.THREE_OF_KIND_TYPE;
            } else if (isTwoPair(handPokers)) {
                payType = PokerGameConstant.TWO_PAIR_TYPE;
            } else if (isAces(handPokers)) {
                payType = PokerGameConstant.ACES_TYPE;
            }
        }
        return payType;
    }

    protected boolean isJackOrBetter(List<Integer> handPokers) {
        int[] kindPokers = kindSum(handPokers);
        for (int i = 10; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (kindPokers[i] >= 2) {
                return true;
            }
        }
        boolean result = (kindPokers[0] >= 2); // ACES
        return result;
    }

    protected boolean isAces(List<Integer> handPokers) {
        int[] kindPokers = kindSum(handPokers);
        boolean result = (kindPokers[0] >= 2); // ACES
        return result;
    }

    protected boolean isTwoPair(List<Integer> handPokers) {
        int[] kindPokers = kindSum(handPokers);
        int ct = 0;
        for (int i = 0; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (kindPokers[i] == PokerGameConstant.TWO_OF_KIND) {
                ct++;
            }
        }
        boolean result = (ct == 2);
        return result;
    }

    public boolean isOnePair(List<Integer> handPokers) {
        int[] kindPokers = kindSum(handPokers);
        int ct = 0;
        for (int i = 0; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (kindPokers[i] == PokerGameConstant.TWO_OF_KIND) {
                ct++;
            }
        }
        boolean result = (ct == 1);
        return result;
    }

    protected boolean isThreeOfKind(List<Integer> handPokers) {
        int[] kindPokers = kindSum(handPokers);
        for (int i = 0; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (kindPokers[i] == PokerGameConstant.THREE_OF_KIND) {
                return true;
            }
        }
        return false;
    }

    protected boolean isFullHouse(List<Integer> handPokers) {
        int[] kindPokers = kindSum(handPokers);
        int ct = 0;
        for (int i = 0; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (kindPokers[i] == PokerGameConstant.THREE_OF_KIND) {
                ct += 10;
            }
            if (kindPokers[i] == PokerGameConstant.TWO_OF_KIND) {
                ct++;
            }
        }
        boolean result = (ct == 11);
        return result;
    }

    protected boolean isFourOfKind(List<Integer> handPokers) {
        int[] pokersKind = kindSum(handPokers);
        for (int i = 0; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (pokersKind[i] == PokerGameConstant.FOUR_OF_KIND) {
                return true;
            }
        }
        return false;
    }

    public int[] kindSum(List<Integer> handPokers) {
        int[] pokers = StringUtil.ListToIntegerArray(handPokers);
        int[] ret = new int[PokerGameConstant.FLUSH_MAX_CARD];
        for (int i = 0; i < pokers.length; ++i) {
            ret[pokers[i] % PokerGameConstant.FLUSH_MAX_CARD]++;
        }
        return ret;
    }

    protected boolean isRoyalFlush(List<Integer> pokers) {
        int[] sortPoker = sort(pokers);
        boolean result = (isStraightFlush(pokers) && (sortPoker[0] % PokerGameConstant.FLUSH_MAX_CARD == 0 && sortPoker[1] % PokerGameConstant.FLUSH_MAX_CARD == 9));
        return result;
    }

    protected boolean isStraightFlush(List<Integer> pokers) {
        boolean result = (isStraight(pokers) && isFlush(pokers));
        return result;
    }

    protected boolean isFlush(List<Integer> pokers) {
        int[] tempPokers = StringUtil.ListToIntegerArray(pokers);
        for (int i = 1; i < tempPokers.length; ++i) {
            if (((int) tempPokers[i] / PokerGameConstant.FLUSH_MAX_CARD) != ((int) tempPokers[i - 1] / PokerGameConstant.FLUSH_MAX_CARD)) {
                return false;
            }
        }
        return true;
    }

    protected static boolean isStraight(List<Integer> pokers) {
        int[] tempPokers = StringUtil.ListToIntegerArray(pokers);
        int mismatch = 0;
        for (int i = 0; i < tempPokers.length; ++i) {
            tempPokers[i] = tempPokers[i] % PokerGameConstant.FLUSH_MAX_CARD;
        }
        Arrays.sort(tempPokers);
        for (int i = 1; i < tempPokers.length; ++i) {
            if (tempPokers[i - 1] != (tempPokers[i] - 1)) {
                mismatch++;
            }
        }
        if (tempPokers[0] % PokerGameConstant.FLUSH_MAX_CARD == 0 && tempPokers[1] % PokerGameConstant.FLUSH_MAX_CARD == 9 && mismatch == 1) {
            return true;
        }
        if (tempPokers[0] % PokerGameConstant.FLUSH_MAX_CARD >= 9) {
            return false;
        }
        boolean result = (mismatch == 0);
        return result;
    }

    protected int[] sort(List<Integer> pokers) {
        int[] temp = StringUtil.ListToIntegerArray(pokers);
        Arrays.sort(temp);
        return temp;
    }

    public List<Integer> readThreeOfKindPositions(List<Integer> pokers) {
        List<Integer> positions = new ArrayList<>();
        int poker = -1;
        int[] pokersKind = kindSum(pokers);
        for (int i = 0; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (pokersKind[i] == PokerGameConstant.THREE_OF_KIND) {
                poker = i;
                break;
            }
        }
        if (poker >= 0) {
            for (int i = 0; i < pokers.size(); i++) {
                if (pokers.get(i) % PokerGameConstant.FLUSH_MAX_CARD == poker) {
                    positions.add(i + 1);
                }
            }
        }
        return positions;
    }

    public List<Integer> readTwoPairPositions(List<Integer> pokers) {
        List<Integer> positions = new ArrayList<>();
        List<Integer> pokerList = new ArrayList<>();
        int[] pokersKind = kindSum(pokers);
        for (int i = 0; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (pokersKind[i] == PokerGameConstant.TWO_OF_KIND) {
                pokerList.add(i);
            }
        }
        if (pokerList != null && !pokerList.isEmpty()) {
            for (int i = 0; i < pokers.size(); i++) {
                if (pokerList.contains(pokers.get(i) % PokerGameConstant.FLUSH_MAX_CARD)) {
                    positions.add(i + 1);
                }
            }
        }
        return positions;
    }

    public List<Integer> readJackOrBetterPositions(List<Integer> pokers) {
        List<Integer> positions = new ArrayList<>();
        List<Integer> pokerList = new ArrayList();
        int[] pokersKind = kindSum(pokers);
        for (int i = 10; i < PokerGameConstant.FLUSH_MAX_CARD; ++i) {
            if (pokersKind[i] >= 2) {
                pokerList.add(i);
                break;
            }
        }
        if (pokersKind[0] >= 2) { // ACES
            pokerList.add(0);
        }
        for (int i = 0; i < pokers.size(); i++) {
            if (pokerList.contains(pokers.get(i) % PokerGameConstant.FLUSH_MAX_CARD)) {
                positions.add(i + 1);
            }
        }
        return positions;
    }

    public List<Integer> readAcesPositions(List<Integer> pokers) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < pokers.size(); i++) {
            if (0 == pokers.get(i) % PokerGameConstant.FLUSH_MAX_CARD) {
                positions.add(i + 1);
            }
        }
        return positions;
    }

    protected int[] computeCardNumber(PokerResult pokerResult, InputInfo input) {
        int[] randomPokers = RandomUtil.getRandomIndex(PokerGameConstant.CARD_MAX_NUMBER, PokerGameConstant.HOLD_CARD_NUMBER);
        if (input != null && input.getInputDealerCards() != null) {
            randomPokers = StringUtil.ListToIntegerArray(input.getInputDealerCards());
        }
        int handPokerCount = handPokersCount();
        List<Integer> handPokers = new ArrayList<>();
        for (int i = 0; i < handPokerCount; i++) {
            handPokers.add(randomPokers[i]);
        }
        int leftPokerCount = PokerGameConstant.HOLD_CARD_NUMBER - handPokerCount;
        int[] leftPokers = new int[leftPokerCount];
        for (int i = 0; i < leftPokerCount; i++) {
            leftPokers[i] = randomPokers[i + handPokerCount];
        }
        pokerResult.setHandPokers(handPokers);
        return leftPokers;
    }

    public PokerResult spin(PokerGameLogicBean gameLogicCache, Map<String, String> engineContextMap) {
        PokerResult pokerResult = gameLogicCache.getPokerResult();
        int[] switchPokers = StringUtil.hexStrToArray(engineContextMap.get("switchPokers"));
        computePokerResult(switchPokers, pokerResult);
        log.debug("lastHandPokers {}", pokerResult.getHandPokers());
        int payType = computePayType(pokerResult);
        long winPay = getPayTable(gameLogicCache)[payType];
        //Any Prize more than $800 will be award at $800.
        long maxTotalPay = maxTotalPay();
        if (maxTotalPay > 0 && winPay >= maxTotalPay) {
            winPay = maxTotalPay;
        }
        pokerResult.setPokerPay(winPay);
        pokerResult.setPokerPayType(payType);
        pokerResult.setPokerPlayStatus(PokerGameConstant.POKER_STATUS_SWITCH_CARD_COMPLETE);
        computeTriggerBonusOrFs(gameLogicCache);
        return pokerResult;
    }

    protected void computeTriggerBonusOrFs(PokerGameLogicBean gameLogicCache) {
        PokerResult pokerResult = gameLogicCache.getPokerResult();
        List<Integer> goldHandPokers = pokerResult.getGoldHandPokers();
        if (goldHandPokers != null && goldHandPokers.size() == 6 && goldHandPokers.contains(PokerGameConstant.GOLD_CARD)) {
            int bonusType = getGoldBonusType(gameLogicCache);
            pokerResult.setGoldCardBonusType(bonusType);
            List<String> nextScenes = new ArrayList<>();
            if (bonusType == 1) {
                log.debug("Trigger Freespin");
                int[][] fsTimesWeight = fsTimesWeight();
                RandomWeightUntil randomWeightUntil = new RandomWeightUntil(fsTimesWeight[0], fsTimesWeight[1]);
                int fsTimes = randomWeightUntil.getRandomResult();
                pokerResult.setTriggerFs(true);
                pokerResult.setTriggerFsCounts(fsTimes);
                nextScenes.add("freeSpin");
                pokerResult.setNextScenes(nextScenes);
            } else if (bonusType == 2) {
                log.debug("Trigger Instant Cash");
                int[][] instantCashPayWeight = instantCashPayWeight();
                RandomWeightUntil randomWeightUntil = new RandomWeightUntil(instantCashPayWeight[0], instantCashPayWeight[1]);
                long instantCashPay = randomWeightUntil.getRandomResult() * totalBet(gameLogicCache.getLines(), gameLogicCache.getBet());
                if (maxTotalPay() > 0 && instantCashPay >= maxTotalPay()) {
                    instantCashPay = maxTotalPay();
                }
                pokerResult.setInstantCashPay(instantCashPay);
            } else {
                log.debug("Trigger bonus");
                pokerResult.setTriggerBonus(true);
                nextScenes.add("bonus");
                pokerResult.setNextScenes(nextScenes);
            }
        }
    }

    protected int getGoldBonusType(PokerGameLogicBean gameLogicCache) {
        return 1;
    }


    public PokerResult dealsInFreeSpin(PokerGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        PokerResult pokerResult = new PokerResult();
        int[] fsRandomIndex = RandomUtil.getRandomIndex(fsPokers().length, PokerGameConstant.HOLD_CARD_NUMBER);
        int[] fsPokers = fsPokers();
        List<Integer> handPokers = new ArrayList<>();
        int pokersCount = handPokersCount();
        for (int i = 0; i < pokersCount; i++) {
            handPokers.add(fsPokers[fsRandomIndex[i]]);
        }
        int[] finalPokers = new int[pokersCount];
        for (int i = 0; i < finalPokers.length; i++) {
            finalPokers[i] = fsPokers[fsRandomIndex[pokersCount + i]];
        }
        engineContextMap.put("fsSwitchPokers", StringUtil.arrayToHexStr(finalPokers));
        log.debug("Fs SwitchPokers {}", JSON.toJSONString(finalPokers));
        pokerResult.setHandPokers(handPokers);
        int fsInitPayType = computePayType(pokerResult);
        long initPay = getPayTable(gameLogicCache)[fsInitPayType];
        pokerResult.setInitPay(initPay);
        pokerResult.setInitPayType(fsInitPayType);
        pokerResult.setPokerPlayStatus(PokerGameConstant.POKER_STATUS_SWITCH_CARD);
        return pokerResult;
    }

    public PokerResult spinInFs(PokerGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        PokerResult pokerResult = gameLogicCache.getPokerFsResult().get(gameLogicCache.getPokerFsResult().size() - 1);
        if (pokerResult != null) {
            int[] switchPokers = StringUtil.hexStrToArray(engineContextMap.get("fsSwitchPokers"));
            computePokerResult(switchPokers, pokerResult);
            log.debug("Fs LastHandPokers {}", pokerResult.getHandPokers());
            int payType = computePayType(pokerResult);
            long winPay = getPayTable(gameLogicCache)[payType];
            int fsMul = getFsMultiplier(gameLogicCache);
            winPay *= fsMul;
            if (maxTotalPay() > 0 && winPay >= maxTotalPay()) {
                winPay = maxTotalPay();
            }
            pokerResult.setPokerPay(winPay);
            pokerResult.setFsMul(fsMul);
            pokerResult.setPokerPayType(payType);
            pokerResult.setPokerPlayStatus(PokerGameConstant.POKER_STATUS_SWITCH_CARD_COMPLETE);
        }
        return pokerResult;
    }

    protected int getFsMultiplier(PokerGameLogicBean gameLogicCache) {
        return 1;
    }

    private void computePokerResult(int[] switchPokers, PokerResult pokerResult) {
        List<Integer> holdPositions = pokerResult.getHoldPositions();
        int handCardCount = handPokersCount();
        List<Integer> handPokers = pokerResult.getHandPokers();
        //TODO GOLD Pokers
        if (holdPositions == null || holdPositions.isEmpty()) {
            for (int i = 0; i < switchPokers.length; i++) {
                handPokers.set(i, switchPokers[i]);
            }
        } else if (holdPositions != null || holdPositions.size() < handCardCount) {
            int[] unHoldPositions = new int[handCardCount - holdPositions.size()];
            int index = 0;
            for (int i = 1; i <= handCardCount; i++) {
                if (!holdPositions.contains(i)) {
                    unHoldPositions[index] = i;
                    index++;
                }
            }
            for (int i = 0; i < unHoldPositions.length; i++) {
                handPokers.set(unHoldPositions[i] - 1, switchPokers[i]);
            }
        }
    }

}
