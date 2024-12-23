package com.gcs.game.engine.poker.bonus;

import com.gcs.game.engine.poker.vo.PokerBonusResult;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;

public abstract class PokerBonus {

    /**
     * bonus start.
     *
     * @param gameLogicBean
     * @param payback
     * @return
     */
    public abstract PokerBonusResult computeBonusStart(PokerGameLogicBean gameLogicBean, int payback);

    /**
     * bonus start.
     *
     * @param gameLogicBean
     * @param payback
     * @return
     */
    public abstract PokerBonusResult computeBonusStart(PokerGameLogicBean gameLogicBean, int payback, InputInfo input);

    /**
     * bonus pick.
     *
     * @param playerInfo
     * @param bonus
     * @return
     */
    public abstract PokerBonusResult computeBonusPick(PokerGameLogicBean gameLogicBean, PlayerInputInfo playerInfo, PokerBonusResult bonus);

    /**
     * get index by frequency.
     *
     * @param payback
     * @return
     */
    protected int getIndexByFrequency(int payback) {
        int result = 0;
        int frequency = (int) (payback / 100.0);
        if (frequency == 88) {
            result = 0;
        } else if (frequency == 90) {
            result = 1;
        } else if (frequency == 92) {
            result = 2;
        } else if (frequency == 94) {
            result = 3;
        } else if (frequency == 96) {
            result = 4;
        }
        return result;
    }

    public void checkInput4BonusPick(PokerGameLogicBean gameLogicBean, PlayerInputInfo playerInfo, PokerBonusResult bonus) throws InvalidPlayerInputException {

    }

}
