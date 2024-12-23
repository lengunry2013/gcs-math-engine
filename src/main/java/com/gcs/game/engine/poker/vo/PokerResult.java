package com.gcs.game.engine.poker.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PokerResult {
    private List<Integer> handPokers = null;
    private List<Integer> goldHandPokers = null;
    private List<Integer> holdPositions = null;

    private long initPay = 0l;

    private int initPayType = 0;

    //TODO init Win Position
    private long pokerPay = 0l;

    private int pokerPayType = 0;

    //TODO result Win Position

    private int pokerPlayStatus = 0;

    private int goldCardBonusType = 0;
    //gold Card random win pay
    private long instantCashPay = 0l;

    private boolean triggerFs = false;

    private int triggerFsCounts = 0;

    private boolean triggerBonus = false;

    private List<String> nextScenes = null;

    private boolean triggerRespin = false;

    private int triggerRespinCounts = 0;
    private int fsMul = 1;

}
