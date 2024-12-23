package com.gcs.game.engine.poker.vo;

import com.gcs.game.engine.slots.vo.BonusBeanInfo;
import com.gcs.game.engine.slots.vo.BonusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@BonusBeanInfo(bonusType = BonusType.PICK_BONUS)
public class PokerBonusResult implements Cloneable{
    private int bonusPlayStatus = -1;

    private int[] pickIndexInfos = null;

    private long totalPay = 0L;

    private long payForPickIndex = 0L;

    private long enterAward = 0L;

    public PokerBonusResult clone() throws CloneNotSupportedException {
        return (PokerBonusResult) super.clone();
    }

}
