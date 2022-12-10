package com.dream.iot.test;

import com.dream.iot.server.manager.DevicePipelineManager;
import com.dream.iot.utils.UniqueIdGen;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/**
 * 服务器信息
 */
public class ServerInfoUtil {

    public static OperatingSystemMXBean system() {
        OperatingSystemMXBean operatingSystemMXBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        operatingSystemMXBean.getProcessCpuTime();
        return operatingSystemMXBean;
    }

    public static SystemInfo getSystemInfo() {
        OperatingSystemMXBean system = system();
        return new SystemInfo().setSystemCpuLoad(system.getSystemCpuLoad())
                .setOs(system.getName())
                .setArch(system.getArch())
                .setVersion(system.getVersion())
                .setProcessCpuLoad(system.getSystemCpuLoad())
                .setProcessCpuTime(system.getProcessCpuTime())
                .setTcpLinkCount(DevicePipelineManager.getTcpLink())
                .setTcpUseLinkCount(DevicePipelineManager.getUseTcpLink())
                .setFreePhysicalMemorySize(system.getFreePhysicalMemorySize())
                .setTotalPhysicalMemorySize(system.getTotalPhysicalMemorySize());
    }

    public static String getMessageId() {
        return UniqueIdGen.messageId();
    }

    /**
     * 打印服务端系统信息
     * @param payload
     */
    public static void printServerInfo(SystemInfo payload) {
        long totalPhysicalMemorySize = payload.getTotalPhysicalMemorySize() / 1024 / 1024;
        long freePhysicalMemorySize = payload.getFreePhysicalMemorySize() / 1024 / 1024;
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.println(String.format("                              IOT信息(TCP) - 总连接：%s - 已注册：%s                     ", payload.getTcpLinkCount(), payload.getTcpUseLinkCount()));
        System.out.println(String.format("                            服务器系统 - 操作系统：%s - 版本：%s                      ", payload.getOs(), payload.getVersion()));
        System.out.println(String.format("   服务器配置 - cpu架构：%s - 总内存：%s(MB) - 可用内存：%s(MB) - cpu使用率(百分比)：%s   "
                , payload.getArch(), totalPhysicalMemorySize, freePhysicalMemorySize
                , payload.getSystemCpuLoad() * 100, payload.getProcessCpuLoad() * 100));
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }

    public static boolean compare(SystemInfo ori, SystemInfo desc) {
        return ori.getFreePhysicalMemorySize() == desc.getFreePhysicalMemorySize() && ori.getProcessCpuLoad() == desc.getProcessCpuLoad();
    }
}
