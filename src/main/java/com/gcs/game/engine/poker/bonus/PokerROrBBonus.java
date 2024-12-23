package com.gcs.game.engine.poker.bonus;

import com.gcs.game.engine.poker.utils.PokerGameConstant;
import com.gcs.game.engine.poker.vo.PokerBonusResult;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.engine.poker.vo.PokerROrBBonusResult;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class PokerROrBBonus extends PokerBonus {
    private static final int PICK_RED = 0;
    private static final int PICK_BLACK = 1;

    protected abstract int[] getAward();

    protected abstract int maxRound();


    @Override
    public PokerBonusResult computeBonusStart(PokerGameLogicBean gameLogicBean, int payback) {
        PokerROrBBonusResult result = new PokerROrBBonusResult();
        int bonusStatus = GameConstant.POKER_GAME_BONUS_STATUS_START;
        int[] pickIndexs = null;
        long totalPay = 0;
        long payForPick = 0;

        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        result.setRoundsReward(getAward());
        result.setEnterAward(0);
        return result;
    }

    @Override
    public PokerBonusResult computeBonusStart(PokerGameLogicBean gameLogicBean, int payback, InputInfo input) {
        return computeBonusStart(gameLogicBean, payback); // TODO support
    }

    @Override
    public PokerBonusResult computeBonusPick(PokerGameLogicBean gameLogicBean, PlayerInputInfo playerInfo, PokerBonusResult bonus) {
        int bonusStatus = GameConstant.POKER_GAME_BONUS_STATUS_PICK;
        int[] reqPickIndex = null;
        //"bonusPickInfos":[0,0,1,0,1,1]
        if (playerInfo != null) {
            reqPickIndex = playerInfo.getBonusPickInfos();
        }
        PokerROrBBonusResult result = null;
        if (bonus != null) {
            PokerROrBBonusResult pokerROrBBonusResult = (PokerROrBBonusResult) bonus;
            int[] roundsReward = pokerROrBBonusResult.getRoundsReward();
            long totalPay = 0L;
            long payForPick = 0L;
            int[] pickIndexs = pokerROrBBonusResult.getPickIndexInfos();
            List<Integer> cardList = pokerROrBBonusResult.getCardList();
            int pickCount = 0;
            if (pickIndexs != null) {
                pickCount = pickIndexs.length;
            }
            if (cardList == null || cardList.isEmpty()) {
                cardList = new ArrayList<>();
            }
            long totalBet = gameLogicBean.getSumBetCredit();
            //The first pick must win the prize
            if (reqPickIndex != null && reqPickIndex.length == 1) {
                pickIndexs = reqPickIndex.clone();
                boolean isPickRed = pickIndexs[pickIndexs.length - 1] == PICK_RED;
                boolean isPickBlack = pickIndexs[pickIndexs.length - 1] == PICK_BLACK;
                payForPick = roundsReward[pickIndexs.length - 1] * totalBet;
                while (true) {
                    int cardNumber = RandomUtil.getRandomInt(PokerGameConstant.CARD_MAX_NUMBER);
                    int card = cardNumber / PokerGameConstant.FLUSH_MAX_CARD;
                    if (isPickRed) {
                        if (card == PokerGameConstant.HEARTS_CARD || card == PokerGameConstant.DIAMONDS_CARD) {
                            cardList.add(cardNumber);
                            break;
                        }
                    } else if (isPickBlack) {
                        if (card == PokerGameConstant.SPADE_CARD || card == PokerGameConstant.CLUBS_CARD) {
                            cardList.add(cardNumber);
                            break;
                        }
                    }
                }
            } else if (reqPickIndex != null && reqPickIndex.length > 1 && reqPickIndex.length == pickCount + 1) {
                int cardNumber = RandomUtil.getRandomInt(PokerGameConstant.CARD_MAX_NUMBER);
                cardList.add(cardNumber);
                pickIndexs = reqPickIndex.clone();
                int card = cardNumber / PokerGameConstant.FLUSH_MAX_CARD;
                boolean isPickRed = pickIndexs[pickIndexs.length - 1] == PICK_RED;
                boolean isPickBlack = pickIndexs[pickIndexs.length - 1] == PICK_BLACK;
                //win red
                if (isPickRed && (card == PokerGameConstant.HEARTS_CARD || card == PokerGameConstant.DIAMONDS_CARD)) {
                    payForPick = roundsReward[pickIndexs.length - 1] * totalBet;
                    if (pickIndexs.length == maxRound()) {
                        totalPay = payForPick; //last pick bonus pay
                        bonusStatus = GameConstant.POKER_GAME_BONUS_STATUS_COMPLETE;
                    }
                }
                //win black
                else if (isPickBlack && (card == PokerGameConstant.SPADE_CARD || card == PokerGameConstant.CLUBS_CARD)) {
                    payForPick = roundsReward[pickIndexs.length - 1] * totalBet;
                    if (pickIndexs.length == maxRound()) {
                        totalPay = payForPick; //last pick bonus pay
                        bonusStatus = GameConstant.POKER_GAME_BONUS_STATUS_COMPLETE;
                    }
                } else {
                    //The Black or Red Bonus is terminated
                    payForPick = 0;
                    totalPay = pokerROrBBonusResult.getPayForPickIndex(); //last pick bonus pay
                    bonusStatus = GameConstant.POKER_GAME_BONUS_STATUS_COMPLETE;
                }

            }
            result = new PokerROrBBonusResult();
            result.setBonusPlayStatus(bonusStatus);
            result.setTotalPay(totalPay);
            result.setPayForPickIndex(payForPick);
            result.setPickIndexInfos(pickIndexs);
            result.setCardList(cardList);
            result.setRoundsReward(roundsReward);
            result.setEnterAward(0);
        }

        return result;
    }
}
