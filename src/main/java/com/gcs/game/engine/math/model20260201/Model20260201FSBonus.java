package com.gcs.game.engine.math.model20260201;


import com.gcs.game.engine.slots.bonus.BaseChoiceFSBonus;
import com.gcs.game.engine.slots.vo.SlotGameLogicBean;
import com.gcs.game.utils.RandomWeightUntil;
import lombok.extern.slf4j.Slf4j;

public class Model20260201FSBonus extends BaseChoiceFSBonus {

    public static final int FREE_SPIN_TYPE_PERSISTENT_WILD = 3;
    public static final int FREE_SPIN_TYPE_TWO_WILD_REELS = 2;
    public static final int FREE_SPIN_TYPE_THREE_SHIFTING_WILD = 1;

    private static RandomWeightUntil fsGroundRandom = null;

    private static RandomWeightUntil fsLightingRandom = null;

    @Override
    protected int[][] getFreeSpinTimes(SlotGameLogicBean gameSessionBean, int payback) {
        return new int[][]{
                {10, 10, 10}}; // scatter symbol 3 trigger fs times
    }

    protected int[] getGroundScriptFSWeight() {
        return Model20260201.GROUND_FREESPIN_SCRIPT_WEIGHT;
    }

    protected int[] getLightingScriptFSWeight() {
        return Model20260201.LIGHTNING_FREESPIN_SCRIPT_WEIGHT;
    }

    @Override
    protected int getTriggerSymbolNumber() {
        return 11;
    }

    protected int computeRandomIDByFSType(int freeSpinType) {
        int random = 0;
        if (freeSpinType == FREE_SPIN_TYPE_PERSISTENT_WILD) {
            if (fsLightingRandom == null) {
                fsLightingRandom = new RandomWeightUntil(getLightingScriptFSWeight());
            }
            random = fsLightingRandom.getRandomResult();
        } else if (freeSpinType == FREE_SPIN_TYPE_TWO_WILD_REELS) {
            random = -1;
        } else if (freeSpinType == FREE_SPIN_TYPE_THREE_SHIFTING_WILD) {
            if (fsGroundRandom == null) {
                fsGroundRandom = new RandomWeightUntil(getGroundScriptFSWeight());
            }
            random = fsGroundRandom.getRandomResult();
        }
        return random;
    }

}
