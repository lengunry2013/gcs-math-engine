package com.gcs.game.engine.keno;

import com.gcs.game.engine.IGameEngine;
import com.gcs.game.engine.keno.utils.KenoEngineUtil;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.slots.vo.SlotGameFeatureVo;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;

import java.util.Map;

public abstract class KenoGameEngine implements IGameEngine {
    protected int payback = 0;
    protected String mathModel = "";

    protected KenoGameLogicBean gameLogicBean = null;

    public KenoGameEngine() {

    }

    public KenoGameEngine(int payback, String mathModel) {
        this.payback = payback;
        this.mathModel = mathModel;
    }

    public KenoGameLogicBean init(BaseGameLogicBean gameLogicBean) throws InvalidGameStateException {
        this.gameLogicBean = (KenoGameLogicBean) gameLogicBean;
        if (gameLogicBean == null) {
            this.gameLogicBean = getDefaultGameLogicData();
        }
        return this.gameLogicBean;
    }

    //not game feature
    public SlotGameFeatureVo loadGameFeature() {
        return null;
    }

    public KenoGameLogicBean getDefaultGameLogicData() throws InvalidGameStateException {
        return KenoEngineUtil.getDefaultGameLogicData(this.mathModel, this.payback);
    }

    public KenoGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input) throws InvalidGameStateException, InvalidBetException {
        return KenoEngineUtil.gameStart(gameLogicRequest, gameLogicMap, input, this.gameLogicBean, this.mathModel);
    }

    public KenoGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextRequest, InputInfo input) throws InvalidPlayerInputException, InvalidGameStateException {
        return KenoEngineUtil.gameProgress(gameLogicRequest, gameLogicMap, playerInput, engineContextRequest, input, this.gameLogicBean, this.mathModel);
    }

    @Override
    public Map getEngineContext() {
        return null;
    }


}
