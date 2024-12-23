package com.gcs.game.engine.slots;

import com.gcs.game.engine.IGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.engine.slots.vo.SlotGameFeature;
import com.gcs.game.exception.InvalidBetException;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.engine.slots.vo.SlotGameFeatureVo;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.exception.InvalidGameStateException;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;

import java.util.Map;


public abstract class SlotGameEngine implements IGameEngine {

    protected int payback = 0;
    protected String mathModel = "";
    protected SlotGameFeatureVo modelFeature = null;

    protected SlotGameLogicBean gameLogicBean = null;
    protected Map<String, String> engineContextMap = null;


    public SlotGameEngine() {
    }

    public SlotGameEngine(int payback, String mathModel) {
        this.payback = payback;
        this.mathModel = mathModel;
        this.modelFeature = SlotEngineUtil.initModelFeature(mathModel, payback);
    }

    public SlotGameFeature loadGameFeature() {
        SlotGameFeature slotGameFeature = new SlotGameFeature();
        slotGameFeature.setMinLine(this.modelFeature.getMinLine());
        slotGameFeature.setMaxLine(this.modelFeature.getMaxLine());
        slotGameFeature.setMaxBet(this.modelFeature.getMinBet());
        slotGameFeature.setMaxBet(this.modelFeature.getMaxBet());
        slotGameFeature.setInitSlotReelsPosition(this.modelFeature.getInitSlotReelsPosition());
        slotGameFeature.setSlotReels(this.modelFeature.getSlotReels());
        slotGameFeature.setSlotFsReels(this.modelFeature.getSlotFsReels());
        slotGameFeature.setOtherSlotReelsMap(this.modelFeature.getOtherSlotReelsMap());
        return slotGameFeature;
    }


    /**
     * init game session.
     *
     * @param gameLogicBean
     * @return
     * @throws InvalidGameStateException
     */
    public SlotGameLogicBean init(BaseGameLogicBean gameLogicBean) throws InvalidGameStateException {
        this.gameLogicBean = (SlotGameLogicBean) gameLogicBean;
        if (this.gameLogicBean == null) {
            this.gameLogicBean = getDefaultGameLogicData();
        }
        return this.gameLogicBean;
    }

    public SlotGameLogicBean getDefaultGameLogicData() throws InvalidGameStateException {
        return SlotEngineUtil.getDefaultGameSession(this.mathModel, this.payback, this.modelFeature);
    }

    /**
     * game start.
     *
     * @param gameLogicMap
     * @param input
     * @return
     * @throws InvalidGameStateException
     */
    public SlotGameLogicBean gameStart(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, InputInfo input) throws InvalidGameStateException, InvalidBetException {
        return SlotEngineUtil.gameStart(gameLogicRequest, gameLogicMap, input, this.gameLogicBean, this.mathModel, this.modelFeature);
    }

    /**
     * spin in free spin, or pick in bonus with input.
     *
     * @param gameLogicRequest
     * @param playerInput
     * @param input
     * @return
     * @throws InvalidPlayerInputException
     * @throws InvalidGameStateException
     */
    public SlotGameLogicBean gameProgress(BaseGameLogicBean gameLogicRequest, Map gameLogicMap, PlayerInputInfo playerInput, Map engineContextRequest, InputInfo input) throws InvalidPlayerInputException, InvalidGameStateException {
        return SlotEngineUtil.gameProgress(gameLogicRequest, playerInput, this.gameLogicBean, this.mathModel, this.modelFeature, payback, input);
    }

    public Map getEngineContext() {
        return this.engineContextMap;
    }


}
