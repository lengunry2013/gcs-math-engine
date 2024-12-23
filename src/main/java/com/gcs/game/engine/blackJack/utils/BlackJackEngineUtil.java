package com.gcs.game.engine.blackJack.utils;

import com.alibaba.fastjson.JSON;
import com.gcs.game.engine.blackJack.model.BaseBlackJackModel;
import com.gcs.game.engine.GameModelFactory;
import com.gcs.game.engine.blackJack.vo.BlackJackBetInfo;
import com.gcs.game.engine.blackJack.vo.BlackJackResult;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.engine.blackJack.vo.BlackJackGameLogicBean;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
public class BlackJackEngineUtil {


    public static BaseBlackJackModel getModel(String gameModel) {
        return GameModelFactory.getInstance().getBlackJackModel(gameModel);
    }

    public static BlackJackGameLogicBean getDefaultGameLogicData(String gameMathID, int payback) throws InvalidGameStateException {
        try {
            BlackJackGameLogicBean result = new BlackJackGameLogicBean();
            result.setMmID(gameMathID);
            result.setJackpotGroupCode("");
            result.setGamePlayStatus(GameConstant.GAME_STATUS_IDLE);
            result.setDenom(1);
            result.setPercentage(payback);
            //result.setMathType(GameClass.TableGame_BlackJack.name());
            return result;
        } catch (Exception e) {
            throw new InvalidGameStateException();
        }
    }

