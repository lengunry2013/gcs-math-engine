package com.gcs.game.engine.blackJack.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class DealerResult {
    private List<Integer> cardsNumber = null;
    private List<Integer> cardsPoint = null;
    private int dealerStatus = 0;

}