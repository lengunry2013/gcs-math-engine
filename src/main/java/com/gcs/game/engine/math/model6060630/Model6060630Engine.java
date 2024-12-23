package com.gcs.game.engine.math.model6060630;

import com.gcs.game.engine.poker.PokerGameEngine;
import com.gcs.game.vo.GameClass;
import com.gcs.game.vo.GameInfo;

@GameInfo(formFactor = "TableGame-Poker", mathType = GameClass.TableGame_Poker, minBet = 1, maxBet = 8, minLine = 25, maxLine = 25, betSteps = {25, 50, 75, 100, 125, 150, 175, 200}, paybacks = {9000, 9200, 9600})
public class Model6060630Engine extends PokerGameEngine {

    public static final String MATH_MODEL = "6060630";

    public Model6060630Engine(int payback, String mmID) {
        super(payback, mmID);
    }

}
