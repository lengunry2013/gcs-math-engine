package com.gcs.game.engine.slots.model;


import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.vo.RecoverInfo;

public interface IWildReelsChange {

    int wildSymbolNo();

    int[] computeWildReels(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot);

    int[] computeWildReels(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, RecoverInfo recoverInfo);
}
