package com.gcs.game.engine.math.model20260530;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import lombok.Data;

import java.util.List;

@Data
public class Model20260530SpinResult extends SlotSpinResult {
    private int featureType = -1;
    private List<Integer> subSymbolPositions = null;
    private List<Integer> subSymbols = null;
    private int wildMul = -1;
    //TODO test use
    private int baseScRandomIndex = -1;
    private int baseWlRandomIndex = -1;
    private int baseWinRandomIndex = -1;
    private int fsScRandomIndex = -1;
    private int fsWlRandomIndex = -1;
    private int fsWlAddRandomIndex = -1;
    private int fsWinRandomIndex = -1;
}
