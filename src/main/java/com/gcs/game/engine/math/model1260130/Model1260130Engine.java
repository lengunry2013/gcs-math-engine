package com.gcs.game.engine.math.model1260130;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

@GameInfo(formFactor = "slots-88l-4x5", mathType = GameClass.Slots_88l_4x5, payType = GamePayType.LINE, minLine = 88, maxLine = 88, minBet = 1, maxBet = 10, rowsCount = 4, reelsCount = 5, betSteps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, paybacks = {9550, 9600, 9650})
public class Model1260130Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "1260130";

    public Model1260130Engine(int payback, String mmID) {
        super(payback, mmID);
    }

}
