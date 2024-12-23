package com.gcs.game.engine.math.model1010802;

import com.gcs.game.engine.slots.SlotGameEngine;
import com.gcs.game.engine.slots.utils.SlotEngineUtil;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.GamePayType;

import java.util.ArrayList;
import java.util.List;

@GameInfo(formFactor = "slots-50l-3x5", mathType = GameClass.Slots_50l_3x5, payType = GamePayType.ALL_WAY, minLine = 50, maxLine = 50, minBet = 1, maxBet = 100, rowsCount = 3, reelsCount = 5, betSteps = {1,50,100}, paybacks = {9600, 9630})
public class Model1010802Engine extends SlotGameEngine {

    public static final String MATH_MODEL = "1010802";

    public Model1010802Engine(int payback, String mmID) {
        this.payback = payback;
        this.mathModel = mmID;
        List<String> otherReelsKeys = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            String key = Model1010802.BASE_REELS_KEY + (i + 1);
            otherReelsKeys.add(key);
        }
        for (int i = 1; i < 5; i++) {
            String key = Model1010802.FREE_SPIN_REELS_KEY + (i + 1);
            otherReelsKeys.add(key);
        }
        this.modelFeature = SlotEngineUtil.initModelFeature(MATH_MODEL, payback, otherReelsKeys);
    }

}
