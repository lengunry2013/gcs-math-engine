package com.gcs.game.engine.math.model20260201;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

import java.util.ArrayList;
import java.util.List;

@GameInfo(formFactor = "slots-25l-3x5", mathType = GameClass.Slots_25l_3x5, payType = GamePayType.LINE, minLine = 25, maxLine = 25, minBet = 2, maxBet = 20, rowsCount = 3, reelsCount = 5, betSteps = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20}, paybacks = {8842, 9000})
public class Model20260201Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260201";

    public Model20260201Engine(int payback, String mmID) {
        this.mathModel = mmID;
        this.payback = payback;
        List<String> otherReelsKeys = new ArrayList<>();
        otherReelsKeys.add(Model20260201.FREE_SPIN_REELS2_KEY);
        otherReelsKeys.add(Model20260201.FREE_SPIN_REELS3_KEY);
        this.modelFeature = SlotEngineUtil.initModelFeature(mmID, payback, otherReelsKeys);
    }

}
