package com.gcs.game.engine.slots.utils.reels;


import com.gcs.game.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
@Slf4j
public class ReelsFileReader {

    public static final boolean IS_REELS_XML_ENCRYPT = true;

    public static final String path = "reels/";

    public static ReelsBean loadReels(String fileName, String model) {
        ReelsBean reelsBean;

        String file = path + fileName;
        Properties properties = new Properties();
        try {
            reelsBean = new ReelsBean();

            if (new File(file).exists()) {
                properties.loadFromXML(new FileInputStream(file));
            } else {
                String f = ReelsFileReader.class.getClassLoader().getResource("../../" + file).getPath();
                properties.loadFromXML(new FileInputStream(f));
            }

            int reelsCount = Integer.parseInt(
                    properties.getProperty("reels_num", "5"));
            reelsBean.setReelsCount(reelsCount);

            int[] initPosition = StringUtil.changeStrToArray(
                    properties.getProperty("reels_initialize", "1 2 3 4 5"), " ");
            reelsBean.setInitReelsPosition(initPosition);

            int cycleNum = Integer.parseInt(
                    properties.getProperty("cycle_num", "12"));

            int[][] reels = getReels(properties, "reels_", reelsCount, model, cycleNum);
            int[][] reelsWeight = getReelsWeight(properties, "weight_reels_", reelsCount, model, cycleNum);
            int[][] fsReels = getReels(properties, "freespin_reels_", reelsCount, model, cycleNum);
            int[][] fsReelsWeight = getReelsWeight(properties, "freespin_weight_reels_", reelsCount, model, cycleNum);

            reelsBean.setReels(reels);
            reelsBean.setReelsWeight(reelsWeight);
            reelsBean.setFsReels(fsReels);
            reelsBean.setFsReelsWeight(fsReelsWeight);

        } catch (IOException e) {
            reelsBean = null;
            log.error("fail to load reels file: " + file, e);
        }
        return reelsBean;
    }

    public static ReelsBean loadReels(String fileName, String model, List<String> otherReelsKeys) {
        ReelsBean reelsBean;

        String file = path + fileName;
        Properties properties = new Properties();
        try {
            reelsBean = new ReelsBean();

            if (new File(file).exists()) {
                properties.loadFromXML(new FileInputStream(file));
            } else {
                String f = ReelsFileReader.class.getClassLoader().getResource("../../" + file).getPath();
                properties.loadFromXML(new FileInputStream(f));
            }

            int reelsCount = Integer.parseInt(
                    properties.getProperty("reels_num", "5"));
            reelsBean.setReelsCount(reelsCount);

            int[] initPosition = StringUtil.changeStrToArray(
                    properties.getProperty("reels_initialize", "1 2 3 4 5"), " ");
            reelsBean.setInitReelsPosition(initPosition);

            int cycleNum = Integer.parseInt(
                    properties.getProperty("cycle_num", "12"));

            int[][] reels = getReels(properties, "reels_", reelsCount, model, cycleNum);
            int[][] reelsWeight = getReelsWeight(properties, "weight_reels_", reelsCount, model, cycleNum);
            int[][] fsReels = getReels(properties, "freespin_reels_", reelsCount, model, cycleNum);
            int[][] fsReelsWeight = getReelsWeight(properties, "freespin_weight_reels_", reelsCount, model, cycleNum);

            Map<String, int[][]> otherReelsMap = new HashMap<>();
            Map<String, int[][]> otherReelWeightMap = new HashMap<>();
            if (otherReelsKeys != null) {
                for (String key : otherReelsKeys) {
                    int[][] otherReels = getReels(properties, key + "_reels_", reelsCount, model, cycleNum);
                    int[][] otherWeights = getReelsWeight(properties, key + "_weight_reels_", reelsCount, model, cycleNum);
                    otherReelsMap.put(key, otherReels);
                    otherReelWeightMap.put(key, otherWeights);
                }
            }

            reelsBean.setReels(reels);
            reelsBean.setReelsWeight(reelsWeight);
            reelsBean.setFsReels(fsReels);
            reelsBean.setFsReelsWeight(fsReelsWeight);
            reelsBean.setOtherReelsMap(otherReelsMap);
            reelsBean.setOtherReelsWeightMap(otherReelWeightMap);

        } catch (IOException e) {
            reelsBean = null;
            log.error("fail to load reels file: " + file, e);
        }
        return reelsBean;
    }

    public static int[][] getReels(Properties properties, String prefix, int reelsCount, String model, int cycleNum) {
        if (!properties.containsKey(prefix + "1")) {
            return null;
        }
        int[][] reels = new int[reelsCount][];
        if (properties != null) {
            for (int i = 0; i < reelsCount; i++) {
                String tempStr = properties.getProperty(prefix + String.valueOf(i + 1));
                if (IS_REELS_XML_ENCRYPT) {
                    tempStr = format(tempStr, model, cycleNum);
                }
                int[] reel = StringUtil.changeStrToArray(tempStr, " ");
                if (reel != null) {
                    reels[i] = reel;
                } else {
                    reels = null;
                    break;
                }
            }

        }
        return reels;
    }

    public static int[][] getReelsWeight(Properties properties, String prefix, int reelsCount, String model, int cycleNum) {
        if (!properties.containsKey(prefix + "1")) {
            return null;
        }
        int[][] reels = new int[reelsCount][];
        if (properties != null) {
            for (int i = 0; i < reelsCount; i++) {
                String tempStr = properties.getProperty(prefix + String.valueOf(i + 1));
                int[] reel = StringUtil.changeStrToArray(tempStr, " ");
                if (reel != null) {
                    reels[i] = reel;
                } else {
                    reels = null;
                    break;
                }
            }

        }
        return reels;
    }

    public static String format(String input, String model, int cycleNum) {
        String s = "";
        int tmp;
        String[] str1 = input.split(" ");
        for (int i = 0; i < str1.length; i++) {
            tmp = Integer.parseInt(str1[i])
                    - Integer.parseInt(model.substring(i % model.length(), i
                    % model.length() + 1));
            if (tmp <= 0) {
                tmp += cycleNum;
            }
            if (i == 0) {
                s += tmp;
            } else {
                s += " " + tmp;
            }
        }
        log.debug("reels===>>>" + s);
        return s;

    }

    public static void main(String[] args) {
        ReelsBean rb = loadReels("reels.model1180130.96.50.xml", "1010133");
        System.out.println("t");
    }


}
