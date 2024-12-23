package com.gcs.game.engine.keno.utils;

import com.alibaba.fastjson.JSON;
import com.gcs.game.engine.GameModelFactory;
import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoResult;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class KenoEngineUtil {

    public static BaseKenoModel getModel(String gameModel) {
        return GameModelFactory.getInstance().getKenoModel(gameModel);
    }

    public static KenoGameLogicBean getDefaultGameLogicData(String mmID, int payback) throws InvalidGameStateException {
        try {
            KenoGameLogicBean result = new KenoGameLogicBean();
            result.setMmID(mmID);
            result.setJackpotGroupCode("");
            result.setGamePlayStatus(GameConstant.GAME_STATUS_IDLE);
            result.setDenom(1);
            result.setPercentage(payback);
            BaseKenoModel model = getModel(mmID);
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

    public static KenoGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input, KenoGameLogicBean gameLogicCache, String mathModel) throws InvalidBetException, InvalidGameStateException {
        try {
            //new game cycle
            setGameLogicRequest(gameLogicMap, gameLogicCache);
            int requestGameStatus = gameLogicCache.getGamePlayStatus();
            log.debug("Game Current Request Status is {}", requestGameStatus);
            //deal button
            if (requestGameStatus == GameConstant.GAME_STATUS_IDLE || requestGameStatus == GameConstant.GAME_STATUS_COMPLETE) {
                log.debug("Game spin");
                BaseKenoModel model = getModel(mathModel);
                if (model != null) {
                    checkBetInfo(gameLogicCache, model);
                    //TODO input
                    KenoResult kenoResult = model.spin(gameLogicCache, input);

                    gameLogicCache.setKenoResult(kenoResult);
                    gameLogicCache.setKenoFsResult(null);

                    computeKenoGameBonus(gameLogicCache, kenoResult);

                    long totalWin = kenoResult.getKenoPay();
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

    private static void computeKenoGameBonus(KenoGameLogicBean gameLogicCache, KenoResult kenoResult) {
        int newGameStatus = 0;
        String nextScene = "slots";
        int freeSpinHitTimes = 0;
        int respinTimes = 0;
        List<String> nextScenes = kenoResult.getNextScenes();
        if (nextScenes == null || nextScenes.isEmpty()) {
            log.debug("Game Complete");
            newGameStatus = GameConstant.GAME_STATUS_COMPLETE;
            gameLogicCache.setHitSceneLeftList(null);
        } else {
            nextScene = nextScenes.get(0);
            if ("freeSpin".equalsIgnoreCase(nextScene)) {
                newGameStatus = GameConstant.KENO_GAME_STATUS_TRIGGER_FREESPIN;
                if (kenoResult.isTriggerRespin()) {
                    log.debug("hit re-Spin");
                    respinTimes = kenoResult.getTriggerRespinCounts();
                    freeSpinHitTimes = kenoResult.getTriggerRespinCounts();
                } else {
                    log.debug("hit Free Spin");
                    freeSpinHitTimes = kenoResult.getTriggerFsCounts();
                }
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
        gameLogicCache.setNextScenes(nextScene);
        gameLogicCache.setFsCountLeft(freeSpinHitTimes);
        gameLogicCache.setRespinCountsLeft(respinTimes);
        if (freeSpinHitTimes > 0) {
            gameLogicCache.setFsHitCounts(new int[]{freeSpinHitTimes});
        } else {
            gameLogicCache.setFsHitCounts(null);
        }
    }

    public static KenoGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextRequest, InputInfo input, KenoGameLogicBean gameLogicCache, String mathModel) throws InvalidGameStateException {
        try {
            //front request HoldPosition info
            KenoGameLogicBean gameLogicBean = (KenoGameLogicBean) gameLogicRequest;
            int reqGameStatus = gameLogicBean.getGamePlayStatus();
            int gameStatus = gameLogicCache.getGamePlayStatus();
            if (reqGameStatus == gameStatus && reqGameStatus != GameConstant.GAME_STATUS_COMPLETE) {
                log.debug("Game Current Request Status is {}", reqGameStatus);
                gameLogicCache.setBetForCurrentStep(0);
                int newGameStatus = reqGameStatus;
                boolean freeSpinComplete = false;
                long winCredit = 0L;
                BaseKenoModel model = getModel(mathModel);
                if (model != null) {
                    if (reqGameStatus == GameConstant.POKER_GAME_STATUS_TRIGGER_FREESPIN) {
                        int fsSize;
                        int requestFsSize;
                        fsSize = gameLogicCache.getKenoFsResult() == null ? 0 : gameLogicCache.getKenoFsResult().size();
                        requestFsSize = gameLogicBean.getKenoFsResult() == null ? 0 : gameLogicBean.getKenoFsResult().size();
                        if (fsSize != requestFsSize) {
                            throw new InvalidGameStateException();
                        }
                        freeSpinComplete = kenoProgress4FreeSpin(gameLogicCache, model, input);
                    }

                    if (freeSpinComplete) {
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
                                freespinHitTimes = gameLogicCache.getKenoResult().getTriggerFsCounts();
                                newGameStatus = GameConstant.KENO_GAME_STATUS_TRIGGER_FREESPIN;
                            }
                        }
                        gameLogicCache.setNextScenes(nextScene);
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

    private static boolean kenoProgress4FreeSpin(KenoGameLogicBean gameLogicCache, BaseKenoModel model, InputInfo input) {
        //freespin keno
        boolean freeSpinComplete = false;
        boolean isBaseGameRespin = false;
        KenoResult kenoResult;
        log.debug("Fs spin");
        kenoResult = model.spinInFs(gameLogicCache, input);
        if (kenoResult != null) {
            //TODO respin
            freeSpinComplete = computeFsKeno(gameLogicCache, kenoResult);

            if (gameLogicCache.getKenoFsResult() == null) {
                List<KenoResult> fsKenoResults = new ArrayList<>();
                gameLogicCache.setKenoFsResult(fsKenoResults);
            }
            gameLogicCache.getKenoFsResult().add(kenoResult);

            long winCredit = kenoResult.getKenoPay();
            gameLogicCache.setPayForCurrentStep(0);
            if (winCredit > 0) {
                long totalWin = winCredit + gameLogicCache.getSumWinCredit();
                gameLogicCache.setSumWinCredit(totalWin);
                gameLogicCache.setSumWinBalance(totalWin * gameLogicCache.getDenom());
                gameLogicCache.setPayForCurrentStep(winCredit);
            }
        }
        if (freeSpinComplete && !isBaseGameRespin) {
            gameLogicCache.setLastScenes("freeSpin");
        }
        return freeSpinComplete;
    }

    private static boolean computeFsKeno(KenoGameLogicBean gameLogicCache, KenoResult kenoResult) {
        boolean freeSpinComplete = false;

        int freeSpinLeft = gameLogicCache.getFsCountLeft();
        freeSpinLeft--;
        if (kenoResult.isTriggerFs()) {
            /*// trigger respin
            if (pokerResult.isTriggerRespin()) {
                freeSpinLeft = computeRespinInFreeSpin(gameLogicCache, kenoResult, freeSpinLeft);
            }*/
            // re-trigger free spin
            freeSpinLeft += kenoResult.getTriggerFsCounts();
            int[] freeSpinHitTimes = gameLogicCache.getFsHitCounts();
            int[] newFreeSpinHitTimes = new int[freeSpinHitTimes.length + 1];
            for (int i = 0; i < freeSpinHitTimes.length; i++) {
                newFreeSpinHitTimes[i] = freeSpinHitTimes[i];
            }
            newFreeSpinHitTimes[freeSpinHitTimes.length] = kenoResult.getTriggerFsCounts();
            gameLogicCache.setFsHitCounts(newFreeSpinHitTimes);
        /*} else if (pokerResult.isTriggerRespin()) {
            // support multi-times respin.
            freeSpinLeft = computeRespinInFreeSpin(gameLogicCache, pokerResult, freeSpinLeft);*/
        } else if (kenoResult.isTriggerBonus()) {
            // hit bonus in respin
            List<String> nextScenes = gameLogicCache.getHitSceneLeftList();
            if (nextScenes == null || nextScenes.isEmpty()) {
                nextScenes = new ArrayList<>();
            }
            if (kenoResult.getNextScenes() != null && kenoResult.getNextScenes().size() > 0) {
                nextScenes.add(0, kenoResult.getNextScenes().get(0));
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


    private static void setGameLogicRequest(Map gameLogicMap, KenoGameLogicBean gameLogicCache) throws InvalidGameStateException, InvalidBetException {
        if (gameLogicMap != null && !gameLogicMap.isEmpty()) {
            gameLogicCache.setBet(Long.parseLong(gameLogicMap.get("bet").toString()));
            gameLogicCache.setLines(Long.parseLong(gameLogicMap.get("lines").toString()));
            gameLogicCache.setDenom(Long.parseLong(gameLogicMap.get("denom").toString()));
            gameLogicCache.setLastScenes("slots");
            List<Integer> selectNumbers = JSON.parseArray(gameLogicMap.get("selectNumbers").toString(), Integer.class);
            if (selectNumbers == null) {
                throw new InvalidGameStateException();
            }
            KenoResult kenoResult = new KenoResult();
            kenoResult.setSelectNumbers(selectNumbers);
            gameLogicCache.setKenoResult(kenoResult);
        } else {
            throw new InvalidGameStateException();
        }
    }

    protected static void checkBetInfo(KenoGameLogicBean gameLogicCache, BaseKenoModel model) throws InvalidBetException {
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
            List<Integer> selectNumbers = gameLogicCache.getKenoResult().getSelectNumbers();
            if (selectNumbers.size() > model.maxSelectNumbersCount() || selectNumbers.size() < model.minSelectNumbersCount()) {
                log.debug("Invalid Select Numbers");
                throw new InvalidBetException();
            }
            gameLogicCache.setSumBetCredit(totalBetCredit);
            gameLogicCache.setSumBetBalance(totalBetCredit * gameLogicCache.getDenom());
            gameLogicCache.setBetForCurrentStep(totalBetCredit);
        } catch (Exception e) {
            log.debug("Invalid Bet Update");
            throw new InvalidBetException();
        }
    }

}
