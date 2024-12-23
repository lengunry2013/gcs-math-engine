package com.gcs.game.engine.poker;

import com.gcs.game.engine.IGameEngine;

import com.gcs.game.engine.poker.utils.PokerEngineUtil;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.engine.slots.vo.SlotGameFeatureVo;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PokerGameEngine implements IGameEngine {
    protected int payback = 0;
    protected String mathModel = "";

    protected PokerGameLogicBean gameLogicBean = null;
    protected Map<String, String> engineContextMap = null;

    public PokerGameEngine() {

    }

    public PokerGameEngine(int payback, String mathModel) {
        this.payback = payback;
        this.mathModel = mathModel;
    }

    public PokerGameLogicBean init(BaseGameLogicBean gameLogicBean) throws InvalidGameStateException {
        this.gameLogicBean = (PokerGameLogicBean) gameLogicBean;
        if (gameLogicBean == null) {
            this.gameLogicBean = getDefaultGameLogicData();
        }
        if (engineContextMap == null) {
            this.engineContextMap = new ConcurrentHashMap<>();
        }
        return this.gameLogicBean;
    }

    //not game feature
    public SlotGameFeatureVo loadGameFeature() {
        return null;
    }

    public PokerGameLogicBean getDefaultGameLogicData() throws InvalidGameStateException {
        return PokerEngineUtil.getDefaultGameLogicData(this.mathModel, this.payback);
    }

    public PokerGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input) throws InvalidGameStateException, InvalidBetException {
        return PokerEngineUtil.gameStart(gameLogicRequest, gameLogicMap, input, this.gameLogicBean, this.mathModel, this.engineContextMap);
    }

    public PokerGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextRequest, InputInfo input) throws InvalidPlayerInputException, InvalidGameStateException {
        return PokerEngineUtil.gameProgress(gameLogicRequest, gameLogicMap, playerInput, engineContextRequest, input, this.gameLogicBean, this.mathModel, this.engineContextMap);
    }

    public Map getEngineContext() {
        return this.engineContextMap;
    }

}
