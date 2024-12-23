package com.gcs.game.vo;

import lombok.Data;

import java.util.List;

@Data
public class MathModels {
    private String mathType;
    private String mmID;
    private List<Integer> paybacks;
    private List<Long> betSteps;
    private long minBet;
    private long maxBet;
    private long minLine;
    private long maxLine;
    private long reelsCount;
    private long rowsCount;
    private String payType;


}
