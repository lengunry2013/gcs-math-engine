package com.gcs.game.engine.blackJack.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlackJackBetInfo implements Cloneable {
    private int handIndex = 0;
    private long bet = 0;
    private long jackpotBet = 0;
    private long splitBet = 0;
    private long insuranceBet = 0;

    public BlackJackBetInfo clone() throws CloneNotSupportedException {
        return (BlackJackBetInfo) super.clone();
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("{").append("handIndex:").append(handIndex).append(",");
        result.append("bet:").append(bet).append(",");
        result.append("jackpotBet:").append(jackpotBet).append(",");
        result.append("splitBet:").append(splitBet).append(",");
        result.append("insuranceBet:").append(insuranceBet);
        result.append("}");
        return result.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.handIndex;
        result = (int) (31 * result + this.bet);
        result = (int) (31 * result + this.jackpotBet);
        result = (int) (31 * result + this.splitBet);
        result = (int) (31 * result + this.insuranceBet);
        return result;
    }

}
