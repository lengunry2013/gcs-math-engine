package com.gcs.game.engine.slots.utils;

import com.gcs.game.engine.GameModelFactory;
import com.gcs.game.engine.slots.bonus.BaseBonus;
import com.gcs.game.engine.slots.model.IFsSceneComputer;
import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.engine.slots.utils.reels.ReelsBean;
import com.gcs.game.engine.slots.utils.reels.ReelsCachePool;
import com.gcs.game.engine.slots.vo.*;
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
public class SlotEngineUtil {

    public static BaseSlotModel getModel(String gameModel) {
        return GameModelFactory.getInstance().getSlotsModel(gameModel);
    }

    public static BaseBonus getBonusModel(String gameModel, String bonusAsset) {
        return GameModelFactory.getInstance().getSlotsBonusModel(gameModel, bonusAsset);
    }

    public static SlotGameFeatureVo initModelFeature(String gameModel, int payback) {
        SlotGameFeatureVo bean = new SlotGameFeatureVo();
        ReelsBean reelsBean = ReelsCachePool.getReels(gameModel, payback);
        if (reelsBean != null) {
            bean.setSlotReels(reelsBean.getReels());
            bean.setSlotReelsWeight(reelsBean.getReelsWeight());
            bean.setSlotFsReels(reelsBean.getFsReels());
            bean.setSlotFsReelsWeight(reelsBean.getFsReelsWeight());
            bean.setInitSlotReelsPosition(reelsBean.getInitReelsPosition());

            BaseSlotModel model = getModel(gameModel);
            if (model != null) {
                bean.setMaxBet(model.maxBetPerLine());
                bean.setMinBet(model.minBetPerLine());
                bean.setMinLine(model.minLines());
                bean.setMaxLine(model.maxLines());
            }
        }
        return bean;
    }

    public static SlotGameFeatureVo initModelFeature(String gameModel, int payback, List<String> otherReelsKeys) {
        SlotGameFeatureVo bean = new SlotGameFeatureVo();
        ReelsBean reelsBean = ReelsCachePool.getReels(gameModel, payback, otherReelsKeys);
        if (reelsBean != null) {
            bean.setSlotReels(reelsBean.getReels());
            bean.setSlotReelsWeight(reelsBean.getReelsWeight());
            bean.setSlotFsReels(reelsBean.getFsReels());
            bean.setSlotFsReelsWeight(reelsBean.getFsReelsWeight());
            bean.setInitSlotReelsPosition(reelsBean.getInitReelsPosition());

            bean.setOtherSlotReelsMap(reelsBean.getOtherReelsMap());
            bean.setOtherSlotReelsWeightMap(reelsBean.getOtherReelsWeightMap());

            BaseSlotModel model = getModel(gameModel);
            if (model != null) {
                bean.setMinBet(model.minBetPerLine());
                bean.setMaxBet(model.maxBetPerLine());
                bean.setMaxLine(model.maxLines());
                bean.setMinLine(model.minLines());
            }
        }
        return bean;
    }

