package com.gcs.game.engine.blackJack.utils;

public class BlackJackGameConstant {
    //handStatus and split hand status
    public static final int HAND_STATUS_IDLE = 0;
    public static final int HAND_STATUS_INSURANCE = 1;
    public static final int HAND_STATUS_HAS_INSURANCE = 2;
    public static final int HAND_STATUS_NO_INSURANCE = 3;
    public static final int HAND_STATUS_WAIT_DEALER_PEEK = 4;
    public static final int HAND_STATUS_INSURANCE_PUSH = 5;
    public static final int HAND_STATUS_INSURANCE_LOSE = 6;
    public static final int HAND_STATUS_BJ_PUSH = 7;
    public static final int HAND_STATUS_BJ_LOSE = 8;
    public static final int HAND_STATUS_BJ_WIN = 9;
    public static final int HAND_STATUS_SPLIT = 10;
    public static final int HAND_STATUS_DOUBLE = 11;
    public static final int HAND_STATUS_HIT = 12;
    public static final int HAND_STATUS_STAND = 13;
    public static final int HAND_STATUS_BUST = 14;
    public static final int HAND_STATUS_PUSH = 15;
    public static final int HAND_STATUS_WIN = 16;
    public static final int HAND_STATUS_LOSE = 17;
    public static final int HAND_STATUS_SITTINGOUT = 18;

    //dealer status
    public static final int DEALER_STATUS_IDLE = 0;
    public static final int DEALER_WAIT_FOR_INSURANCE = 1;
    public static final int DEALER_PEEK_BJ = 2;
    public static final int DEALER_BLACKJACK = 3;
    public static final int DEALER_WAIT_FOR_HANDS = 4;
    public static final int DEALER_DEAL = 5;
    public static final int DEALER_BUST = 6;
    public static final int DEALER_COMPLETE = 7;
    public static final int CARD_MAX_NUMBER = 52;

    public static final int CARD_A_POINT = 1;
    public static final int CARD_A_MAX_POINT = 11;
    public static final int CARD_PEEK_POINT = 10;
    public static final int BLACK_JACK_POINT = 21;
    public static final int DEALER_CARD_COMPLETE_POINT = 17;

    //progressive Blackjack jackpot pay index
    public static final int ONE_ACE = 1;
    public static final int TWO_UNSUITED_ACES = 2;
    public static final int TWO_SUITED_ACES = 3;
    public static final int THREE_UNSUITED_ACES = 4;
    public static final int FOUR_UNSUITED_ACES = 5;
    public static final int THREE_SUITED_ACES = 6;
    public static final int FOUR_SUITED_ACES = 7;


}
