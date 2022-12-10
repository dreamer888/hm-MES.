package com.dream.iot;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.websocket.HttpRequestWrapper;
import io.netty.util.AttributeKey;

public interface CoreConst {

    /**
     * 客户端解码器
     */
    String CLIENT_DECODER_HANDLER = "ClientProtocolDecoder";

    /**
     * 客户端编码器
     */
    String CLIENT_ENCODER_HANDLER = "ClientProtocolEncoder";

    /**
     * 客户端业务处理器
     */
    String CLIENT_SERVICE_HANDLER = "ClientServiceHandler";

    /**
     * 服务端解码器
     */
    String SERVER_DECODER_HANDLER = "ServerProtocolDecoder";

    /**
     * 服务端编码器
     */
    String SERVER_ENCODER_HANDLER = "ServerProtocolEncoder";

    /**
     * 服务端业务处理器
     */
    String SERVER_SERVICE_HANDLER = "ServerServiceHandler";

    /**
     * 存活状态事件处理器
     */
    String IDLE_STATE_EVENT_HANDLER = "IdleStateEventHandler";

    /**
     * 客户端上下线、存活等事件处理器
     */
    String EVENT_MANAGER_HANDLER = "EventManagerHandler";

    /**
     * 设备编号的KEY
     */
    AttributeKey EQUIP_CODE = AttributeKey.newInstance("IOT:EquipCode");

    /**
     * Websocket请求对象
     */
    AttributeKey<HttpRequestWrapper> WEBSOCKET_REQ = AttributeKey.newInstance("IOT:WEBSOCKET:REQ");

    /**
     * 标识客户端连接因为重复而被关闭
     */
    AttributeKey<Boolean> CLIENT_OVERRIDE_CLOSED = AttributeKey.newInstance("IOT:CLIENT:OVERRIDE:CLOSED");

    /**
     * Websocket关闭标识
     */
    AttributeKey<Boolean> WEBSOCKET_CLOSE = AttributeKey.newInstance("IOT:WEBSOCKET:CLOSE");

    /**
     * 客户端key
     */
    AttributeKey<ClientConnectProperties> CLIENT_KEY = AttributeKey.newInstance("IOT:ClientKey");

    /**
     * 客户端连接时间
     */
    AttributeKey<Long> CLIENT_ONLINE_TIME = AttributeKey.newInstance("IOT:CLIENT:ONLINE:TIME");
}
