package com.gcs.game.engine.blackJack.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlackJackResult implements Cloneable {
    private int handIndex = 0;
    private List<Integer> cardsNumber = null;
    private List<Integer> cardsPoint = null;
    private long betPay = 0;
    private long jackpotPay = 0;
    private long splitPay = 0;
    private long insurancePay = 0;
    private boolean hasInsurance = false;
    private boolean hasSplit = false;
    private boolean hasDouble = false;
    private boolean splitHandHasDouble = false;
    private int handStatus = 0;
    private List<Integer> splitCardsNumber = null;
    private List<Integer> splitCardsPoint = null;
    private int splitHandStatus = 0;
}
