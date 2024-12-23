package com.gcs.game.engine.slots.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gcs.game.vo.BaseGameLogicBean;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SlotGameLogicBean extends BaseGameLogicBean {
    private static ObjectMapper jsonMapper = new ObjectMapper().registerModule(new SimpleModule().addDeserializer(SlotBonusResult.class, new SlotBonusResultDeserializer())).registerModule(new SimpleModule().addDeserializer(SlotSpinResult.class, new SlotSpinResultDeserializer()));
    private static ObjectReader jsonReader = jsonMapper.readerFor(SlotGameLogicBean.class);
    private long bet = 1;
    private long lines = 1;

    private SlotSpinResult slotSpinResult = null;

    private List<SlotSpinResult> slotFsSpinResults = null;

    private SlotBonusResult slotBonusResult = null;

    private List<SlotBonusResult> slotBonusResultList = null;

    private String slotBsAsset = "";

    private String nextScenes = null;

    private String lastScenes = null;

    private int fsCountLeft = 0;

    private int[] fsHitCounts = null;

    private List<String> hitSceneLeftList = null;

    private boolean isRespin = false;

    private int respinCountsLeft = 0;

    private int slotGameCount = 1;
    private int baseReelsType = 0;
    private Map<String, CollectInfo> summation = new HashMap();

    private Map<String, CollectInfo> consumedSummation = new HashMap();

    private List<SlotSpinResult> slotSpinResult4Multi = null;

    private List<List<SlotSpinResult>> slotFsSpinResults4Multi = null;

    public CollectInfo getSummation(String key) {
        if (this.summation == null) {
            this.summation = new HashMap<>();
        }
        return this.summation.get(key);
    }

    public void setSummation(String key, CollectInfo accumulation) {
        if (this.summation == null) {
            this.summation = new HashMap<>();
        }
        this.summation.put(key, accumulation);
    }


    public static SlotGameLogicBean deserialize(String jsonStr) throws JsonProcessingException {
        return jsonReader.readValue(jsonStr);
    }

}
