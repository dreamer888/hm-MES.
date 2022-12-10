package com.dream.iot.plc.omron;

import com.dream.iot.plc.PlcException;
import com.dream.iot.plc.PlcProtocolType;
import com.dream.iot.utils.ByteUtil;
import com.dream.iot.plc.I18n;

public final class OmronUtils {

    /**
     * 解析欧姆龙的数据地址 比如D100， E1.100
     * @param address 数据地址
     * @param isBit 是否是位地址
     * @return 解析后的结果地址对象
     */
    public static byte[] analysisAddress(String address, boolean isBit) {
        byte[] result = new byte[4];
        try {
            switch (address.charAt(0)) {
                case 'D':
                case 'd':
                    // DM区数据
                    result[0] = getModel(OmronFinsModel.DM, isBit);
                    break;
                case 'C':
                case 'c':
                    // CIO区数据
                    result[0] = getModel(OmronFinsModel.CIO, isBit);
                    break;
                case 'W':
                case 'w':
                    // WR区
                    result[0] = getModel(OmronFinsModel.WR, isBit);
                    break;
                case 'H':
                case 'h':
                    // HR区
                    result[0] = getModel(OmronFinsModel.HR, isBit);
                    break;
                case 'A':
                case 'a':
                    // AR区
                    result[0] = getModel(OmronFinsModel.AR, isBit);
                    break;
                case 'T':
                case 't':
                    result[0] =getModel(OmronFinsModel.TIM, isBit);
                    break;
                case 'E':
                case 'e':
                    String[] splits = address.split("\\.");
                    int block = Integer.parseInt(splits[0].substring(1), 16);
                    if (block < 16) {
                        result[0] = isBit ? (byte) (0x20 + block) : (byte) (0xA0 + block);
                    } else {
                        result[0] = isBit ? (byte) (0xE0 + block - 16) : (byte) (0x60 + block - 16);
                    }
                    break;
                default: throw new PlcException("不支持的地址["+address+"]", PlcProtocolType.Omron);
            }

            if (address.charAt(0) == 'E' || address.charAt(0) == 'e') {
                String[] splits = address.split("\\.");
                if (isBit) {
                    // 位操作
                    int addr = Integer.parseInt(splits[1]);
                    byte[] bytes = ByteUtil.getBytes(addr);
                    result[1] = bytes[1];
                    result[2] = bytes[0];

                    if (splits.length > 2) {
                        result[3] = Byte.parseByte(splits[2]);
                        if (result[3] > 15) {
                            throw new PlcException("输入的位地址只能在0-15之间", PlcProtocolType.Omron);
                        }
                    }
                } else {
                    // 字操作
                    int addr = Integer.parseInt(splits[1]);
                    byte[] bytes = ByteUtil.getBytes(addr);
                    result[1] = bytes[1];
                    result[2] = bytes[0];
                }
            } else {
                if (isBit) {
                    // 位操作
                    String[] splits = address.substring(1).split("\\.");
                    int addr = Integer.parseInt(splits[0]);
                    byte[] bytes = ByteUtil.getBytes(addr);
                    result[1] = bytes[1];
                    result[2] = bytes[0];
                    if (splits.length > 1) {
                        result[3] = Byte.parseByte(splits[1]);
                        if (result[3] > 15) {
                            throw new PlcException("输入的位地址只能在0-15之间", PlcProtocolType.Omron);
                        }
                    }
                } else {
                    // 字操作
                    int addr = Integer.parseInt(address.substring(1));
                    byte[] bytes = ByteUtil.getBytes(addr);
                    result[1] = bytes[1];
                    result[2] = bytes[0];
                }
            }
        } catch (NumberFormatException e) {
            throw new PlcException("地址格式错误["+address+"]", PlcProtocolType.Omron);
        }
        return result;
    }

    /**
     * 分析响应数据 并且获取读取的数据
     * @param response
     * @param isRead
     * @return
     */
    public static byte[] responseValidAnalysis(byte[] response, boolean isRead) {
        validErrorCode(response);

        byte[] result = new byte[response.length - 16];
        System.arraycopy(response, 16, result, 0, result.length);
        return udpResponseValidAnalysis(result, isRead);
    }

    public static int getErrorCode(byte[] response) {
        if (response.length >= 16) {

            // 提取错误码校验
            byte[] buffer = new byte[4];
            buffer[0] = response[15];
            buffer[1] = response[14];
            buffer[2] = response[13];
            buffer[3] = response[12];

            return ByteUtil.bytesToInt(buffer, 0);
        }

        throw new PlcException("数据接收异常", PlcProtocolType.Omron);
    }

    /**
     * 返回状态
     * @param response
     * @return
     */
    public static boolean isSuccess(byte[] response) {
        return getErrorCode(response) == 0;
    }

    /**
     * 校验plc错误码 0.成功 其余的都是错误
     * @param response
     * @throws PlcException 失败直接抛出异常
     */
    public static void validErrorCode(byte[] response) throws PlcException {
        int errorCode = getErrorCode(response);
        if(errorCode > 0) {
            throw new PlcException(getStatusDescription(errorCode), PlcProtocolType.Omron);
        }
    }

    /**
     * 验证欧姆龙的Fins-Udp返回的数据是否正确的数据，如果正确的话，并返回所有的数据内容<br />
     * @param response 来自欧姆龙返回的数据内容
     * @param isRead 是否读取
     * @return 带有是否成功的结果对象
     */
    public static byte[] udpResponseValidAnalysis(byte[] response, boolean isRead) {
        if (response.length >= 14) {
            int err = response[12] * 256 + response[13];

            if (!isRead) {
                return new byte[0];
            } else {
                byte[] content = new byte[response.length - 14];
                if (content.length > 0) {
                    System.arraycopy(response, 14, content, 0, content.length);
                } else {
                    throw new PlcException(getStatusDescription(err), PlcProtocolType.Omron);
                }

                return content;
            }
        } else {
            throw new PlcException("数据接收异常", PlcProtocolType.Omron);
        }
    }
    private static byte getModel(OmronFinsModel model, boolean isBit) {
        return isBit ? model.getBit() : model.getWord();
    }

    /**
     * 获取错误信息的字符串描述文本
     * @param err 错误码
     * @return 文本描述
     */
    public static String getStatusDescription(int err) {
        switch (err) {
            case 0:
                return I18n.errorConst.OmronStatus0();
            case 1:
                return I18n.errorConst.OmronStatus1();
            case 2:
                return I18n.errorConst.OmronStatus2();
            case 3:
                return I18n.errorConst.OmronStatus3();
            case 20:
                return I18n.errorConst.OmronStatus20();
            case 21:
                return I18n.errorConst.OmronStatus21();
            case 22:
                return I18n.errorConst.OmronStatus22();
            case 23:
                return I18n.errorConst.OmronStatus23();
            case 24:
                return I18n.errorConst.OmronStatus24();
            case 25:
                return I18n.errorConst.OmronStatus25();
            default:
                return I18n.errorConst.UnknownError();
        }
    }
}
