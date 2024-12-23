package com.gcs.game.engine.poker.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PokerBonusResultDeserializer extends JsonDeserializer<PokerBonusResult> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PokerBonusResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        PokerBonusResult result;
        JsonNode jNode = jp.getCodec().readTree(jp);
        if (jNode.findValue("roundsReward") != null) {
            result = objectMapper.treeToValue(jNode, PokerROrBBonusResult.class);
        } else {
            result = objectMapper.treeToValue(jNode, PokerBonusResult.class);
        }
        return result;
    }

}
