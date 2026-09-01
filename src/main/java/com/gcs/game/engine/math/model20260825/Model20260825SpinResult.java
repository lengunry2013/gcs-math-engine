package com.gcs.game.engine.math.model20260825;

import com.gcs.game.engine.slots.vo.SlotSpinResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Model20260825SpinResult extends SlotSpinResult {
    /*
    swType=1 is A,2 is B,3 is C,4 is AB,5 is AC, 6 is BC,7 is ABC
     */
    private int swType = 0;
    /*
      grid all pays
     */
    private List<long[]> linkBonusSwPays = null;
    /*
      grid Multiplier
     */
    private List<int[]> swMultis = null;
    /*
      grid each collect pays
     */
    private List<Long> colPays = null;
    /*
      grid total collect pays
     */
    private List<Long> colTotalPays = null;
    /*
      grid total collect icons,1 is show collect icon, 0 is not show collect
     */
    private List<int[]> colIcons = null;
    /*
      grid is full balls
     */
    private List<Boolean> isFullBalls = null;
    /*
      grid is new balls or add collect Reset fsTimes=3
     */
    private List<Boolean> isGridTriggerFs = null;
    /*
      grid each spin remaining FS count
     */
    private List<Integer> gridFsLeftCounts = null;
}
