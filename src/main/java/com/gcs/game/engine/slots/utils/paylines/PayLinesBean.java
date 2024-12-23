package com.gcs.game.engine.slots.utils.paylines;

import java.util.HashMap;
import java.util.Map;

public class PayLinesBean {

    private int linesCount = -1;

    private Map<Integer, int[]> paylinesMap = new HashMap<>();

    public int getLinesCount() {
        return linesCount;
    }

    public void setLinesCount(int linesCount) {
        this.linesCount = linesCount;
    }

    public Map<Integer, int[]> getPaylinesMap() {
        return paylinesMap;
    }

    public void setPaylinesMap(Map<Integer, int[]> paylinesMap) {
        this.paylinesMap = paylinesMap;
    }
}
