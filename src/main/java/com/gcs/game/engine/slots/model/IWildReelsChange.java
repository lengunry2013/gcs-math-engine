package com.gcs.game.engine.slots.model;


import com.gcs.game.engine.slots.vo.SlotGameLogicBean;

public interface IWildReelsChange {

    int wildSymbolNo();

    int[] computeWildReels(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot);

}
