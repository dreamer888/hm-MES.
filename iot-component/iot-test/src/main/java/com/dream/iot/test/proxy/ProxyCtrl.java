package com.dream.iot.test.proxy;

import com.dream.iot.client.ClientProxySync;
import com.dream.iot.client.handle.IotBody;
import com.dream.iot.client.handle.IotCtrl;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.proxy.ProxyClientMessageBody;
import com.dream.iot.proxy.ProxyClientResponseBody;
import com.dream.iot.server.protocol.LocalLoopProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IotCtrl("ctrl.test.")
public class ProxyCtrl {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @IotCtrl("param")
    public void test(String deviceSn, @IotBody ProxyClientMessageBody body, String status) {
        logger.info("代理客户端测试 类型：参数测试 - 设备编号：{} - 状态：{}", deviceSn
                , deviceSn != null && body !=null && status != null? "通过" : "不通过");
    }

    @IotCtrl("sync")
    public ClientProxySync sync(String deviceSn) {
        // 返回控制设备协议
        return new LocalLoopProtocol().setFreeProtocolHandle(protocol -> {
            logger.info("代理客户端测试 类型：代理测试 - 设备编号：{} - 状态：{}", deviceSn, "通过");

            // 响应给应用客户端操作设备的一些状态
            return new ProxyClientResponseBody("控制设备成功", ExecStatus.success);
        });
    }

    @IotCtrl("multi")
    public void multi(String deviceSn) {
        logger.info("代理客户端测试 类型：多客户端测试 - 设备编号：{} - 状态：{}", deviceSn, "通过");
    }
}
