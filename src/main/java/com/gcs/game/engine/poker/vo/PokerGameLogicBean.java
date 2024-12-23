package com.gcs.game.engine.poker.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gcs.game.vo.BaseGameLogicBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PokerGameLogicBean extends BaseGameLogicBean {
    private static ObjectMapper jsonMapper = new ObjectMapper().registerModule(new SimpleModule().addDeserializer(PokerBonusResult.class, new PokerBonusResultDeserializer())).registerModule(new SimpleModule().addDeserializer(PokerResult.class, new PokerResultDeserializer()));
    private static ObjectReader jsonReader = jsonMapper.readerFor(PokerGameLogicBean.class);

    private long bet = 1;
    private long lines = 1;
    private PokerResult pokerResult = null;

    private List<PokerResult> pokerFsResult = null;

    private PokerBonusResult pokerBonusResult = null;

    private List<PokerBonusResult> pokerBonusResultsList = null;

    private String pokerBsAsset = "";

    private String nextScenes = null;

    private String lastScenes = null;

    private int fsCountLeft = 0;

    private int[] fsHitCounts = null;

    private List<String> hitSceneLeftList = null;

    private boolean isRespin = false;

    private int respinCountsLeft = 0;

    public static PokerGameLogicBean deserialize(String jsonStr) throws JsonProcessingException {
        return jsonReader.readValue(jsonStr);
    }


}
