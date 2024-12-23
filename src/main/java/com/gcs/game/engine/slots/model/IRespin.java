package com.gcs.game.engine.slots.model;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;

public interface IRespin {

    int computeRespin(SlotGameLogicBean gameLogicCache, int[] displaySymbols, boolean isSlot, SlotSpinResult spinResult);

    SlotSpinResult respin(SlotGameLogicBean gameLogicCache, int[] displaySymbols, int[] stopPosition, boolean isSlot);
}
