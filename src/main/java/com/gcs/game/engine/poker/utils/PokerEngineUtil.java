package com.gcs.game.engine.poker.utils;

import com.alibaba.fastjson.JSON;
import com.gcs.game.engine.GameModelFactory;
import com.gcs.game.engine.poker.bonus.PokerBonus;
import com.gcs.game.engine.poker.model.BasePokerModel;
import com.gcs.game.engine.poker.vo.PokerBonusResult;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.engine.poker.vo.PokerResult;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class PokerEngineUtil {


    public static BasePokerModel getModel(String gameModel) {
        return GameModelFactory.getInstance().getPokerModel(gameModel);
    }

    public static PokerBonus getBonusModel(String gameModel, String bonusAsset) {
        return GameModelFactory.getInstance().getPokerBonusModel(gameModel, bonusAsset);
    }

    public static PokerGameLogicBean getDefaultGameLogicData(String gameMathID, int payback) throws InvalidGameStateException {
        try {
            PokerGameLogicBean result = new PokerGameLogicBean();
            result.setMmID(gameMathID);
            result.setJackpotGroupCode("");
            result.setGamePlayStatus(GameConstant.GAME_STATUS_IDLE);
            result.setDenom(1);
            result.setPercentage(payback);
            BasePokerModel model = getModel(gameMathID);
            if (model != null) {
                long lines = model.minLines();
                long bet = model.minBet();
                long totalBetCredit = model.totalBet(lines, bet);
                result.setSumBetCredit(totalBetCredit);
                result.setSumBetBalance(totalBetCredit);
            }
            return result;
        } catch (Exception e) {
            throw new InvalidGameStateException();
        }
    }

    public static PokerGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input, PokerGameLogicBean gameLogicCache, String mathModel, Map<String, String> engineContextMap) throws InvalidGameStateException, InvalidBetException {
        try {
            //new game cycle
            setGameLogicRequest(gameLogicMap, gameLogicCache);
            int requestGameStatus = gameLogicCache.getGamePlayStatus();
            log.debug("Game Current Request Status is {}", requestGameStatus);
            //deal button
            if (requestGameStatus == GameConstant.GAME_STATUS_IDLE || requestGameStatus == GameConstant.GAME_STATUS_COMPLETE) {
                log.debug("Game Deals");
                BasePokerModel model = getModel(mathModel);
                if (model != null) {
                    checkBetInfo(gameLogicCache, model);
                    PokerResult pokerResult = null;
                    //TODO input
                    pokerResult = model.deals(gameLogicCache, engineContextMap, input);

                    gameLogicCache.setPokerResult(pokerResult);
                    gameLogicCache.setPokerFsResult(null);
                    gameLogicCache.setPokerBonusResult(null);
                    gameLogicCache.setPokerBonusResultsList(null);
                    gameLogicCache.setGamePlayStatus(GameConstant.POKER_GAME_STATUS_SWITCH_CARD);
                    gameLogicCache.setPokerBsAsset("");
                    gameLogicCache.setNextScenes(null);
                    gameLogicCache.setLastScenes(null);
                    gameLogicCache.setFsCountLeft(0);
                    gameLogicCache.setFsHitCounts(null);
                    gameLogicCache.setHitSceneLeftList(null);
                    gameLogicCache.setRespin(false);
                    gameLogicCache.setRespinCountsLeft(0);

                    long totalWin = pokerResult.getPokerPay();
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

    public static PokerGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextRequest, InputInfo input, PokerGameLogicBean gameLogicCache, String mathModel, Map<String, String> engineContextMap) throws InvalidPlayerInputException, InvalidGameStateException {
        try {
            //front request HoldPosition info
            PokerGameLogicBean gameLogicBean = (PokerGameLogicBean) gameLogicRequest;
            int reqGameStatus = gameLogicBean.getGamePlayStatus();
            int gameStatus = gameLogicCache.getGamePlayStatus();
            if (reqGameStatus == gameStatus && reqGameStatus != GameConstant.GAME_STATUS_COMPLETE) {
                log.debug("Game Current Request Status is {}", reqGameStatus);
                gameLogicCache.setBetForCurrentStep(0);
                if (engineContextMap == null || engineContextMap.isEmpty()) {
                    engineContextMap.putAll(engineContextRequest);
                }
                List<Integer> holdPositions = null;
                if (gameLogicMap != null && !gameLogicMap.isEmpty()) {
                    if (gameLogicMap.containsKey("holdPositions")) {
                        Object holdPositionsObj = gameLogicMap.get("holdPositions");
                        if (holdPositionsObj != null) {
                            holdPositions = JSON.parseArray(holdPositionsObj.toString(), Integer.class);
                            if (reqGameStatus == GameConstant.POKER_GAME_STATUS_SWITCH_CARD) {
                                gameLogicCache.getPokerResult().setHoldPositions(holdPositions);
                            } else {
                                if (gameLogicCache.getPokerFsResult() != null && !gameLogicCache.getPokerFsResult().isEmpty() && gameLogicCache.getPokerFsResult().size() > 0) {
                                    PokerResult fsPokerResult = gameLogicCache.getPokerFsResult().get(gameLogicCache.getPokerFsResult().size() - 1);
                                    fsPokerResult.setHoldPositions(holdPositions);
                                }
                            }
                        }
                    }
                }
                int newGameStatus = reqGameStatus;
                boolean bonusComplete = false;
                boolean freeSpinComplete = false;
                long winCredit = 0L;
                String currentBonusAsset = "";
                int payback = gameLogicCache.getPercentage();
                BasePokerModel model = getModel(mathModel);
                if (model != null) {
                    if (reqGameStatus == GameConstant.POKER_GAME_STATUS_SWITCH_CARD) {
                        log.debug("Switch Card for spin");
                        if (gameLogicCache.getPokerResult() != null) {
                            PokerResult pokerResult = model.spin(gameLogicCache, engineContextMap);
                            computeBasePokerGameBonus(gameLogicCache);
                            winCredit = pokerResult.getPokerPay() + pokerResult.getInstantCashPay();
                            computeTotalWin(gameLogicCache, model, winCredit);
                        }
                    } else if (reqGameStatus == GameConstant.POKER_GAME_STATUS_TRIGGER_BONUS) {
                        currentBonusAsset = gameLogicCache.getPokerBsAsset();
                        PokerBonus bonusModel = getBonusModel(mathModel, currentBonusAsset);
                        if (gameLogicCache.getPokerBonusResult() == null
                                || gameLogicCache.getPokerBonusResult().getBonusPlayStatus() == GameConstant.POKER_GAME_BONUS_STATUS_COMPLETE) {
                            // start bonus
                            PokerBonusResult bonusResult;
                            if (input != null) {
                                log.debug("bonus start with input");
                                bonusResult = bonusModel.computeBonusStart(gameLogicCache, payback, input);
                            } else {
                                log.debug("bonus start");
                                bonusResult = bonusModel.computeBonusStart(gameLogicCache, payback);
                            }
                            if (bonusResult != null) {
                                gameLogicCache.setPokerBonusResult(bonusResult);
                            }
                        } else {
                            PokerBonusResult bonus = gameLogicCache.getPokerBonusResult();
                            // check input
                            bonusModel.checkInput4BonusPick(gameLogicCache, playerInput, bonus);
                            log.debug("pick bonus");
                            PokerBonusResult bonusResult = bonusModel.computeBonusPick(gameLogicCache, playerInput, bonus);
                            if (bonusResult != null) {
                                gameLogicCache.setPokerBonusResult(bonusResult);
                                if (bonusResult.getBonusPlayStatus() == GameConstant.POKER_GAME_BONUS_STATUS_COMPLETE) {
                                    bonusComplete = true;
                                    gameLogicCache.setLastScenes(currentBonusAsset);
                                    winCredit = bonusResult.getTotalPay();
                                    if (model.maxTotalPay() > 0 && winCredit >= model.maxTotalPay()) {
                                        winCredit = model.maxTotalPay();
                                    }

                                    List<PokerBonusResult> bonusResultList = gameLogicCache.getPokerBonusResultsList();
                                    if (bonusResultList == null) {
                                        bonusResultList = new ArrayList<>();
                                    }
                                    bonusResultList.add(bonusResult.clone());
                                    gameLogicCache.setPokerBonusResultsList(bonusResultList);
                                }
                            }
                        }
                        computeTotalWin(gameLogicCache, model, winCredit);
                    } else if (reqGameStatus == GameConstant.POKER_GAME_STATUS_TRIGGER_FREESPIN) {
                        int fsSize;
                        int requestFsSize;
                        fsSize = gameLogicCache.getPokerFsResult() == null ? 0 : gameLogicCache.getPokerFsResult().size();
                        requestFsSize = gameLogicBean.getPokerFsResult() == null ? 0 : gameLogicBean.getPokerFsResult().size();
                        if (fsSize != requestFsSize) {
                            throw new InvalidGameStateException();
                        }
                        if (gameLogicCache.getPokerFsResult() != null && !gameLogicCache.getPokerFsResult().isEmpty() && gameLogicCache.getPokerFsResult().size() > 0) {
                            PokerResult fsPokerResult = gameLogicCache.getPokerFsResult().get(gameLogicCache.getPokerFsResult().size() - 1);
                            if (fsPokerResult.getPokerPlayStatus() == PokerGameConstant.POKER_STATUS_SWITCH_CARD) {
                                freeSpinComplete = pokerProgress4FreeSpin(gameLogicCache, model, engineContextMap, input);
                            } else {
                                pokerDeals4FreeSpin(gameLogicCache, model, engineContextMap, input);
                            }
                        } else {
                            pokerDeals4FreeSpin(gameLogicCache, model, engineContextMap, input);
                        }
                    }

                    if (freeSpinComplete || bonusComplete) {
                        String bonusAsset = "";
                        String nextScene = "slots";
                        int freespinHitTimes = 0;
                        List<String> nextScenes = gameLogicCache.getHitSceneLeftList();
                        if (nextScenes == null || nextScenes.isEmpty()) {
                            newGameStatus = GameConstant.GAME_STATUS_COMPLETE;
                        } else {
                            nextScene = nextScenes.get(0);
                            if (nextScenes.size() > 1) {
                                nextScenes.remove(0);
                            } else {
                                gameLogicCache.setHitSceneLeftList(null);
                            }

                            if ("freeSpin".equalsIgnoreCase(nextScene)) {
                                log.debug("hit Free Spin");
                                freespinHitTimes = getFreeSpinHitTimes(gameLogicCache);
                                newGameStatus = GameConstant.POKER_GAME_STATUS_TRIGGER_FREESPIN;
                            } else {
                                log.debug("hit Bonus");
                                bonusAsset = nextScene;
                                newGameStatus = GameConstant.POKER_GAME_STATUS_TRIGGER_BONUS;
                            }
                        }
                        gameLogicCache.setNextScenes(nextScene);
                        gameLogicCache.setPokerBsAsset(bonusAsset);
                        gameLogicCache.setFsCountLeft(freespinHitTimes);
                        if (freespinHitTimes > 0) {
                            int[] freeSpinHitTimes = gameLogicCache.getFsHitCounts();
                            if (freeSpinHitTimes == null) {
                                gameLogicCache.setFsHitCounts(new int[]{freespinHitTimes});
                            } else {
                                int[] newFreeSpinHitTimes = new int[freeSpinHitTimes.length + 1];
                                for (int i = 0; i < freeSpinHitTimes.length; i++) {
                                    newFreeSpinHitTimes[i] = freeSpinHitTimes[i];
                                }
                                newFreeSpinHitTimes[freeSpinHitTimes.length] = freespinHitTimes;
                                gameLogicCache.setFsHitCounts(newFreeSpinHitTimes);
                            }
                        }
                    }
                    if (reqGameStatus != GameConstant.POKER_GAME_STATUS_SWITCH_CARD) {
                        gameLogicCache.setGamePlayStatus(newGameStatus);
                    }
                    //TODO progressive jackpot
                }
            } else {
                throw new InvalidGameStateException();
            }
            return gameLogicCache;
        } catch (Exception e) {
            throw new InvalidGameStateException();
        }

    }

    private static PokerResult pokerDeals4FreeSpin(PokerGameLogicBean gameLogicCache, BasePokerModel model, Map<String, String> engineContextMap, InputInfo input) {
        // free spin deals
        if (gameLogicCache.getRespinCountsLeft() > 0) {
            gameLogicCache.setRespin(true);
        } else {
            gameLogicCache.setRespin(false);
        }
        if (gameLogicCache.isRespin()) {
            gameLogicCache.setRespinCountsLeft(gameLogicCache.getRespinCountsLeft() - 1);
        }

        PokerResult pokerResult;
        log.debug("Fs deals");
        pokerResult = model.dealsInFreeSpin(gameLogicCache, engineContextMap, input);
        if (pokerResult != null) {
            //TODO respin
            if (gameLogicCache.getPokerFsResult() == null) {
                List<PokerResult> fsPokerResult = new ArrayList<>();
                gameLogicCache.setPokerFsResult(fsPokerResult);
            }
            gameLogicCache.getPokerFsResult().add(pokerResult);
        }
        return pokerResult;
    }

    private static void computeTotalWin(PokerGameLogicBean gameLogicCache, BasePokerModel model, long winCredit) {
        gameLogicCache.setPayForCurrentStep(0);
        if (winCredit > 0) {
            long totalWin = winCredit + gameLogicCache.getSumWinCredit();
            gameLogicCache.setSumWinCredit(totalWin);
            gameLogicCache.setSumWinBalance(totalWin * gameLogicCache.getDenom());
            gameLogicCache.setPayForCurrentStep(winCredit);
        }
    }

    private static int getFreeSpinHitTimes(PokerGameLogicBean gameLogicCache) {
        int hitFreespinTimes = 0;
       /* if (gameLogicCache != null && gameLogicCache.getSlotGameCount() > 1) {
            hitFreespinTimes = gameLogicCache.getSlotSpinResult4Multi().get(0).getTriggerFsCounts();
        } else
         */
        if (gameLogicCache != null) {
            hitFreespinTimes = gameLogicCache.getPokerResult().getTriggerFsCounts();
        }
        return hitFreespinTimes;
    }


    protected static boolean pokerProgress4FreeSpin(PokerGameLogicBean gameLogicCache, BasePokerModel model, Map<String, String> engineContextMap, InputInfo input) {
        //freespin switch card
        boolean freeSpinComplete = false;
        boolean isBaseGameRespin = false;
        PokerResult pokerResult;
        log.debug("Fs spin");
        pokerResult = model.spinInFs(gameLogicCache, engineContextMap, input);
        if (pokerResult != null) {
            //TODO respin
            freeSpinComplete = computeFsPoker(gameLogicCache, pokerResult);
            long winCredit = pokerResult.getPokerPay();
            computeTotalWin(gameLogicCache, model, winCredit);
        }
        if (freeSpinComplete && !isBaseGameRespin) {
            gameLogicCache.setLastScenes("freeSpin");
        }
        return freeSpinComplete;
    }

    protected static boolean computeFsPoker(PokerGameLogicBean gameLogicCache, PokerResult pokerResult) {
        boolean freeSpinComplete = false;

        int freeSpinLeft = gameLogicCache.getFsCountLeft();
        freeSpinLeft--;
        if (pokerResult.isTriggerFs()) {
            /*// trigger respin
            if (pokerResult.isTriggerRespin()) {
                freeSpinLeft = computeRespinInFreeSpin(gameLogicCache, pokerResult, freeSpinLeft);
            }*/
            // re-trigger free spin
            freeSpinLeft += pokerResult.getTriggerFsCounts();
            int[] freeSpinHitTimes = gameLogicCache.getFsHitCounts();
            int[] newFreeSpinHitTimes = new int[freeSpinHitTimes.length + 1];
            for (int i = 0; i < freeSpinHitTimes.length; i++) {
                newFreeSpinHitTimes[i] = freeSpinHitTimes[i];
            }
            newFreeSpinHitTimes[freeSpinHitTimes.length] = pokerResult.getTriggerFsCounts();
            gameLogicCache.setFsHitCounts(newFreeSpinHitTimes);
        /*} else if (pokerResult.isTriggerRespin()) {
            // support multi-times respin.
            freeSpinLeft = computeRespinInFreeSpin(gameLogicCache, pokerResult, freeSpinLeft);*/
        } else if (pokerResult.isTriggerBonus()) {
            // hit bonus in respin
            List<String> nextScenes = gameLogicCache.getHitSceneLeftList();
            if (nextScenes == null || nextScenes.isEmpty()) {
                nextScenes = new ArrayList<>();
            }
            if (pokerResult.getNextScenes() != null && pokerResult.getNextScenes().size() > 0) {
                nextScenes.add(0, pokerResult.getNextScenes().get(0));
            }
            gameLogicCache.setHitSceneLeftList(nextScenes);

            if (freeSpinLeft <= 0) {
                freeSpinComplete = true;
            }
        } else if (freeSpinLeft <= 0) {
            freeSpinComplete = true;
        }
        gameLogicCache.setFsCountLeft(freeSpinLeft);
        return freeSpinComplete;
    }

    protected static void computeBasePokerGameBonus(PokerGameLogicBean gameLogicCache) {
        int newGameStatus;
        String bonusAsset = "";
        String nextScene = "slots";
        int freeSpinHitTimes = 0;
        int respinTimes = 0;
        PokerResult pokerResult = gameLogicCache.getPokerResult();
        List<String> nextScenes = pokerResult.getNextScenes();
        if (nextScenes == null || nextScenes.isEmpty()) {
            log.debug("Game complete");
            newGameStatus = GameConstant.GAME_STATUS_COMPLETE;
            gameLogicCache.setHitSceneLeftList(null);
        } else {
            nextScene = nextScenes.get(0);
            if ("freeSpin".equalsIgnoreCase(nextScene)) {
                newGameStatus = GameConstant.POKER_GAME_STATUS_TRIGGER_FREESPIN;
                if (pokerResult.isTriggerRespin()) {
                    log.debug("hit re-Spin");
                    respinTimes = pokerResult.getTriggerRespinCounts();
                    freeSpinHitTimes = pokerResult.getTriggerRespinCounts();
                } else {
                    log.debug("hit Free Spin");
                    freeSpinHitTimes = pokerResult.getTriggerFsCounts();
                }
            } else {
                log.debug("hit Bonus");
                bonusAsset = nextScene;
                newGameStatus = GameConstant.POKER_GAME_STATUS_TRIGGER_BONUS;
            }
            if (nextScenes.size() > 1) {
                List<String> sceneLeft = new ArrayList<>();
                for (int i = 1; i < nextScenes.size(); i++) {
                    sceneLeft.add(nextScenes.get(i));
                }
                gameLogicCache.setHitSceneLeftList(sceneLeft);
            } else {
                gameLogicCache.setHitSceneLeftList(null);
            }
        }
        gameLogicCache.setRespin(false);
        gameLogicCache.setGamePlayStatus(newGameStatus);
        gameLogicCache.setPokerBsAsset(bonusAsset);
        gameLogicCache.setNextScenes(nextScene);
        gameLogicCache.setFsCountLeft(freeSpinHitTimes);
        gameLogicCache.setRespinCountsLeft(respinTimes);
        if (freeSpinHitTimes > 0) {
            gameLogicCache.setFsHitCounts(new int[]{freeSpinHitTimes});
        } else {
            gameLogicCache.setFsHitCounts(null);
        }
    }

    private static void setGameLogicRequest(Map gameLogicMap, PokerGameLogicBean gameLogicCache) throws InvalidGameStateException, InvalidBetException {
        if (gameLogicMap != null && !gameLogicMap.isEmpty()) {
            gameLogicCache.setBet(Long.parseLong(gameLogicMap.get("bet").toString()));
            gameLogicCache.setLines(Long.parseLong(gameLogicMap.get("lines").toString()));
            gameLogicCache.setDenom(Long.parseLong(gameLogicMap.get("denom").toString()));
            gameLogicCache.setLastScenes("slots");
            //TODO gamePlayStatus
            //gameLogicCache.setGamePlayStatus(Integer.parseInt(gameLogicMap.get("gamePlayStatus").toString()));

        } else {
            throw new InvalidGameStateException();
        }
    }

    protected static void checkBetInfo(PokerGameLogicBean gameLogicCache, BasePokerModel model) throws InvalidBetException {
        try {
            long bet = gameLogicCache.getBet();
            if (bet < model.minBet()) {
                bet = model.minBet();
            }
            if (bet > model.maxBet()) {
                bet = model.maxBet();
            }
            long lines = gameLogicCache.getLines();
            if (lines < model.minLines()) {
                lines = model.minLines();
            }
            if (lines > model.maxLines()) {
                lines = model.maxLines();
            }
            gameLogicCache.setBet(bet);
            gameLogicCache.setLines(lines);
            long totalBetCredit = model.totalBet(bet, lines);
            gameLogicCache.setSumBetCredit(totalBetCredit);
            gameLogicCache.setSumBetBalance(totalBetCredit * gameLogicCache.getDenom());
            gameLogicCache.setBetForCurrentStep(totalBetCredit);
        } catch (Exception e) {
            log.debug("Invalid Bet Update");
            throw new InvalidBetException();
        }
    }

}
