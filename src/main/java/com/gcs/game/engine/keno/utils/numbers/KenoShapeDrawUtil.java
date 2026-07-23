package com.gcs.game.engine.keno.utils.numbers;

import com.gcs.game.utils.RandomUtil;
import com.gcs.game.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class KenoShapeDrawUtil {
    // 获取数字在面板上的位置 (行, 列)
    private static int[] getPosition(int number) {
        int row = (number - 1) / 10;  // 0-based行
        int col = (number - 1) % 10;  // 0-based列
        return new int[]{row, col};
    }

    // SetA: 2行×3列 (连续3列，上下2行)
    public static List<Integer> drawShapeA(List<Integer> available) {
        // 先找所有可能的起始位置
        List<int[]> possibleStarts = new ArrayList<>();

        for (int num : available) {
            int[] pos = getPosition(num);
            int row = pos[0];
            int col = pos[1];

            // 检查是否能放下2行×3列 (需要col+2 < 10, row+1 < 8)
            if (col + 2 < 10 && row + 1 < 8) {
                // 检查这6个位置是否都在available中
                List<Integer> candidate = new ArrayList<>();
                boolean valid = true;

                for (int r = row; r <= row + 1; r++) {
                    for (int c = col; c <= col + 2; c++) {
                        int targetNum = r * 10 + c + 1;
                        if (!available.contains(targetNum)) {
                            valid = false;
                            break;
                        }
                        candidate.add(targetNum);
                    }
                    if (!valid) break;
                }

                if (valid && candidate.size() == 6) {
                    // 随机选择一个有效的候选
                    possibleStarts.add(new int[]{row, col});
                }
            }
        }

        if (possibleStarts.isEmpty()) {
            // 如果找不到完美匹配，使用备用方案：随机选6个连续位置
            System.out.println("SetA No possible starts found");
            log.error("SetA No possible starts found");
            return fallbackDrawShapeA(available);
        }

        // 随机选择一个起始位置
        int[] start = possibleStarts.get(RandomUtil.getRandomInt(possibleStarts.size()));
        int row = start[0];
        int col = start[1];

        List<Integer> result = new ArrayList<>();
        for (int r = row; r <= row + 1; r++) {
            for (int c = col; c <= col + 2; c++) {
                result.add(r * 10 + c + 1);
            }
        }
        return result;
    }

    // SetB: 2-1-2形状 (上排2个，中间1个，下排2个)
    public static List<Integer> drawShapeB(List<Integer> available) {
        List<int[]> possibleCenters = new ArrayList<>();

        for (int num : available) {
            int[] pos = getPosition(num);
            int row = pos[0];
            int col = pos[1];

            // 检查是否能形成2-1-2形状 (需要row+2 < 8, col-1 >= 0, col+1 < 10)
            if (row + 2 < 8 && col - 1 >= 0 && col + 1 < 10) {
                // 检查形状是否完整
                List<Integer> candidate = new ArrayList<>();
                boolean valid = true;

                // 上排: col-1, col (左右2个)
                int topLeft = row * 10 + (col - 1) + 1;   // 左
                int topRight = row * 10 + (col + 1) + 1;  // 右
                if (!available.contains(topLeft) || !available.contains(topRight)) {
                    valid = false;
                } else {
                    candidate.add(topLeft);
                    candidate.add(topRight);
                }

                // 中间: col (中间1个)
                int middle = (row + 1) * 10 + col + 1;
                if (valid && !available.contains(middle)) {
                    valid = false;
                } else if (valid) {
                    candidate.add(middle);
                }

                // 下排: col-1, col (左右2个)
                int bottomLeft = (row + 2) * 10 + (col - 1) + 1;   // 左
                int bottomRight = (row + 2) * 10 + (col + 1) + 1;  // 右
                if (valid && (!available.contains(bottomLeft) || !available.contains(bottomRight))) {
                    valid = false;
                } else if (valid) {
                    candidate.add(bottomLeft);
                    candidate.add(bottomRight);
                }

                if (valid && candidate.size() == 5) {
                    possibleCenters.add(new int[]{row, col});
                }
            }
        }

        if (possibleCenters.isEmpty()) {
            System.out.println("SetB No possible centers found");
            log.error("SetB No possible centers found");
            return fallbackDrawShapeB(available);
        }

        int[] center = possibleCenters.get(RandomUtil.getRandomInt(possibleCenters.size()));
        int row = center[0];
        int col = center[1];

        List<Integer> result = new ArrayList<>();
        // 上排: 左右2个 (col-1 和 col+1)
        result.add(row * 10 + (col - 1) + 1);
        result.add(row * 10 + (col + 1) + 1);
        // 中间: 中间1个 (col)
        result.add((row + 1) * 10 + col + 1);
        // 下排: 左右2个 (col-1 和 col+1)
        result.add((row + 2) * 10 + (col - 1) + 1);
        result.add((row + 2) * 10 + (col + 1) + 1);

        return result;
    }

    // SetC: 2行×2列
    public static List<Integer> drawShapeC(List<Integer> available) {
        List<int[]> possibleStarts = new ArrayList<>();

        for (int num : available) {
            int[] pos = getPosition(num);
            int row = pos[0];
            int col = pos[1];

            if (col + 1 < 10 && row + 1 < 8) {
                List<Integer> candidate = new ArrayList<>();
                boolean valid = true;

                // 2行×2列
                for (int r = row; r <= row + 1; r++) {
                    for (int c = col; c <= col + 1; c++) {
                        int targetNum = r * 10 + c + 1;
                        if (!available.contains(targetNum)) {
                            valid = false;
                            break;
                        }
                        candidate.add(targetNum);
                    }
                    if (!valid) break;
                }

                if (valid && candidate.size() == 4) {
                    possibleStarts.add(new int[]{row, col});
                }
            }
        }

        if (possibleStarts.isEmpty()) {
            System.out.println("SetC No possible starts found");
            log.error("SetC No possible starts found");
            return fallbackDrawShapeC(available);
        }

        int[] start = possibleStarts.get(RandomUtil.getRandomInt(possibleStarts.size()));
        int row = start[0];
        int col = start[1];

        List<Integer> result = new ArrayList<>();
        for (int r = row; r <= row + 1; r++) {
            for (int c = col; c <= col + 1; c++) {
                result.add(r * 10 + c + 1);
            }
        }
        return result;
    }

    // SetD: 3行×1列 (竖直)
    public static List<Integer> drawShapeD(List<Integer> available) {
        List<int[]> possibleStarts = new ArrayList<>();

        for (int num : available) {
            int[] pos = getPosition(num);
            int row = pos[0];
            int col = pos[1];

            // 检查是否能放下3行×1列 (需要row+2 < 8)
            if (row + 2 < 8) {
                // 检查这3个位置是否都在available中
                List<Integer> candidate = new ArrayList<>();
                boolean valid = true;

                for (int r = row; r <= row + 2; r++) {
                    int targetNum = r * 10 + col + 1;
                    if (!available.contains(targetNum)) {
                        valid = false;
                        break;
                    }
                    candidate.add(targetNum);
                }

                if (valid && candidate.size() == 3) {
                    possibleStarts.add(new int[]{row, col});
                }
            }
        }

        if (possibleStarts.isEmpty()) {
            System.out.println("SetD No possible centers found");
            log.error("SetD No possible centers found");
            return fallbackDrawShapeD(available);
        }

        // 随机选择一个起始位置
        int[] start = possibleStarts.get(RandomUtil.getRandomInt(possibleStarts.size()));
        int row = start[0];
        int col = start[1];

        List<Integer> result = new ArrayList<>();
        for (int r = row; r <= row + 2; r++) {
            result.add(r * 10 + col + 1);
        }
        return result;
    }

    private static List<Integer> fallbackDrawShapeD(List<Integer> available) {
        Collections.shuffle(available);
        return new ArrayList<>(available.subList(0, Math.min(3, available.size())));
    }

    private static List<Integer> fallbackDrawShapeB(List<Integer> available) {
        Collections.shuffle(available);
        return new ArrayList<>(available.subList(0, Math.min(5, available.size())));
    }

    private static List<Integer> fallbackDrawShapeC(List<Integer> available) {
        Collections.shuffle(available);
        return new ArrayList<>(available.subList(0, Math.min(4, available.size())));
    }

    // 备用方案：如果找不到完美形状，随机选数量匹配的数字
    private static List<Integer> fallbackDrawShapeA(List<Integer> available) {
        Collections.shuffle(available);
        return new ArrayList<>(available.subList(0, Math.min(6, available.size())));
    }

    // 辅助方法
    public static int[] getAllRandomDigits() {
        int[] arr = new int[80];
        for (int i = 0; i < 80; i++) arr[i] = i + 1;
        return arr;
    }

    public static List<Integer> getLeftNumbers(int[] allNumbers, List<Integer> numberList) {
        List<Integer> left = new ArrayList<>();
        for (int num : allNumbers) {
            if (!numberList.contains(num)) {
                left.add(num);
            }
        }
        return left;
    }

    private static List<Integer> getSelectNumbers(List<Integer> leftNumbers) {
        int[] selectCounts = new int[]{10};
        int countIndex = RandomUtil.getRandomInt(selectCounts.length);
        int count = selectCounts[countIndex];
        int[] allNumbers = StringUtil.ListToIntegerArray(leftNumbers);
        if (count > 0) {
            int[] randomIndex = RandomUtil.getRandomIndex(allNumbers.length, count);
            List<Integer> randomNumbers = new ArrayList<>();
            for (int index = 0; index < randomIndex.length; index++) {
                randomNumbers.add(allNumbers[randomIndex[index]]);
            }
            return randomNumbers;
        }
        return null;
    }

    public static void main(String[] args) {
        int[] allNumbers = getAllRandomDigits();
        for (int i = 0; i < 10000000; i++) {
            List<Integer> leftNumbers = StringUtil.IntegerArrayToList(allNumbers);
            List<Integer> setA = drawShapeA(leftNumbers);
            if (!setA.isEmpty()) {
                System.out.println(setA.toString());
                leftNumbers.removeAll(setA);
            }
            List<Integer> setB = drawShapeB(leftNumbers);
            if (!setB.isEmpty()) {
                System.out.println(setB.toString());
                leftNumbers.removeAll(setB);
            }
            List<Integer> setC = drawShapeC(leftNumbers);
            if (!setC.isEmpty()) {
                System.out.println(setC.toString());
                leftNumbers.removeAll(setC);
            }
            List<Integer> setD = drawShapeD(leftNumbers);
            if (!setD.isEmpty()) {
                System.out.println(setD.toString());
                leftNumbers.removeAll(setD);
            }
            List<Integer> selectNumbers = getSelectNumbers(leftNumbers);
            System.out.println(selectNumbers.toString());
            System.out.println("模拟次数Number=" + (i + 1));
        }

    }

}
