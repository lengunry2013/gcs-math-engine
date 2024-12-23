package com.gcs.game.engine.blackJack.model;

import com.gcs.game.engine.blackJack.utils.BlackJackGameConstant;
import com.gcs.game.engine.blackJack.vo.BlackJackBetInfo;
import com.gcs.game.engine.blackJack.vo.BlackJackGameLogicBean;
import com.gcs.game.engine.blackJack.vo.BlackJackResult;
import com.gcs.game.engine.blackJack.vo.DealerResult;
import com.gcs.game.engine.math.modelGCBJ00102.ModelGCBJ00102;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public abstract class BaseBlackJackModel {
    public abstract long minBet();

    public abstract long maxBet();

    public abstract int minHandsCount();

    public abstract int maxHandsCount();

    public abstract int cardDecks();

    protected abstract long[] getPayTable();

    protected abstract double getBlackJackPay();

    protected abstract int getPay();

    protected abstract int getInsurancePay();

    protected abstract long computeJackpotPay(BlackJackGameLogicBean gameLogicBean, BlackJackResult blackJackResult, BlackJackBetInfo betInfo);


    public void deal(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap) {
        int[] allCardsNumber = computeCardsNumber();
        engineContextMap.put("allCardsNumber", StringUtil.arrayToHexStr(allCardsNumber));
        //first dealer cards
        int[] dealerCards = new int[2];
        for (int i = 0; i < dealerCards.length; i++) {
            dealerCards[i] = allCardsNumber[i];
        }
        DealerResult dealerResult = computeDealDealerResult(gameLogicCache, dealerCards[0]);
        gameLogicCache.setDealerResult(dealerResult);
        //cache dealer second card
        log.debug("dealerSecondCard: {}", dealerCards[1]);
        engineContextMap.put("dealerSecondCard", StringUtil.arrayToHexStr(new int[]{dealerCards[1]}));
        List<BlackJackResult> blackJackResultlist = new ArrayList<>();
        gameLogicCache.setBlackJackResults(blackJackResultlist);
        List<BlackJackBetInfo> blackJackBetInfoList = gameLogicCache.getBlackJackBetInfos();
        for (int i = 0; i < maxHandsCount(); i++) {
            BlackJackBetInfo betInfo = blackJackBetInfoList.get(i);
            BlackJackResult blackJackResult;
            int cardCurrentLen = getCardCurrentLen(gameLogicCache, engineContextMap);
            if (betInfo.getBet() > 0) {
                int[] handCards = new int[2];
                for (int j = 0; j < handCards.length; j++) {
                    handCards[j] = allCardsNumber[cardCurrentLen + j];
                }
                blackJackResult = computeDealHandResult(gameLogicCache, dealerResult, handCards);
                //jackpot bet>0
                if (betInfo.getJackpotBet() > 0) {
                    long jackpotPay = computeJackpotPay(gameLogicCache, blackJackResult,betInfo);
                    blackJackResult.setJackpotPay(jackpotPay);
                }
            } else {
                blackJackResult = new BlackJackResult();
                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_SITTINGOUT);
            }
            blackJackResult.setHandIndex(i + 1);
            blackJackResultlist.add(blackJackResult);
        }
        gameLogicCache.setCurrentHandIndex(1);
        //all hand stand Dealer status is deal or complete
        if (dealerResult.getDealerStatus() == BlackJackGameConstant.DEALER_WAIT_FOR_HANDS) {
            computeDealerDealStatus(gameLogicCache, engineContextMap);
        }
    }

    public void deal(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        int[] allCardsNumber = computeCardsNumber();
        engineContextMap.put("allCardsNumber", StringUtil.arrayToHexStr(allCardsNumber));
        //first dealer cards
        int[] dealerCards = new int[2];
        for (int i = 0; i < dealerCards.length; i++) {
            dealerCards[i] = allCardsNumber[i];
        }
        if (input.getInputDealerCards() != null && input.getInputDealerCards().size() > 1) {
            for (int i = 0; i < dealerCards.length; i++) {
                dealerCards[i] = input.getInputDealerCards().get(i);
            }
        }
        //cache dealer second card
        log.debug("dealerSecondCard: {}", dealerCards[1]);
        engineContextMap.put("dealerSecondCard", StringUtil.arrayToHexStr(new int[]{dealerCards[1]}));
        DealerResult dealerResult = computeDealDealerResult(gameLogicCache, dealerCards[0]);
        gameLogicCache.setDealerResult(dealerResult);
        List<BlackJackResult> blackJackResultlist = new ArrayList<>();
        gameLogicCache.setBlackJackResults(blackJackResultlist);
        List<BlackJackBetInfo> blackJackBetInfoList = gameLogicCache.getBlackJackBetInfos();
        for (int i = 0; i < maxHandsCount(); i++) {
            BlackJackBetInfo betInfo = blackJackBetInfoList.get(i);
            BlackJackResult blackJackResult;
            int cardCurrentLen = getCardCurrentLen(gameLogicCache, engineContextMap);
            if (betInfo.getBet() > 0) {
                int[] handCards = new int[2];
                for (int j = 0; j < handCards.length; j++) {
                    handCards[j] = allCardsNumber[cardCurrentLen + j];
                    //input test
                    if (input != null && input.getInputHandsCards() != null && input.getInputHandsCards().size() > i) {
                        List<Integer> inputHandsCards = input.getInputHandsCards().get(i);
                        if (inputHandsCards != null && inputHandsCards.size() > j) {
                            handCards[j] = inputHandsCards.get(j);
                        }
                    }
                }
                blackJackResult = computeDealHandResult(gameLogicCache, dealerResult, handCards);
                //jackpot bet>0
                if (betInfo.getJackpotBet() > 0) {
                    long jackpotPay = computeJackpotPay(gameLogicCache, blackJackResult, betInfo);
                    blackJackResult.setJackpotPay(jackpotPay);
                }
            } else {
                blackJackResult = new BlackJackResult();
                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_SITTINGOUT);
            }
            blackJackResult.setHandIndex(i + 1);
            blackJackResultlist.add(blackJackResult);
        }
        gameLogicCache.setCurrentHandIndex(1);
        //all hand stand Dealer status is deal or complete
        if (dealerResult.getDealerStatus() == BlackJackGameConstant.DEALER_WAIT_FOR_HANDS) {
            computeDealerDealStatus(gameLogicCache, engineContextMap);
        }
    }

    /**
     * 3hand BlackJack complete sile bet index
     */
    protected int computeSideBetIndex(BlackJackResult blackJackResult) {
        int sideBetIndex = 0;
        List<Integer> cardsNumber = blackJackResult.getCardsNumber();
        int cardACount = 0;
        if (cardsNumber != null) {
            for (int card : cardsNumber) {
                int point = computeCardPoint(card);
                if (point == BlackJackGameConstant.CARD_A_POINT) {
                    cardACount++;
                } else {
                    break;
                }
            }
            sideBetIndex = cardACount;
            if (cardACount > 1) {
                int firstCard = cardsNumber.get(0);
                boolean isSuit = true;
                for (int i = 1; i < cardsNumber.size(); i++) {
                    if (firstCard != cardsNumber.get(i)) {
                        isSuit = false;
                    }
                }
                if (cardACount == 2) {
                    sideBetIndex = isSuit ? BlackJackGameConstant.TWO_SUITED_ACES : BlackJackGameConstant.TWO_UNSUITED_ACES;
                } else if (cardACount == 3) {
                    sideBetIndex = isSuit ? BlackJackGameConstant.THREE_SUITED_ACES : BlackJackGameConstant.THREE_UNSUITED_ACES;
                } else if (cardACount == 4) {
                    sideBetIndex = isSuit ? BlackJackGameConstant.FOUR_SUITED_ACES : BlackJackGameConstant.FOUR_UNSUITED_ACES;
                } else if (cardACount > 4) {
                    for (int i = 1; i < 4; i++) {
                        if (firstCard != cardsNumber.get(i)) {
                            isSuit = false;
                        }
                    }
                    sideBetIndex = isSuit ? BlackJackGameConstant.FOUR_SUITED_ACES : BlackJackGameConstant.FOUR_UNSUITED_ACES;
                }
            }
        }
        return sideBetIndex;
    }

    protected int getCardCurrentLen(BlackJackGameLogicBean gameLogicBean, Map<String, String> engineContextMap) {
        //dealer card
        int len = gameLogicBean.getDealerResult().getCardsNumber().size();
        List<BlackJackResult> blackJackResultlist = gameLogicBean.getBlackJackResults();
        if (len < 2 && engineContextMap != null && engineContextMap.containsKey("dealerSecondCard")) {
            int[] dealerSecondCard = StringUtil.hexStrToArray(engineContextMap.get("dealerSecondCard"));
            if (dealerSecondCard != null && dealerSecondCard.length > 0) {
                len += dealerSecondCard.length;
            }
        }
        if (blackJackResultlist != null) {
            //normal hands
            len += blackJackResultlist.stream().filter(blackJackResult -> blackJackResult != null && blackJackResult.getCardsNumber() != null).mapToInt(blackJackResult -> blackJackResult.getCardsNumber().size()).sum();
            //split hands
            len += blackJackResultlist.stream().filter(blackJackResult -> blackJackResult != null && blackJackResult.getSplitCardsNumber() != null).mapToInt(blackJackResult -> blackJackResult.getSplitCardsNumber().size()).sum();
        }
        return len;
    }

    protected BlackJackResult computeDealHandResult(BlackJackGameLogicBean gameLogicCache, DealerResult dealerResult, int[] handCards) {
        BlackJackResult blackJackResult = new BlackJackResult();
        if (handCards != null) {
            boolean isPair = isPair(handCards);
            List<Integer> cardsNumber = StringUtil.IntegerArrayToList(handCards);
            List<Integer> cardsPoint = computeDealCardPoint(cardsNumber);
            boolean isBlackJack = cardsPoint.get(0) == BlackJackGameConstant.BLACK_JACK_POINT;
            int handStatus = BlackJackGameConstant.HAND_STATUS_DOUBLE;
            if (dealerResult.getDealerStatus() == BlackJackGameConstant.DEALER_WAIT_FOR_INSURANCE) {
                handStatus = BlackJackGameConstant.HAND_STATUS_INSURANCE;
            } else if (dealerResult.getDealerStatus() == BlackJackGameConstant.DEALER_PEEK_BJ) {
                handStatus = BlackJackGameConstant.HAND_STATUS_WAIT_DEALER_PEEK;
            } else if (isBlackJack) {
                handStatus = BlackJackGameConstant.HAND_STATUS_BJ_WIN;
            } else if (isPair) {
                handStatus = BlackJackGameConstant.HAND_STATUS_SPLIT;
            }
            blackJackResult.setCardsNumber(cardsNumber);
            blackJackResult.setCardsPoint(cardsPoint);
            blackJackResult.setHandStatus(handStatus);
        }

        return blackJackResult;
    }

    protected boolean isPair(int[] handCards) {
        boolean isPair = false;
        if (handCards != null && handCards.length > 1) {
            int firstCard = (handCards[0] - 1) % 13 + 1;
            int secondCard = (handCards[1] - 1) % 13 + 1;
            if (firstCard == secondCard) {
                isPair = true;
            }
        }
        return isPair;
    }


    protected DealerResult computeDealDealerResult(BlackJackGameLogicBean gameLogicCache, int dealerCard) {
        int point = computeCardPoint(dealerCard);
        List<Integer> cardPoint = new ArrayList<>();
        cardPoint.add(point);
        int dealerStatus = computeDealerStatus(gameLogicCache, point);
        DealerResult dealerResult = new DealerResult();
        List<Integer> cardsNumber = new ArrayList<>();
        cardsNumber.add(dealerCard);
        dealerResult.setCardsNumber(cardsNumber);
        if (point == 1) {
            cardPoint.add(11);
        }
        dealerResult.setCardsPoint(cardPoint);
        dealerResult.setDealerStatus(dealerStatus);
        return dealerResult;
    }

    protected int computeDealerStatus(BlackJackGameLogicBean gameLogicCache, int point) {
        int dealerStatus = BlackJackGameConstant.DEALER_STATUS_IDLE;
        int gamePlayStatus = gameLogicCache.getGamePlayStatus();
        if (gamePlayStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_DEAL) {
            if (point == BlackJackGameConstant.CARD_A_POINT) {
                dealerStatus = BlackJackGameConstant.DEALER_WAIT_FOR_INSURANCE;
            } else if (point == BlackJackGameConstant.CARD_PEEK_POINT) {
                dealerStatus = BlackJackGameConstant.DEALER_PEEK_BJ;
            } else {
                dealerStatus = BlackJackGameConstant.DEALER_WAIT_FOR_HANDS;
            }
        }

        return dealerStatus;
    }

    protected int computeCardPoint(int card) {
        int point = 0;
        int cardIndex = (card - 1) % 13 + 1;
        point = Math.min(cardIndex, 10);
        return point;
    }

    protected int[] computeCardsNumber() {
        int cardLen = BlackJackGameConstant.CARD_MAX_NUMBER * cardDecks();
        int[] cardsNumberResult = new int[cardLen];
        int[] cardsNumber = new int[cardLen];
        for (int i = 0; i < cardDecks(); i++) {
            for (int j = 0; j < BlackJackGameConstant.CARD_MAX_NUMBER; j++) {
                cardsNumber[i * BlackJackGameConstant.CARD_MAX_NUMBER + j] = j + 1;
            }
        }
        int[] cardIndexArray = RandomUtil.getRandomIndex(cardLen);
        for (int i = 0; i < cardIndexArray.length; i++) {
            cardsNumberResult[i] = cardsNumber[cardIndexArray[i]];
        }
        return cardsNumberResult;
    }

    public long computeTotalBet(BlackJackGameLogicBean gameLogicCache) {
        List<BlackJackBetInfo> betInfos = gameLogicCache.getBlackJackBetInfos();
        AtomicLong totalBet = new AtomicLong();
        if (betInfos != null) {
            betInfos.forEach(betInfo -> {
                totalBet.addAndGet(betInfo.getBet());
                totalBet.addAndGet(betInfo.getJackpotBet());
                totalBet.addAndGet(betInfo.getSplitBet());
                totalBet.addAndGet(betInfo.getInsuranceBet());
            });

        }
        return totalBet.longValue();
    }

    public long computeTotalWin(BlackJackGameLogicBean gameLogicCache) {
        AtomicLong totalWin = new AtomicLong();
        List<BlackJackResult> blackJackList = gameLogicCache.getBlackJackResults();
        if (blackJackList != null) {
            blackJackList.forEach(blackResult -> {
                totalWin.addAndGet(blackResult.getBetPay());
                totalWin.addAndGet(blackResult.getJackpotPay());
                totalWin.addAndGet(blackResult.getInsurancePay());
                totalWin.addAndGet(blackResult.getSplitPay());
            });
        }
        return totalWin.longValue();
    }

    public void HandsInsurance(BlackJackGameLogicBean gameLogicCache) {
        int handIndex = gameLogicCache.getCurrentHandIndex();
        int gamePlayStatus = gameLogicCache.getGamePlayStatus();
        BlackJackResult blackJackResult = gameLogicCache.getBlackJackResults().get(handIndex - 1);
        BlackJackBetInfo betInfo = gameLogicCache.getBlackJackBetInfos().get(handIndex - 1);
        if (blackJackResult != null && betInfo.getBet() > 0) {
            if (gamePlayStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_INSURANCE && blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_INSURANCE) {
                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_HAS_INSURANCE);
                blackJackResult.setHasInsurance(true);
                betInfo.setInsuranceBet(betInfo.getBet() / 2);
            } else if (gamePlayStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_NO_INSURANCE && blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_INSURANCE) {
                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_NO_INSURANCE);
                betInfo.setInsuranceBet(0);
                blackJackResult.setHasInsurance(false);
            }
            //all hand made insurance decision,dealer peek blackjack
            int betMaxHand = getBetMaxHand(gameLogicCache);
            if (handIndex == betMaxHand) {
                gameLogicCache.getDealerResult().setDealerStatus(BlackJackGameConstant.DEALER_PEEK_BJ);
            }
        }
    }

    protected int getBetMaxHand(BlackJackGameLogicBean gameLogicCache) {
        List<BlackJackBetInfo> betInfos = gameLogicCache.getBlackJackBetInfos();
        int betMaxHand = 1;
        if (betInfos != null && !betInfos.isEmpty()) {
            for (int i = 0; i < betInfos.size(); i++) {
                if (betInfos.get(i).getBet() > 0) {
                    betMaxHand = i + 1;
                }
            }
        }
        return betMaxHand;
    }

    public void DealerPeekBlackJack(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap) {
        List<BlackJackResult> blackResultList = gameLogicCache.getBlackJackResults();
        List<BlackJackBetInfo> betInfoList = gameLogicCache.getBlackJackBetInfos();
        if (blackResultList != null) {
            boolean isBlackJack = isDealerBlackJack(gameLogicCache, engineContextMap);
            for (int i = 0; i < blackResultList.size(); i++) {
                BlackJackBetInfo betInfo = betInfoList.get(i);
                //handStatus not sittingOut
                if (betInfo.getBet() > 0) {
                    BlackJackResult blackJackResult = blackResultList.get(i);
                    int handCardPoint = blackJackResult.getCardsPoint().get(0);
                    if (isBlackJack) {
                        if (blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_HAS_INSURANCE) {
                            blackJackResult.setInsurancePay(getInsurancePay() * betInfo.getInsuranceBet());
                            if (handCardPoint == BlackJackGameConstant.BLACK_JACK_POINT) {
                                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_INSURANCE_PUSH);
                                blackJackResult.setBetPay(betInfo.getBet());
                            } else {
                                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_INSURANCE_LOSE);
                                blackJackResult.setBetPay(0);
                            }
                        } else if (blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_NO_INSURANCE || blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_WAIT_DEALER_PEEK) {
                            blackJackResult.setInsurancePay(0);
                            if (handCardPoint == BlackJackGameConstant.BLACK_JACK_POINT) {
                                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_BJ_PUSH);
                                blackJackResult.setBetPay(betInfo.getBet());
                            } else {
                                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_BJ_LOSE);
                                blackJackResult.setBetPay(0);
                            }
                        }
                    } else {
                        boolean isPair = isPair(StringUtil.ListToIntegerArray(blackJackResult.getCardsNumber()));
                        if (handCardPoint == BlackJackGameConstant.BLACK_JACK_POINT) {
                            blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_BJ_WIN);
                            blackJackResult.setBetPay(StringUtil.doubleMul(getBlackJackPay(), betInfo.getBet()));
                        } else if (isPair) {
                            blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_SPLIT);
                        } else {
                            blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_DOUBLE);
                        }
                    }
                }
            }
            //Dealer is not BlackJack
            if (!isBlackJack) {
                //all hand BlackJack Dealer status is deal or complete
                computeDealerDealStatus(gameLogicCache, engineContextMap);
            }
        }

    }

    protected boolean isDealerBlackJack(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap) {
        int peekCardNumber = getDealerSecondCard(gameLogicCache, engineContextMap);
        DealerResult dealerResult = gameLogicCache.getDealerResult();
        int lastCardPoint = dealerResult.getCardsPoint().get(0);
        int point = computeCardPoint(peekCardNumber);
        //last card is A
        if (lastCardPoint == BlackJackGameConstant.CARD_A_POINT && point == BlackJackGameConstant.CARD_PEEK_POINT) {
            point = BlackJackGameConstant.BLACK_JACK_POINT;
        } else if (lastCardPoint == BlackJackGameConstant.CARD_PEEK_POINT && point == BlackJackGameConstant.CARD_A_POINT) {
            point = BlackJackGameConstant.BLACK_JACK_POINT;
        }
        if (point == BlackJackGameConstant.BLACK_JACK_POINT) {
            dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_BLACKJACK);
            dealerResult.getCardsNumber().add(peekCardNumber);
            dealerResult.getCardsPoint().clear();
            dealerResult.getCardsPoint().add(point);
            return true;
        } else {
            dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_WAIT_FOR_HANDS);
            return false;
        }
    }

    protected int getDealerSecondCard(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap) {
        int dealerSecondCard;
        DealerResult dealerResult = gameLogicCache.getDealerResult();
        int dealerSize = dealerResult.getCardsNumber().size();
        if (dealerSize < 2 && engineContextMap != null && engineContextMap.containsKey("dealerSecondCard")) {
            int[] secondCard = StringUtil.hexStrToArray(engineContextMap.get("dealerSecondCard"));
            dealerSecondCard = secondCard[0];
        } else {
            int[] allCardsNumber = StringUtil.hexStrToArray(engineContextMap.get("allCardsNumber"));
            int currentLen = getCardCurrentLen(gameLogicCache, engineContextMap);
            dealerSecondCard = allCardsNumber[currentLen];
        }
        return dealerSecondCard;
    }

    public void HandSplit(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        int[] allCardsNumber = StringUtil.hexStrToArray(engineContextMap.get("allCardsNumber"));
        int currentLen = getCardCurrentLen(gameLogicCache, engineContextMap);
        int handIndex = gameLogicCache.getCurrentHandIndex();
        BlackJackResult blackJackResult = gameLogicCache.getBlackJackResults().get(handIndex - 1);
        BlackJackBetInfo betInfo = gameLogicCache.getBlackJackBetInfos().get(handIndex - 1);
        if (blackJackResult != null && betInfo.getBet() > 0) {
            betInfo.setSplitBet(betInfo.getBet());
            List<Integer> cardList = blackJackResult.getCardsNumber();
            blackJackResult.getCardsNumber().set(0, cardList.get(0));
            List<Integer> splitCardsList = new ArrayList<>();
            splitCardsList.add(cardList.get(1));
            //test input
            blackJackResult.getCardsNumber().set(1, allCardsNumber[currentLen]);
            splitCardsList.add(allCardsNumber[currentLen + 1]);
            int len = blackJackResult.getCardsNumber().size();
            if (input != null && input.getInputHandsCards() != null && input.getInputHandsCards().size() >= handIndex) {
                List<Integer> inputHandCards = input.getInputHandsCards().get(handIndex - 1);
                if (inputHandCards != null && inputHandCards.size() > len) {
                    blackJackResult.getCardsNumber().set(1, inputHandCards.get(len));
                }
                if (inputHandCards != null && inputHandCards.size() > len + 1) {
                    splitCardsList.set(1, inputHandCards.get(len + 1));
                }
            }
            //Blackjack cannot occur after split card
            List<Integer> cardPointList = computeDealCardPoint(blackJackResult.getCardsNumber());
            blackJackResult.setCardsPoint(cardPointList);
            List<Integer> cardSplitPointList = computeDealCardPoint(splitCardsList);
            blackJackResult.setSplitCardsNumber(splitCardsList);
            blackJackResult.setSplitCardsPoint(cardSplitPointList);
            blackJackResult.setHasSplit(true);
            blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_DOUBLE);
            blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_DOUBLE);
            if (cardPointList.get(0) == BlackJackGameConstant.BLACK_JACK_POINT) {
                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_STAND);
            }
            if (cardSplitPointList.get(0) == BlackJackGameConstant.BLACK_JACK_POINT) {
                blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_STAND);
            }
            //Recalculate jackpotPay after split
            if (this instanceof ModelGCBJ00102 && betInfo.getJackpotBet() > 0) {
                long jackpotPay = computeJackpotPay(gameLogicCache, blackJackResult, betInfo);
                blackJackResult.setJackpotPay(jackpotPay);
            }
            //all hand stand Dealer status is deal or complete
            computeDealerDealStatus(gameLogicCache, engineContextMap);
        }

    }

    protected void computeDealerDealStatus(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap) {
        boolean isDealerDeal = computeAllHandStatus(gameLogicCache);
        //all hand is stand or BlackJack
        if (isDealerDeal) {
            boolean isHandBust = isAllHandBust(gameLogicCache);
            DealerResult dealerResult = gameLogicCache.getDealerResult();
            int dealerSecondCard = getDealerSecondCard(gameLogicCache, engineContextMap);
            dealerResult.getCardsNumber().add(dealerSecondCard);
            computeDealerDealPoint(gameLogicCache, dealerSecondCard);
            int point = dealerResult.getCardsPoint().get(dealerResult.getCardsPoint().size() - 1);
            //player all hand is BlackJack,Dealer peek is BlackJack end game
            if (gameLogicCache.getGamePlayStatus() == GameConstant.BJ_BLACKJACK_GAME_STATUS_DEAL || gameLogicCache.getGamePlayStatus() == GameConstant.BJ_BLACKJACK_GAME_STATUS_PEEK_BLACKJACK || isHandBust) {
                dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_COMPLETE);
                computeDealerComplete(gameLogicCache);
            } else if (point >= BlackJackGameConstant.DEALER_CARD_COMPLETE_POINT) {
                dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_COMPLETE);
                computeDealerComplete(gameLogicCache);
            } else {
                dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_DEAL);
            }
        }
    }

    private boolean isAllHandBust(BlackJackGameLogicBean gameLogicCache) {
        boolean isHandBust = true;
        List<BlackJackResult> blackJackResultList = gameLogicCache.getBlackJackResults();
        if (blackJackResultList != null) {
            for (BlackJackResult blackJackResult : blackJackResultList) {
                int handStatus = blackJackResult.getHandStatus();
                int splitHandStatus = blackJackResult.getSplitHandStatus();
                if (handStatus == BlackJackGameConstant.HAND_STATUS_SITTINGOUT) {
                    continue;
                }
                if (handStatus != BlackJackGameConstant.HAND_STATUS_BUST) {
                    isHandBust = false;
                    break;
                } else if (blackJackResult.isHasSplit() && splitHandStatus != BlackJackGameConstant.HAND_STATUS_BUST) {
                    isHandBust = false;
                    break;
                }
            }
        }
        return isHandBust;
    }

    protected boolean computeAllHandStatus(BlackJackGameLogicBean gameLogicCache) {
        List<BlackJackResult> blackJackResultList = gameLogicCache.getBlackJackResults();
        boolean isDealerDeal = true;
        if (blackJackResultList != null) {
            for (BlackJackResult blackJackResult : blackJackResultList) {
                int handStatus = blackJackResult.getHandStatus();
                int splitHandStatus = blackJackResult.getSplitHandStatus();
                if (handStatus == BlackJackGameConstant.HAND_STATUS_SITTINGOUT) {
                    continue;
                }
                if (handStatus != BlackJackGameConstant.HAND_STATUS_BJ_WIN && handStatus != BlackJackGameConstant.HAND_STATUS_BUST && handStatus != BlackJackGameConstant.HAND_STATUS_STAND) {
                    isDealerDeal = false;
                    break;
                } else if (blackJackResult.isHasSplit() && splitHandStatus != BlackJackGameConstant.HAND_STATUS_BUST && splitHandStatus != BlackJackGameConstant.HAND_STATUS_STAND) {
                    isDealerDeal = false;
                    break;
                }
            }
        }
        return isDealerDeal;
    }

    protected List<Integer> computeDealCardPoint(List<Integer> cardsNumber) {
        if (cardsNumber != null) {
            List<Integer> cardPointList = new ArrayList<>();
            for (int cardNumber : cardsNumber) {
                int tempPoint = computeCardPoint(cardNumber);
                if (!cardPointList.isEmpty()) {
                    //last card is A,maybe is 1 or 11
                    if (cardPointList.size() > 1) {
                        //BlackJack
                        if (tempPoint == BlackJackGameConstant.CARD_PEEK_POINT) {
                            cardPointList.clear();
                            cardPointList.add(BlackJackGameConstant.BLACK_JACK_POINT);
                        } else {
                            for (int i = 0; i < cardPointList.size(); i++) {
                                cardPointList.set(i, cardPointList.get(i) + tempPoint);
                            }
                        }
                    } else {
                        int lastCardPoint = cardPointList.get(0);
                        //card is A,maybe is 1 or 11
                        if (tempPoint == BlackJackGameConstant.CARD_A_POINT) {
                            if (lastCardPoint == BlackJackGameConstant.CARD_PEEK_POINT) {
                                cardPointList.set(0, BlackJackGameConstant.BLACK_JACK_POINT);
                            } else {
                                tempPoint += lastCardPoint;
                                cardPointList.set(0, tempPoint);
                                cardPointList.add(lastCardPoint + BlackJackGameConstant.CARD_A_MAX_POINT);
                            }
                        } else {
                            tempPoint += lastCardPoint;
                            cardPointList.set(0, tempPoint);
                        }
                    }
                } else {
                    cardPointList.add(tempPoint);
                    if (tempPoint == BlackJackGameConstant.CARD_A_POINT) {
                        cardPointList.add(BlackJackGameConstant.CARD_A_MAX_POINT);
                    }
                }
            }
            return cardPointList;
        }
        return null;
    }

    public void HandDoubled(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        int handIndex = gameLogicCache.getCurrentHandIndex();
        BlackJackResult blackJackResult = gameLogicCache.getBlackJackResults().get(handIndex - 1);
        BlackJackBetInfo betInfo = gameLogicCache.getBlackJackBetInfos().get(handIndex - 1);
        if (blackJackResult != null && betInfo.getBet() > 0) {
            int card = getHandNextCard(gameLogicCache, engineContextMap, input);
            int point = computeCardPoint(card);
            List<Integer> cardsPointList;
            //split hand doubled
            if (gameLogicCache.getSplitIndex() == 1 && blackJackResult.isHasSplit()) {
                betInfo.setSplitBet(betInfo.getSplitBet() * 2);
                blackJackResult.getSplitCardsNumber().add(card);
                blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_DOUBLE);
                blackJackResult.setSplitHandHasDouble(true);
                cardsPointList = blackJackResult.getSplitCardsPoint();
            } else {
                betInfo.setBet(betInfo.getBet() * 2);
                blackJackResult.getCardsNumber().add(card);
                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_DOUBLE);
                blackJackResult.setHasDouble(true);
                cardsPointList = blackJackResult.getCardsPoint();
            }
            int lastPoint = cardsPointList.get(cardsPointList.size() - 1);
            if (lastPoint <= BlackJackGameConstant.CARD_PEEK_POINT) {
                if (point == BlackJackGameConstant.CARD_A_POINT) {
                    point = BlackJackGameConstant.CARD_A_MAX_POINT + lastPoint;
                } else {
                    point += lastPoint;
                }
            } else {
                int tempPoint = point;
                tempPoint += lastPoint;
                //Bust get small point
                if (tempPoint > BlackJackGameConstant.BLACK_JACK_POINT) {
                    point += cardsPointList.get(0);
                } else {
                    point = tempPoint;
                }
            }
            if (gameLogicCache.getSplitIndex() == 1 && blackJackResult.isHasSplit()) {
                blackJackResult.getSplitCardsPoint().clear();
                blackJackResult.getSplitCardsPoint().add(point);
                if (point > BlackJackGameConstant.BLACK_JACK_POINT) {
                    blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_BUST);
                } else {
                    blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_STAND);
                }
            } else {
                if (point > BlackJackGameConstant.BLACK_JACK_POINT) {
                    blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_BUST);
                } else {
                    blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_STAND);
                }
                blackJackResult.getCardsPoint().clear();
                blackJackResult.getCardsPoint().add(point);

                //Recalculate jackpotPay after double
                if (this instanceof ModelGCBJ00102 && betInfo.getJackpotBet() > 0) {
                    long jackpotPay = computeJackpotPay(gameLogicCache, blackJackResult, betInfo);
                    blackJackResult.setJackpotPay(jackpotPay);
                }
            }
            //all hand stand Dealer status is deal or complete
            computeDealerDealStatus(gameLogicCache, engineContextMap);
        }

    }

    public void HandHit(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        int handIndex = gameLogicCache.getCurrentHandIndex();
        BlackJackResult blackJackResult = gameLogicCache.getBlackJackResults().get(handIndex - 1);
        BlackJackBetInfo betInfo = gameLogicCache.getBlackJackBetInfos().get(handIndex - 1);
        if (blackJackResult != null && betInfo.getBet() > 0) {
            int card = getHandNextCard(gameLogicCache, engineContextMap, input);
            int point = computeCardPoint(card);
            List<Integer> cardsPointList;
            //split hand Hit
            if (gameLogicCache.getSplitIndex() == 1 && blackJackResult.isHasSplit()) {
                blackJackResult.getSplitCardsNumber().add(card);
                cardsPointList = blackJackResult.getSplitCardsPoint();
            } else {
                blackJackResult.getCardsNumber().add(card);
                cardsPointList = blackJackResult.getCardsPoint();
            }
            int lastPoint = cardsPointList.get(cardsPointList.size() - 1);
            if (cardsPointList.size() > 1) {
                int tempPoint = point;
                tempPoint += lastPoint;
                if (tempPoint > BlackJackGameConstant.BLACK_JACK_POINT) {
                    point += cardsPointList.get(0);
                    cardsPointList.clear();
                    cardsPointList.add(point);
                } else if (tempPoint == BlackJackGameConstant.BLACK_JACK_POINT) {
                    point = BlackJackGameConstant.BLACK_JACK_POINT;
                    cardsPointList.clear();
                    cardsPointList.add(point);
                } else {
                    for (int i = 0; i < cardsPointList.size(); i++) {
                        cardsPointList.set(i, point + cardsPointList.get(i));
                    }
                }
            } else {
                if (lastPoint <= BlackJackGameConstant.CARD_PEEK_POINT) {
                    if (point == BlackJackGameConstant.CARD_A_POINT) {
                        point = BlackJackGameConstant.CARD_A_MAX_POINT + lastPoint;
                        if (point == BlackJackGameConstant.BLACK_JACK_POINT) {
                            cardsPointList.set(0, point);
                        } else {
                            cardsPointList.clear();
                            cardsPointList.add(BlackJackGameConstant.CARD_A_POINT + lastPoint);
                            cardsPointList.add(point);
                        }
                    } else {
                        point += lastPoint;
                        cardsPointList.set(0, point);
                    }
                } else {
                    point += lastPoint;
                    cardsPointList.set(0, point);
                }
            }
            lastPoint = cardsPointList.get(cardsPointList.size() - 1);
            int HandStatus = BlackJackGameConstant.HAND_STATUS_HIT;
            if (lastPoint > BlackJackGameConstant.BLACK_JACK_POINT) {
                HandStatus = BlackJackGameConstant.HAND_STATUS_BUST;
            } else if (lastPoint == BlackJackGameConstant.BLACK_JACK_POINT) {
                HandStatus = BlackJackGameConstant.HAND_STATUS_STAND;
            }
            if (gameLogicCache.getSplitIndex() == 1 && blackJackResult.isHasSplit()) {
                blackJackResult.setSplitCardsPoint(cardsPointList);
                blackJackResult.setSplitHandStatus(HandStatus);
            } else {
                blackJackResult.setCardsPoint(cardsPointList);
                blackJackResult.setHandStatus(HandStatus);
                //Recalculate jackpotPay after double
                if (this instanceof ModelGCBJ00102 && betInfo.getJackpotBet() > 0) {
                    long jackpotPay = computeJackpotPay(gameLogicCache, blackJackResult, betInfo);
                    blackJackResult.setJackpotPay(jackpotPay);
                }
            }
            //all hand stand Dealer status is deal or complete
            computeDealerDealStatus(gameLogicCache, engineContextMap);
        }

    }

    protected int getHandNextCard(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        int[] allCardsNumber = StringUtil.hexStrToArray(engineContextMap.get("allCardsNumber"));
        int currentLen = getCardCurrentLen(gameLogicCache, engineContextMap);
        int handIndex = gameLogicCache.getCurrentHandIndex();
        BlackJackResult blackJackResult = gameLogicCache.getBlackJackResults().get(handIndex - 1);
        int card = allCardsNumber[currentLen];
        //test input TODO
        if (input != null && input.getInputHandsCards().size() > 0) {
            List<Integer> inputHandsCard = input.getInputHandsCards().get(handIndex - 1);
            int len = blackJackResult.getCardsNumber().size();
            if (blackJackResult.isHasSplit()) {
                len += blackJackResult.getSplitCardsNumber().size();
            }
            if (inputHandsCard != null && inputHandsCard.size() > len) {
                card = inputHandsCard.get(len);
            }
        }
        return card;
    }

    public void HandStand(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap) {
        int handIndex = gameLogicCache.getCurrentHandIndex();
        BlackJackResult blackJackResult = gameLogicCache.getBlackJackResults().get(handIndex - 1);
        if (blackJackResult != null) {
            //split stand
            if (gameLogicCache.getSplitIndex() == 1 && blackJackResult.isHasSplit()) {
                int len = blackJackResult.getSplitCardsPoint().size();
                if (len > 1) {
                    if (blackJackResult.getSplitCardsPoint().get(len - 1) > BlackJackGameConstant.BLACK_JACK_POINT) {
                        blackJackResult.getSplitCardsPoint().remove(len - 1);
                    } else {
                        blackJackResult.getSplitCardsPoint().remove(0);
                    }
                }
                blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_STAND);
            } else {
                int len = blackJackResult.getCardsPoint().size();
                if (len > 1) {
                    if (blackJackResult.getCardsPoint().get(len - 1) > BlackJackGameConstant.BLACK_JACK_POINT) {
                        blackJackResult.getCardsPoint().remove(len - 1);
                    } else {
                        blackJackResult.getCardsPoint().remove(0);
                    }

                }
                blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_STAND);
            }
            //all hand stand Dealer status is deal or complete
            computeDealerDealStatus(gameLogicCache, engineContextMap);
        }

    }

    public void dealerDraw(BlackJackGameLogicBean gameLogicCache, Map<String, String> engineContextMap, InputInfo input) {
        DealerResult dealerResult = gameLogicCache.getDealerResult();
        List<BlackJackResult> blackJackResultList = gameLogicCache.getBlackJackResults();
        List<BlackJackBetInfo> blackJackBetInfoList = gameLogicCache.getBlackJackBetInfos();
        //dealer draw
        int dealCard = getDealerSecondCard(gameLogicCache, engineContextMap);
        //test input
        if (input != null && input.getInputDealerCards() != null && input.getInputDealerCards().size() > dealerResult.getCardsNumber().size()) {
            dealCard = input.getInputDealerCards().get(dealerResult.getCardsNumber().size());
        }
        dealerResult.getCardsNumber().add(dealCard);
        computeDealerDealPoint(gameLogicCache, dealCard);
        int point = dealerResult.getCardsPoint().get(dealerResult.getCardsPoint().size() - 1);
        if (point > BlackJackGameConstant.BLACK_JACK_POINT) {
            dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_BUST);
            for (int i = 0; i < blackJackResultList.size(); i++) {
                BlackJackResult blackJackResult = blackJackResultList.get(i);
                BlackJackBetInfo betInfo = blackJackBetInfoList.get(i);
                if (betInfo.getBet() > 0) {
                    int handPoint = blackJackResult.getCardsPoint().get(blackJackResult.getCardsPoint().size() - 1);
                    if (handPoint > BlackJackGameConstant.BLACK_JACK_POINT && blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_BUST) {
                        blackJackResult.setBetPay(betInfo.getBet());
                    } else if (handPoint == BlackJackGameConstant.BLACK_JACK_POINT && blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_BJ_WIN) {
                        long blackJackpotPay = StringUtil.doubleMul(getBlackJackPay(), betInfo.getBet());
                        blackJackResult.setBetPay(blackJackpotPay);
                    } else {
                        blackJackResult.setBetPay(getPay() * betInfo.getBet());
                        blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_WIN);
                    }
                    if (blackJackResult.isHasSplit()) {
                        int splitHandPoint = blackJackResult.getSplitCardsPoint().get(blackJackResult.getSplitCardsPoint().size() - 1);
                        if (splitHandPoint > BlackJackGameConstant.BLACK_JACK_POINT && blackJackResult.getSplitHandStatus() == BlackJackGameConstant.HAND_STATUS_BUST) {
                            blackJackResult.setSplitPay(betInfo.getSplitBet());
                        } else {
                            blackJackResult.setSplitPay(getPay() * betInfo.getSplitBet());
                            blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_WIN);
                        }
                    }
                }
            }
        } else if (point >= BlackJackGameConstant.DEALER_CARD_COMPLETE_POINT) {
            dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_COMPLETE);
            computeDealerComplete(gameLogicCache);
        } else {
            dealerResult.setDealerStatus(BlackJackGameConstant.DEALER_DEAL);
        }

    }

    protected void computeDealerComplete(BlackJackGameLogicBean gameLogicCache) {
        List<BlackJackResult> blackJackResultList = gameLogicCache.getBlackJackResults();
        List<BlackJackBetInfo> blackJackBetInfoList = gameLogicCache.getBlackJackBetInfos();
        DealerResult dealerResult = gameLogicCache.getDealerResult();
        int dealerPoint = dealerResult.getCardsPoint().get(dealerResult.getCardsPoint().size() - 1);
        if (blackJackResultList != null && !blackJackResultList.isEmpty()) {
            for (int i = 0; i < blackJackResultList.size(); i++) {
                BlackJackResult blackJackResult = blackJackResultList.get(i);
                BlackJackBetInfo betInfo = blackJackBetInfoList.get(i);
                //hand bet
                if (betInfo.getBet() > 0) {
                    int handPoint = blackJackResult.getCardsPoint().get(blackJackResult.getCardsPoint().size() - 1);
                    if (handPoint > BlackJackGameConstant.BLACK_JACK_POINT && blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_BUST) {
                        blackJackResult.setBetPay(0);
                    } else if (handPoint == BlackJackGameConstant.BLACK_JACK_POINT && blackJackResult.getHandStatus() == BlackJackGameConstant.HAND_STATUS_BJ_WIN) {
                        long blackJackpotPay = StringUtil.doubleMul(getBlackJackPay(), betInfo.getBet());
                        blackJackResult.setBetPay(blackJackpotPay);
                    } else if (handPoint > dealerPoint) {
                        blackJackResult.setBetPay(getPay() * betInfo.getBet());
                        blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_WIN);
                    } else if (handPoint == dealerPoint) {
                        blackJackResult.setBetPay(betInfo.getBet());
                        blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_PUSH);
                    } else {
                        blackJackResult.setBetPay(0);
                        blackJackResult.setHandStatus(BlackJackGameConstant.HAND_STATUS_LOSE);
                    }
                    //split no blackjack
                    if (blackJackResult.isHasSplit()) {
                        int splitHandPoint = blackJackResult.getSplitCardsPoint().get(blackJackResult.getSplitCardsPoint().size() - 1);
                        if (splitHandPoint > BlackJackGameConstant.BLACK_JACK_POINT && blackJackResult.getSplitHandStatus() == BlackJackGameConstant.HAND_STATUS_BUST) {
                            blackJackResult.setSplitPay(0);
                        } else if (splitHandPoint > dealerPoint) {
                            blackJackResult.setSplitPay(getPay() * betInfo.getSplitBet());
                            blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_WIN);
                        } else if (splitHandPoint == dealerPoint) {
                            blackJackResult.setSplitPay(betInfo.getSplitBet());
                            blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_PUSH);
                        } else {
                            blackJackResult.setSplitPay(0);
                            blackJackResult.setSplitHandStatus(BlackJackGameConstant.HAND_STATUS_LOSE);
                        }
                    }
                }

            }

        }

    }

    protected void computeDealerDealPoint(BlackJackGameLogicBean gameLogicCache, int dealCard) {
        List<Integer> dealerCardPointList = gameLogicCache.getDealerResult().getCardsPoint();
        int lastPoint = dealerCardPointList.get(dealerCardPointList.size() - 1);
        int point = computeCardPoint(dealCard);
        if (dealerCardPointList.size() > 1) {
            int tempPoint = point;
            tempPoint += lastPoint;
            if (tempPoint > BlackJackGameConstant.BLACK_JACK_POINT) {
                int firstPoint = dealerCardPointList.get(0);
                dealerCardPointList.clear();
                dealerCardPointList.add(point + firstPoint);
            } else if (tempPoint >= BlackJackGameConstant.DEALER_CARD_COMPLETE_POINT) {
                dealerCardPointList.clear();
                dealerCardPointList.add(tempPoint);
            } else {
                for (int i = 0; i < dealerCardPointList.size(); i++) {
                    dealerCardPointList.set(i, point + dealerCardPointList.get(i));
                }
                if (dealerCardPointList.get(dealerCardPointList.size() - 1) >= BlackJackGameConstant.DEALER_CARD_COMPLETE_POINT) {
                    dealerCardPointList.remove(0);
                }
            }
        } else {
            if (point == BlackJackGameConstant.CARD_A_POINT) {
                point += lastPoint;
                if (lastPoint < BlackJackGameConstant.CARD_PEEK_POINT) {
                    dealerCardPointList.clear();
                    dealerCardPointList.add(point);
                    dealerCardPointList.add(BlackJackGameConstant.CARD_A_MAX_POINT + lastPoint);
                    //second point >17 end
                    if (dealerCardPointList.get(dealerCardPointList.size() - 1) >= BlackJackGameConstant.DEALER_CARD_COMPLETE_POINT) {
                        dealerCardPointList.remove(0);
                    }
                } else if (lastPoint == BlackJackGameConstant.CARD_PEEK_POINT) {
                    dealerCardPointList.set(0, BlackJackGameConstant.BLACK_JACK_POINT);
                } else {
                    dealerCardPointList.set(0, point);
                }
            } else {
                point += lastPoint;
                dealerCardPointList.set(0, point);
            }
        }
        gameLogicCache.getDealerResult().setCardsPoint(dealerCardPointList);
    }

    public void checkBetInfo(BlackJackGameLogicBean gameLogicCache) throws InvalidBetException {
        try {
            List<BlackJackBetInfo> betInfos = gameLogicCache.getBlackJackBetInfos();
            List<BlackJackResult> blackJackResultList = gameLogicCache.getBlackJackResults();
            int index = 0;
            if (betInfos != null && !betInfos.isEmpty()) {
                BlackJackResult blackJackResult = null;
                for (BlackJackBetInfo betInfo : betInfos) {
                    if (blackJackResultList != null && !blackJackResultList.isEmpty() && betInfo.getBet() > 0) {
                        blackJackResult = blackJackResultList.get(index);
                    }
                    if (betInfo.getBet() > 0 && betInfo.getBet() < minBet()) {
                        betInfo.setBet(minBet());
                    }
                    if (betInfo.getJackpotBet() > 0 && betInfo.getJackpotBet() < minBet()) {
                        betInfo.setJackpotBet(minBet());
                    }
                    if (betInfo.getSplitBet() > 0 && betInfo.getSplitBet() < minBet()) {
                        betInfo.setSplitBet(minBet());
                    }
                    if (blackJackResult != null && betInfo.getBet() > 0) {
                        boolean isHasDouble = blackJackResult.isHasDouble();
                        if (isHasDouble) {
                            if (betInfo.getBet() > 2 * maxBet()) {
                                betInfo.setBet(2 * maxBet());
                            }
                        } else if (betInfo.getBet() > maxBet()) {
                            betInfo.setBet(maxBet());
                        }
                    } else if (betInfo.getBet() > maxBet()) {
                        betInfo.setBet(maxBet());
                    }
                    if (betInfo.getJackpotBet() > maxBet()) {
                        betInfo.setJackpotBet(maxBet());
                    }
                    if (blackJackResult != null && betInfo.getSplitBet() > 0) {
                        boolean isSplitHandHasDouble = blackJackResult.isSplitHandHasDouble();
                        if (isSplitHandHasDouble) {
                            if (betInfo.getSplitBet() > 2 * maxBet()) {
                                betInfo.setSplitBet(2 * maxBet());
                            }
                        } else if (betInfo.getSplitBet() > maxBet()) {
                            betInfo.setSplitBet(maxBet());
                        }
                    }
                    if (betInfo.getBet() == 0 && betInfo.getJackpotBet() > 0) {
                        log.debug("Invalid Bet Update");
                        throw new InvalidBetException();
                    }
                    index++;
                }
            }
        } catch (Exception e) {
            log.debug("Invalid Bet Update");
            throw new InvalidBetException();
        }
    }

}
