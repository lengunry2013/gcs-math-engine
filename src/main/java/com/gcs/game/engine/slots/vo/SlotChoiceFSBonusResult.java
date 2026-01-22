package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotChoiceFSBonusResult extends SlotBonusResult {

    public static final int FREE_SPIN_TYPE_1 = 1;
    public static final int FREE_SPIN_TYPE_2 = 2;
    public static final int FREE_SPIN_TYPE_3 = 3;

    private int fsType = -1;

    private int[] fsPick = null;

    private int randomIndex4FS = 0;

}
