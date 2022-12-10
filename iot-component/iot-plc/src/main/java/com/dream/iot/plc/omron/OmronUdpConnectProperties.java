package com.dream.iot.plc.omron;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.udp.UdpClientConnectProperties;
import org.springframework.util.StringUtils;

public class OmronUdpConnectProperties extends UdpClientConnectProperties
{

    /**
     * 上位机的节点地址，如果你的电脑的Ip地址为192.168.0.8，那么这个值就是8
     */
    private byte SA1;

    /**
     * PLC的节点地址，这个值在配置了ip地址之后是默认赋值的，默认为Ip地址的最后一位
     */
    private byte DA1;

    private String omronConnectKey;

    protected OmronUdpConnectProperties() { }

    public OmronUdpConnectProperties(String remoteHost, Integer remotePort) {
        super(remoteHost, remotePort);
        this.setHost(remoteHost);
    }

    public OmronUdpConnectProperties(String remoteHost, Integer remotePort, String localHost) {
        super(remoteHost, remotePort, localHost, null);
        this.setHost(remoteHost);
        this.setLocalHost(localHost);
    }

    public OmronUdpConnectProperties(String remoteHost, Integer remotePort, String localHost, Integer localPort) {
        super(remoteHost, remotePort, localHost, localPort);

        if(StringUtils.hasText(remoteHost)) {
            DA1 = (byte) Integer.parseInt(remoteHost.substring(remoteHost.lastIndexOf( "." ) + 1));
        }
        if(StringUtils.hasText(localHost)) {
            SA1 = (byte) Integer.parseInt(localHost.substring(localHost.lastIndexOf( "." ) + 1));
        }

    }

    /*public OmronUdpConnectProperties(String remoteHost, Integer remotePort, long allIdleTime, long readerIdleTime, long writerIdleTime) {
        super(remoteHost, remotePort, allIdleTime, readerIdleTime, writerIdleTime);
        this.setHost(remoteHost);
    }
*/
    @Override
    public String connectKey() {
        if(this.omronConnectKey == null) {
            this.omronConnectKey = super.connectKey() + ":" + SA1 + ":" + DA1;
            this.setConnectKey(this.omronConnectKey);
        }

        return this.omronConnectKey;
    }

    public byte getSA1() {
        return SA1;
    }

    public void setSA1(byte SA1) {
        this.SA1 = SA1;
    }

    public byte getDA1() {
        return DA1;
    }

    public void setDA1(byte DA1) {
        this.DA1 = DA1;
    }

    /**
     * 请使用ip地址
     * @param ipAddress
     */
    @Override
    public void setHost(String ipAddress) {
        super.setHost(ipAddress);
        if(StringUtils.hasText(ipAddress)) {
            DA1 = (byte) Integer.parseInt(ipAddress.substring(ipAddress.lastIndexOf( "." ) + 1));
        }
    }

    /**
     * 请使用ip地址
     * @param ipAddress
     */
    @Override
    public void setLocalHost(String ipAddress) {
        super.setLocalHost(ipAddress);
        if(StringUtils.hasText(ipAddress)) {
            SA1 = (byte) Integer.parseInt(ipAddress.substring(ipAddress.lastIndexOf( "." ) + 1));
        }
    }
}
