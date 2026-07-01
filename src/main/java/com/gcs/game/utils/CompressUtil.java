package com.gcs.game.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CompressUtil {

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
     * 动态压缩：根据scIndex长度决定使用2个还是3个long
     *
     * @param fsPosition 5个值，每个0-255
     * @param scIndex    15或30个值，每个0-15
     * @return long数组（2个或3个）
     */
    public static long[] compressToLongs(int[] fsPosition, int[] scIndex) {
        if (fsPosition.length != 5) {
            throw new IllegalArgumentException("fsPosition长度必须为5");
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

        // 判断需要几个long
        int totalBits = 40 + (scIndex.length * 4);
        int longCount = (totalBits + 63) / 64; // 向上取整

        long[] result = new long[longCount];
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

        // 2. 压缩 scIndex (每个值4位)
        for (int value : scIndex) {
            result[currentLong] = (result[currentLong] << 4) | (value & 0x0F);
            bitOffset += 4;
            if (bitOffset == 64) {
                currentLong++;
                bitOffset = 0;
            }
        }

        // 如果最后一个long不满64位，左移补0（可选）
        if (bitOffset > 0 && bitOffset < 64) {
            result[currentLong] = result[currentLong] << (64 - bitOffset);
        }

        return result;
    }

    /**
     * 解压数据（自动识别长度）
     */
    public static void decompressFromLongs(long[] compressed,
                                           int[] fsPosition,
                                           int[] scIndex) {
        // 先提取所有位
        StringBuilder allBits = new StringBuilder();
        for (long value : compressed) {
            String binary = String.format("%64s", Long.toBinaryString(value))
                    .replace(' ', '0');
            allBits.append(binary);
        }

        String bits = allBits.toString();
        int totalBits = compressed.length * 64;

        // 1. 提取 fsPosition (前40位，5个值，每个8位)
        for (int i = 0; i < 5; i++) {
            int start = i * 8;
            String byteStr = bits.substring(start, start + 8);
            fsPosition[i] = Integer.parseInt(byteStr, 2);
        }

        // 2. 提取 scIndex (从第40位开始，每个4位)
        // 根据剩余位数计算scIndex长度
        int remainingBits = totalBits - 40;
        int scIndexCount = remainingBits / 4;

        // 如果scIndexCount大于scIndex数组长度，取数组长度
        int actualCount = Math.min(scIndexCount, scIndex.length);

        for (int i = 0; i < actualCount; i++) {
            int start = 40 + i * 4;
            if (start + 4 <= totalBits) {
                String nibbleStr = bits.substring(start, start + 4);
                scIndex[i] = Integer.parseInt(nibbleStr, 2);
            }
        }

        // 如果scIndex数组比实际数据大，剩余部分填0
        for (int i = actualCount; i < scIndex.length; i++) {
            scIndex[i] = 0;
        }
    }

    /**
     * 压缩为字符串（统一格式）
     */
    public static String compressToString(int[] fsPosition, int[] scIndex) {
        long[] compressed = compressToLongs(fsPosition, scIndex);
        StringBuilder result = new StringBuilder();

        // 先存储长度信息（使用2位十六进制表示long数量）
        result.append(String.format("%02x", compressed.length));

        // 再存储数据
        for (long value : compressed) {
            result.append(String.format("%016x", value));
        }

        return result.toString();
    }

    /**
     * 从字符串解压
     */
    public static void decompressFromString(String recoverData,
                                            int[] fsPosition,
                                            int[] scIndex) {
        if (recoverData.length() < 2) {
            throw new IllegalArgumentException("RecoverData长度不足");
        }

        // 读取长度信息
        int longCount = Integer.parseInt(recoverData.substring(0, 2), 16);
        if (longCount < 2 || longCount > 3) {
            throw new IllegalArgumentException("无效的long数量: " + longCount);
        }

        // 验证字符串长度
        int expectedLength = 2 + longCount * 16;
        if (recoverData.length() != expectedLength) {
            throw new IllegalArgumentException("RecoverData长度不正确: " + recoverData.length() +
                    ", 期望: " + expectedLength);
        }

        // 提取数据
        long[] compressed = new long[longCount];
        for (int i = 0; i < longCount; i++) {
            int start = 2 + i * 16;
            String part = recoverData.substring(start, start + 16);
            compressed[i] = Long.parseUnsignedLong(part, 16);
        }

        decompressFromLongs(compressed, fsPosition, scIndex);
    }

    public static void main(String[] args) {
        /*int[] slotReelStopPosition = {31, 20, 3, 22, 1};
        int baseGameMul = 1;
        int[] scSymbol = new int[]{0, 12, 13, 0, 12};
        int scTriggerIndex = 2;
        long firstPart = compressToLong(slotReelStopPosition, baseGameMul);
        System.out.println("slots压缩后的long值: " + String.format("%016x", firstPart));
        long secondPart = compressToLong(scSymbol, scTriggerIndex);
        String recoverData = String.format("%016x", firstPart) + String.format("%016x", secondPart);
        String secondPart2 = recoverData.substring(16, 32);
        long secondLong2 = Long.parseUnsignedLong(secondPart2, 16);
        long finalSecondPart = compressToLong(secondLong2, 2);
        System.out.println("slots压缩最终结果: " + String.format("%016x", finalSecondPart));
        recoverData = String.format("%016x", firstPart) + String.format("%016x", finalSecondPart);
        System.out.println("最后压缩的数据recoverData: " + recoverData);
        // 解压缩验证
        String decodedFirstPart = recoverData.substring(0, 16);
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

        int[] fsPosition = {25, 90, 81, 20, 12};

        // ============ 测试场景1：scIndex长度为30 ============
        System.out.println("========== 场景1：scIndex长度30 ==========");
        int[] scIndex30 = new int[30];
        for (int i = 0; i < 30; i++) {
            scIndex30[i] = i % 16;
        }

        // 压缩
        long[] compressed30 = CompressUtil.compressToLongs(fsPosition, scIndex30);
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

        // ============ 测试场景2：scIndex长度为15 ============
        System.out.println("\n========== 场景2：scIndex长度15 ==========");
        int[] scIndex15 = new int[15];
        for (int i = 0; i < 15; i++) {
            scIndex15[i] = i % 16;
        }

        // 压缩
        long[] compressed15 = CompressUtil.compressToLongs(fsPosition, scIndex15);
        System.out.println("需要 " + compressed15.length + " 个long");
        for (int i = 0; i < compressed15.length; i++) {
            System.out.printf("long[%d]: 0x%016x%n", i, compressed15[i]);
        }

        // 转为字符串
        String recoverData15 = CompressUtil.compressToString(fsPosition, scIndex15);
        System.out.println("RecoverData: " + recoverData15);
        System.out.println("长度: " + recoverData15.length());

        // 解压
        int[] decodedFsPos15 = new int[5];
        int[] decodedScIndex15 = new int[15];
        CompressUtil.decompressFromString(recoverData15, decodedFsPos15, decodedScIndex15);
        System.out.println("解压后decodedFsPos: " + Arrays.toString(decodedFsPos15));
        System.out.println("解压后scIndex长度: " + decodedScIndex15.length);
        System.out.println("数据一致性: " + Arrays.equals(scIndex15, decodedScIndex15));
    }
}
