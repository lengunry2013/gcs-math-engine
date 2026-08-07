package com.gcs.game.engine.math.model20260804;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

import java.util.ArrayList;
import java.util.List;

@GameInfo(formFactor = "slots-50l-4x5", mathType = GameClass.Slots_50l_4x5, payType = GamePayType.ALL_WAY, minLine = 50, maxLine = 50, minBet = 1, maxBet = 20, rowsCount = 4, reelsCount = 5, betSteps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, paybacks = {8808, 9049})
public class Model20260804Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260804";

    public Model20260804Engine(int payback, String mmID) {
        this.mathModel = mmID;
        this.payback = payback;
        List<String> otherReelsKeys = new ArrayList<>();
        otherReelsKeys.add(Model20260804.FREE_SPIN_REELS2_KEY);
        otherReelsKeys.add(Model20260804.FREE_SPIN_REELS3_KEY);
        this.modelFeature = SlotEngineUtil.initModelFeature(mmID, payback, otherReelsKeys);
    }


}
