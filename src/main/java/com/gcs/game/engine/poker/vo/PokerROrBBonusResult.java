package com.gcs.game.engine.poker.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PokerROrBBonusResult extends PokerBonusResult {
    private List<Integer> cardList = null;

    private int[] roundsReward = null;

}
