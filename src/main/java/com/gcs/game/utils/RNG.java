package com.gcs.game.utils;

public class RNG {
    static {
        System.loadLibrary("RNG");
    }

    public native int getRandomNumber(int pRange, boolean bZeroBased);
    public native String[] getRandomNumberArray(int pRange, boolean bZeroBased, long pLength);
    public native String getLibraryVersion();

    public static void main(String[] args) {
        RNG example = new RNG();
        int[] distribution = new int[100]; // 用于统计0-99每个数字出现的次数
        int totalNumbers = 1000000;

        // 记录开始时间
        long startTime = System.nanoTime();

        // 生成随机数并统计分布
        for(int i = 0; i < totalNumbers; i++) {
            int num = example.getRandomNumber(100, false); // 生成0-99的随机数
            distribution[num-1]++;
        }

        // 记录结束时间
        long endTime = System.nanoTime();
        long duration = endTime - startTime; // 纳秒
        double milliseconds = duration / 1_000_000.0; // 转换为毫秒

        // 打印分布统计
        // 打印分布统计
        System.out.println("Random Number Distribution Statistics (Total " + totalNumbers + " draws):");
        for(int i = 0; i < distribution.length; i++) {
            double percentage = (double)distribution[i] / totalNumbers * 100;
            System.out.printf("Number %2d: appeared %5d times, percentage %.2f%%\n", i+1, distribution[i], percentage);
        }

// 计算并显示统计信息
        double expectedPercentage = 100.0 / 100; // 期望每个数字出现的概率
        System.out.println("\nExpected probability for each number: " + expectedPercentage + "%");

// 显示执行时间
        System.out.printf("\nExecution time: %.3f milliseconds\n", milliseconds);
        System.out.printf("Average time per random number generation: %.3f microseconds\n", (milliseconds * 1000) / totalNumbers);

    }


}