    public static SlotGameLogicBean getDefaultGameSession(String gameModel, int payback, SlotGameFeatureVo modelFeature) throws InvalidGameStateException {
        try {
            SlotGameLogicBean result = new SlotGameLogicBean();
            result.setMmID(gameModel);
            result.setJackpotGroupCode("");
            result.setGamePlayStatus(GameConstant.GAME_STATUS_IDLE);
            result.setPercentage(payback);
            //result.setMathType(GameClass.Slots_243w_3x5.name());
            result.setDenom(1);
            BaseSlotModel model = SlotEngineUtil.getModel(gameModel);
            if (model != null) {
                long betPerLine = model.minBetPerLine();
                long lines = model.minLines();
                result.setBet(betPerLine);
                result.setLines(lines);
                result.setDenom(1);
                long totalBetCredit = model.totalBet(lines, betPerLine);
                result.setSumBetCredit(totalBetCredit);
                result.setSumBetBalance(totalBetCredit);
            }
            if (modelFeature != null) {
                int[] initPositions = modelFeature.getInitSlotReelsPosition();
                if (initPositions != null) {
                    SlotSpinResult baseSpinResult = new SlotSpinResult();
                    baseSpinResult.setSlotReelStopPosition(initPositions);
                    if (model != null) {
                        int[][] reels = modelFeature.getSlotReels();
                        int[] displaySymbols = model.getDisplaySymbols(reels, initPositions);
                        if (displaySymbols != null) {
                            baseSpinResult.setSlotDisplaySymbols(displaySymbols);
                        }
                    }
                    result.setSlotSpinResult(baseSpinResult);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("", e);
            throw new InvalidGameStateException();
        }
    }

    /**
     * game spin start
     *
     * @param gameLogicRequest
     * @param gameLogicMap
     * @param input
     * @param gameLogicCache
     * @param gameModel
     * @param modelFeature
     * @return
     * @throws InvalidGameStateException
     * @throws InvalidBetException
     */
    public static SlotGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input, SlotGameLogicBean gameLogicCache, String gameModel, SlotGameFeatureVo modelFeature) throws InvalidGameStateException, InvalidBetException {
        try {
            int gameStatus = gameLogicCache.getGamePlayStatus();
            if (gameStatus == GameConstant.SLOT_GAME_STATUS_IDLE || gameStatus == GameConstant.SLOT_GAME_STATUS_COMPLETE) {
                log.debug("Game Current Status is {}", gameStatus);
                /*if (gameLogicBean != null) {
                    gameLogicCache.setBetInstanceID(slotGameLogicBean.getBetInstanceID());
                    gameLogicCache.setStepID(slotGameLogicBean.getStepID());
                    gameLogicCache.setSpecialSettingsMap(slotGameLogicBean.getSpecialSettingsMap());
                }*/
                // respin
                if (gameLogicCache.getRespinCountsLeft() > 0) {
                    gameLogicCache.setRespin(true);
                } else {
                    gameLogicCache.setRespin(false);
                }
                //game-start update bet info
                gameLogicCache.setBet(Long.parseLong(gameLogicMap.get("bet").toString()));
                gameLogicCache.setLines(Long.parseLong(gameLogicMap.get("lines").toString()));
                gameLogicCache.setDenom(Long.parseLong(gameLogicMap.get("denom").toString()));
                //gameLogicCache.setJackpotGroupCode(slotGameLogicBean.getJackpotGroupCode());
                gameLogicCache.setLastScenes("slots");
                gameLogicCache.setConsumedSummation(null);
                gameLogicCache.setSumWinCredit(0);
                gameLogicCache.setSumWinBalance(0);
                BaseSlotModel model = SlotEngineUtil.getModel(gameModel);
                /*if (model != null && gameLogicCache.getSlotGameCount() > 1) {
                    BaseMultiSlotReelsModel multiSlotReelsModel = (BaseMultiSlotReelsModel) model;
                    List<SlotSpinResult> spinResultList;
                    if (input != null && input.getInputPosition() != null) {
                        log.debug("spin with input");
                        spinResultList = multiSlotReelsModel.spin4MultiSlot(modelFeature, gameSessionCache, input);
                    } else {
                        log.debug("spin");
                        spinResultList = multiSlotReelsModel.spin4MultiSlot(modelFeature, gameSessionCache);
                    }
                    if (spinResultList != null && !spinResultList.isEmpty()) {
                        log.debug("spin result list is not null");
                        for (SlotSpinResult spinResult : spinResultList) {
                            if (spinResult != null) {
                                spinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_SPIN_IN_BASE_GAME);
                            }
                        }

                        gameLogicCache.setBaseSpinResult(null);
                        gameLogicCache.setFsSpinResults(null);
                        gameLogicCache.setBonusResult(null);
                        gameLogicCache.setBonusResultList(null);
                        gameLogicCache.setBaseSpinResult4MultiSlot(spinResultList);
                        gameLogicCache.setFsSpinResults4MultiSlot(null);

                        computeBaseGameBonus(gameLogicCache, spinResultList);

                        long winCredit = 0;
                        for (int i = 0; i < spinResultList.size(); i++) {
                            winCredit += spinResultList.get(i).getPayAmount();
                        }
                        long winCent = winCredit * gameSessionCache.getDenom();

                        gameSessionCache.setTotalWinCredit(winCredit);
                        gameSessionCache.setTotalWinBalance(winCent);
                    }
                } else*/
                if (model != null) {
                    checkBetInfo(gameLogicCache, model);
                    SlotSpinResult baseSpinResult;
                    if (input != null && input.getInputPosition() != null) {
                        log.debug("spin with input");
                        baseSpinResult = model.spin(modelFeature, gameLogicCache, input);
                    } else {
                        log.debug("spin");
                        baseSpinResult = model.spin(modelFeature, gameLogicCache);
                    }
                    if (baseSpinResult != null) {
                        log.debug("spin result is not null");
                        baseSpinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_SPIN_IN_BASE_GAME);

                        gameLogicCache.setSlotSpinResult(baseSpinResult);
                        gameLogicCache.setSlotFsSpinResults(null);
                        gameLogicCache.setSlotBonusResult(null);
                        gameLogicCache.setSlotBonusResultList(null);
                        gameLogicCache.setSlotSpinResult4Multi(null);
                        gameLogicCache.setSlotFsSpinResults4Multi(null);

                        computeBaseGameBonus(gameLogicCache, baseSpinResult);

                        long denom = gameLogicCache.getDenom();
                        long winCredit = baseSpinResult.getSlotPay();
                        long winCent = winCredit * denom;

                        gameLogicCache.setSumWinCredit(winCredit);
                        gameLogicCache.setSumWinBalance(winCent);
                        gameLogicCache.setPayForCurrentStep(winCredit);
                    }
                }
                /*ProgressiveProcessor.slotGamePlay(gameSessionCache, gameModel, GameConstant.SPIN_TYPE_BASE_GAME);
                if (gameSessionCache.getGameStatus() == GameConstant.SLOT_GAME_STATUS_COMPLETE) {
                    ProgressiveProcessor.computeJackpotBonus(gameSessionCache);
                }*/
            } else {
                throw new InvalidGameStateException();
            }
            return gameLogicCache;
        } catch (InvalidBetException e) {
            log.error("", e);
            throw new InvalidBetException();
        } catch (Exception e) {
            log.error("", e);
            throw new InvalidGameStateException();
        }
    }

