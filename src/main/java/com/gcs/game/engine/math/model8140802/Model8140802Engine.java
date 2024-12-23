package com.gcs.game.engine.math.model8140802;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

@GameInfo(formFactor = "slots-243w-3x5", mathType = GameClass.Slots_243w_3x5, payType = GamePayType.ALL_WAY, minLine = 50, maxLine = 50, minBet = 1, maxBet = 20, rowsCount = 3, reelsCount = 5, betSteps = {1, 3, 5, 7, 10, 15, 20}, paybacks = {9551, 9601, 9651})
public class Model8140802Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "8140802";

    public Model8140802Engine(int payback, String mmID) {
        super(payback, mmID);
    }

}
