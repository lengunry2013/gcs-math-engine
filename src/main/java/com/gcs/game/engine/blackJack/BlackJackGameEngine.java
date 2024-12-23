package com.gcs.game.engine.blackJack;

import com.gcs.game.engine.IGameEngine;
import com.gcs.game.engine.blackJack.utils.BlackJackEngineUtil;
import com.gcs.game.engine.slots.vo.SlotGameFeatureVo;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.engine.blackJack.vo.BlackJackGameLogicBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BlackJackGameEngine implements IGameEngine {
    protected int payback = 0;
    protected String mathModel = "";

    protected BlackJackGameLogicBean gameLogicBean = null;
    protected Map<String, String> engineContextMap = null;

    public BlackJackGameEngine() {

    }

    public BlackJackGameEngine(int payback, String mathModel) {
        this.payback = payback;
        this.mathModel = mathModel;
    }

    public BlackJackGameLogicBean init(BaseGameLogicBean gameLogicBean) throws InvalidGameStateException {
        this.gameLogicBean = (BlackJackGameLogicBean) gameLogicBean;
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

    public BlackJackGameLogicBean getDefaultGameLogicData() throws InvalidGameStateException {
        return BlackJackEngineUtil.getDefaultGameLogicData(this.mathModel, this.payback);
    }

    public BlackJackGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input) throws InvalidGameStateException, InvalidBetException {
        return BlackJackEngineUtil.gameStart(gameLogicRequest, gameLogicMap, input, this.gameLogicBean, this.mathModel, this.engineContextMap);
    }

    public BlackJackGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextRequest, InputInfo input) throws InvalidPlayerInputException, InvalidGameStateException {
        return BlackJackEngineUtil.gameProgress(gameLogicRequest, gameLogicMap, playerInput, engineContextRequest, input, this.gameLogicBean, this.mathModel, this.engineContextMap);
    }

    public Map getEngineContext() {
        return this.engineContextMap;
    }

}
