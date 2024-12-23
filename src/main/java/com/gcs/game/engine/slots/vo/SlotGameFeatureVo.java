package com.gcs.game.engine.slots.vo;

import com.gcs.game.vo.BaseGameFeature;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SlotGameFeatureVo extends BaseGameFeature {
    private long minLine = 1;

    private long maxLine = 1;

    private int[][] slotReels = null;

    private int[][] slotReelsWeight = null;

    private int[][] slotFsReels = null;

    private int[][] slotFsReelsWeight = null;

    private int[] initSlotReelsPosition = null;

    private Map<String, int[][]> otherSlotReelsMap = null;

    private Map<String, int[][]> otherSlotReelsWeightMap = null;

}
