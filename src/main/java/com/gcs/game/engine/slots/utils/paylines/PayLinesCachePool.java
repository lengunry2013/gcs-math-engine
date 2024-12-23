package com.gcs.game.engine.slots.utils.paylines;

import java.util.HashMap;
import java.util.Map;

public class PayLinesCachePool {

    public static final Object payLinesMapSyncObj = new Object();

    public static Map<String, PayLinesBean> payLinesBeanMap = new HashMap<>();

    public static PayLinesBean getPayLines(String fileName) {
        PayLinesBean paylinesBean = payLinesBeanMap.get(fileName);
        if (paylinesBean == null) {
            paylinesBean = PayLinesFileReader.loadPayLines(fileName);
            if (paylinesBean != null) {
                synchronized (payLinesMapSyncObj) {
                    payLinesBeanMap.put(fileName, paylinesBean);
                }
            }
        }
        return paylinesBean;
    }

}
