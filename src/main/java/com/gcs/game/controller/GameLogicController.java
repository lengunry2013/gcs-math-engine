package com.gcs.game.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcs.game.engine.common.Environment;
import com.gcs.game.engine.GameEngineFactory;
import com.gcs.game.engine.IGameEngine;
import com.gcs.game.engine.common.cache.GameMathCacheStorage;
import com.gcs.game.exception.*;
import com.gcs.game.vo.BaseGameFeature;
import com.gcs.game.vo.PlayerInputInfo;
import com.gcs.game.vo.BaseGameLogicBean;
import com.gcs.game.vo.InputInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class GameLogicController {

    private String apiKey;
    private String apiSecret;

    public GameLogicController() {
       /* PropertiesLoader loader = new PropertiesLoader("mathConfig.properties");

        String encApiKey = loader.getProperty("gcs.engine.server.api.key");
        String encApiSecret = loader.getProperty("gcs.engine.server.api.secret");

        //TODO decrypt key
        apiKey = Security.decrypt(encApiKey, "AVIVSYEK");
        apiSecret = Security.decrypt(encApiSecret, "AVIVSYEK");*/
    }

    @GetMapping("/api/math-engine/math-models")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMathModels(
            @RequestHeader(name = "tokenID") String token)
            throws ServiceOfflineException, AuthenticationFailException {
        log.debug(">>> Request math-models Info.");
        log.debug(">>> token {}", token);
        Environment.checkOffline();
        if (!checkAuthentication(token)) throw new AuthenticationFailException();

        Map<String, Object> res = new TreeMap<>();
        List<Object> mathModelsOut = new ArrayList<>();
        List<Object> mathTypeOut = new ArrayList<>();
        GameMathCacheStorage.getInstance().getMathModelsCache().forEach((key, value) -> mathModelsOut.add(value));
        GameMathCacheStorage.getInstance().getMathTypesCache().forEach((key, value) -> mathTypeOut.add(value));
        res.put("mathModels", mathModelsOut);
        res.put("mathTypes", mathTypeOut);
        log.debug("<<< Response Msg: {}", res);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @GetMapping("/api/math-engine/{mmID}/{payback}/feature")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMathFeature(
            @RequestHeader(name = "tokenID") String token,
            @PathVariable("mmID") String mmID, @PathVariable("payback") int payback
    ) throws ServiceOfflineException, AuthenticationFailException {
        log.debug(">>> Request feature.");
        log.debug(">>> token {}", token);
        log.debug(">>> mmID {}", mmID);
        log.debug(">>> payback {}", payback);
        Environment.checkOffline();
        if (!checkAuthentication(token)) throw new AuthenticationFailException();

        Map<String, Object> res = new TreeMap<>();
        if (!GameMathCacheStorage.getInstance().isValidMathInfo(mmID, payback)) {
            res.put("error", "MathModel or Payback Not found.");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }

        IGameEngine engine = GameEngineFactory.getGameEngine(payback, mmID);
        BaseGameFeature result;
        result = engine.loadGameFeature();

        res.put("mathFeature", result);
        log.debug("<<< Response Msg: {}", JSON.toJSONString(res, SerializerFeature.WriteMapNullValue));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/api/math-engine/{mmID}/{payback}/default")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDefaultGameLogicData(
            @RequestHeader(name = "tokenID") String token,
            @PathVariable("mmID") String mmID, @PathVariable("payback") int payback
    ) throws ServiceOfflineException, AuthenticationFailException {
        log.debug(">>> Request default.");
        log.debug(">>> token {}", token);
        log.debug(">>> mmID {}", mmID);
        log.debug(">>> payback {}", payback);
        Environment.checkOffline();
        if (!checkAuthentication(token)) throw new AuthenticationFailException();

        Map<String, Object> res = new TreeMap<>();
        if (!GameMathCacheStorage.getInstance().isValidMathInfo(mmID, payback)) {
            res.put("error", "MathModel or Payback Not found.");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }

        IGameEngine engine = GameEngineFactory.getGameEngine(payback, mmID);
        BaseGameLogicBean result = null;
        try {
            result = engine.getDefaultGameLogicData();
        } catch (InvalidGameStateException e) {
            e.printStackTrace();
            res.put("error", "Invalid Game State");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        res.put("gameLogicData", result);
        log.debug("<<< Response Msg: {}", JSON.toJSONString(res, SerializerFeature.WriteMapNullValue));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/api/math-engine/{mmID}/{payback}/game-start")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> gameStart(
            @RequestHeader(name = "tokenID") String token,
            @PathVariable("mmID") String mmID, @PathVariable("payback") int payback,
            @RequestBody JSONObject gameLogicInfo
    ) throws ServiceOfflineException, AuthenticationFailException {
        log.debug(">>> Request game-start.");
        log.debug(">>> token {}", token);
        log.debug(">>> mmID {}", mmID);
        log.debug(">>> payback {}", payback);
        Environment.checkOffline();
        if (!checkAuthentication(token)) throw new AuthenticationFailException();

        Map<String, Object> res = new TreeMap<>();
        if (!GameMathCacheStorage.getInstance().isValidMathInfo(mmID, payback)) {
            res.put("error", "MathModel or Payback Not found.");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        BaseGameLogicBean result;
        IGameEngine engine = GameEngineFactory.getGameEngine(payback, mmID);
        try {
            Map gameLogicData = (Map) gameLogicInfo.get("gameLogicData");
            JSONObject gameSessionBeanJson = null;
            if (gameLogicData != null) {
                gameSessionBeanJson = new JSONObject(gameLogicData);
            }
            BaseGameLogicBean gameLogicBean = BaseGameLogicBean.deserialize(gameSessionBeanJson);
            Map gameLogicRequest = (Map) gameLogicInfo.get("gameLogicRequest");
            Map inputInfoMap = (Map) gameLogicInfo.get("inputInfo");
            ObjectMapper mapper = new ObjectMapper();
            InputInfo inputInfo = mapper.convertValue(inputInfoMap, InputInfo.class);

            //game start
            engine.init(gameLogicBean);
            result = engine.gameStart(gameLogicBean, gameLogicRequest, inputInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            res.put("error", "Invalid Request Json");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
            res.put("error", "Invalid Request Json");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (InvalidBetException e) {
            e.printStackTrace();
            res.put("error", "Invalid Bet Update");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (InvalidGameStateException e) {
            e.printStackTrace();
            res.put("error", "Invalid Game State");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        res.put("gameLogicData", result);
        res.put("engineContextMap", engine.getEngineContext());
        log.debug("<<< Response Msg: {}", JSON.toJSONString(res, SerializerFeature.WriteMapNullValue));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/api/math-engine/{mmID}/{payback}/game-process")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> gameProgress(
            @RequestHeader(name = "tokenID") String token,
            @PathVariable("mmID") String mmID, @PathVariable("payback") int payback,
            @RequestBody JSONObject gameLogicInfo
    ) throws ServiceOfflineException, AuthenticationFailException {
        log.debug(">>> Request game-process.");
        log.debug(">>> token {}", token);
        log.debug(">>> mmID {}", mmID);
        log.debug(">>> payback {}", payback);
        Environment.checkOffline();
        if (!checkAuthentication(token)) throw new AuthenticationFailException();

        Map<String, Object> res = new TreeMap<>();
        if (!GameMathCacheStorage.getInstance().isValidMathInfo(mmID, payback)) {
            res.put("error", "MathModel or Payback Not found.");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }

        IGameEngine engine = GameEngineFactory.getGameEngine(payback, mmID);
        BaseGameLogicBean result = null;
        try {
            JSONObject gameSessionBeanJson = new JSONObject((Map) gameLogicInfo.get("gameLogicData"));
            BaseGameLogicBean gameLogicBean = BaseGameLogicBean.deserialize(gameSessionBeanJson);
            Map gameLogicRequest = (Map) gameLogicInfo.get("gameLogicRequest");
            Map engineContextRequest = (Map) gameLogicInfo.get("engineContextMap");
            Map playerInputInfoMap = (Map) gameLogicInfo.get("playerInputInfo");
            Map inputInfoMap = (Map) gameLogicInfo.get("inputInfo");

            ObjectMapper mapper = new ObjectMapper();
            PlayerInputInfo playerInputInfo = mapper.convertValue(playerInputInfoMap, PlayerInputInfo.class);
            InputInfo inputInfo = mapper.convertValue(inputInfoMap, InputInfo.class);
            //game progress
            engine.init(gameLogicBean);
            result = engine.gameProgress(gameLogicBean, gameLogicRequest, playerInputInfo, engineContextRequest, inputInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            res.put("error", "Invalid Request Json");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
            res.put("error", "Invalid Request Json");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (InvalidPlayerInputException e) {
            e.printStackTrace();
            res.put("error", "Invalid Player Input");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } catch (InvalidGameStateException e) {
            e.printStackTrace();
            res.put("error", "Invalid Game State");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        res.put("gameLogicData", result);
        res.put("engineContextMap", engine.getEngineContext());
        log.debug("<<< Response Msg: {}", JSON.toJSONString(res, SerializerFeature.WriteMapNullValue));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    protected boolean checkAuthentication(String token) {
        return true;
        /*String keyAndTotp = token.replace("Bearer ", "");
        String[] values = keyAndTotp.split("#");
        if(values.length!=2) return false;
        return TOTPUtil.verifyTOTPFlexibility(this.apiKey, this.apiSecret, values[1]);*/
    }

}