    protected static void checkBetInfo(SlotGameLogicBean gameLogicCache, BaseSlotModel model) throws InvalidBetException {
        try {
            long betPerLine = gameLogicCache.getBet();
            if (betPerLine < model.minBetPerLine()) {
                betPerLine = model.minBetPerLine();
            }
            if (betPerLine > model.maxBetPerLine()) {
                betPerLine = model.maxBetPerLine();
            }
            long lines = gameLogicCache.getLines();
            if (lines < model.minLines()) {
                lines = model.minLines();
            }
            if (lines > model.maxLines()) {
                lines = model.maxLines();
            }
            gameLogicCache.setBet(betPerLine);
            gameLogicCache.setLines(lines);
            long totalBetCredit = model.totalBet(gameLogicCache.getLines(), gameLogicCache.getBet()) * gameLogicCache.getSlotGameCount();
            gameLogicCache.setSumBetCredit(totalBetCredit);
            gameLogicCache.setSumBetBalance(totalBetCredit * gameLogicCache.getDenom());
            gameLogicCache.setBetForCurrentStep(totalBetCredit);
        } catch (Exception e) {
            log.debug("Invalid Bet Update");
            throw new InvalidBetException();
        }

    }

    protected static void computeBaseGameBonus(SlotGameLogicBean gameLogicBean, SlotSpinResult spinResult) {
        int newGameStatus;
        String bonusAsset = "";
        String nextScene = "slots";
        int freeSpinHitTimes = 0;
        int respinTimes = 0;
        List<String> nextScenes = spinResult.getNextScenes();
        if (nextScenes == null || nextScenes.isEmpty()) {
            log.debug("game complete");
            newGameStatus = GameConstant.SLOT_GAME_STATUS_COMPLETE;
            gameLogicBean.setHitSceneLeftList(null);
        } else {
            nextScene = nextScenes.get(0);
            if ("freeSpin".equalsIgnoreCase(nextScene)) {
                newGameStatus = GameConstant.SLOT_GAME_STATUS_TRIGGER_FREESPIN;
                if (spinResult.isTriggerRespin()) {
                    log.debug("hit re-Spin");
                    respinTimes = spinResult.getTriggerRespinCounts();
                    freeSpinHitTimes = spinResult.getTriggerRespinCounts();
                } else {
                    log.debug("hit Free Spin");
                    freeSpinHitTimes = spinResult.getTriggerFsCounts();
                }
            } else {
                log.debug("hit Bonus");
                bonusAsset = nextScene;
                newGameStatus = GameConstant.SLOT_GAME_STATUS_TRIGGER_BONUS;
            }
            if (nextScenes.size() > 1) {
                List<String> sceneLeft = new ArrayList<>();
                for (int i = 1; i < nextScenes.size(); i++) {
                    sceneLeft.add(nextScenes.get(i));
                }
                gameLogicBean.setHitSceneLeftList(sceneLeft);
            } else {
                gameLogicBean.setHitSceneLeftList(null);
            }
        }
        gameLogicBean.setRespin(false);
        gameLogicBean.setGamePlayStatus(newGameStatus);
        gameLogicBean.setSlotBsAsset(bonusAsset);
        gameLogicBean.setNextScenes(nextScene);
        gameLogicBean.setFsCountLeft(freeSpinHitTimes);
        gameLogicBean.setRespinCountsLeft(respinTimes);
        if (freeSpinHitTimes > 0) {
            gameLogicBean.setFsHitCounts(new int[]{freeSpinHitTimes});
        } else {
            gameLogicBean.setFsHitCounts(null);
        }
    }

