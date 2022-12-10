package com.dream.iot.plc.siemens;

import com.dream.iot.plc.PlcException;

public final class SiemensUtils {

    public static int[] analysisAddress(String address) {
        int[] result = new int[] {0, 0, 0};
        char startChar = address.charAt(0);
        switch (startChar) {
            case 'I':
                result[0] = 0x81;
                result[1] = calculateAddressStarted(address.substring(1)); break;
            case 'Q':
                result[0] = 0x82;
                result[1] = calculateAddressStarted(address.substring(1)); break;
            case 'M':
                result[0] = 0x83;
                result[1] = calculateAddressStarted(address.substring(1)); break;
            case 'D':
                result[0] = 0x84;
                String[] adds = address.split("\\.");
                if (address.charAt(1) == 'B') {
                    result[2] = Integer.parseInt(adds[0].substring(2));
                } else {
                    result[2] = Integer.parseInt(adds[0].substring(1));
                }

                result[1] = calculateAddressStarted(address.substring(address.indexOf('.') + 1)); break;
            case 'T':
                result[0] = 0x1D;
                result[1] = calculateAddressStarted(address.substring(1)); break;
            case 'C':
                result[0] = 0x1C;
                result[1] = calculateAddressStarted(address.substring(1)); break;
            case 'V':
                result[0] = 0x84;
                result[2] = 1;
                result[1] = calculateAddressStarted(address.substring(1)); break;

            default: throw new PlcException("不支持的地址格式["+address+"]");
        }

        return result;
    }

    public static int calculateAddressStarted(String address) {
        if (address.indexOf('.') < 0) {
            return Integer.parseInt(address) * 8;
        } else {
            String[] temp = address.split("\\.");
            return Integer.parseInt(temp[0]) * 8 + Integer.parseInt(temp[1]);
        }
    }
}
