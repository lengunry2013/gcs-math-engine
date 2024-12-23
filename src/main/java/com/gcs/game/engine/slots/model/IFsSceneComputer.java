package com.gcs.game.engine.slots.model;


import com.gcs.game.engine.slots.vo.SlotSpinResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;

public interface IFsSceneComputer {

    /**
     * compute trigger respin.
     *
     * @param gameLogicCache
     * @param spinResult
     */
    boolean computeNextSceneWhileTriggerBonusInRespin(SlotGameLogicBean gameLogicCache, SlotSpinResult spinResult);

}
