package com.gcs.game.engine.math.model20260507;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Model20260507SpinResult extends SlotSpinResult {

    private int reelsType = 1;

    private List<Integer> swPosition = new ArrayList<>();

    private int triggerSwCount = -1;

    private int triggerActiveLevel = -1;

    private int endSwCount = -1;

    private int endActiveLevel = -1;

    private int[] linkBonusDisplaySymbol = null;

    private long grantWin = 0;

    private List<Long> swSymbolsWin = null;

}
