package com.dream.iot.plc.omron;

import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.plc.*;

import java.util.Arrays;
import java.util.List;

/**
 * 基于tcp实现的操作欧姆龙PLC设备的协议
 */
public class OmronTcpProtocol extends PlcClientProtocol<OmronMessage> {

    private byte[] data;

    /**
     * 使用默认的客户端
     */
    public OmronTcpProtocol() {
        super(1);
    }

    /**
     * 使用指定配置的客户端
     * @param properties
     */
    public OmronTcpProtocol(OmronConnectProperties properties) {
        super(properties, 1);
    }

    @Override
    public ClientSocketProtocol buildRequestMessage() {
        if(this.requestMessage() != null) {
            return this;
        }

        return super.buildRequestMessage();
    }

    @Override
    protected OmronMessage doBuildRequestMessage() {

        OmronMessageBody requestBody;
        if(this.getWriteAddress() == null) { // 读plc请求
            requestBody = OmronMessageBody.buildReadRequestBody(this.getBatchAddress());
        } else { // 写plc请求
            requestBody = OmronMessageBody.buildWriteRequestBody(this.getWriteAddress());
        }

        OmronTcpClient iotClient = (OmronTcpClient) getIotClient();
        OmronConnectProperties config = iotClient.getConfig();
        OmronMessageHeader requestHeader = new OmronMessageHeader();

        requestHeader.buildRequestHeader(config.getSA1(), config.getDA1(), requestBody.getLength());
        return new OmronMessage(requestHeader, requestBody);
    }

    @Override
    public void doBuildResponseMessage(OmronMessage responseMessage) {
        OmronMessage requestMessage = requestMessage();
        byte[] message = responseMessage.getMessage();

        // 读
        if(requestMessage.getMessage()[27] == 0x01) {
            this.data = OmronUtils.responseValidAnalysis(message, true);
        } else { // 写
            OmronUtils.responseValidAnalysis(message, false);
        }
    }

    @Override
    public DataTransfer getDataTransfer() {
        return OmronDataTransfer.getInstance();
    }

    @Override
    public void writeFull(byte[] fullMessage) {
        this.requestMessage = new OmronMessage(new OmronMessageHeader(fullMessage));
        this.sync(this.getTimeout()).request();
    }

    @Override
    public byte[] readFull(byte[] fullMessage) {
        this.requestMessage = new OmronMessage(new OmronMessageHeader(fullMessage));
        this.sync(this.getTimeout()).request();
        return this.data;
    }

    @Override
    public List<byte[]> batchRead(List<ReadAddress> batchAddress) {
        throw new PlcException("[" +protocolType()+ "]不支持批量读取", PlcProtocolType.Omron);
    }

    /**
     * 读取字符串
     * 注：{length} 读取的长度 = length * 2 <br />
     *  比如要读取 "abcde"五个字节的内容那么length = 5 但是实际返回的会是 5 * 2个字节
     * @param address
     * @param length 读取的字节长度 = (length * 2)字节
     * @param encoding 使用的编码
     * @return
     */
    @Override
    public String readString(String address, short length, String encoding) {
        return super.readString(address, length, encoding);
    }

    @Override
    protected Class<OmronMessage> getMessageClass() {
        return OmronMessage.class;
    }

    @Override
    public PlcProtocolType protocolType() {
        return PlcProtocolType.Omron;
    }

    @Override
    protected List<byte[]> doRead(List<ReadAddress> batchAddress) {
        return Arrays.asList(this.data);
    }

}
