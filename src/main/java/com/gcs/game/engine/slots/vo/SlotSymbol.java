package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SlotSymbol {

    private int symbolNumber = -1;

    private int symbolType = -1;

    private int symbolHitType = -1;

    private int[] wildSymbols = null;

    private long[] pay = null;

    private long[] payInFreeSpin = null;

    private int minHitCount = -1;

    public SlotSymbol() {

    }


}
