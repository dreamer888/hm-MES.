-- taos数据库测试脚本
create database if not exists iot keep 10 update 2 PRECISION 'ns';
create stable if not exists meters (
    ts timestamp,
    v double,
    i double,
    power1 double,
    power2 double,
    py double
) tags (
    location nchar(20),
    device_sn nchar(20)
);