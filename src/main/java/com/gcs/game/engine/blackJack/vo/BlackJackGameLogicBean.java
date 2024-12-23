package com.gcs.game.engine.blackJack.vo;

import com.gcs.game.vo.BaseGameLogicBean;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlackJackGameLogicBean extends BaseGameLogicBean {
    private int currentHandIndex = 0;
    private int splitIndex = 0;
    private List<BlackJackBetInfo> blackJackBetInfos = null;
    private List<BlackJackResult> blackJackResults = null;
    private DealerResult dealerResult = null;
    private List<Integer> gameSteps = null;


}
