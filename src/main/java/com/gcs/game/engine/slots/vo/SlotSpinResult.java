package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SlotSpinResult implements Cloneable {

    private int[] slotReelStopPosition = null;

    private int[] slotDisplaySymbols = null;

    private int[] hitSlotLines = null;

    private int[] hitSlotSymbols = null;

    private int[] hitSlotSymbolsSound = null;

    private int[] hitSlotSymbolCount = null;

    private long[] hitSlotPays = null;

    private int[] hitSlotMuls = null;

    private int[][] hitSlotPositions = null;

    private int[] slotWildReels = null;

    private int[] slotWildPositions = null;

    private long slotPay = 0L;

    private boolean triggerFs = false;

    private int triggerFsCounts = 0;

    private boolean triggerBonus = false;

    private List<String> nextScenes = null;

    private int baseGameMul = 1;

    private int fsMul = 1;

    private boolean triggerRespin = false;

    private int triggerRespinCounts = 0;

    private int spinType = -1;

    @Override
    public SlotSpinResult clone() throws CloneNotSupportedException {
        SlotSpinResult slotSpinResult = (SlotSpinResult) super.clone();
        if (this.slotReelStopPosition != null) {
            slotSpinResult.slotReelStopPosition = slotReelStopPosition.clone();
        }
        if (this.slotDisplaySymbols != null) {
            slotSpinResult.slotDisplaySymbols = slotDisplaySymbols.clone();
        }
        if (this.hitSlotLines != null) {
            slotSpinResult.hitSlotLines = hitSlotLines.clone();
        }
        if (this.hitSlotSymbols != null) {
            slotSpinResult.hitSlotSymbols = hitSlotSymbols.clone();
        }
        if (this.hitSlotSymbolsSound != null) {
            slotSpinResult.hitSlotSymbolsSound = hitSlotSymbolsSound.clone();
        }
        if (this.hitSlotSymbolCount != null) {
            slotSpinResult.hitSlotSymbolCount = hitSlotSymbolCount.clone();
        }
        if (this.hitSlotPays != null) {
            slotSpinResult.hitSlotPays = hitSlotPays.clone();
        }
        if (this.hitSlotPositions != null) {
            slotSpinResult.hitSlotPositions = new int[this.hitSlotPositions.length][];
            for (int i = 0; i < this.hitSlotPositions.length; i++) {
                slotSpinResult.hitSlotPositions[i] = hitSlotPositions[i].clone();
            }
        }
        if (this.slotWildReels != null) {
            slotSpinResult.slotWildReels = slotWildReels.clone();
        }
        if (this.slotWildPositions != null) {
            slotSpinResult.slotWildPositions = slotWildPositions.clone();
        }
        if (this.nextScenes != null) {
            slotSpinResult.nextScenes = new ArrayList<>();
            for (int i = 0; i < this.nextScenes.size(); i++) {
                slotSpinResult.nextScenes.add(this.nextScenes.get(i));
            }
        }
        return slotSpinResult;
    }

}
