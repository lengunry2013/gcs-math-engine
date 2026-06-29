package com.gcs.game.engine.math.model20260625;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

import java.util.ArrayList;
import java.util.List;

@GameInfo(formFactor = "slots-25l-3x5", mathType = GameClass.Slots_25l_3x5, payType = GamePayType.LINE, minLine = 25, maxLine = 25, minBet = 1, maxBet = 10, rowsCount = 3, reelsCount = 5, betSteps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, paybacks = {8800, 9011})
public class Model20260625Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260625";

    public Model20260625Engine(int payback, String mmID) {
        this.mathModel = mmID;
        this.payback = payback;
        List<String> otherReelsKeys = new ArrayList<>();
        otherReelsKeys.add(Model20260625.BASE_REELS_KEY);
        otherReelsKeys.add(Model20260625.FREE_SPIN_REELS_KEY);
        this.modelFeature = SlotEngineUtil.initModelFeature(mmID, payback, otherReelsKeys);
    }


}
