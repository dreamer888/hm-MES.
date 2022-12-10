package com.dream.iot.test.plc;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;

public class TestPlcUtils {

    public static boolean[] randomBools(int num) {
        boolean[] booleans = new boolean[num];
        for (int i=0; i<num; i++) {
            booleans[i] = RandomUtil.randomInt(0, 2) == 0 ? false : true;
        }

        return booleans;
    }

    public static short[] randomShorts(int num) {
        short[] shorts = new short[num];
        for (int i=0; i<num; i++) {
            shorts[i] = (short) RandomUtil.randomInt(Short.MIN_VALUE, Short.MAX_VALUE);
        }

        return shorts;
    }

    public static int[] randomInts(int num) {
        int[] values = new int[num];
        for (int i=0; i<num; i++) {
            values[i] = RandomUtil.randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        return values;
    }

    public static long[] randomLongs(int num) {
        long[] values = new long[num];
        for (int i=0; i<num; i++) {
            values[i] = RandomUtil.randomLong(Long.MIN_VALUE, Long.MAX_VALUE);
        }

        return values;
    }

    public static float[] randomFloats(int num) {
        float[] values = new float[num];
        for (int i=0; i<num; i++) {
            values[i] = (float) RandomUtil.randomDouble(Float.MIN_VALUE, Float.MAX_VALUE);
        }

        return values;
    }

    public static double[] randomDoubles(int num) {
        double[] values = new double[num];
        for (int i=0; i<num; i++) {
            values[i] = RandomUtil.randomDouble(Double.MIN_VALUE, Double.MAX_VALUE);
        }

        return values;
    }

    public static boolean arrayEquals(Object arr, Object arr1) {
        if(arr.getClass().isArray() && arr1.getClass().isArray()) {
           Object[] arrO = ArrayUtil.wrap(arr);
           Object[] arr11 = ArrayUtil.wrap(arr1);
           for (int i=0; i< arrO.length; i++) {
               if(!arrO[i].equals(arr11[i])) {
                   return false;
               }
           }
           return true;
        } else {
            return false;
        }
    }
}
