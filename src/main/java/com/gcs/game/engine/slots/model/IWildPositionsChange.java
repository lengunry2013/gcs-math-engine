package com.gcs.game.engine.slots.model;


import com.gcs.game.engine.slots.vo.SlotGameLogicBean;

public interface IWildPositionsChange {

    int wildSymbolNo();

    int[] computeWildPositions(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot);

}
