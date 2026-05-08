package com.gcs.game.engine.math.model20260103;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import lombok.Data;

import java.util.List;

@Data
public class Model20260103SpinResult extends SlotSpinResult {
    private int mysterySymbol = 2;

    private int reelsType = 1;

    private int fsType = -1;

    //一局fs可以中奖多次JP bonus level
    private int[] hitLevels = new int[]{-1, -1, -1, -1};

    //在fs中记录每次fs中奖不同字母类型的数量
    private int[] jpBonusLevelsCount = new int[4];

}
