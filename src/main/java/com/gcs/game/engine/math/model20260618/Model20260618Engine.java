package com.gcs.game.engine.math.model20260618;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

import java.util.ArrayList;
import java.util.List;

@GameInfo(formFactor = "slots-20l-4x5", mathType = GameClass.Slots_20l_4x5, payType = GamePayType.LINE, minLine = 20, maxLine = 20, minBet = 1, maxBet = 10, rowsCount = 4, reelsCount = 5, betSteps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, paybacks = {9000})
public class Model20260618Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260618";

    public Model20260618Engine(int payback, String mmID) {
        this.mathModel = mmID;
        this.payback = payback;
        List<String> otherReelsKeys = new ArrayList<>();
        otherReelsKeys.add(Model20260618.FREE_SPIN_REELS2_KEY);
        this.modelFeature = SlotEngineUtil.initModelFeature(mmID, payback, otherReelsKeys);
    }

}
