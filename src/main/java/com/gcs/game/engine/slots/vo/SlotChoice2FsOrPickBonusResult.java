package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotChoice2FsOrPickBonusResult extends SlotPickTerminatorBonusResult {

    public static final int FREE_SPIN_TYPE_TWO_STACKS_WILDS = 1;
    public static final int FREE_SPIN_TYPE_FOUR_ROAMING_WILDS = 2;

    private int fsType = -1;

    private int[] fsOrBonusPick = null;


}
