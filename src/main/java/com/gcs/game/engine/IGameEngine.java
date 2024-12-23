package com.gcs.game.engine;

import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.vo.BaseGameFeature;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;

import java.util.Map;

public interface IGameEngine {
    BaseGameLogicBean init(BaseGameLogicBean gameLogicBean) throws InvalidGameStateException;

    BaseGameFeature loadGameFeature();

    BaseGameLogicBean getDefaultGameLogicData() throws InvalidGameStateException;

    BaseGameLogicBean gameStart(BaseGameLogicBean gameLogicBean, Map gameLogicMap, InputInfo input) throws InvalidGameStateException, InvalidBetException;

    BaseGameLogicBean gameProgress(BaseGameLogicBean gameLogicBean, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextMap, InputInfo input) throws InvalidPlayerInputException, InvalidGameStateException;

    Map getEngineContext();

}
