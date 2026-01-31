package com.gcs.game.engine.slots.bonus;

import com.gcs.game.engine.slots.vo.SlotBonusResult;
import com.gcs.game.engine.slots.vo.SlotChoiceFSBonusResult;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.exception.InvalidPlayerInputException;
import com.gcs.game.utils.GameConstant;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.vo.RecoverInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChoiceFSBonus extends BaseBonus {

    protected abstract int[][] getFreeSpinTimes(SlotGameLogicBean gameSessionBean, int payback);

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameSessionBean, int payback) {
        SlotChoiceFSBonusResult result = new SlotChoiceFSBonusResult();
        int hitSymbolCount = getHitSymbolCount(gameSessionBean.getSlotSpinResult());
        int index = 0;
        if (hitSymbolCount >= 3) {
            index = hitSymbolCount - 3;
        }

        int[][] freespinTimes = getFreeSpinTimes(gameSessionBean, payback);
        int bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_START;

        int[] pickIndexs = null;
        long totalPay = 0;
        long payForPick = 0;

        result.setFsPick(freespinTimes[index]);
        result.setBonusPlayStatus(bonusStatus);
        result.setPickIndexInfos(pickIndexs);
        result.setTotalPay(totalPay);
        result.setPayForPickIndex(payForPick);
        return result;
    }

    public SlotBonusResult computeBonusStart(SlotGameLogicBean gameSessionBean, int payback, InputInfo input, RecoverInfo recoverInfo) {
        return computeBonusStart(gameSessionBean, payback);
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
        SlotChoiceFSBonusResult result = null;
        int[] freeSpinsPick;
        int freespinType;
        long totalPay = 0;
        long payForPick = 0;
        int random = 0;
        if (bonus != null) {
            SlotChoiceFSBonusResult baseChoiceFSBonusResult = (SlotChoiceFSBonusResult) bonus;
            freeSpinsPick = baseChoiceFSBonusResult.getFsPick();
            freespinType = baseChoiceFSBonusResult.getFsType();

            if (reqPickIndex != null && reqPickIndex.length >= 1) {
                int pickIndex = reqPickIndex[0];
                int pickResult = freeSpinsPick[pickIndex];

                if (pickIndex == 0) {
                    freespinType = SlotChoiceFSBonusResult.FREE_SPIN_TYPE_1;
                } else if (pickIndex == 1) {
                    freespinType = SlotChoiceFSBonusResult.FREE_SPIN_TYPE_2;
                } else if (pickIndex == 2) {
                    freespinType = SlotChoiceFSBonusResult.FREE_SPIN_TYPE_3;
                }
                random = computeRandomIDByFSType(freespinType);

                bonusStatus = GameConstant.SLOT_GAME_BONUS_STATUS_COMPLETE;
                List<String> nextScenes = gameSessionBean.getHitSceneLeftList();
                if (nextScenes == null) {
                    nextScenes = new ArrayList<>();
                    gameSessionBean.setHitSceneLeftList(nextScenes);
                }
                nextScenes.add(0, "freeSpin");
                int freeSpinMultiplier = 1;
                if (gameSessionBean.getSlotGameCount() == 1) {
                    gameSessionBean.getSlotSpinResult().setTriggerFsCounts(pickResult);
                    gameSessionBean.getSlotSpinResult().setFsMul(freeSpinMultiplier);
                } else {
                    gameSessionBean.getSlotSpinResult4Multi().get(0).setTriggerFsCounts(pickResult);
                    gameSessionBean.getSlotSpinResult4Multi().get(0).setFsMul(freeSpinMultiplier);
                }

            }
            result = new SlotChoiceFSBonusResult();
            result.setFsPick(freeSpinsPick);
            result.setFsType(freespinType);
            result.setBonusPlayStatus(bonusStatus);
            result.setPickIndexInfos(reqPickIndex);
            result.setTotalPay(totalPay);
            result.setPayForPickIndex(payForPick);
            result.setRandomIndex4FS(random);
        }
        return result;
    }

    protected int computeRandomIDByFSType(int freeSpinType) {
        return -1;
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

            SlotChoiceFSBonusResult bonusTemp = (SlotChoiceFSBonusResult) bonus;
            int[] freeSpinOrBonusPick = bonusTemp.getFsPick();

            if (reqPickIndex.length != 1 || reqPickIndex[0] < 0 || reqPickIndex[0] >= freeSpinOrBonusPick.length) {
                throw new InvalidPlayerInputException();
            }
        }
    }

}
