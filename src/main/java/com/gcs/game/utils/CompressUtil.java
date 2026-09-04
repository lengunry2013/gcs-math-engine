package com.gcs.game.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CompressUtil {

    private static final int BITS_4 = 4;
    private static final int BITS_8 = 8;
    private static final int MASK_4 = 0x0F;
    private static final int MASK_8 = 0xFF;

    public static long compressToLong(int[] positions, int multiplier) {
        long result = 0L;
        // 将positions的每个字节放入long中
        for (int pos : positions) {
            if (pos < 0 || pos > 255) {
                log.error("Invalid position:{} ", pos);
                throw new IllegalArgumentException("position值必须在0-255范围内");
            }
            result = convertTo8Bit(result, pos); // 左移8位，然后放入一个字节
        }
        // 放入multiplier
        result = convertTo8Bit(result, multiplier);
        return result;
    }

    public static long compressToLong(long recoverData, int wagerType) {
        // 放入wager type
        return convertTo8Bit(recoverData, wagerType);
    }

    public static long convertTo8Bit(long result, int param) {
        result = (result << 8) | (param & 0xFF); // 左移8位，然后放入一个字节
        return result;
    }

    public static long convertTo4Bit(long result, int param) {
        result = (result << 4) | (param & 0x0F); // 左移4位，然后放入半个字节
        return result;
    }


    // 解压缩方法（如果需要）
    public static void decompressFromLong(long compressed, int[] positions, int[] multiplier) {
        multiplier[0] = (int) (compressed >> 8 & 0xFF);
        if (positions != null) {
            for (int i = positions.length - 1; i >= 0; i--) {
                positions[i] = (int) ((compressed >> (8 * (6 - i))) & 0xFF);
            }
        }
    }

    // 解压缩方法（如果需要）
    public static void decompressFrom2Long(long compressed, int[] positions, int[] multiplier) {
        multiplier[0] = (int) (compressed & 0xFF);
        if (positions != null) {
            for (int i = positions.length - 1; i >= 0; i--) {
                positions[i] = (int) ((compressed >> (8 * (5 - i))) & 0xFF);
            }
        }
    }

    //第一位解压
    public static int decompressWagerType(long compressed) {
        return (int) (compressed & 0xFF);
    }

    public static long compressWith4Bits(int[] fsPos, List<Integer> wildPos) {
        // 确保wildPos有5个值，不足补0
        List<Integer> fixedWildPos = new ArrayList<>(wildPos);
        while (fixedWildPos.size() < 5) {
            fixedWildPos.add(0);
        }
        for (int value : fixedWildPos) {
            if (value < 0 || value > 15) {
                log.error("Invalid wild position:{} ", value);
                throw new IllegalArgumentException("wildPosition值必须在0-15范围内: " + value);
            }
        }
        long result = 0L;
        // 1. 先压缩 slotPos 的5个字节（40位）
        for (int fsPo : fsPos) {
            result = convertTo8Bit(result, fsPo);
        }
        // 2. 压缩 wildPos 的5个4位值（20位）
        for (int wildPo : fixedWildPos) {
            result = convertTo4Bit(result, wildPo); // 0x0F = 00001111，只取低4位
        }

        return result;
    }

    public static void decompressWith4Bits(long compressed,
                                           int[] fsPos,
                                           List<Integer> wildPos) {
        wildPos.clear();
        long temp = compressed;

        // 1. 先提取 wildPos 的5个4位值
        for (int i = 0; i < 5; i++) {
            int value = (int) (temp & 0x0F);
            wildPos.add(0, value);  // 添加到开头（逆序）
            temp >>= 4;
        }
        // 2. 再提取 fsPos 的5个字节
        for (int i = fsPos.length - 1; i >= 0; i--) {
            fsPos[i] = (int) (temp & 0xFF);
            temp >>= 8;
        }

        // 可选：移除末尾的0（如果原始数据不足5个）
        while (!wildPos.isEmpty() && wildPos.get(wildPos.size() - 1) == 0) {
            wildPos.remove(wildPos.size() - 1);
        }
    }

    /**
     * 固定使用3个long压缩数据
     *
     * @param fsPosition 5个值，每个0-255
     * @param scIndex    15或30个值，每个0-15
     * @return 压缩后的3个long数组
     */
    public static long[] compressTo3Longs(int[] fsPosition, int[] scIndex) {
        if (fsPosition.length != 5) {
            throw new IllegalArgumentException("fsPosition长度必须为5");
        }
        if (scIndex.length != 15 && scIndex.length != 30) {
            throw new IllegalArgumentException("scIndex长度必须为15或30");
        }

        // 验证范围
        for (int value : fsPosition) {
            if (value < 0 || value > 255) {
                throw new IllegalArgumentException("fsPosition值必须在0-255: " + value);
            }
        }
        for (int value : scIndex) {
            if (value < 0 || value > 15) {
                throw new IllegalArgumentException("scIndex值必须在0-15: " + value);
            }
        }

        long[] result = new long[3];
        int currentLong = 0;
        int bitOffset = 0;

        // 1. 压缩 fsPosition (5个值，每个8位)
        for (int value : fsPosition) {
            result[currentLong] = (result[currentLong] << 8) | (value & 0xFF);
            bitOffset += 8;
            if (bitOffset == 64) {
                currentLong++;
                bitOffset = 0;
            }
        }

        // 2. 压缩 scIndex (每个值4位，最多30个)
        int maxIndex = Math.min(scIndex.length, 30);
        for (int i = 0; i < maxIndex; i++) {
            int value = scIndex[i];
            result[currentLong] = (result[currentLong] << 4) | (value & 0x0F);
            bitOffset += 4;
            if (bitOffset == 64) {
                currentLong++;
                bitOffset = 0;
            }
        }

        // 3. 如果scIndex不足30个，用0填充剩余位置
        int remaining = 30 - maxIndex;
        for (int i = 0; i < remaining; i++) {
            result[currentLong] = (result[currentLong] << 4) | 0;
            bitOffset += 4;
            if (bitOffset == 64) {
                currentLong++;
                bitOffset = 0;
            }
        }

        // 如果最后一个long有剩余位，左移补0
        if (bitOffset > 0 && currentLong < 3) {
            result[currentLong] = result[currentLong] << (64 - bitOffset);
        }

        return result;
    }

    /**
     * 从3个long解压数据
     */
    public static void decompressFrom3Longs(long[] compressed,
                                            int[] fsPosition,
                                            int[] scIndex) {
        if (compressed.length != 3) {
            throw new IllegalArgumentException("需要3个long");
        }

        // 提取所有位
        StringBuilder allBits = new StringBuilder();
        for (long value : compressed) {
            String binary = String.format("%64s", Long.toBinaryString(value))
                    .replace(' ', '0');
            allBits.append(binary);
        }

        String bits = allBits.toString();

        // 1. 提取 fsPosition (前40位，5个值，每个8位)
        for (int i = 0; i < 5; i++) {
            int start = i * 8;
            String byteStr = bits.substring(start, start + 8);
            fsPosition[i] = Integer.parseInt(byteStr, 2);
        }

        // 2. 提取 scIndex (从第40位开始，每个4位)
        int maxCount = Math.min(scIndex.length, 30);
        for (int i = 0; i < maxCount; i++) {
            int start = 40 + i * 4;
            if (start + 4 <= bits.length()) {
                String nibbleStr = bits.substring(start, start + 4);
                scIndex[i] = Integer.parseInt(nibbleStr, 2);
            }
        }

        // 如果scIndex数组比实际数据大，剩余部分填0
        for (int i = maxCount; i < scIndex.length; i++) {
            scIndex[i] = 0;
        }
    }

    /**
     * 压缩为固定长度的字符串 (3个long = 48位十六进制)
     */
    public static String compressToString(int[] fsPosition, int[] scIndex) {
        long[] compressed = compressTo3Longs(fsPosition, scIndex);
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (long value : compressed) {
            if (index == 2) {
                result.append(Long.toUnsignedString(value));
            } else {
                result.append(Long.toUnsignedString(value)).append("a");
            }
            index++;
        }
        return result.toString();
    }

    /**
     * 从固定长度字符串解压
     */
    public static void decompressFromString(String recoverData,
                                            int[] fsPosition,
                                            int[] scIndex) {
        String[] recoverDataStr = recoverData.split("a");
        if (recoverDataStr.length < 3) {
            throw new IllegalArgumentException("recoverData 数据不对");
        }
        long[] compressed = new long[3];
        for (int i = 0; i < 3; i++) {
            compressed[i] = Long.parseUnsignedLong(recoverDataStr[i]);
        }
        decompressFrom3Longs(compressed, fsPosition, scIndex);
    }

    public static void main(String[] args) {
        /*int[] slotReelStopPosition = {0, 32, 3, 22, 1};
        int baseGameMul = 1;
        int[] scSymbol = new int[]{12, 12, 13, 0, 12};
        int scTriggerIndex = 2;
        long firstPart = compressToLong(slotReelStopPosition, baseGameMul);
        String firstPartStr = String.valueOf(firstPart);
        System.out.println("slots压缩第1个long值: " + firstPart);
        long secondPart = compressToLong(scSymbol, scTriggerIndex);
        //String recoverData = String.format("%016x", firstPart) + String.format("%016x", secondPart);
        String secondPartStr = String.valueOf(secondPart);
        System.out.println("slots压缩第2个long值: " + secondPartStr);
        long finalSecondPart = compressToLong(secondPart, 2);
        String recoverData = firstPartStr + secondPartStr;
        System.out.println("最后压缩的数据recoverData: " + recoverData);*/
        // 解压缩验证
        /*String decodedFirstPart = recoverData.substring(0, 16);
        long decodedFirstLong = Long.parseUnsignedLong(decodedFirstPart, 16);
        int[] decodedPositions = new int[5];
        int[] decodedMultiplier = new int[1];
        decompressFrom2Long(decodedFirstLong, decodedPositions, decodedMultiplier);
        String decodedSecondPart = recoverData.substring(16, 32);
        long decodedSecondLong = Long.parseUnsignedLong(decodedSecondPart, 16);
        int[] decodedScSymbol = new int[5];
        int[] decodedScIndex = new int[1];
        decompressFromLong(decodedSecondLong, decodedScSymbol, decodedScIndex);
        int wagerType = decompressWagerType(decodedSecondLong);
        System.out.println("解压后positions: " + java.util.Arrays.toString(decodedPositions));
        System.out.println("解压后ReelsType: " + decodedMultiplier[0]);
        System.out.println("解压后ScSymbols: " + java.util.Arrays.toString(decodedScSymbol));
        System.out.println("解压后Sc Trigger Index: " + decodedScIndex[0]);
        System.out.println("解压后wager: " + wagerType);*/

      /*  slotReelStopPosition = new int[]{54, 68, 31, 61, 78};
        List<Integer> wildPos = new ArrayList<Integer>();
        wildPos.add(4);
        long press = compressWith4Bits(slotReelStopPosition, wildPos);
        System.out.println("\nfs压缩后的long值: " + press);

        int[] decodedSlotPos = new int[5];
        List<Integer> decodedWildPos = new ArrayList<>();
        press = 239061745790959616L;
        decompressWith4Bits(press, decodedSlotPos, decodedWildPos);

        System.out.println("解压后:");
        System.out.println("slotPos: " + Arrays.toString(decodedSlotPos));
        System.out.println("wildPos: " + decodedWildPos);*/

        /*int[] fsPosition = {24, 13, 30, 48, 3};

        // ============ 测试场景1：scIndex长度为30 ============
        System.out.println("========== 场景1：scIndex长度30 ==========");
        int[] scIndex30 = new int[30];
        scIndex30[6] = 10;

        // 压缩
        long[] compressed30 = CompressUtil.compressTo3Longs(fsPosition, scIndex30);
        System.out.println("需要 " + compressed30.length + " 个long");
        for (int i = 0; i < compressed30.length; i++) {
            System.out.printf("long[%d]: 0x%016x%n", i, compressed30[i]);
        }

        // 转为字符串
        String recoverData30 = CompressUtil.compressToString(fsPosition, scIndex30);
        System.out.println("RecoverData: " + recoverData30);
        System.out.println("长度: " + recoverData30.length());

        // 解压
        int[] decodedFsPos5 = new int[5];
        int[] decodedScIndex30 = new int[30];
        CompressUtil.decompressFromString(recoverData30, decodedFsPos5, decodedScIndex30);
        System.out.println("解压后fs Position: " + Arrays.toString(decodedFsPos5));
        System.out.println("解压后scIndex长度: " + decodedScIndex30.length);
        System.out.println("数据一致性: " + Arrays.equals(scIndex30, decodedScIndex30));
        System.out.println("解压后scIndex: " + Arrays.toString(decodedScIndex30));*/
        int[] positions = {53, 68, 15, 13, 43};
        int[] swSymbols = {0, 0, 0, 0, 0};
        int[] swPaysIndex = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int randomIndex = 0;
        long result = compressIntArrToLong(positions, swSymbols);
        long firstPart = convertTo4Bit(result, randomIndex);
        long secondPart = compressIntArrTo4BitLong(swPaysIndex);
        int wagerType = 0;
        long finalSecondPart = convertTo4Bit(secondPart, wagerType);
        System.out.println("firstPart: " + firstPart);
        System.out.println("finalSecondPart: " + finalSecondPart);
        System.out.println(Long.toUnsignedString(firstPart) + "a" + Long.toUnsignedString(finalSecondPart));

        int[] decodePositions = new int[5];
        int[] decodeSwSymbols = new int[5];
        int[] random = new int[1];
        int[] decodeSwPaysIndex = new int[15];
        int[] decodeWagerType = new int[1];
        decompressFirstLong(firstPart, decodePositions, decodeSwSymbols, random);
        System.out.println("原始 positions: " + Arrays.toString(positions));
        System.out.println("解密 positions=" + Arrays.toString(decodePositions));
        System.out.println("原始 swSymbols: " + Arrays.toString(swSymbols));
        System.out.println("解码 swSymbols: " + Arrays.toString(decodeSwSymbols));
        System.out.println("原始 randomIndex: " + randomIndex);
        System.out.println("解码 randomIndex: " + random[0]);

        decompressSecondLong(finalSecondPart, decodeSwPaysIndex, decodeWagerType);
        System.out.println("decodeSwPaysIndex=" + Arrays.toString(decodeSwPaysIndex));
        System.out.println("decodeWagerType=" + Arrays.toString(decodeWagerType));
        //Fs
        int[] fsSwPaysIndex = new int[30];
        fsSwPaysIndex[1] = 1;
        fsSwPaysIndex[10] = 5;
        int[] fsSwMultiData = new int[30];
        fsSwMultiData[1] = 2;
        fsSwMultiData[10] = 3;
        int[] fsColIconData = new int[30];
        fsColIconData[1] = 1;
        String encodeStr = compressTo6LongString(fsSwPaysIndex, fsSwMultiData, fsColIconData);
        int[] decodeFsSwPaysIndex = new int[30];
        int[] decodeFsSwMultiData = new int[30];
        int[] decodeFsColIconData = new int[30];
        decompressFromStringSimple(encodeStr, decodeFsSwPaysIndex, decodeFsSwMultiData, decodeFsColIconData);
        System.out.println("原始 fsSwPaysIndex: " + Arrays.toString(fsSwPaysIndex));
        System.out.println("解密 fsSwPaysIndex=" + Arrays.toString(decodeFsSwPaysIndex));
        System.out.println("原始 fsSwMultiData: " + Arrays.toString(fsSwMultiData));
        System.out.println("解码 fsSwMultiData: " + Arrays.toString(decodeFsSwMultiData));
        System.out.println("原始 fsColIconData: " + Arrays.toString(fsColIconData));
        System.out.println("解码 fsColIconData: " + Arrays.toString(decodeFsColIconData));

    }

    public static long compressIntArrToLong(int[] positions, int[] swSymbols) {
        long result = 0L;
        // 将positions的每个字节放入long中
        for (int pos : positions) {
            if (pos < 0 || pos > 255) {
                log.error("Invalid position:{} ", pos);
                throw new IllegalArgumentException("position值必须在0-255范围内");
            }
            result = convertTo8Bit(result, pos); // 左移8位，然后放入一个字节
        }
        if (swSymbols != null) {
            for (int symbol : swSymbols) {
                result = convertTo4Bit(result, symbol); // 左移4位，然后放入半个字节
            }
        }
        return result;
    }

    public static long compressIntArrTo4BitLong(int[] arrays) {
        long result = 0L;
        if (arrays != null) {
            for (int index : arrays) {
                result = convertTo4Bit(result, index); // 左移4位，然后放入半个字节
            }
        }
        return result;
    }

    /**
     * 从压缩的long中解压出positions、swSymbols和randomIndex
     * 第一个Long结构：positions(40位) + swSymbols(20位) + randomIndex(4位) = 64位
     */
    public static void decompressFirstLong(long compressed, int[] positions, int[] swSymbols, int[] randomIndex) {
        int totalBits = 64;
        // 5*8 + 5*4 + 1*4 = 64
        // 提取positions (5个，每个8位) - 位置0-4
        for (int i = 0; i < 5; i++) {
            positions[i] = extract8Bit(compressed, i, totalBits);
        }

        // 由于前面5个位置是8位占用了10给位置，提取swSymbols (5个，每个4位) - 位置10-14
        for (int i = 0; i < 5; i++) {
            swSymbols[i] = extract4Bit(compressed, i + 10, totalBits);
        }

        // 提取randomIndex (1个，4位) - 15开始
        if (randomIndex != null && randomIndex.length > 0) {
            randomIndex[0] = extract4Bit(compressed, 15, totalBits);
        }
    }

    /**
     * 从压缩的long中解压出swPayIndex数组 (15个值，每个4位)
     * 并提取wagerType (最后4位)
     */
    public static void decompressSecondLong(long compressed, int[] swPayIndex, int[] wagerType) {
        int totalBits = 64; // 15*4 + 1*4 = 64

        // 提取swPayIndex (15个，每个4位) - 位置0-14
        for (int i = 0; i < 15; i++) {
            swPayIndex[i] = extract4Bit(compressed, i, totalBits);
        }

        // 提取wagerType (1个，4位) - 位置15
        if (wagerType != null && wagerType.length > 0) {
            wagerType[0] = extract4Bit(compressed, 15, totalBits);
        }
    }


    /**
     * 从long中提取指定位置的4位值
     */
    public static int extract4Bit(long source, int position, int totalBits) {
        int shift = totalBits - (position + 1) * BITS_4;
        if (shift < 0) {
            throw new IllegalArgumentException("位置超出范围: position=" + position + ", totalBits=" + totalBits);
        }
        return (int) ((source >> shift) & MASK_4);
    }

    /**
     * 从long中提取指定位置的8位值
     */
    public static int extract8Bit(long source, int position, int totalBits) {
        int shift = totalBits - (position + 1) * BITS_8;
        if (shift < 0) {
            throw new IllegalArgumentException("位置超出范围: position=" + position + ", totalBits=" + totalBits);
        }
        return (int) ((source >> shift) & MASK_8);
    }


    public static long[] compressTo6Longs(int[] swPaysIndex, int[] swMultiData, int[] colIconData) {
        // 验证长度
        if (swPaysIndex.length != 30) {
            throw new IllegalArgumentException("swPaysIndex长度必须为30");
        }
        if (swMultiData.length != 30) {
            throw new IllegalArgumentException("swMultiData长度必须为30");
        }
        if (colIconData.length != 30) {
            throw new IllegalArgumentException("colIconData长度必须为30");
        }
        // 验证范围：0-15
        for (int value : swPaysIndex) {
            if (value < 0 || value > 15) {
                throw new IllegalArgumentException("swPaysIndex值必须在0-15: " + value);
            }
        }
        for (int value : swMultiData) {
            if (value < 0 || value > 15) {
                throw new IllegalArgumentException("swMultiData值必须在0-15: " + value);
            }
        }
        for (int value : colIconData) {
            if (value < 0 || value > 15) {
                throw new IllegalArgumentException("colIconData值必须在0-15: " + value);
            }
        }

        long[] result = new long[6];
        int currentLong = 0;
        int bitOffset = 0;

        // 1. 压缩 swPaysIndex (30个值，每个4位)
        for (int i = 0; i < swPaysIndex.length; i++) {
            int value = swPaysIndex[i];
            result[currentLong] = (result[currentLong] << 4) | (value & 0x0F);
            bitOffset += 4;
            if (bitOffset == 64) {
                currentLong++;
                bitOffset = 0;
            }
        }

        // 如果第一个long还有剩余位（实际上30*4=120位，需要2个long，第一个long刚好64位满）
        // 这里不需要额外处理，因为30*4=120，正好2个long（64+56），第二个long还有8位空闲

        // 2. 压缩 swMultiData (30个值，每个4位)
        for (int i = 0; i < swMultiData.length; i++) {
            int value = swMultiData[i];
            result[currentLong] = (result[currentLong] << 4) | (value & 0x0F);
            bitOffset += 4;
            if (bitOffset == 64) {
                currentLong++;
                bitOffset = 0;
            }
        }

        // 3. 压缩 colIconData (30个值，每个4位)
        for (int i = 0; i < swMultiData.length; i++) {
            int value = colIconData[i];
            result[currentLong] = (result[currentLong] << 4) | (value & 0x0F);
            bitOffset += 4;
            if (bitOffset == 64) {
                currentLong++;
                bitOffset = 0;
            }
        }

        // 如果最后一个long有剩余位，左移补0
        if (bitOffset > 0 && currentLong < 6) {
            result[currentLong] = result[currentLong] << (64 - bitOffset);
        }
        return result;
    }

    public static String compressTo6LongString(int[] swPaysIndex, int[] swMultiData, int[] colIconData) {
        long[] compressed = compressTo6Longs(swPaysIndex, swMultiData, colIconData);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < compressed.length; i++) {
            if (i > 0) {
                result.append("a");  // 使用"a"作为分隔符，与之前保持一致
            }
            result.append(Long.toUnsignedString(compressed[i]));
        }
        return result.toString();
    }

    public static void decompressFromStringSimple(String str, int[] swPaysIndex, int[] swMultiData, int[] colIconData) {
        String[] parts = str.split("a");
        if (parts.length != 6) {
            throw new IllegalArgumentException("字符串必须包含6个Long值");
        }

        long[] compressed = new long[6];
        for (int i = 0; i < 6; i++) {
            compressed[i] = Long.parseUnsignedLong(parts[i]);
        }

        // 将所有数据合并到一个Long数组中
        long[] allData = new long[6];
        System.arraycopy(compressed, 0, allData, 0, 6);

        // 从6个Long中提取90个4位值
        int[] values = new int[90];
        int bitPos = 0;
        for (int i = 0; i < 90; i++) {
            int longIndex = bitPos / 64;
            int bitOffset = bitPos % 64;
            values[i] = (int) ((allData[longIndex] >> (64 - bitOffset - 4)) & 0x0F);
            bitPos += 4;
        }

        int len = swPaysIndex.length;
        // 分配到3个数组
        System.arraycopy(values, 0, swPaysIndex, 0, len);
        System.arraycopy(values, len, swMultiData, 0, len);
        System.arraycopy(values, len * 2, colIconData, 0, len);
    }


}
