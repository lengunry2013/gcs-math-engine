package com.gcs.game.engine.keno.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KenoResult {

    private List<Integer> selectNumbers = null;

    private List<Integer> randomNumbers = null;

    private List<List<Integer>> additionsSetsNumbers = null;

    private List<Integer> extraDrawNumbers = null;
    private int BaseMul = 1;
    private long kenoPay = 0;
    private List<Integer> winMul = null;
    private List<Integer> fsCountsList = null;
    private List<Integer> setsMatchCount = null;
    private int mixHitMatchCount = 0;
    private int matchCount = 0;
    private boolean triggerFs = false;

    private int triggerFsCounts = 0;

    private boolean triggerBonus = false;

    private List<String> nextScenes = null;

    private boolean triggerRespin = false;

    private int triggerRespinCounts = 0;
    private int fsMul = 1;

}
