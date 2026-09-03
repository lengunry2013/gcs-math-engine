package com.gcs.game.engine.math.model20260825;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;


@GameInfo(formFactor = "slots-25l-3x5", mathType = GameClass.Slots_25l_3x5, payType = GamePayType.LINE, minLine = 25, maxLine = 25, minBet = 2, maxBet = 20, rowsCount = 3, reelsCount = 5, betSteps = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20}, paybacks = {8807, 9053})
public class Model20260825Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260825";

    public Model20260825Engine(int payback, String mmID) {
        super(payback, mmID);
    }


}
