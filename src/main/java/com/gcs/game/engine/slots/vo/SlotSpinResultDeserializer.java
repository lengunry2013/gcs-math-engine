package com.gcs.game.engine.slots.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcs.game.engine.math.model1260130.Model1260130SpinResult;


import java.io.IOException;

public class SlotSpinResultDeserializer extends JsonDeserializer<SlotSpinResult> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SlotSpinResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        SlotSpinResult result;
        JsonNode jNode = jp.getCodec().readTree(jp);
        if (jNode.findValue("nextFsMul") != null) {
            result = objectMapper.treeToValue(jNode, Model1260130SpinResult.class);
        } else if (jNode.findValue("respinNextMul") != null || jNode.findValue("fsNextMul") != null) {
            result = objectMapper.treeToValue(jNode, Model1260130SpinResult.class);
        } else {
            result = objectMapper.treeToValue(jNode, SlotSpinResult.class);
        }
        return result;
    }

}
