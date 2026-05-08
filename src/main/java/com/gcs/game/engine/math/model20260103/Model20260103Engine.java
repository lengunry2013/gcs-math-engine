package com.gcs.game.engine.math.model20260103;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

import java.util.ArrayList;
import java.util.List;

@GameInfo(formFactor = "slots-243w-3x5", mathType = GameClass.Slots_243w_3x5, payType = GamePayType.ALL_WAY, minLine = 50, maxLine = 50, minBet = 1, maxBet = 10, rowsCount = 3, reelsCount = 5, betSteps = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, paybacks = {8816, 9016})
public class Model20260103Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "20260103";

    public Model20260103Engine(int payback, String mmID) {
        this.mathModel = mmID;
        this.payback = payback;
        List<String> otherReelsKeys = new ArrayList<>();
        otherReelsKeys.add(Model20260103.BASE_REELS_KEY);
        otherReelsKeys.add(Model20260103.FREE_SPIN_REELS_KEY);
        this.modelFeature = SlotEngineUtil.initModelFeature(mmID, payback, otherReelsKeys);
    }


}
