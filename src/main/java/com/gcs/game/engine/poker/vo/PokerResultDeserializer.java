package com.gcs.game.engine.poker.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PokerResultDeserializer extends JsonDeserializer<PokerResult> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PokerResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        PokerResult result;
        JsonNode jNode = jp.getCodec().readTree(jp);
        result = objectMapper.treeToValue(jNode, PokerResult.class);
        return result;
    }

}