    protected static void computeBaseGameBonus(SlotGameLogicBean gameLogicBean, List<SlotSpinResult> spinResultList) {
        int newGameStatus = GameConstant.SLOT_GAME_STATUS_COMPLETE;
        String bonusAsset = "";
        String nextScene = "slots";
        int freeSpinHitTimes = 0;
        int respinTimes = 0;

        boolean triggerRespin = false;
        boolean triggerFreeSpin = false;
        List<String> nextScenes = new ArrayList<>();
        for (SlotSpinResult spinResult : spinResultList) {
            List<String> tempNextScenes = spinResult.getNextScenes();
            if (tempNextScenes != null && tempNextScenes.size() > 0) {
                for (String scene : tempNextScenes) {
                    if ("freeSpin".equalsIgnoreCase(scene)) {
                        if (spinResult.isTriggerRespin()) {
                            log.debug("hit re-Spin");
                            triggerRespin = true;
                            if (respinTimes < spinResult.getTriggerRespinCounts()) {
                                respinTimes = spinResult.getTriggerRespinCounts();
                            }
                            freeSpinHitTimes = spinResult.getTriggerRespinCounts();
                        } else {
                            triggerFreeSpin = true;
                            log.debug("hit Free Spin");
                            freeSpinHitTimes = spinResult.getTriggerFsCounts();
                        }
                    } else {
                        if (!nextScenes.contains(scene)) {
                            nextScenes.add(scene);
                        }
                    }

                }
            }
        }
        if (triggerFreeSpin) {
            nextScenes.add(0, "freeSpin");
        }
        if (triggerRespin) {
            nextScenes.add(0, "freeSpin");
        }

        if (nextScenes != null && nextScenes.size() > 0) {
            nextScene = nextScenes.get(0);
            if ("freeSpin".equalsIgnoreCase(nextScene)) {
                newGameStatus = GameConstant.SLOT_GAME_STATUS_TRIGGER_FREESPIN;
            } else {
                bonusAsset = nextScene;
                newGameStatus = GameConstant.SLOT_GAME_STATUS_TRIGGER_BONUS;
            }
            if (nextScenes.size() > 1) {
                List<String> sceneLeft = new ArrayList<>();
                for (int i = 1; i < nextScenes.size(); i++) {
                    sceneLeft.add(nextScenes.get(i));
                }
                gameLogicBean.setHitSceneLeftList(sceneLeft);
            } else {
                gameLogicBean.setHitSceneLeftList(null);
            }
        }

        if (newGameStatus == GameConstant.SLOT_GAME_STATUS_COMPLETE) {
            log.debug("game complete");
            gameLogicBean.setHitSceneLeftList(null);
        }
        gameLogicBean.setRespin(false);
        gameLogicBean.setGamePlayStatus(newGameStatus);
        gameLogicBean.setSlotBsAsset(bonusAsset);
        gameLogicBean.setNextScenes(nextScene);
        gameLogicBean.setFsCountLeft(freeSpinHitTimes);
        gameLogicBean.setRespinCountsLeft(respinTimes);
        if (freeSpinHitTimes > 0) {
            gameLogicBean.setFsHitCounts(new int[]{freeSpinHitTimes});
        } else {
            gameLogicBean.setFsHitCounts(null);
        }
    }

