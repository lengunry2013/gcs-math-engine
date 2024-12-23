package com.gcs.game.engine.slots.utils.reels;

import java.util.Map;

public class ReelsBean {

    private String reelsFileName = "";

    private String model = "";

    private int payback = 0;

    private int[][] reels = null;

    private int[][] reelsWeight = null;

    private int[][] fsReels = null;

    private int[][] fsReelsWeight = null;

    private int[] initReelsPosition = null;

    private Map<String, int[][]> otherReelsMap = null;

    private Map<String, int[][]> otherReelsWeightMap = null;

    private int reelsCount = 0;

    public ReelsBean() {
    }

    public String getReelsFileName() {
        return reelsFileName;
    }

    public void setReelsFileName(String reelsFileName) {
        this.reelsFileName = reelsFileName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPayback() {
        return payback;
    }

    public void setPayback(int payback) {
        this.payback = payback;
    }

    public int[][] getReels() {
        return reels;
    }

    public void setReels(int[][] reels) {
        this.reels = reels;
    }

    public int[][] getReelsWeight() {
        return reelsWeight;
    }

    public void setReelsWeight(int[][] reelsWeight) {
        this.reelsWeight = reelsWeight;
    }

    public int[][] getFsReels() {
        return fsReels;
    }

    public void setFsReels(int[][] fsReels) {
        this.fsReels = fsReels;
    }

    public int[][] getFsReelsWeight() {
        return fsReelsWeight;
    }

    public void setFsReelsWeight(int[][] fsReelsWeight) {
        this.fsReelsWeight = fsReelsWeight;
    }

    public int[] getInitReelsPosition() {
        return initReelsPosition;
    }

    public void setInitReelsPosition(int[] initReelsPosition) {
        this.initReelsPosition = initReelsPosition;
    }

    public int getReelsCount() {
        return reelsCount;
    }

    public void setReelsCount(int reelsCount) {
        this.reelsCount = reelsCount;
    }

    public Map<String, int[][]> getOtherReelsMap() {
        return otherReelsMap;
    }

    public void setOtherReelsMap(Map<String, int[][]> otherReelsMap) {
        this.otherReelsMap = otherReelsMap;
    }

    public Map<String, int[][]> getOtherReelsWeightMap() {
        return otherReelsWeightMap;
    }

    public void setOtherReelsWeightMap(Map<String, int[][]> otherReelsWeightMap) {
        this.otherReelsWeightMap = otherReelsWeightMap;
    }
}