    public static BlackJackGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input, BlackJackGameLogicBean gameLogicCache, String mathModel, Map<String, String> engineContextMap) throws InvalidGameStateException, InvalidBetException {
        try {
            //new game cycle
            gameLogicCache.setBlackJackBetInfos(null);
            setGameLogicRequest(gameLogicMap, gameLogicCache);
            int requestGameStatus = gameLogicCache.getGamePlayStatus();
            log.debug("Game Current Request Status is {}", requestGameStatus);
            //Deal button,Rebet and Deal button,Double and Deal
            if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_DEAL) {
                log.debug("Deal Or Rebet And Deal Or Double and Deal");
                BaseBlackJackModel model = getModel(mathModel);
                if (model != null) {
                    model.checkBetInfo(gameLogicCache);
                    long totalBet = model.computeTotalBet(gameLogicCache);
                    gameLogicCache.setSumBetCredit(totalBet);
                    gameLogicCache.setSumBetBalance(totalBet * gameLogicCache.getDenom());
                    if (input != null && input.getInputDealerCards() != null && input.getInputHandsCards() != null) {
                        log.debug("Deal with input");
                        model.deal(gameLogicCache, engineContextMap, input);
                    } else {
                        log.debug("Deal");
                        model.deal(gameLogicCache, engineContextMap);
                    }
                    int dealerStatus = gameLogicCache.getDealerResult().getDealerStatus();
                    if (dealerStatus == BlackJackGameConstant.DEALER_COMPLETE) {
                        gameLogicCache.setGamePlayStatus(GameConstant.GAME_STATUS_COMPLETE);
                    }
                    gameLogicCache.setBetForCurrentStep(totalBet);
                    long totalWin = model.computeTotalWin(gameLogicCache);
                    gameLogicCache.setPayForCurrentStep(totalWin);
                    gameLogicCache.setSumWinCredit(totalWin);
                    gameLogicCache.setSumWinBalance(totalWin * gameLogicCache.getDenom());
                }
            } else {
                throw new InvalidGameStateException();
            }
            return gameLogicCache;
        } catch (InvalidBetException e) {
            throw new InvalidBetException();
        } catch (Exception e) {
            throw new InvalidGameStateException();
        }
    }

    public static BlackJackGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextRequest, InputInfo input, BlackJackGameLogicBean gameLogicCache, String mathModel, Map<String, String> engineContextMap) throws InvalidPlayerInputException, InvalidGameStateException {
        try {
            //front request denom,gamePlayStatus,currentHandIndex,splitIndex info
            BlackJackGameLogicBean gameLogicBean = (BlackJackGameLogicBean) gameLogicRequest;
            int lastGameStatus = gameLogicBean.getGamePlayStatus();
            if (lastGameStatus == gameLogicCache.getGamePlayStatus()) {
                setGameLogicRequest(gameLogicMap, gameLogicCache);
                int requestGameStatus = gameLogicCache.getGamePlayStatus();
                log.debug("Game Current Request Status is {}", requestGameStatus);
                if (engineContextMap == null || engineContextMap.isEmpty()) {
                    engineContextMap.putAll(engineContextRequest);
                }
                BaseBlackJackModel model = getModel(mathModel);
                if (model != null) {
                    model.checkBetInfo(gameLogicCache);
                    long lastTotalBet = model.computeTotalBet(gameLogicCache);
                    gameLogicCache.setBetForCurrentStep(0);
                    gameLogicCache.setPayForCurrentStep(0);
                    gameLogicCache.setSumBetCredit(lastTotalBet);
                    gameLogicCache.setSumBetBalance(lastTotalBet * gameLogicCache.getDenom());
                    if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_INSURANCE) {
                        log.debug("Hand Has Insurance");
                        model.HandsInsurance(gameLogicCache);
                        long totalBet = model.computeTotalBet(gameLogicCache);
                        gameLogicCache.setBetForCurrentStep(totalBet - lastTotalBet);
                        gameLogicCache.setSumBetCredit(totalBet);
                        gameLogicCache.setSumBetBalance(totalBet * gameLogicCache.getDenom());
                    } else if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_NO_INSURANCE) {
                        log.debug("Hand No Insurance");
                        model.HandsInsurance(gameLogicCache);
                    } else if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_PEEK_BLACKJACK) {
                        log.debug("Dealer Peek BlackJack");
                        model.DealerPeekBlackJack(gameLogicCache, engineContextMap);
                        int dealerStatus = gameLogicCache.getDealerResult().getDealerStatus();
                        //blackJack end game
                        if (dealerStatus == BlackJackGameConstant.DEALER_BLACKJACK || dealerStatus == BlackJackGameConstant.DEALER_COMPLETE) {
                            gameLogicCache.setGamePlayStatus(GameConstant.GAME_STATUS_COMPLETE);
                            long totalWin = model.computeTotalWin(gameLogicCache);
                            gameLogicCache.setSumWinCredit(totalWin);
                            gameLogicCache.setSumWinBalance(totalWin * gameLogicCache.getDenom());
                            //TODO first jackpotPay
                            gameLogicCache.setPayForCurrentStep(totalWin);
                        }
                    } else if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_SPLIT) {
                        log.debug("Hands Split");
                        model.HandSplit(gameLogicCache, engineContextMap, input);
                        long totalBet = model.computeTotalBet(gameLogicCache);
                        gameLogicCache.setBetForCurrentStep(totalBet - lastTotalBet);
                        gameLogicCache.setSumBetCredit(totalBet);
                        gameLogicCache.setSumBetBalance(totalBet * gameLogicCache.getDenom());
                        computeBlackJackComplete(gameLogicCache, model);
                    } else if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_DOUBLE) {
                        log.debug("Hands Doubled");
                        model.HandDoubled(gameLogicCache, engineContextMap, input);
                        long totalBet = model.computeTotalBet(gameLogicCache);
                        gameLogicCache.setBetForCurrentStep(totalBet - lastTotalBet);
                        gameLogicCache.setSumBetCredit(totalBet);
                        gameLogicCache.setSumBetBalance(totalBet * gameLogicCache.getDenom());
                        computeBlackJackComplete(gameLogicCache, model);
                    } else if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_HIT) {
                        log.debug("Hands Hit");
                        model.HandHit(gameLogicCache, engineContextMap, input);
                        computeBlackJackComplete(gameLogicCache, model);
                    } else if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_STAND) {
                        log.debug("Hands Stand");
                        model.HandStand(gameLogicCache, engineContextMap);
                        computeBlackJackComplete(gameLogicCache, model);
                    } else if (requestGameStatus == GameConstant.BJ_BLACKJACK_GAME_STATUS_DEALER_DRAW) {
                        log.debug("Dealer deal");
                        model.dealerDraw(gameLogicCache, engineContextMap, input);
                        int dealerStatus = gameLogicCache.getDealerResult().getDealerStatus();
                        //blackJack end game
                        if (dealerStatus == BlackJackGameConstant.DEALER_BUST || dealerStatus == BlackJackGameConstant.DEALER_COMPLETE) {
                            gameLogicCache.setGamePlayStatus(GameConstant.GAME_STATUS_COMPLETE);
                            long totalWin = model.computeTotalWin(gameLogicCache);
                            gameLogicCache.setSumWinCredit(totalWin);
                            gameLogicCache.setSumWinBalance(totalWin * gameLogicCache.getDenom());
                            gameLogicCache.setPayForCurrentStep(totalWin);
                        }
                    } else {
                        throw new InvalidGameStateException();
                    }
                }
            } else {
                throw new InvalidGameStateException();
            }
            return gameLogicCache;
        } catch (Exception e) {
            throw new InvalidGameStateException();
        }

    }

    private static void setGameLogicRequest(Map gameLogicMap, BlackJackGameLogicBean gameLogicCache) throws InvalidGameStateException, CloneNotSupportedException, InvalidBetException {
        if (gameLogicMap != null && !gameLogicMap.isEmpty()) {
            int requestGameStatus = Integer.parseInt(gameLogicMap.get("gamePlayStatus").toString());
            long denom = Long.parseLong(gameLogicMap.get("denom").toString());
            String betInfoStr = gameLogicMap.get("blackJackBetInfos").toString().replace("=", ":");
            List<BlackJackBetInfo> betInfos = JSON.parseArray(betInfoStr, BlackJackBetInfo.class);
            if (gameLogicMap.containsKey("gameSteps")) {
                List<Integer> gameSteps = JSON.parseArray(gameLogicMap.get("gameSteps").toString(), Integer.class);
                gameLogicCache.setGameSteps(gameSteps);
            }
            if (gameLogicMap.containsKey("currentHandIndex")) {
                int currentHandIndex = Integer.parseInt(gameLogicMap.get("currentHandIndex").toString());
                log.debug("currentHandIndex is {}", currentHandIndex);
                List<BlackJackResult> blackJackResultList = gameLogicCache.getBlackJackResults();
                if (blackJackResultList != null && !blackJackResultList.isEmpty()) {
                    int dealerStatus = gameLogicCache.getDealerResult().getDealerStatus();
                    if (currentHandIndex > blackJackResultList.size()) {
                        throw new InvalidGameStateException();
                    }
                    //check fronted currentHandIndex
                    if (dealerStatus == BlackJackGameConstant.DEALER_WAIT_FOR_HANDS) {
                        int handIndex = 1;
                        for (BlackJackResult blackJackResult : blackJackResultList) {
                            int handStatus = blackJackResult.getHandStatus();
                            int splitHandStatus = blackJackResult.getSplitHandStatus();
                            boolean isNextHand = isNextHand(handStatus);
                            if (blackJackResult.isHasSplit() && (splitHandStatus == BlackJackGameConstant.HAND_STATUS_BUST
                                    || splitHandStatus == BlackJackGameConstant.HAND_STATUS_STAND) && isNextHand) {
                                handIndex++;
                            } else if (!blackJackResult.isHasSplit() && isNextHand) {
                                handIndex++;
                            } else {
                                break;
                            }
                        }
                        if (currentHandIndex != handIndex) {
                            throw new InvalidGameStateException();
                        }
                    }
                }
                gameLogicCache.setCurrentHandIndex(currentHandIndex);
            }
            if (gameLogicMap.containsKey("splitIndex")) {
                int splitIndex = Integer.parseInt(gameLogicMap.get("splitIndex").toString());
                gameLogicCache.setSplitIndex(splitIndex);
            }
            if (betInfos == null || betInfos.isEmpty()) {
                throw new InvalidBetException();
            }
            if (gameLogicCache.getBlackJackBetInfos() != null && !gameLogicCache.getBlackJackBetInfos().isEmpty()) {
                List<BlackJackBetInfo> lastBetInfos = new ArrayList<>();
                List<BlackJackBetInfo> currentBetInfos = new ArrayList<>();
                for (BlackJackBetInfo tempBetInfo : gameLogicCache.getBlackJackBetInfos()) {
                    lastBetInfos.add(tempBetInfo.clone());
                }
                for (BlackJackBetInfo tempBetInfo : betInfos) {
                    currentBetInfos.add(tempBetInfo.clone());
                }
                lastBetInfos.sort(Comparator.comparing(BlackJackBetInfo::hashCode));
                currentBetInfos.sort(Comparator.comparing(BlackJackBetInfo::hashCode));
                if (lastBetInfos.size() != currentBetInfos.size() || !lastBetInfos.toString().equals(currentBetInfos.toString())) {
                    throw new InvalidBetException();
                }
            }
            gameLogicCache.setDenom(denom);
            gameLogicCache.setBlackJackBetInfos(betInfos);
            gameLogicCache.setGamePlayStatus(requestGameStatus);
        } else {
            throw new InvalidGameStateException();
        }
    }

    private static boolean isNextHand(int handStatus) {
        return handStatus == BlackJackGameConstant.HAND_STATUS_BJ_WIN || handStatus == BlackJackGameConstant.HAND_STATUS_BUST ||
                handStatus == BlackJackGameConstant.HAND_STATUS_STAND || handStatus == BlackJackGameConstant.HAND_STATUS_SITTINGOUT;
    }

    protected static void computeBlackJackComplete(BlackJackGameLogicBean gameLogicCache, BaseBlackJackModel model) {
        int dealerStatus = gameLogicCache.getDealerResult().getDealerStatus();
        if (dealerStatus == BlackJackGameConstant.DEALER_COMPLETE) {
            gameLogicCache.setGamePlayStatus(GameConstant.GAME_STATUS_COMPLETE);
            long totalWin = model.computeTotalWin(gameLogicCache);
            gameLogicCache.setSumWinCredit(totalWin);
            gameLogicCache.setSumWinBalance(totalWin * gameLogicCache.getDenom());
            gameLogicCache.setPayForCurrentStep(totalWin);
        }
    }

}
