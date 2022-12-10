package com.dream.iot.plc.siemens;

import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.plc.PlcProtocolType;

public class SiemensMessageHeader extends DefaultMessageHead {

    public SiemensMessageHeader(byte[] message) {
        this((String) null);
        this.setMessage(message);
    }

    public SiemensMessageHeader(String messageId) {
        super(messageId, messageId, PlcProtocolType.SiemensS7);
    }

    /**
     * @param readCount 要读取的地址数量(批量读取) 最大值 256
     * @return
     */
    public static SiemensMessageHeader buildReadHeader(short readCount) {
        int length = 19 + readCount * 12;

        byte[] messages = new byte[] {
                0x03, 0x00, // 固定标识
                (byte) (length / 256), (byte) (length % 256), // 整个报文长度, 报文头 + 报文体
                0x02, (byte) 0xF0, (byte) 0x80, // 固定字节
                0x32, // 协议标识
                0x01, // 请求 03. 响应
                0x00, 0x00,
                0x00, 0x01, // 自增的报文标识, 响应头会原封返回
                0x00, 0x0E, // 参数命令数据总长度 固定
                0x00, 0x00, // 读取存储数据时为00, 读取型号为Data数据长度
                0x04, // 读取
                (byte) readCount // 读取地址数量
        };

        return new SiemensMessageHeader(messages);
    }

    public static SiemensMessageHeader buildWriteHeader(int writeByteCount) {
        int length = 35 + writeByteCount;
        byte[] message = new byte[] {
                0x03, 0x00, // 固定标识
                (byte) (length / 256), (byte) (length % 256), // 整个报文长度, 报文头 + 报文体
                0x02, (byte) 0xF0, (byte) 0x80, // 固定字节
                0x32, // 协议标识
                0x01, // 请求 03. 响应
                0x00, 0x00,
                0x00, 0x01, // 自增的报文标识, 响应头会原封返回
                0x00, 0x0E, // 参数命令数据总长度 固定
                (byte) ((4 + writeByteCount) / 256), (byte) ((4 + writeByteCount) % 256), // 写入长度+4
                0x05, // 写入
                0x01
        };

        return new SiemensMessageHeader(message);
    }

    public static SiemensMessageHeader buildBitWriteHeader() {
        byte[] message = new byte[] {
                0x03, 0x00, // 固定标识
                0x00, (byte) 24, // 整个报文长度, 报文头 + 报文体
                0x02, (byte) 0xF0, (byte) 0x80, // 固定字节
                0x32, // 协议标识
                0x01, // 请求 03. 响应
                0x00, 0x00,
                0x00, 0x01, // 自增的报文标识, 响应头会原封返回
                0x00, 0x0E, // 参数命令数据总长度 固定
                0x00, 0x05, // 写入长度+4
                0x05, // 写入
                0x01
        };

        return new SiemensMessageHeader(message);
    }

}
