package com.gcs.game.engine.slots.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SlotBonusResultDeserializer extends JsonDeserializer<SlotBonusResult> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SlotBonusResult deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        SlotBonusResult result;
        JsonNode jNode = jp.getCodec().readTree(jp);
        /*if (jNode.findValue("jungleTourMap") != null) {
            result = objectMapper.treeToValue(jNode, BaseJungleTourWithSIPBonusResult.class);
        } else if (jNode.findValue("bonusType") != null) {
            result = objectMapper.treeToValue(jNode, BaseMultiSlotFsOrPickBonusResult.class);
        } else if (jNode.findValue("freeSpinsPick") != null) {
            result = objectMapper.treeToValue(jNode, BaseChoiceFSBonusResult.class);
        } else */
        if (jNode.findValue("fsType") != null) {
            result = objectMapper.treeToValue(jNode, SlotChoice2FsOrPickBonusResult.class);
        } else if (jNode.findValue("pickCharacters") != null && jNode.findValue("pickPays") != null) {
            result = objectMapper.treeToValue(jNode, SlotPickTerminatorBonusResult.class);
        }
       /* else if (jNode.findValue("freeSpinOrBonusPick") != null) {
            result = objectMapper.treeToValue(jNode, BaseChoiceFsOrMatchBonusResult.class);
        } else if (jNode.findValue("pickCharacters") != null) {
            result = objectMapper.treeToValue(jNode, BaseChoiceBonusResult.class);
        } else if (jNode.findValue("pickRoundFlags") != null) {
            result = objectMapper.treeToValue(jNode, BasePickWithSIPBonusResult.class);
        } else if (jNode.findValue("cardList") != null) {
            result = objectMapper.treeToValue(jNode, BaseHighOrLowBonusResult.class);
        } else if (jNode.findValue("pickAwards") != null) {
            result = objectMapper.treeToValue(jNode, BasePickAwardBonusResult.class);
        } else if (jNode.findValue("bankerCardList") != null) {
            result = objectMapper.treeToValue(jNode, BaseBaccaratResult.class);
        } else if (jNode.findValue("pendingJackpotID") != null) {
            result = objectMapper.treeToValue(jNode, ProgressiveJackpotBonusResult.class);
        } */
        else {
            result = objectMapper.treeToValue(jNode, SlotBonusResult.class);
        }
        return result;
    }

}
