package com.gcs.game.engine.math.model20260531;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;


@GameInfo(formFactor = "slots-25l-3x5", mathType = GameClass.Slots_25l_3x5, payType = GamePayType.LINE, minLine = 25, maxLine = 25, minBet = 1, maxBet = 10, rowsCount = 3, reelsCount = 5, betSteps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, paybacks = {8800})
public class Model20260531Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260531";

    public Model20260531Engine(int payback, String mmID) {
        super(payback, mmID);
    }


}
