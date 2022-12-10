package com.dream.iot.test;

public class SystemInfo {

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
    private long freePhysicalMemorySize;

    /**
     * 总物理内存
     */
    private long totalPhysicalMemorySize;

    /**
     * tcp连接数
     */
    private long tcpLinkCount;

    /**
     * 已注册设备编号的tcp连接
     */
    private long tcpUseLinkCount;

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

    public long getTcpLinkCount() {
        return tcpLinkCount;
    }

    public SystemInfo setTcpLinkCount(long tcpLinkCount) {
        this.tcpLinkCount = tcpLinkCount;
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

    public long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public SystemInfo setFreePhysicalMemorySize(long freePhysicalMemorySize) {
        this.freePhysicalMemorySize = freePhysicalMemorySize;
        return this;
    }

    public long getTotalPhysicalMemorySize() {
        return totalPhysicalMemorySize;
    }

    public SystemInfo setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        return this;
    }

    public long getTcpUseLinkCount() {
        return tcpUseLinkCount;
    }

    public SystemInfo setTcpUseLinkCount(long tcpUseLinkCount) {
        this.tcpUseLinkCount = tcpUseLinkCount;
        return this;
    }
}
