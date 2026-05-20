package com.gcs.game.engine.math.model20260104;

import com.gcs.game.engine.math.model20260103.Model20260103;
import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

import java.util.ArrayList;
import java.util.List;

@GameInfo(formFactor = "slots-3125w-5x5", mathType = GameClass.Slots_3125w_5x5, payType = GamePayType.ALL_WAY, minLine = 50, maxLine = 50, minBet = 1, maxBet = 10, rowsCount = 5, reelsCount = 5, betSteps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, paybacks = {8811, 9011})
public class Model20260104Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260104";

    public Model20260104Engine(int payback, String mmID) {
        this.mathModel = mmID;
        this.payback = payback;
        List<String> otherReelsKeys = new ArrayList<>();
        otherReelsKeys.add(Model20260103.BASE_REELS_KEY);
        otherReelsKeys.add(Model20260103.FREE_SPIN_REELS_KEY);
        this.modelFeature = SlotEngineUtil.initModelFeature(mmID, payback, otherReelsKeys);
    }


}
