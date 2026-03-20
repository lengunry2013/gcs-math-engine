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

    public static void main(String[] args) {
        int[] slotReelStopPosition = {31, 20, 1, 22, 1};
        int baseGameMul = 5;

        long compressed = compressToLong(slotReelStopPosition, baseGameMul);
        System.out.println("slots压缩后的long值: " + compressed);
        long compressResult = compressToLong(compressed, 1);
        System.out.println("slots压缩最终结果: " + compressResult);
        // 解压缩验证
        int[] decodedPositions = new int[5];
        int[] decodedMultiplier = new int[1];
        decompressFromLong(compressResult, decodedPositions, decodedMultiplier);
        int wagerType = decompressWagerType(compressResult);
        System.out.println("解压后positions: " + java.util.Arrays.toString(decodedPositions));
        System.out.println("解压后multiplier: " + decodedMultiplier[0]);
        System.out.println("解压后wager: " + wagerType);

        slotReelStopPosition = new int[]{54, 68, 31, 61, 78};
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
        System.out.println("wildPos: " + decodedWildPos);
    }
}
