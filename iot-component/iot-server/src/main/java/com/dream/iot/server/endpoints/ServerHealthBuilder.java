package com.dream.iot.server.endpoints;

import com.dream.iot.DeviceManager;
import com.dream.iot.server.ServerComponentFactory;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

public class ServerHealthBuilder {

    private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static RuntimeMXBean mxb = ManagementFactory.getRuntimeMXBean();
    private static OperatingSystemMXBean system =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static ServerHealthEntity build(ServerComponentFactory factory) {
        ServerHealthEntity.JvmInfo jvmInfo = buildJvmInfo();
        ServerHealthEntity.SystemInfo systemInfo = buildSystemInfo();
        ServerHealthEntity.ComponentInfo componentInfo = buildComponentInfo(factory);
        return new ServerHealthEntity(0, systemInfo, jvmInfo, componentInfo);
    }

    public static ServerHealthEntity.SystemInfo buildSystemInfo() {
        return new ServerHealthEntity.SystemInfo().setSystemCpuLoad(system.getSystemCpuLoad())
                .setOs(system.getName())
                .setArch(system.getArch())
                .setVersion(system.getVersion())
                .setProcessCpuLoad(system.getProcessCpuLoad())
                .setProcessCpuTime(system.getProcessCpuTime())
                .setFreePMSize(system.getFreePhysicalMemorySize())
                .setTotalPMSize(system.getTotalPhysicalMemorySize());
    }

    public static ServerHealthEntity.JvmInfo buildJvmInfo() {
        return new ServerHealthEntity.JvmInfo(memoryMXBean.getHeapMemoryUsage(), memoryMXBean.getNonHeapMemoryUsage());
    }

    public static ServerHealthEntity.ComponentInfo buildComponentInfo(ServerComponentFactory factory) {
        List<ServerHealthEntity.Component> components = new ArrayList<>();
        factory.getTcpserverComponents().forEach(item -> {
            DeviceManager deviceManager = item.getDeviceManager();
            components.add(new ServerHealthEntity.Component(item.config().getPort(), item.getName()
                    , "tcp", item.startTime(), deviceManager.size(), deviceManager.useSize()));
        });

        factory.getUdpServerComponents().forEach(item -> {
            components.add(new ServerHealthEntity.Component(item.config().getPort(), item.getName(), "udp", item.startTime()));
        });

        return new ServerHealthEntity.ComponentInfo(components);
    }

    public static Result toResult(long appStartTime, ServerComponentFactory factory) {
        ServerHealthEntity entity = ServerHealthBuilder.build(factory);
        entity.setStartTime(appStartTime);
        return Result.success(entity);
    }

}
