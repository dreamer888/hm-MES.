package com.dream.iot.plc.omron;

import com.dream.iot.Message;
import com.dream.iot.plc.PlcException;
import com.dream.iot.plc.PlcProtocolType;
import com.dream.iot.plc.ReadAddress;
import com.dream.iot.plc.WriteAddress;
import com.dream.iot.plc.siemens.AddressType;
import com.dream.iot.utils.ByteUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OmronMessageBody implements Message.MessageBody{

    private byte[] message;

    protected OmronMessageBody(byte[] message) {
        this.message = message;
    }

    public static OmronMessageBody buildReadRequestBody(List<ReadAddress> batchAddress) {
        if(CollectionUtils.isEmpty(batchAddress)) {
            throw new PlcException("请指定要读取的点位信息", PlcProtocolType.Omron);
        }

        byte[] message = new byte[8];
        ReadAddress readAddress = batchAddress.get(0);
        message[0] = message[1] = 0x01;  // 读取
        byte[] address = OmronUtils.analysisAddress(readAddress.getAddress(), readAddress.getType() == AddressType.Bit);
        System.arraycopy(address, 0, message, 2, 4);

        byte[] bytes = ByteUtil.getBytes(readAddress.getLength());
        message[6] = bytes[1];
        message[7] = bytes[0];

        return new OmronMessageBody(message);
    }

    public static OmronMessageBody buildWriteRequestBody(WriteAddress writeAddress) {
        int length = writeAddress.getData().length;
        byte[] message = new byte[8 + length];

        message[0] = 0x01;  // 写入
        message[1] = 0x02;

        boolean isBit = writeAddress.getType() == AddressType.Bit;
        byte[] address = OmronUtils.analysisAddress(writeAddress.getAddress(), isBit);
        System.arraycopy(address, 0, message, 2, 4);

        if (isBit) {
            message[6] = (byte) (length / 256);
            message[7] = (byte) (length % 256);
        } else {
            message[6] = (byte) (length / 2 / 256);
            message[7] = (byte) (length / 2 % 256);
        }

        System.arraycopy(writeAddress.getData(), 0, message, 8, length);
        return new OmronMessageBody(message);
    }

    @Override
    public byte[] getMessage() {
        return this.message;
    }
}
