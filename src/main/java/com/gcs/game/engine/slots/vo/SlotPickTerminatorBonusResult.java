package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotPickTerminatorBonusResult extends SlotBonusResult {

    private long[] pickCharacters = null;

    private long[] pickPays = null;

    private long[] displayCharacters4Reveal = null;
}
