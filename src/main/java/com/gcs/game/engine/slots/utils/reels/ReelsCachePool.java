package com.gcs.game.engine.slots.utils.reels;


import com.gcs.game.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReelsCachePool {

    public static final Object reelsBeanMapSyncObj = new Object();

    public static Map<String, ReelsBean> reelsBeanMap = new HashMap<>();

    public static ReelsBean getReels(String model, int payback) {
        double paybackReal = payback * 1.0 / 100;
        String fileName = "reels.model" + model + "." + StringUtil.doubleToString(paybackReal) + ".xml";
        ReelsBean reelsBean = reelsBeanMap.get(fileName);
        if (reelsBean == null) {
            reelsBean = ReelsFileReader.loadReels(fileName, model);
            setInitReelsBean(reelsBean, fileName, model, payback);
        }
        return reelsBean;
    }

    public static ReelsBean getReels(String model, int payback, List<String> otherReelsKeys) {
        double paybackReal = payback * 1.0 / 100;
        String fileName = "reels.model" + model + "." + StringUtil.doubleToString(paybackReal) + ".xml";
        ReelsBean reelsBean = reelsBeanMap.get(fileName);
        if (reelsBean == null) {
            reelsBean = ReelsFileReader.loadReels(fileName, model, otherReelsKeys);
            setInitReelsBean(reelsBean, fileName, model, payback);
        }
        return reelsBean;
    }

    private static void setInitReelsBean(ReelsBean reelsBean, String fileName, String model, int payback) {
        if (reelsBean != null) {
            reelsBean.setReelsFileName(fileName);
            reelsBean.setModel(model);
            reelsBean.setPayback(payback);

            synchronized (reelsBeanMapSyncObj) {
                reelsBeanMap.put(fileName, reelsBean);
            }
        }
    }

}
