package com.gcs.game.utils;

public class GameConstant {

    public static final int GAME_STATUS_IDLE = 0;

    public static final int GAME_STATUS_COMPLETE = 1000;

    //Slot Game
    public static final int SLOT_GAME_STATUS_IDLE = 0;
    public static final int SLOT_GAME_STATUS_TRIGGER_FREESPIN = 200;
    public static final int SLOT_GAME_STATUS_TRIGGER_BONUS = 500;
    public static final int SLOT_GAME_STATUS_COMPLETE = 1000;

    public static final int SLOT_GAME_BONUS_STATUS_START = 100;
    public static final int SLOT_GAME_BONUS_STATUS_PICK = 200;
    public static final int SLOT_GAME_BONUS_STATUS_COMPLETE = 1000;

    //game collect
    public static final String KEY_BONUS_ACUM_METER = "BONUS_ACUM_METER";

    //BACKJACK game play status
    public static final int BJ_BLACKJACK_GAME_STATUS_DEAL = 100;
    public static final int BJ_BLACKJACK_GAME_STATUS_INSURANCE = 200;
    public static final int BJ_BLACKJACK_GAME_STATUS_NO_INSURANCE = 201;
    public static final int BJ_BLACKJACK_GAME_STATUS_PEEK_BLACKJACK = 202;
    public static final int BJ_BLACKJACK_GAME_STATUS_SPLIT = 300;
    public static final int BJ_BLACKJACK_GAME_STATUS_DOUBLE = 400;
    public static final int BJ_BLACKJACK_GAME_STATUS_HIT = 500;
    public static final int BJ_BLACKJACK_GAME_STATUS_STAND = 600;
    public static final int BJ_BLACKJACK_GAME_STATUS_DEALER_DRAW = 700;

    //POKER game play status
    public static final int POKER_GAME_STATUS_SWITCH_CARD = 100;
    public static final int POKER_GAME_STATUS_TRIGGER_FREESPIN = 200;
    public static final int POKER_GAME_STATUS_TRIGGER_BONUS = 500;

    public static final int POKER_GAME_BONUS_STATUS_START = 100;
    public static final int POKER_GAME_BONUS_STATUS_PICK = 200;
    public static final int POKER_GAME_BONUS_STATUS_COMPLETE = 1000;

    //Keno Game play status
    public static final int KENO_GAME_STATUS_TRIGGER_FREESPIN = 200;

}
