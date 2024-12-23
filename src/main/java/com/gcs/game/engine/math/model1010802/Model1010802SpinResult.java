package com.gcs.game.engine.math.model1010802;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import lombok.Data;

import java.util.List;

@Data
public class Model1010802SpinResult extends SlotSpinResult {

    private List<Integer> remainPositions = null;

    private List<Integer> wildPositionsOnReel = null;

    private List<Integer> wildFeatureTypes = null;

    private int[] respinPositions = null;

    private long collectWild = 0;
    private int fsReelsType = 0;
    //进入respin的乘积
    private int respinNextMul = 1;
    //fs下一次乘积
    private int fsNextMul = 1;
    private boolean isTriggerCollectWild = false;

}
