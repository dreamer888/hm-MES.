1. 首先创建测试环境
   1. 安装Taos数据库(https://www.taosdata.com/docs/cn/v2.0/getting-started#install)
   2. 安装taos客户端
   3. 配置系统的host文件(windows/linux自己找教程), 在配置文件里面加入ip和域名的对应关系
   以windows为例：假设ip是 192.168.20.100 域名是：node1 则在hosts文件配置如下：192.168.20.100 node1
   然后在配置数据源url的时候必须使用域名：jdbc:TAOS://node1:6030/test
2. 启用Taos测试
   1. 配置文件启用：iot.test.taos-start=true
   2. 配置并发数也就是设备数：iot.num=1000
   3. 调整对象[FixedLengthClientRequestHandle]的maxOfPeer参数和period参数来测试批量写入功能