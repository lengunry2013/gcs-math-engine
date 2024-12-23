package com.gcs.game.engine.slots.vo;

import com.gcs.game.vo.BaseGameFeature;
import lombok.Data;

import java.util.Map;
@Data
public class SlotGameFeature extends BaseGameFeature {
    private long minLine = 1;

    private long maxLine = 1;

    private int[][] slotReels = null;

    private int[][] slotFsReels = null;

    private Map<String, int[][]> otherSlotReelsMap = null;

    private int[] initSlotReelsPosition = null;


}
