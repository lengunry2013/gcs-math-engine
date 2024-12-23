package com.gcs.game.engine.keno.vo;

import com.gcs.game.vo.BaseGameLogicBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KenoGameLogicBean extends BaseGameLogicBean {
    //private static ObjectMapper jsonMapper = new ObjectMapper().registerModule(new SimpleModule().addDeserializer(PokerResult.class, new PokerResultDeserializer()));
    //private static ObjectReader jsonReader = jsonMapper.readerFor(KenoGameLogicBean.class);

    private long bet = 1;
    private long lines = 1;
    private KenoResult kenoResult = null;

    private List<KenoResult> kenoFsResult = null;

    private String nextScenes = null;

    private String lastScenes = null;

    private int fsCountLeft = 0;

    private int[] fsHitCounts = null;

    private List<String> hitSceneLeftList = null;

    private boolean isRespin = false;

    private int respinCountsLeft = 0;

    /*public static KenoGameLogicBean deserialize(String jsonStr) throws JsonProcessingException {
        return jsonReader.readValue(jsonStr);
    }*/


}
