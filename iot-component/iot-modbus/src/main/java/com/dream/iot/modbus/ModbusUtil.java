package com.dream.iot.modbus;

import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.utils.ByteUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ModbusUtil {

    /**
     * 高位在前，低位在后
     * @param bytes
     * @return
     */
    public static String getCRC(byte[] bytes) {
        return getCRC(bytes, false);
    }

    /**
     * @param bytes
     * @param lb 是否低位在前, 高位在后
     * @return
     */
    public static String getCRC(byte[] bytes, boolean lb) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }

        //结果转换为16进制
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }

        if(lb) { // 低位在前, 高位在后
            result = result.substring(2, 4) + result.substring(0, 2);
        }

        return result;
    }

    public static Payload resolvePayload(byte[] content, short start, ModbusCode code) {
        Payload payload;
        switch (code) {
            case Read01:
            case Read02:
                payload = new RealCoilPayload(start, content); break;
            case Read03:
            case Read04:
                payload = new ReadPayload(content, start); break;
            default:
                payload = WritePayload.getInstance();
        }

        return payload;
    }

    public static Write10Build write10Build(Object... args) {
        int num = 0; List<byte[]> bytes = new ArrayList<>();
        for(Object arg : args) {
            if(arg instanceof Integer) {
                num += 2;
                bytes.add(ByteUtil.getBytes((Integer) arg));
            } else if(arg instanceof Long) {
                num += 4;
                bytes.add(ByteUtil.getBytes((Long) arg));
            } else if(arg instanceof Float) {
                num += 2;
                bytes.add(ByteUtil.getBytes((Float) arg));
            } else if(arg instanceof Double) {
                num += 4;
                bytes.add(ByteUtil.getBytes((Double) arg));
            } else if(arg instanceof Short) {
                num += 1;
                bytes.add(ByteUtil.getBytesOfReverse((Short) arg));
            } else if(arg instanceof Byte) {
                num += 1;
                bytes.add(new byte[]{0x00, (byte) arg});
            } else if(arg instanceof String) {
                byte[] bytes1 = arg.toString().getBytes(StandardCharsets.UTF_8);
                if(bytes1.length % 2 != 0) {
                    num += bytes1.length / 2 + 1;
                    byte[] addMessage = new byte[bytes1.length + 1];
                    ByteUtil.addBytes(addMessage, bytes1, 0);
                    bytes.add(addMessage);
                } else {
                    num += bytes1.length / 2;
                    bytes.add(bytes1);
                }
            } else {
                throw new ModbusProtocolException("不支持的数据类型", ModbusCode.Write10);
            }
        }

        Integer length = bytes.stream().map(item -> item.length).reduce((a, b) -> a + b).get();
        byte[] write = new byte[length];

        int index = 0;
        for(int i=0; i<bytes.size(); i++) {
            byte[] values = bytes.get(i);
            ByteUtil.addBytes(write, values, index);
            index += values.length;
        }

        return new Write10Build(num, write);
    }

    public static class Write10Build {
        public int num;
        public byte[] message;

        public Write10Build(int num, byte[] message) {
            this.num = num;
            this.message = message;
        }
    }
}
