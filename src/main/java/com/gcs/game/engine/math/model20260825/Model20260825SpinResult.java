package com.gcs.game.engine.math.model20260825;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Model20260825SpinResult extends SlotSpinResult {
    private int swType = 0;
    private List<long[]> linkBonusSwPays = null;
    private List<int[]> swMultis = null;
    private List<Long> colPays = null;
    private List<Long> colTotalPays = null;
    private List<int[]> colIcons = null;
    private List<Boolean> isFullBalls = null;
    private List<Boolean> isGridTriggerFs = null;
    private List<Integer> gridFsLeftCounts = null;
}
