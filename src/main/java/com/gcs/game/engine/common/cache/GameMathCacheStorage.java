package com.gcs.game.engine.common.cache;

import com.gcs.game.utils.StringUtil;
import com.gcs.game.vo.GameInfo;
import com.gcs.game.vo.MathModels;
import com.gcs.game.vo.MathTypes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GameMathCacheStorage {
    //volatile除了保证内存可见性，还可以防止指令重排序
    private static volatile GameMathCacheStorage instant;

    public static GameMathCacheStorage getInstance() {
        if (instant == null) {
            synchronized (GameMathCacheStorage.class) {
                if (instant == null) {
                    instant = new GameMathCacheStorage();
                }
            }
        }
        return instant;
    }

    @Getter
    private Map<String, MathModels> mathModelsCache;
    @Getter
    private Map<String, MathTypes> mathTypesCache;

    public void init() {
        mathModelsCache = new ConcurrentHashMap<>();
        mathTypesCache = new ConcurrentHashMap<>();
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("com.gcs.game.engine.math"));
        Set<Class<?>> maths = reflections.getTypesAnnotatedWith(GameInfo.class);
        Class<?> tempMath = null;
        try {
            for (Class<?> math : maths) {
                tempMath = math;
                GameInfo annotation = math.getAnnotation(GameInfo.class);
                Field mathModelField = math.getDeclaredField("MATH_MODEL");
                String mmID = (String) mathModelField.get(null);
                MathModels mathModel = new MathModels();
                mathModel.setMmID(mmID);
                mathModel.setMathType(annotation.mathType().name());
                mathModel.setPaybacks(StringUtil.IntegerArrayToList(annotation.paybacks()));
                mathModel.setBetSteps(StringUtil.LongArrayToList(annotation.betSteps()));
                mathModel.setMinBet(annotation.minBet());
                mathModel.setMaxBet(annotation.maxBet());
                mathModel.setMinLine(annotation.minLine());
                mathModel.setMaxLine(annotation.maxLine());
                mathModel.setReelsCount(annotation.reelsCount());
                mathModel.setRowsCount(annotation.rowsCount());
                mathModel.setPayType(annotation.payType().name());
                MathTypes mathType = new MathTypes();
                mathType.setDescription(annotation.formFactor());
                mathType.setMathType(annotation.mathType().name());
                mathModelsCache.put(mmID, mathModel);
                mathTypesCache.put(mmID, mathType);
                //TODO
                StringBuilder traceMsg = new StringBuilder();
                traceMsg.append("Math Model Info ").append(mmID).append(" ").append(mathModel.getMathType()).
                        append(" ").append(mathType.getDescription()).append(" ");
                for (int payback : mathModel.getPaybacks()) {
                    traceMsg.append(payback).append("% ");
                }
                log.debug(traceMsg.toString());
            }
        } catch (NoSuchFieldException e) {
            log.error("Failed to get math_model from {}:", tempMath, e);
            //throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("Failed to get math_model from {}:", tempMath, e);
            //throw new RuntimeException(e);
        }
    }

    public boolean isValidMathInfo(String mmID, int payback) {
        if (mathModelsCache.containsKey(mmID)) {
            MathModels math = mathModelsCache.get(mmID);
            return math.getPaybacks().contains(payback);
        }
        return false;
    }

}
