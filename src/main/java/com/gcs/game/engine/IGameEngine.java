package com.gcs.game.engine;

import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.vo.*;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;

import java.util.Map;

public interface IGameEngine {
    BaseGameLogicBean init(BaseGameLogicBean gameLogicBean) throws InvalidGameStateException;

    BaseGameFeature loadGameFeature();

    BaseGameLogicBean getDefaultGameLogicData() throws InvalidGameStateException;

    BaseGameLogicBean gameStart(BaseGameLogicBean gameLogicBean, Map gameLogicMap, InputInfo input, RecoverInfo recoverInfo) throws InvalidGameStateException, InvalidBetException;

    BaseGameLogicBean gameProgress(BaseGameLogicBean gameLogicBean, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextMap, InputInfo input, RecoverInfo recoverInfo) throws InvalidPlayerInputException, InvalidGameStateException;

    Map getEngineContext();

}
