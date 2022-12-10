package com.dream.iot.server;

import com.dream.iot.*;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.server.protocol.ServerInitiativeProtocol;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * <p>用来管理{@link ProtocolPreservable#relationKey()}与{@link Protocol}的映射关系</p>
 * 在报文超时后会自动剔除超时的协议
 * Create Date By 2017-09-17
 * @author dream
 * @since 1.7
 */
public class ServerTimeoutProtocolManager extends AbstractProtocolTimeoutManager {

    @Autowired
    private IotThreadManager iotThreadManager;

    public ServerTimeoutProtocolManager(List<ProtocolTimeoutStorage> timeoutStorages) {
        super(timeoutStorages);
    }

    public ServerTimeoutProtocolManager(List<ProtocolTimeoutStorage> timeoutStorages, Executor executor) {
        super(timeoutStorages, executor);
    }

    protected String protocolRemoveHandle(Protocol protocol) {
        if(protocol instanceof ServerInitiativeProtocol) {
           try {
               ((ServerInitiativeProtocol<?>) protocol).setExecStatus(ExecStatus.timeout);

               return protocol.getEquipCode();
           } finally {
               // 同步请求则释放锁 由调用线程继续执行业务
               if(((ServerInitiativeProtocol<?>) protocol).isSyncRequest()) {
                   ((ServerInitiativeProtocol<?>) protocol).releaseLock();
               } else {
                   // 异步请求使用工作线程执行业务
                   iotThreadManager.getWorkerGroup().next().execute(() -> {
                       // 执行业务
                       ((ServerInitiativeProtocol<?>) protocol).exec(IotServeBootstrap.BUSINESS_FACTORY);
                   });
               }
           }
        }

        return null;
    }
}
