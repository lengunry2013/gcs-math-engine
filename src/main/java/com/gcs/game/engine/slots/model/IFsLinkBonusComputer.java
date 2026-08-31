package com.gcs.game.engine.slots.model;

import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.engine.slots.vo.SlotSpinResult;

public interface IFsLinkBonusComputer {

    /**
     * link bonus left fs
     *
     * @param gameLogicCache
     * @param spinResult
     * @return
     */
    boolean computeFsCountLeftWhileLinkBonus(SlotGameLogicBean gameLogicCache, SlotSpinResult spinResult);

    void computeTotalPays(SlotGameLogicBean gameLogicCache, SlotSpinResult spinResult,Boolean freeSpinComplete);
}
