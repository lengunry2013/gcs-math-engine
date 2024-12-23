package com.gcs.game.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gcs.game.engine.blackJack.vo.BlackJackGameLogicBean;
import com.gcs.game.engine.keno.vo.KenoGameLogicBean;
import com.gcs.game.engine.poker.vo.PokerGameLogicBean;
import com.gcs.game.engine.slots.vo.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseGameLogicBean {
    private String engineVersion = "DEMO";

    private String mmID = "";

/*    //poker/roulette/slots
    private String mathType = "";*/

    private String jackpotGroupCode = "";

    private long denom = 1;

    private long sumBetCredit = 0;

    private long sumBetBalance = 0;

    private long sumWinCredit = 0;

    private long sumWinBalance = 0;

    private long betForCurrentStep = 0;
    private long payForCurrentStep = 0;

    private int gamePlayStatus = 0;

    private int percentage = 0;

    private PgJackpotInfo pgJackpotInfo = null;


    public static BaseGameLogicBean deserialize(JSONObject gameSessionBeanJson) throws JsonProcessingException {
        if (gameSessionBeanJson != null) {
            String mmID = gameSessionBeanJson.getString("mmID");
            BaseGameLogicBean bean = null;
            if ("GCBJ00101".equalsIgnoreCase(mmID) || "GCBJ00102".equalsIgnoreCase(mmID)) {
                bean = gameSessionBeanJson.toJavaObject(BlackJackGameLogicBean.class);
            } else if ("8140802".equalsIgnoreCase(mmID) || "1260130".equalsIgnoreCase(mmID) || "1010802".equalsIgnoreCase(mmID)) {
                bean = SlotGameLogicBean.deserialize(gameSessionBeanJson.toJSONString());
            } else if ("6080630".equalsIgnoreCase(mmID) || "6060630".equalsIgnoreCase(mmID)) {
                bean = PokerGameLogicBean.deserialize(gameSessionBeanJson.toJSONString());
            } else if ("5070530".equalsIgnoreCase(mmID)) {
                bean = gameSessionBeanJson.toJavaObject(KenoGameLogicBean.class);
            }
            return bean;
        }
        return null;
    }

}
