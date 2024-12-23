package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@BonusBeanInfo(bonusType = BonusType.PICK_BONUS)
public class SlotBonusResult implements Cloneable{
    private int bonusPlayStatus = -1;

    private int[] pickIndexInfos = null;

    private long totalPay = 0L;

    private long payForPickIndex = 0L;

    private long enterAward = 0L;

    public SlotBonusResult clone() throws CloneNotSupportedException {
        return (SlotBonusResult) super.clone();
    }

}
