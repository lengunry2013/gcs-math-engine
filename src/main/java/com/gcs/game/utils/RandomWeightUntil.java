package com.gcs.game.utils;

public class RandomWeightUntil {

    private int[] values = null;

    private int[] weights = null;

    private int weightLength = 0;

    /**
     * Constructor.
     *
     * @param array
     * @param weight
     */
    public RandomWeightUntil(int[] array, int[] weight) {
        if (array != null && weight != null && array.length > 0
                && array.length == weight.length) {
            this.values = array.clone();
            this.weights = weight.clone();
            this.weightLength = 0;
            for (int i = 0; i < array.length; i++) {
                this.weightLength += weight[i];
            }
        }
    }

    /**
     * Constructor.
     *
     * @param weight
     */
    public RandomWeightUntil(int[] weight) {
        if (weight != null && weight.length > 0) {
            this.weights = weight.clone();
            for (int i = 0; i < weight.length; i++) {
                this.weightLength += weight[i];
            }
        }
    }

    public int getRandomResult() {
        int result = 0;
        if (this.weightLength > 0) {
            int index = RandomUtil.getRandomInt(this.weightLength);
            int tempIndex = 0;
            for (int i = 0; i < this.weights.length; i++) {
                tempIndex += this.weights[i];
                if (index < tempIndex) {
                    if (this.values != null) {
                        result = this.values[i];
                    } else {
                        result = i;
                    }
                    break;
                }
            }
        }
        return result;
    }

    public void remove(int valueInArray) {
        int valueIndex = -1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == valueInArray) {
                valueIndex = i;
                break;
            }
        }
        if (valueIndex >= 0) {
            int[] newValues = new int[values.length - 1];
            int[] newWeights = new int[values.length - 1];
            int index = 0;
            for (int i = 0; i < this.values.length; i++) {
                if (i == valueIndex) {
                    continue;
                } else {
                    newValues[index] = values[i];
                    index++;
                }
            }
            index = 0;
            for (int i = 0; i < this.values.length; i++) {
                if (i == valueIndex) {
                    continue;
                } else {
                    newWeights[index] = weights[i];
                    index++;
                }
            }

            this.values = newValues;
            this.weights = newWeights;
            this.weightLength = 0;
            for (int i = 0; i < this.values.length; i++) {
                this.weightLength += this.weights[i];
            }
        }

    }

}
