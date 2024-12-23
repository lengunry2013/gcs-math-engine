package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotSymbolHitResult {

    private int hitSymbol = -1;

    private int hitSymbolSound = -1;

    private int hitLine = -1;

    private int hitCount = -1;

    private long hitPay = 0L;

    private int hitMul = -1;

    private int[] hitPosition = null;

    private boolean triggerFs= false;

    private int triggerFsCounts = 0;

    private boolean triggerBonus = false;

    private String bonusAsset = null;

    private boolean triggerRespin = false;

    private int triggerRespinCounts = 0;
}
