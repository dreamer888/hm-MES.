package com.dream.iot.server.endpoints;

import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * 服务健康实体
 */
public class ServerHealthEntity {

    /**
     * 应用启动时间
     */
    private long startTime;

    /**
     * 采集时间
     */
    private long collectTime;

    /**
     * 服务信息
     */
    private JvmInfo jvm;

    /**
     * 系统信息
     */
    private SystemInfo system;

    /**
     * 服务信息
     */
    private ComponentInfo server;

    public ServerHealthEntity(long startTime, SystemInfo system, JvmInfo jvm, ComponentInfo server) {
        this.jvm = jvm;
        this.system = system;
        this.server = server;
        this.startTime = startTime;
        this.collectTime = System.currentTimeMillis();
    }

    public static class SystemInfo {

        /**
         * 操作系统名称
         */
        private String os;

        /**
         * cpu架构
         */
        private String arch;

        /**
         * 操作系统版本
         */
        private String version;

        /**
         * 进程占用的cpu时间片段
         */
        private long processCpuTime;

        /**
         * 系统cpu使用率
         */
        private double systemCpuLoad;

        /**
         * jvm进程cpu使用率
         */
        private double processCpuLoad;

        /**
         *  可用的物理内存
         */
        private long freePMSize;

        /**
         * 总物理内存
         */
        private long totalPMSize;

        public String getOs() {
            return os;
        }

        public SystemInfo setOs(String os) {
            this.os = os;
            return this;
        }

        public String getArch() {
            return arch;
        }

        public SystemInfo setArch(String arch) {
            this.arch = arch;
            return this;
        }

        public String getVersion() {
            return version;
        }

        public SystemInfo setVersion(String version) {
            this.version = version;
            return this;
        }

        public long getProcessCpuTime() {
            return processCpuTime;
        }

        public SystemInfo setProcessCpuTime(long processCpuTime) {
            this.processCpuTime = processCpuTime;
            return this;
        }

        public double getSystemCpuLoad() {
            return systemCpuLoad;
        }

        public SystemInfo setSystemCpuLoad(double systemCpuLoad) {
            this.systemCpuLoad = systemCpuLoad;
            return this;
        }

        public double getProcessCpuLoad() {
            return processCpuLoad;
        }

        public SystemInfo setProcessCpuLoad(double processCpuLoad) {
            this.processCpuLoad = processCpuLoad;
            return this;
        }

        public long getFreePMSize() {
            return freePMSize;
        }

        public SystemInfo setFreePMSize(long freePMSize) {
            this.freePMSize = freePMSize;
            return this;
        }

        public long getTotalPMSize() {
            return totalPMSize;
        }

        public SystemInfo setTotalPMSize(long totalPMSize) {
            this.totalPMSize = totalPMSize;
            return this;
        }
    }

    /**
     * jvm信息
     */
    public static class JvmInfo {

        /**
         * 堆内存信息
         */
        private MemoryUsage heapMemory;

        /**
         * 非堆内存信息
         */
        private MemoryUsage nonHeapMemory;

        public JvmInfo(MemoryUsage heapMemory, MemoryUsage nonHeapMemory) {
            this.heapMemory = heapMemory;
            this.nonHeapMemory = nonHeapMemory;
        }

        public MemoryUsage getHeapMemory() {
            return heapMemory;
        }

        public void setHeapMemory(MemoryUsage heapMemory) {
            this.heapMemory = heapMemory;
        }

        public MemoryUsage getNonHeapMemory() {
            return nonHeapMemory;
        }

        public void setNonHeapMemory(MemoryUsage nonHeapMemory) {
            this.nonHeapMemory = nonHeapMemory;
        }
    }

    public static class ComponentInfo {

        /**
         * 各个服务组件信息
         */
        private List<Component> components;

        public ComponentInfo(List<Component> components) {
            this.components = components;
        }

        public List<Component> getComponents() {
            return components;
        }

        public void setComponents(List<Component> components) {
            this.components = components;
        }
    }

    public static class Component {

        /**
         * 监听端口
         */
        private int port;

        /**
         * 服务组件名称
         */
        private String name;

        /**
         * 协议类型 tcp or udp
         */
        private String protocol;

        /**
         * 组件端口启动时间
         */
        private long startTime;

        /**
         * 已连接数
         */
        private long linkingCount;

        /**
         * 已注册数量
         */
        private long registerCount;

        public Component(int port, String name, String protocol, long startTime) {
            this(port, name, protocol, startTime, 0, 0);
        }

        public Component(int port, String name, String protocol, long startTime, long linkingCount, long registerCount) {
            this.port = port;
            this.name = name;
            this.protocol = protocol;
            this.startTime = startTime;
            this.linkingCount = linkingCount;
            this.registerCount = registerCount;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getLinkingCount() {
            return linkingCount;
        }

        public void setLinkingCount(long linkingCount) {
            this.linkingCount = linkingCount;
        }

        public long getRegisterCount() {
            return registerCount;
        }

        public void setRegisterCount(long registerCount) {
            this.registerCount = registerCount;
        }
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public SystemInfo getSystem() {
        return system;
    }

    public void setSystem(SystemInfo system) {
        this.system = system;
    }

    public JvmInfo getJvm() {
        return jvm;
    }

    public void setJvm(JvmInfo jvm) {
        this.jvm = jvm;
    }

    public ComponentInfo getServer() {
        return server;
    }

    public void setServer(ComponentInfo server) {
        this.server = server;
    }
}
