package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotBonusSymbol extends SlotSymbol {

    private String bonusAsset = null;

    private int[] hitFsCounts = null;


}
