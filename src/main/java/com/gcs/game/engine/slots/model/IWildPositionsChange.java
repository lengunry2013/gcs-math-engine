package com.gcs.game.engine.slots.model;


import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.vo.RecoverInfo;

public interface IWildPositionsChange {

    int wildSymbolNo();

    int[] computeWildPositions(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot);

    int[] computeWildPositions(SlotGameLogicBean gameLogicBean, int[] displaySymbols, boolean isSlot, RecoverInfo recoverInfo);
}