    /**
     * game progress
     *
     * @param gameLogicRequest
     * @param playerInput
     * @param gameLogicCache
     * @param gameModel
     * @param modelFeature
     * @param payback
     * @param input
     * @return
     * @throws InvalidPlayerInputException
     * @throws InvalidGameStateException
     */
    public static SlotGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, PlayerInputInfo playerInput, SlotGameLogicBean gameLogicCache, String gameModel, SlotGameFeatureVo modelFeature, int payback, InputInfo input) throws InvalidPlayerInputException, InvalidGameStateException {
        try {
            if (gameLogicCache != null) {
                if (playerInput != null && gameLogicRequest != null) {
                    SlotGameLogicBean slotsGameLogicBean = (SlotGameLogicBean) gameLogicRequest;
                    int gameStatus = gameLogicCache.getGamePlayStatus();
                    int reqGameStatus = slotsGameLogicBean.getGamePlayStatus();
                    if (gameStatus == reqGameStatus && gameStatus != GameConstant.SLOT_GAME_STATUS_COMPLETE) {
                        //gameLogicCache.setBetInstanceID(slotsGameLogicBean.getBetInstanceID());
                        //gameLogicCache.setStepID(slotsGameLogicBean.getStepID());
                        //gameLogicCache.setSpecialSettingsMap(gameLogicRequest.getSpecialSettingsMap());
                        gameLogicCache.setBetForCurrentStep(0);
                        int newGameStatus = reqGameStatus;
                        boolean bonusComplete = false;
                        boolean freeSpinComplete = false;
                        long winCredit = 0L;
                        String currentBonusAsset = "";
                        if (reqGameStatus == GameConstant.SLOT_GAME_STATUS_TRIGGER_BONUS) {
                            currentBonusAsset = gameLogicCache.getSlotBsAsset();
                            BaseBonus bonusModel = getBonusModel(gameModel, currentBonusAsset);
                            if (gameLogicCache.getSlotBonusResult() == null
                                    || gameLogicCache.getSlotBonusResult().getBonusPlayStatus() == GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE) {
                                // start bonus
                                SlotBonusResult bonusResult;
                                if (input != null) {
                                    log.debug("bonus with input");
                                    bonusResult = bonusModel.computeBonusStart(gameLogicCache, payback, input);
                                } else {
                                    log.debug("bonus");
                                    bonusResult = bonusModel.computeBonusStart(gameLogicCache, payback);
                                }
                                if (bonusResult != null) {
                                    // winCredit = bonusResult.getPayForPick();
                                    gameLogicCache.setSlotBonusResult(bonusResult);
                                }
                            } else {
                                SlotBonusResult bonus = gameLogicCache.getSlotBonusResult();
                                // check input
                                bonusModel.checkInput4BonusPick(gameLogicCache, playerInput, bonus);
                                SlotBonusResult bonusResult = bonusModel.computeBonusPick(gameLogicCache, playerInput, bonus);
                                if (bonusResult != null) {
                                    gameLogicCache.setSlotBonusResult(bonusResult);
                                    if (bonusResult.getBonusPlayStatus() == GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE) {
                                        bonusComplete = true;
                                        gameLogicCache.setLastScenes(currentBonusAsset);
                                        winCredit = bonusResult.getTotalPay();

                                        List<SlotBonusResult> bonusResultList = gameLogicCache.getSlotBonusResultList();
                                        if (bonusResultList == null) {
                                            bonusResultList = new ArrayList<>();
                                        }
                                        bonusResultList.add(bonusResult.clone());
                                        gameLogicCache.setSlotBonusResultList(bonusResultList);

                                       /* ResponseGamePlayBean progressive = gameSessionCache.getProgressiveInfo();
                                        if (!"jackpot".equalsIgnoreCase(currentBonusAsset) && progressive != null && progressive.getPendingJackpotID() <= 0) {
                                            ProgressiveProcessor.slotGamePlay(gameSessionCache, gameModel, GameConstant.SPIN_TYPE_BONUS);
                                        }*/
                                    }
                                }
                            }

                            long denom = gameLogicCache.getDenom();
                            long winBalance = winCredit * denom;
                            gameLogicCache.setSumWinBalance(gameLogicCache.getSumWinBalance() + winBalance);
                            gameLogicCache.setSumWinCredit(gameLogicCache.getSumWinCredit() + winCredit);
                            gameLogicCache.setPayForCurrentStep(winCredit);

                        } else if (reqGameStatus == GameConstant.SLOT_GAME_STATUS_TRIGGER_FREESPIN) {
                            int fsSize;
                            int requestFsSize;
                            if (gameLogicCache.getSlotGameCount() == 1) {
                                fsSize = gameLogicCache.getSlotFsSpinResults() == null ? 0 : gameLogicCache.getSlotFsSpinResults().size();
                                requestFsSize = slotsGameLogicBean.getSlotFsSpinResults() == null ? 0 : slotsGameLogicBean.getSlotFsSpinResults().size();
                            } else {
                                fsSize = gameLogicCache.getSlotFsSpinResults4Multi() == null ? 0 : gameLogicCache.getSlotFsSpinResults4Multi().get(0).size();
                                requestFsSize = slotsGameLogicBean.getSlotFsSpinResults4Multi() == null ? 0 : slotsGameLogicBean.getSlotFsSpinResults4Multi().get(0).size();
                            }
                            if (fsSize != requestFsSize) {
                                throw new InvalidGameStateException();
                            }
                            freeSpinComplete = gameProgress4FreeSpin(gameLogicCache, gameModel, modelFeature, input);

                           /* ResponseGamePlayBean progressive = gameLogicCache.getProgressiveInfo();
                            if (progressive != null && progressive.getPendingJackpotID() <= 0) {
                                if (gameSessionCache.isRespin()) {
                                    ProgressiveProcessor.slotGamePlay(gameLogicCache, gameModel, SlotEngineConstant.SPIN_TYPE_RESPIN);
                                } else {
                                    ProgressiveProcessor.slotGamePlay(gameLogicCache, gameModel, SlotEngineConstant.SPIN_TYPE_FREE_SPIN);
                                }
                            }*/
                        }

                        if (freeSpinComplete || bonusComplete) {
                            String bonusAsset = "";
                            String nextScene = "slots";
                            int freespinHitTimes = 0;
                            List<String> nextScenes = gameLogicCache.getHitSceneLeftList();
                            if (nextScenes == null || nextScenes.isEmpty()) {
                                newGameStatus = GameConstant.SLOT_GAME_STATUS_COMPLETE;
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
                                    newGameStatus = GameConstant.SLOT_GAME_STATUS_TRIGGER_FREESPIN;
                                } else {
                                    log.debug("hit Bonus");
                                    bonusAsset = nextScene;
                                    newGameStatus = GameConstant.SLOT_GAME_STATUS_TRIGGER_BONUS;
                                }
                            }
                            gameLogicCache.setNextScenes(nextScene);
                            gameLogicCache.setSlotBsAsset(bonusAsset);
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
                        gameLogicCache.setGamePlayStatus(newGameStatus);
                        /*if (!"jackpot".equalsIgnoreCase(currentBonusAsset)
                                && gameLogicCache.getGamePlayStatus() == GameConstant.SLOT_GAME_STATUS_COMPLETE) {
                            ProgressiveProcessor.computeJackpotBonus(gameSessionCache);
                        }*/
                    } else {
                        throw new InvalidGameStateException();
                    }
                } else {
                    throw new InvalidPlayerInputException();
                }
            } else {
                throw new InvalidGameStateException();
            }
            return gameLogicCache;
        } catch (InvalidPlayerInputException e) {
            throw new InvalidPlayerInputException();
        } catch (Exception e) {
            log.error("", e);
            throw new InvalidGameStateException();
        }
    }

    private static int getFreeSpinHitTimes(SlotGameLogicBean gameLogicCache) {
        int hitFreespinTimes = 0;
        if (gameLogicCache != null && gameLogicCache.getSlotGameCount() > 1) {
            hitFreespinTimes = gameLogicCache.getSlotSpinResult4Multi().get(0).getTriggerFsCounts();
        } else if (gameLogicCache != null) {
            hitFreespinTimes = gameLogicCache.getSlotSpinResult().getTriggerFsCounts();
        }
        return hitFreespinTimes;
    }

    protected static boolean gameProgress4FreeSpin(SlotGameLogicBean gameLogicCache, String gameModel, SlotGameFeatureVo modelFeature, InputInfo input) {
        boolean freeSpinComplete = false;
        // free spin
        if (gameLogicCache.getRespinCountsLeft() > 0) {
            gameLogicCache.setRespin(true);
        } else {
            gameLogicCache.setRespin(false);
        }
        if (gameLogicCache.isRespin()) {
            gameLogicCache.setRespinCountsLeft(gameLogicCache.getRespinCountsLeft() - 1);
        }

        BaseSlotModel model = SlotEngineUtil.getModel(gameModel);
        boolean isBaseGameRespin = false;
        /*if (model != null && gameLogicCache.getSlotGameCount() > 1) {
            BaseMultiSlotReelsModel multiSlotReelsModel = (BaseMultiSlotReelsModel) model;
            List<SlotSpinResult> spinResultList;
            if (input != null && input.getInputPosition() != null) {
                log.debug("spin with input");
                spinResultList = multiSlotReelsModel.spinInFreeSpin4MultiSlot(modelFeature, gameLogicCache, input);
            } else {
                log.debug("spin");
                spinResultList = multiSlotReelsModel.spinInFreeSpin4MultiSlot(modelFeature, gameLogicCache);
            }
            if (spinResultList != null && !spinResultList.isEmpty()) {
                log.debug("spin result list is not null");
                if (gameLogicCache.isRespin()) {
                    boolean isRespinInBaseGame = model.isRespinInBaseGame(gameLogicCache);
                    for (SlotSpinResult spinResult : spinResultList) {
                        if (spinResult != null) {
                            if (isRespinInBaseGame) {
                                isBaseGameRespin = true;
                                spinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_RESPIN_IN_BASE_GAME);
                            } else {
                                spinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_RESPIN_IN_FREE_SPIN);
                            }
                        }
                    }
                } else {
                    for (SlotSpinResult spinResult : spinResultList) {
                        if (spinResult != null) {
                            spinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_SPIN_IN_FREE_SPIN);
                        }
                    }
                }

                List<List<SlotSpinResult>> fsSpinResults = gameLogicCache.getFsSpinResults4MultiSlot();
                if (fsSpinResults == null) {
                    fsSpinResults = new ArrayList<>();
                    for (int i = 0; i < gameLogicCache.getSlotGameCount(); i++) {
                        List<SlotSpinResult> fsList4One = new ArrayList<>();
                        fsSpinResults.add(fsList4One);
                    }
                    gameLogicCache.setFsSpinResults4MultiSlot(fsSpinResults);
                }

                for (int i = 0; i < spinResultList.size(); i++) {
                    SlotSpinResult spinResult = spinResultList.get(i);
                    List<SlotSpinResult> fsList4One = fsSpinResults.get(i);
                    fsList4One.add(spinResult);
                }

                freeSpinComplete = computeFreeSpinGame(gameLogicCache, spinResultList);

                long winCredit = 0;
                for (int i = 0; i < spinResultList.size(); i++) {
                    if (spinResultList.get(i) != null) {
                        winCredit += spinResultList.get(i).getSlotPay();
                    }
                }
                long winCent = winCredit * gameLogicCache.getDenom();
                gameLogicCache.setSumWinCredit(gameLogicCache.getSumWinCredit() + winCredit);
                gameLogicCache.setSumWinBalance(gameLogicCache.getSumWinBalance() + winCent);
            }
        } else */
        if (model != null) {
            SlotSpinResult spinResult;
            if (input != null && input.getInputPosition() != null) {
                log.debug("Fs spin with input");
                spinResult = model.spinInFreeSpin(modelFeature, gameLogicCache, input);
            } else {
                log.debug("Fs spin");
                spinResult = model.spinInFreeSpin(modelFeature, gameLogicCache);
            }
            if (spinResult != null) {
                if (gameLogicCache.isRespin()) {
                    boolean isRespinInBaseGame = model.isRespinInBaseGame(gameLogicCache);
                    if (isRespinInBaseGame) {
                        isBaseGameRespin = true;
                        spinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_RESPIN_IN_BASE_GAME);
                    } else {
                        spinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_RESPIN_IN_FREE_SPIN);
                    }
                } else {
                    spinResult.setSpinType(SlotEngineConstant.SPIN_TYPE_SPIN_IN_FREE_SPIN);
                }

                if (gameLogicCache.getSlotFsSpinResults() == null) {
                    List<SlotSpinResult> fsSpinResults = new ArrayList<>();
                    gameLogicCache.setSlotFsSpinResults(fsSpinResults);
                }
                gameLogicCache.getSlotFsSpinResults().add(spinResult);

                if (model != null && model instanceof IFsSceneComputer) {
                    IFsSceneComputer freeSpinSceneComputer = (IFsSceneComputer) model;
                    freeSpinComplete = freeSpinSceneComputer.computeNextSceneWhileTriggerBonusInRespin(gameLogicCache, spinResult);
                } else {
                    freeSpinComplete = computeFreeSpinGame(gameLogicCache, spinResult);
                }

                long winCredit = spinResult.getSlotPay();
                long denom = gameLogicCache.getDenom();
                long winBalance = winCredit * denom;
                gameLogicCache.setSumWinCredit(gameLogicCache.getSumWinCredit() + winCredit);
                gameLogicCache.setSumWinBalance(gameLogicCache.getSumWinBalance() + winBalance);
                gameLogicCache.setPayForCurrentStep(winCredit);
            }
        }
        if (freeSpinComplete && !isBaseGameRespin) {
            gameLogicCache.setLastScenes("freeSpin");
        }
        return freeSpinComplete;

    }

    protected static boolean computeFreeSpinGame(SlotGameLogicBean gameLogicCache, SlotSpinResult spinResult) {
        boolean freeSpinComplete = false;

        int freeSpinLeft = gameLogicCache.getFsCountLeft();
        freeSpinLeft--;
        if (spinResult.isTriggerFs()) {
            // trigger respin
            if (spinResult.isTriggerRespin()) {
                freeSpinLeft = computeRespinInFreeSpin(gameLogicCache, spinResult, freeSpinLeft);
            }
            // re-trigger free spin
            freeSpinLeft += spinResult.getTriggerFsCounts();
            int[] freeSpinHitTimes = gameLogicCache.getFsHitCounts();
            int[] newFreeSpinHitTimes = new int[freeSpinHitTimes.length + 1];
            for (int i = 0; i < freeSpinHitTimes.length; i++) {
                newFreeSpinHitTimes[i] = freeSpinHitTimes[i];
            }
            newFreeSpinHitTimes[freeSpinHitTimes.length] = spinResult.getTriggerFsCounts();
            gameLogicCache.setFsHitCounts(newFreeSpinHitTimes);
        } else if (spinResult.isTriggerRespin()) {
            // support multi-times respin.
            freeSpinLeft = computeRespinInFreeSpin(gameLogicCache, spinResult, freeSpinLeft);
        } else if (spinResult.isTriggerBonus()) {
            // hit bonus in respin
            List<String> nextScenes = gameLogicCache.getHitSceneLeftList();
            if (nextScenes == null || nextScenes.isEmpty()) {
                nextScenes = new ArrayList<>();
            }
            if (spinResult.getNextScenes() != null && spinResult.getNextScenes().size() > 0) {
                nextScenes.add(0, spinResult.getNextScenes().get(0));
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

    public static int computeRespinInFreeSpin(SlotGameLogicBean gameLogicCache, SlotSpinResult spinResult, int freeSpinLeft) {
        if (gameLogicCache.getRespinCountsLeft() > 0) {
            gameLogicCache.setRespinCountsLeft(gameLogicCache.getRespinCountsLeft() + spinResult.getTriggerRespinCounts());
        } else {
            gameLogicCache.setRespinCountsLeft(spinResult.getTriggerRespinCounts());
        }
        freeSpinLeft += spinResult.getTriggerRespinCounts();
        int[] freeSpinHitTimes = gameLogicCache.getFsHitCounts();
        int[] newFreeSpinHitTimes = new int[freeSpinHitTimes.length + 1];
        for (int i = 0; i < freeSpinHitTimes.length; i++) {
            newFreeSpinHitTimes[i] = freeSpinHitTimes[i];
        }
        newFreeSpinHitTimes[freeSpinHitTimes.length] = spinResult.getTriggerRespinCounts();
        gameLogicCache.setFsHitCounts(newFreeSpinHitTimes);
        return freeSpinLeft;
    }

    protected static boolean computeFreeSpinGame(SlotGameLogicBean gameSessionCache, List<SlotSpinResult> spinResultList) {
        boolean freeSpinComplete = false;

        int freeSpinLeft = gameSessionCache.getFsCountLeft();
        freeSpinLeft--;

        boolean triggerRespin = false;
        int respinTimes = 0;

        for (SlotSpinResult spinResult : spinResultList) {
            if (spinResult == null) {
                continue;
            }
            // TODO support multi-times respin.
            if (spinResult.isTriggerRespin()) {
                log.debug("hit re-Spin in FS");
                triggerRespin = true;
                if (respinTimes < spinResult.getTriggerRespinCounts()) {
                    respinTimes = spinResult.getTriggerRespinCounts();
                }
            } else if (spinResult.isTriggerBonus()) {
                // hit bonus in respin
                List<String> nextScenes = gameSessionCache.getHitSceneLeftList();
                if (nextScenes == null || nextScenes.isEmpty()) {
                    nextScenes = new ArrayList<>();
                }
                if (spinResult.getNextScenes() != null && spinResult.getNextScenes().size() > 0) {
                    String newNext = spinResult.getNextScenes().get(0);
                    if (!nextScenes.contains(newNext)) {
                        nextScenes.add(0, newNext);
                    }
                }
                gameSessionCache.setHitSceneLeftList(nextScenes);
            } else {
                // TODO no bonus/freespin in freespin
            }
        }
        if (triggerRespin) {
            gameSessionCache.setRespinCountsLeft(respinTimes);

            freeSpinLeft += respinTimes;
            int[] freeSpinHitTimesArray = gameSessionCache.getFsHitCounts();
            int[] newFreeSpinHitTimes = new int[freeSpinHitTimesArray.length + 1];
            for (int i = 0; i < freeSpinHitTimesArray.length; i++) {
                newFreeSpinHitTimes[i] = freeSpinHitTimesArray[i];
            }
            newFreeSpinHitTimes[freeSpinHitTimesArray.length] = respinTimes;
            gameSessionCache.setFsHitCounts(newFreeSpinHitTimes);
        } else if (freeSpinLeft <= 0) {
            freeSpinComplete = true;
        }
        gameSessionCache.setFsCountLeft(freeSpinLeft);
        return freeSpinComplete;
    }

}
