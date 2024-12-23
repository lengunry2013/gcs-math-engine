package com.gcs.game.engine.math.modelGCBJ00101;

import com.gcs.game.engine.blackJack.BlackJackGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;

@GameInfo(formFactor = "TableGame-BlackJack-8x5", mathType = GameClass.TableGame_BlackJack, minBet = 10, maxBet = 10000, minLine = 1, maxLine = 1,betSteps = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000}, paybacks = {9550, 9600, 9650})
public class ModelGCBJ00101Engine extends BlackJackGameEngine {

    public static final String MATH_MODEL = "GCBJ00101";

    public ModelGCBJ00101Engine(int payback, String mmID) {
        super(payback, mmID);
    }

}
