package com.gcs.game.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InputInfo {
    private List<List<Integer>> inputHandsCards;
    private List<Integer> inputDealerCards;
    private List<int[]> inputPosition = null;

    private int[] pickCharacters = null;

}
