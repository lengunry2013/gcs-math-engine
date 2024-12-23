package com.gcs.game.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcs.game.engine.blackJack.vo.BlackJackBetInfo;
import com.gcs.game.vo.InputInfo;
import com.gcs.game.vo.MathModels;
import com.gcs.game.vo.MathTypes;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class StringUtil {
    public static String arrayToHexStr(int[] array) {
        StringBuilder result = new StringBuilder();
        if (array != null) {
            for (int number : array) {
                if (number < 16) {
                    result.append("0").append(Integer.toHexString(number));
                } else {
                    result.append(Integer.toHexString(number));
                }

            }
        }
        return result.toString();
    }

    public static int[] hexStrToArray(String str) {
        int[] result = new int[str.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
        }
        return result;
    }

    public static int[] ListToIntegerArray(List<Integer> list) {
        int[] result = null;
        if (list != null) {
            result = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                result[i] = list.get(i);
            }
        }
        return result;
    }

    public static List<Integer> IntegerArrayToList(int[] arrays) {
        List<Integer> list = null;
        if (arrays != null) {
            list = new ArrayList<>();
            for (int element : arrays) {
                list.add(element);
            }
        }
        return list;
    }

    public static boolean contains(int[] array, int value) {
        boolean result = false;
        if (array != null && array.length > 0) {
            for (int temp : array) {
                if (temp == value) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public static List<Long> LongArrayToList(long[] arrays) {
        List<Long> list = null;
        if (arrays != null) {
            list = new ArrayList<>();
            for (long element : arrays) {
                list.add(element);
            }
        }
        return list;
    }

    public static int[] changeStrToArray(String value, String splitStr) {
        int[] result = null;
        if (value != null && !"".equals(value.trim())) {
            try {
                String[] balls = value.trim().split(splitStr);
                if (balls != null) {
                    result = new int[balls.length];
                    for (int i = 0; i < balls.length; i++) {
                        result[i] = Integer.parseInt(balls[i].trim());
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return result;
    }

    public static String doubleToString(double num) {
        DecimalFormat temp = new DecimalFormat("0.00");
        return temp.format(num);
    }

    public static long doubleMul(double num, long value) {
        BigDecimal bd1 = BigDecimal.valueOf(num);
        BigDecimal bd2 = BigDecimal.valueOf(value);
        return bd1.multiply(bd2).longValue();
    }

    public static void main(String[] args) {
        /*List<MathTypes> list = new ArrayList<>();
        MathTypes mathTypes1 = new MathTypes();
        mathTypes1.setMathType("dsf");
        mathTypes1.setDescription("dd");
        list.add(mathTypes1);
        MathTypes mathTypes2 = new MathTypes();
        mathTypes2.setMathType("ff1");
        mathTypes2.setDescription("ff2");
        list.add(mathTypes2);
        Map<String, Object> res = new TreeMap<>();
        res.put("mathTyes", list);
        List<MathModels> list2 = new ArrayList<>();
        String str = JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
        System.out.println(str);
        double num=2.5;
        long bet=10000;
        long value = doubleMul(num, bet);
        System.out.println(value);*/
        String str = "1731073230";
        int[] arr = hexStrToArray(str);
        int[] temp = new int[arr.length];
        System.out.println(JSON.toJSONString(arr));
        int index = 0;
        for (int card : arr) {
            temp[index] = card % 13;
            index++;
        }
        System.out.println(JSON.toJSONString(temp));
    }


}
