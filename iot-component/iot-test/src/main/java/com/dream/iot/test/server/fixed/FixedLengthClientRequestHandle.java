package com.dream.iot.test.server.fixed;

import com.dream.iot.consts.ExecStatus;
import com.dream.iot.redis.handle.RedisListHandle;
import com.dream.iot.server.ServerProtocolHandle;
import com.dream.iot.test.TestConst;
import com.dream.iot.test.taos.TaosBreakerUsingStable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

/**
 * 固定长度+redis测试
 */
public class FixedLengthClientRequestHandle implements ServerProtocolHandle<FixedLengthClientRequestProtocol>
        , RedisListHandle<FixedLengthClientRequestProtocol, TaosBreakerUsingStable> {

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 从redis消费数据
    @Override
    public Integer consumer(List<TaosBreakerUsingStable> objects) {
        return objects.size();
    }

    // 此方法直接写业务
    @Override
    public Object handle(FixedLengthClientRequestProtocol protocol) {
        final String equipCode = protocol.getEquipCode();

        scheduler.execute(() -> {
            final long id = Thread.currentThread().getId();
            new FixedLengthServerRequestProtocol(equipCode).sync(2000).request(protocol1 -> {
                final long id1 = Thread.currentThread().getId();
                if(protocol1 instanceof FixedLengthServerRequestProtocol) {
                    final ExecStatus execStatus = ((FixedLengthServerRequestProtocol) protocol1).getExecStatus();
                    // 同步请求 调用线程 == 执行线程
                    if(execStatus == ExecStatus.timeout) {
                        if(id == id1) {
                            logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端同步超时测试", "sync(long) + request()", equipCode, "通过");
                        } else {
                            logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端同步超时测试", "sync(long) + request()", equipCode, "失败");
                        }
                    } else {
                        if(id == id1) {
                            logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端同步测试", "sync(long) + request()", equipCode, "通过");
                        } else {
                            logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端同步测试", "sync(long) + request()", equipCode, "失败");
                        }
                    }
                }

                return null;
            });

            new FixedLengthServerRequestProtocol(equipCode).timeout(2000).request(protocol2 -> {
                final long id1 = Thread.currentThread().getId();
                if(protocol2 instanceof FixedLengthServerRequestProtocol) {
                    final ExecStatus execStatus = ((FixedLengthServerRequestProtocol) protocol2).getExecStatus();
                    if(execStatus == ExecStatus.timeout) {
                        // 异步请求 调用线程 != 执行线程
                        if(id != id1) {
                            logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端异步超时测试", "timeout(long) + request()", equipCode, "通过");
                        } else {
                            logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端异步超时测试", "timeout(long) + request()", equipCode, "失败");
                        }
                    } else {
                        if(id != id1) {
                            logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端异步测试", "timeout(long) + request()", equipCode, "通过");
                        } else {
                            logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "服务端异步测试", "timeout(long) + request()", equipCode, "失败");
                        }
                    }
                }
                return null;
            });
        });

        return null;
    }

    /**
     * 数据将写入到此Key的list里面
     * @return
     */
    @Override
    public String getKey() {
        return "Fixed_Redis_Key";
    }

}
