package com.gcs.game.engine.math.model20260923;

import com.gcs.game.engine.keno.KenoGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;

@GameInfo(formFactor = "TableGame-Keno", mathType = GameClass.TableGame_Keno, minBet = 1, maxBet = 8, minLine = 25, maxLine = 25, betSteps = {25, 50, 75, 100, 125, 150, 175, 200}, paybacks = {9550, 9600, 9650})
public class Model20260923Engine extends KenoGameEngine {

    public static final String MATH_MODEL = "20260923";

    public Model20260923Engine(int payback, String mmID) {
        super(payback, mmID);
    }
}
