package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotChoiceBonusResult extends SlotBonusResult {

    private int[] pickCharacters = null;

    private long[] charactersRewards = null;

    private int[] charactersCount = null;

    private int[] charactersCountWithWild = null;

    private int[] hitCharacters = null;

    private long[] hitCharactersPay = null;

    private int[] displayCharacters4Reveal = null;

    private int bonusMul = 1;

    private String bonusWinPattern = "";

}
