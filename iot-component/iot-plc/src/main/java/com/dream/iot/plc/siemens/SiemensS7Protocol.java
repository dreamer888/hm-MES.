package com.dream.iot.plc.siemens;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.plc.*;
import com.dream.iot.utils.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 西门子S200序列
 */
public class SiemensS7Protocol extends PlcClientProtocol<SiemensS7Message> {

    private byte[] data;

    /**
     * 使用默认的客户端
     */
    public SiemensS7Protocol() {
        super(2);
    }

    /**
     * 使用指定配置的客户端
     * @see SiemensConnectProperties#connectKey() 如果客户端不存在则重新创建
     * @see SiemensS7Component#createNewClient(ClientConnectProperties)
     * @param properties 要操作的plc配置
     */
    public SiemensS7Protocol(ClientConnectProperties properties) {
        super(properties, 2);
    }

    @Override
    public ClientSocketProtocol buildRequestMessage() {
        if(this.requestMessage() != null) {
            return this;
        }

        return super.buildRequestMessage();
    }

    @Override
    protected SiemensS7Message doBuildRequestMessage() {
        SiemensMessageBody body; SiemensMessageHeader header;

        // 写plc
        WriteAddress writeAddress = this.getWriteAddress();
        if(writeAddress != null) {
            body = SiemensMessageBody.buildWriteBody(writeAddress);
            header = SiemensMessageHeader.buildWriteHeader(writeAddress.getData().length);
        } else { // 读plc
            body = SiemensMessageBody.buildReadBody(this.getBatchAddress());
            header = SiemensMessageHeader.buildReadHeader((short) this.getBatchAddress().size());
        }

        return new SiemensS7Message(header, body);
    }

    @Override
    public void doBuildResponseMessage(SiemensS7Message responseMessage) {
        byte[] message = responseMessage.getMessage();

        byte rwId = message[19];
        if(rwId == 4) { // 读取指令响应
            this.data = ByteUtil.subBytes(message, 25);
        } else { // 写入指令响应

        }
    }

    /**
     * 批量读取
     * @see ReadAddress#getAddress() M100, I100, Q100, DB1.100
     * @param batchAddress
     * @return
     */
    @Override
    protected List<byte[]> doRead(List<ReadAddress> batchAddress) {
        List<byte[]> result = new ArrayList<>();
        int start = 0;
        for (int index=0; index < batchAddress.size(); index++) {
            ReadAddress address = batchAddress.get(index);

            byte[] bytes = ByteUtil.subBytes(this.data, start, start + address.getLength());

            if(address.getLength() == 1) {
                start += address.getLength() + 5;
            } else {
                start += address.getLength() + 4;
            }

            result.add(bytes);
        }

        return result;
    }

    @Override
    protected Class<SiemensS7Message> getMessageClass() {
        return SiemensS7Message.class;
    }

    @Override
    public PlcProtocolType protocolType() {
        return PlcProtocolType.SiemensS7;
    }

    @Override
    public DataTransfer getDataTransfer() {
        return SiemensDataTransfer.getInstance();
    }

    @Override
    public void writeFull(byte[] fullMessage) {
        this.requestMessage = new SiemensS7Message(new SiemensMessageHeader(fullMessage));
        this.sync(this.getTimeout()).request();
    }

    @Override
    public byte[] readFull(byte[] fullMessage) {
        this.requestMessage = new SiemensS7Message(new SiemensMessageHeader(fullMessage));
        this.sync(this.getTimeout()).request();
        return this.data;
    }

    public byte[] getData() {
        return data;
    }
}
