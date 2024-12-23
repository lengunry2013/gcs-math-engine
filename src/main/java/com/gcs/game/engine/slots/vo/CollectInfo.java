package com.gcs.game.engine.slots.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CollectInfo {

    private long summationValue = 0L;

    private List<Long> summationSteps = new ArrayList<>();

}
