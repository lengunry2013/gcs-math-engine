package com.gcs.game.engine.math.model20260701;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import lombok.Data;

@Data
public class Model20260701SpinResult extends SlotSpinResult {

    private int[] linkBonusRandomPayIndexs = null;
    private int[] linkBonusSwPays = null;
    private int respinTimes = 0;
    private int grandWin = 0;
    private int colWin = 0;

}
