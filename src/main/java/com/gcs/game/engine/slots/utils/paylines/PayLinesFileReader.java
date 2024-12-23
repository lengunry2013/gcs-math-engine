package com.gcs.game.engine.slots.utils.paylines;

import com.gcs.game.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PayLinesFileReader {

    public static final String path = "paylines/";

    public static PayLinesBean loadPayLines(String fileName) {
        PayLinesBean bean = null;
        if (fileName != null && !"".equals(fileName)) {
            String file = path + fileName;
            Properties properties = new Properties();
            FileInputStream fis = null;
            try {
                bean = new PayLinesBean();

                if (new File(file).exists()) {
                    fis = new FileInputStream(file);
                    properties.load(fis);
                } else {
                    InputStream is = PayLinesFileReader.class.getClassLoader().getResourceAsStream(file);
                    properties.load(is);
                }

                int linesCount = Integer.parseInt(properties.getProperty("count"));
                bean.setLinesCount(linesCount);

                Map<Integer, int[]> paylinesMap = new HashMap<>();
                for (int i = 0; i < linesCount; i++) {
                    int line = i + 1;
                    String positionsStr = properties.getProperty("line" + (line));
                    int[] positions = StringUtil.changeStrToArray(positionsStr, ",");
                    paylinesMap.put(line, positions);
                }
                bean.setPaylinesMap(paylinesMap);


            } catch (IOException e) {
                bean = null;
                System.err.println("fail to load paylines file: " + file);
                System.err.println(e);
            }finally {
                try {
                    if(fis != null) fis.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
        return bean;
    }

}
