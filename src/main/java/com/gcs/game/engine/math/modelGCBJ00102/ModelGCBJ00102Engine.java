package com.gcs.game.engine.math.modelGCBJ00102;

import com.gcs.game.engine.blackJack.BlackJackGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;

@GameInfo(formFactor = "TableGame-BlackJack-8x3", mathType = GameClass.TableGame_BlackJack_8x3, minBet = 1, maxBet = 10000, minLine = 1, maxLine = 1, betSteps = {1, 5, 10, 25, 50, 100, 200, 500, 1000, 2000, 5000, 10000}, paybacks = {9555, 9600, 9958})
public class ModelGCBJ00102Engine extends BlackJackGameEngine {

    public static final String MATH_MODEL = "GCBJ00102";

    public ModelGCBJ00102Engine(int payback, String mmID) {
        super(payback, mmID);
    }

}
