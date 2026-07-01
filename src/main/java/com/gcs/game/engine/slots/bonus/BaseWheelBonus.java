package com.gcs.game.engine.slots.bonus;

import com.gcs.game.engine.slots.vo.SlotBonusResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.engine.slots.vo.SlotWheelBonusResult;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.utils.RandomUtil;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.vo.RecoverInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseWheelBonus extends BaseBonus {

    protected abstract int[][] getWheelAwardWeight();

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameSessionBean, int payback) {
        SlotWheelBonusResult result = new SlotWheelBonusResult();
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_START;

        int[] pickIndexs = null;
        long totalPay = 0;
        long payForPick = 0;

        int betLevel = (int) (gameSessionBean.getBet() - 1);
        int[] awardWeights = getWheelAwardWeight()[betLevel];
        int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(awardWeights);
        if (gameSessionBean.getHitJackpotLevel() > 0) {
            randomIndex = gameSessionBean.getHitJackpotLevel() - 1;
        }
        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        result.setHitLevel(randomIndex + 1);
        return result;
    }

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameSessionBean, int payback, InputInfo input, RecoverInfo recoverInfo) {
        SlotWheelBonusResult result = new SlotWheelBonusResult();
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_START;

        int[] pickIndexs = null;
        long totalPay = 0;
        long payForPick = 0;
        int hitLevel = -1;
        if (recoverInfo != null) {
            input = new InputInfo();
            input.setBonusWinPattern(recoverInfo.getRecoverData());
        }
        if (input != null && input.getBonusWinPattern() != null) {
            try {
                hitLevel = Integer.parseInt(input.getBonusWinPattern());
            } catch (NumberFormatException e) {
                //record log
                log.error("recoverInfo not number: " + recoverInfo.getRecoverData());
            }
        } else {
            int betLevel = (int) (gameSessionBean.getBet() - 1);
            int[] awardWeights = getWheelAwardWeight()[betLevel];
            int randomIndex = RandomUtil.getRandomIndexFromArrayWithWeight(awardWeights);
            hitLevel = randomIndex + 1;
        }
        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        result.setHitLevel(hitLevel);
        return result;
    }

    public SlotBonusResult computeBonusPick(SlotGameLogicBean gameSessionBean, PlayerInputInfo playerInfo, SlotBonusResult bonus, RecoverInfo recoverInfo) {
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_PICK;
        int[] reqPickIndex = null;
        if (playerInfo != null) {
            reqPickIndex = playerInfo.getBonusPickInfos();
        }
        if (recoverInfo != null) {
            int recoverData = Integer.parseInt(recoverInfo.getRecoverData());
            reqPickIndex = new int[]{recoverData};
        }

        SlotWheelBonusResult result = null;
        long totalPay = 0;
        long payForPick = 0;
        int hitLevel = -1;
        if (bonus != null) {
            SlotWheelBonusResult slotWheelBonusResult = (SlotWheelBonusResult) bonus;
            hitLevel = slotWheelBonusResult.getHitLevel();
            if (reqPickIndex != null && reqPickIndex.length > 0) {
                if (hitLevel > 0) {
                    bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                }
            }
            result = new SlotWheelBonusResult();
            result.setBonusPlayStatus(bonusStatus);
            result.setPickIndexInfos(reqPickIndex);
            result.setTotalPay(totalPay);
            result.setPayForPickIndex(payForPick);
            result.setHitLevel(hitLevel);
        }
        return result;
    }

    public void checkInput4BonusPick(SlotGameLogicBean gameSessionBean, PlayerInputInfo playerInfo, SlotBonusResult bonus, RecoverInfo recoverInfo) throws InvalidPlayerInputException {
        if (recoverInfo == null) {
            int[] reqPickIndex = null;
            if (playerInfo != null) {
                reqPickIndex = playerInfo.getBonusPickInfos();
            }
            if (reqPickIndex == null || reqPickIndex.length == 0) {
                throw new InvalidPlayerInputException();
            }
            if (reqPickIndex[0] < 0) {
                throw new InvalidPlayerInputException();
            }
        }
    }

}
