package com.gcs.game.engine.slots.bonus;

import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.engine.slots.vo.SlotBonusResult;
import com.gcs.game.engine.slots.vo.SlotSpinResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.vo.InputInfo;

public abstract class BaseBonus {

    protected abstract int getTriggerSymbolNumber();

    /**
     * bonus start.
     *
     * @param gameLogicBean
     * @param payback
     * @return
     */
    public abstract SlotBonusResult computeBonusStart(SlotGameLogicBean gameLogicBean, int payback);

    /**
     * bonus start.
     *
     * @param gameLogicBean
     * @param payback
     * @return
     */
    public abstract SlotBonusResult computeBonusStart(SlotGameLogicBean gameLogicBean, int payback, InputInfo input);

    /**
     * bonus pick.
     *
     * @param playerInfo
     * @param bonus
     * @return
     */
    public abstract SlotBonusResult computeBonusPick(SlotGameLogicBean gameLogicBean, PlayerInputInfo playerInfo, SlotBonusResult bonus);

    /**
     * get hit symbol count.
     *
     * @param spinResult
     * @return
     */
    protected int getHitSymbolCount(SlotSpinResult spinResult) {
        int hitCount = 0;
        int hitSymbol = getTriggerSymbolNumber();
        if (spinResult != null) {
            int[] hitSymbols = spinResult.getHitSlotSymbols();
            int[] hitSymbolsCount = spinResult.getHitSlotSymbolCount();
            if (hitSymbols != null && hitSymbolsCount != null && hitSymbols.length == hitSymbolsCount.length) {
                for (int i = 0; i < hitSymbols.length; i++) {
                    if (hitSymbols[i] == hitSymbol) {
                        hitCount = hitSymbolsCount[i];
                        break;
                    }
                }
            }
        }
        return hitCount;
    }

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

    public void checkInput4BonusPick(SlotGameLogicBean gameLogicBean, PlayerInputInfo playerInfo, SlotBonusResult bonus) throws InvalidPlayerInputException {

    }

}
