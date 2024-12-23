package com.gcs.game.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomUtil {

    /**
     * get random number.
     *
     * @param max
     * @return
     */
    public static int getRandomInt(int max) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(max);
        // return (int) (random.nextDouble() * max);
    }

    /**
     * get an int value from array with weight.
     *
     * @param array
     * @param weight
     * @return
     */
    public static int getRandomFromArrayWithWeight(int[] array, int[] weight) {
        if (array != null && weight != null && array.length > 0
                && array.length == weight.length) {
            List<Integer> newArray = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < weight[i]; j++) {
                    newArray.add(array[i]);
                }
            }
            int index = getRandomInt(newArray.size());
            return newArray.get(index);
        }
        return 0;
    }

    /**
     * get random index from array with weight.
     *
     * @param weight
     * @return
     */
    public static int getRandomIndexFromArrayWithWeight(int[] weight) {
        if (weight != null && weight.length > 0) {
            List<Integer> newArray = new ArrayList<>();
            for (int i = 0; i < weight.length; i++) {
                for (int j = 0; j < weight[i]; j++) {
                    newArray.add(i);
                }
            }
            int index = getRandomInt(newArray.size());
            return newArray.get(index);
        }
        return 0;
    }

    /**
     * get random index from array with weight.
     *
     * @param weight
     * @return
     */
    public static int getRandomIndexFromArrayWithWeight(long[] weight) {
        if (weight != null && weight.length > 0) {
            List<Integer> newArray = new ArrayList<>();
            for (int i = 0; i < weight.length; i++) {
                for (long j = 0; j < weight[i]; j++) {
                    newArray.add(i);
                }
            }
            int index = getRandomInt(newArray.size());
            return newArray.get(index);
        }
        return 0;
    }

    public static int[] getRandomIndex(int count) {
        return getRandomIndex(count, count);
    }

    public static int[] getRandomIndex(int count, int elementCount) {
        if (count > 0 && count >= elementCount) {
            int[] result = new int[elementCount];
            List<Integer> tempList = new ArrayList<>();
            for (int i = 0; i < count; ++i) {
                tempList.add(i);
            }
            for (int i = 0; i < elementCount; ++i) {
                int idx = getRandomInt(tempList.size());
                result[i] = tempList.get(idx);
                tempList.remove(idx);
            }
            return result;
        } else {
            return null;
        }
    }

    public static int[] getRandomIndex(int[] weight, int elementCount) {
        if (weight != null && weight.length > 0 && weight.length >= elementCount) {
            int[] result = new int[elementCount];
            List<Integer> newArray = new ArrayList<>();
            for (int i = 0; i < weight.length; i++) {
                for (int j = 0; j < weight[i]; j++) {
                    newArray.add(i);
                }
            }
            List<Integer> indexLeft = new ArrayList<>();
            for (int i = 0; i < weight.length; i++) {
                indexLeft.add(i);
            }

            List<Integer> weightLeft = (List<Integer>) Arrays.asList(weight).stream();
            for (int i = 0; i < elementCount; ++i) {
                int idx = getRandomInt(newArray.size());
                result[i] = newArray.get(idx);

                for (int m = 0; m < indexLeft.size(); m++) {
                    if (indexLeft.get(m) == result[i]) {
                        indexLeft.remove(m);
                        weightLeft.remove(m);
                        break;
                    }
                }

                newArray = new ArrayList<>();
                for (int m = 0; m < weightLeft.size(); m++) {
                    for (int n = 0; n < weightLeft.get(m); n++) {
                        newArray.add(indexLeft.get(m));
                    }
                }
            }
            return result;
        } else {
            return null;
        }
    }

}
