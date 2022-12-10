### 物联网网络中间件
使用java语言且基于netty, spring boot, redis等开源项目开发来的物联网网络中间件, 支持udp, tcp通讯等底层协议和http, mqtt, websocket(默认实现和自定义协议头实现), modbus(tcp,rtu),plc,dtu(支持心跳，设备注册功能以及AT协议和自定义协议支持),dtu for modbus tcp,dtu for modbus rtu组件适配 等上层协议. 主打工业物联网底层网络交互、设备管理、数据存储、大数据处理. (其中plc包括西门子S7系列，欧姆龙Fins，罗克韦尔CIP，三菱MC). 数据存储将使用taos（TdEngine时序数据库）数据库以及redis消息队列
#### 如果您觉得的还可以点个star让更多开发者了解此项目
#### 主要特性
- 支持服务端启动监听多个端口, 统一所有协议可使用的api接口
- 包含一套代理客户端通信协议，支持调用：客户端 -> 服务端 -> 设备 -> 服务端 -> 客户端
- 支持设备协议对象和其业务对象进行分离(支持默认业务处理器【spring单例注入】和自定义业务处理器)
- 支持同步和异步调用设备, 支持应用程序代理客户端和设备服务端和设备三端之间的同步和异步调用
- 服务端支持设备上线/下线/异常的事件通知, 支持自定义心跳事件， 客户端支持断线重连
- 丰富的日志打印功能，包括设备上线，下线提示， 一个协议的生命周期(请求或者请求+响应)等
- 支持请求时如果连接断线会自动重连(同步等待成功后发送)
- 支持客户端发送请求时如果客户端不存在将自动创建客户端(同步等待成功后发送)
- 支持作为mqtt网关，将从工业物联网采集的数据更加简单方便的发布到mqtt服务器
- 支持常用的物联网协议比如：mqtt、plc、modbus、websocket
- 支持通过dtu方式使用modbus协议操作plc
- 支持 mysql8.0  和 TdEngine  

#### 模拟工具
1. [QtSwissArmyKnife](https://gitee.com/qsaker/QtSwissArmyKnife) 支持udp、tcp、modbus、websocket、串口等调试  ，源码
2. [IotClient] 支持plc(西门子，欧姆龙，三菱)，modbus，串口，mqtt，tcp, udp等模拟和调试 。源码
3. NetAssist.exe  ，二进制
4. UartAssist.exe  ， 二进制
5. CX-Programmer  ,plc调试软件，需要自己网络下载

**其它工具**

1、windows 下 杀进程工具  ， 使用方法  ， 比如要杀 nginx  ，  命令窗口 输入tskill  nginx  

2 服务安装和卸载工具 ， 



### 开发文档
#### 1. 名词解释
1. 报文对象(Message)：报文是对在网络中进行传输的二进制数据的封装，也是二进制数据的载体，在一定程度上 报文 = 二进制数据
2. 协议对象(Protocol)：协议是报文的一个规范约束，比如报文内容是：0x04 AF CD EE 03，那怎么知道这一串表示的是什么呢，协议就是对这一串数据的声明, 比如第一个字节代表数据后面还有几个字节的长度, 第二个字节是电压，第三字节是电流，第四个字节是校验位
3. 组件对象(FrameworkComponent)：在服务端，组件用来管理一个端口所需要的各种接口；在客户端，组件用来管理连接同一个服务器的所有接口以及已经连接的所有客户端。比如服务端的设备管理器，报文需要用到的编解码器，同步异步处理都是由组件来管理
4. 同步：在调用请求的时候，请求线程会加锁阻塞，直到接收到响应或者超时来解锁
5. 异步：在调用请求的时候，请求线程在发送完报文后直接返回，不阻塞调用线程， 而是注册一个回调函数，等到对方响应或者超时的时候在做业务处理
6. 编解码器：用来对网络上的二进制数据进行拆包和粘包的处理对象
7. 协议工厂(ProtocolFactory)：用来创建各个协议对象的地方(因为一个客户端可能包含多个功能(协议), 每个功能对应一个协议对象那就会有很多协议对象，协议工厂用来管理协议对象的创建)
8. 协议处理器(ProtocolHandle): 用来对协议做业务处理的

下面将由一个例子来展开说明iot框架的使用
 **例子：比如服务端接受到客户端报文如下：0x01 11 12 13 05 06 EE FF 八个字节， 如果客户端发送完此报文之后没有连续发送， 服务端接受到的数据就是一包完整的报文， 我们可以很容易的读取缓存区的内容然后进行处理; 那如果第一包发送完之后服务端还在忙其他的时没有及时读取缓冲区内容这时候又接收到了客户端的第二个报文，这时候数据缓冲区的数据如下：0x01 11 12 13 05 06 EE FF 02 21 22 23 25 26 AA BB CC DD，这时候程序读取的缓冲区是两包完整的报文，这时候程序怎么将两包报文拆开处理呢？这时候就需要编解码器上场了！** 
如何处理上面报文粘包和拆包的情况呢？netty提供了一下几种常用的解码器

#### 2. 编解码器
1. FixedLengthFrameDecoder: 固定长度解码器是最简单的一种方式，每个包的长度是固定的，比如每个包都是8个字节，那么程序就可以以8字节为单位进行拆包
2. LineBasedFrameDecoder：换行符解码器是让每个报文都用换行符结尾，这时候程序可以循环读取每个字节判断，如果这个字节是换行符就说明已经读完了一包报文
3. DelimiterBasedFrameDecoder：如果我们的数据里面刚好包含换行符这时候读取就会出错，这时候可以用自定义分隔符来拆分报文
4. LengthFieldBasedFrameDecoder：不管是换行符解码还是自定义分隔符解码，都需要循环判断每个字节，如果在一包完整的报文很长的情况下性能会非常差，这时候有个非常好用且性能极高的解码器，长度字段解码，就是在报文里面加入一个长度字段用来标识整个报文的长度
5. ByteToMessageDecoder：如果还有更好的解码方式可以使用自定义报文解码
6. SimpleChannelInboundHandler：简单的解码器

在设备对接的时候厂家一般会提供协议文档，然后就需要我们来选择合适的解码器，当我们确认好了解码器之后就可以开始编码了，下面先开始服务端的对接教程
#### 3. 服务端教程
编写服务端网络程序时需要监听某个端口来给客户端连接， 当我们选择某个解码器之后就可以选择对应的服务端解码器组件来开启某个端口，iot框架适配了netty提供的几个常用的解码器
##### 创建解码器组件
1. FixedLengthFrameDecoderServerComponent 使用固定长度解码器的服务端组件
2. LineBasedFrameDecoderServerComponent 使用换行符解码器的服务端组件
3. DelimiterBasedFrameDecoderServerComponent 使用自定义分隔符解码器的服务端组件
4. LengthFieldBasedFrameDecoderServerComponent 使用长度字段解码器的服务端组件
5. ByteToMessageDecoderServerComponent 使用自定义解码器的服务端组件
6. DatagramPacketDecoderServerComponent udp协议的服务端组件
7. SimpleChannelDecoderComponent 简单自定义解码器对应的组件

以下是使用LengthFieldBasedFrameDecoderServerComponent示例
```
// 首先：必须先创建一个组件对象来继承LengthFieldBasedFrameDecoderServerComponent
// 以iot-test模块的断路器服务端模拟为例
public class BreakerServerComponent extends LengthFieldBasedFrameDecoderServerComponent<BreakerServerMessage> {

    public BreakerServerComponent(ConnectProperties connectProperties) {
        super(connectProperties, ByteOrder.LITTLE_ENDIAN, 256, 0
                , 4, 0, 0, true);
    }
    xxx 实现省略
}
// 注：要求传入ConnectProperties对象作为构造参数， 此对象可以指定ip和端口
```
我们看到上面的组件需要一个泛型参数BreakerServerMessage, 此参数就是报文对象，上面我们说过报文对象是一个二进制数据载体，用于在iot框架各个对象中进行使用，下面我们来看看报文对象除了作为数据载体还有哪些扩展功能

##### 创建报文对象
```
// 创建服务端报文对象必须继承ServerMessage类
public class BreakerServerMessage extends ServerMessage {

    public BreakerServerMessage(byte[] message) {
        super(message);
    }

    //省略其他构造函数

    @Override
    protected MessageHead doBuild(byte[] message) {
        this.messageBody = MessageCreator.buildBreakerBody(message);
        return MessageCreator.buildBreakerHeader(message);
    }
}
```
1. 首先报文对象的构造函数BreakerServerMessage#BreakerServerMessage(byte[])必须存在
2. 报文对象是连接组件和协议的桥梁，所以需要为每个服务端组件创建一个与之对应的报文对象
3. 需要在BreakerServerMessage#doBuild(byte[])方法里初步解析出对应的equipCode、messageId、protocolType几个参数，这也是对客户端请求数据的初步解析

当我们创建了组件和报文类之后就可以启动应用了， 这时候日志里面会打印出组件配置的端口已经开启监听了。到了这里已经开了一个好头了算是成功了一半了，接下来就是创建协议对象了，从厂家那里拿到的协议文档至少包含一个协议， 一般我们建议为文档里面的每个协议创建一个对应的协议对象(Protocol)
##### 创建协议对象
协议对象就是用来将接收到的二进制数据解析成和协议文档里对应的字段的；出于框架架构的需要我们将协议分成两种类型 如下：
1. ClientInitiativeProtocol 声明此协议是客户端主动发起的协议  比如断路器主动上报当前的电流、电压、或报警
2. ServerInitiativeProtocol 声明此协议是服务端主动发起的协议  比如服务端下发断开断路器的指令

首先我们先看一下ClientInitiativeProtocol方法声明， 还是以断路器为例

```
// 用来接收断路器主动上报的电压电流等数据
public class DataAcceptProtocol extends ClientInitiativeProtocol<BreakerServerMessage> {

    private double v; // 电压
    private double i; // 电流
    private double power1; // 有功功率
    private double power2; // 无功功率
    private double py; // 功率因素

    public DataAcceptProtocol(BreakerServerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(BreakerServerMessage requestMessage) {
        byte[] message = requestMessage.getBody().getMessage();
        this.v = ByteUtil.bytesToInt(message, 0) / 100.0;
        this.i = ByteUtil.bytesToInt(message, 4) / 100.0;
        this.power1 = ByteUtil.bytesToInt(message, 8) / 100.0;
        this.power2 = ByteUtil.bytesToInt(message, 12) / 100.0;
        this.py = ByteUtil.bytesToShort(message, 16) / 100.0;
    }

    // 响应断路器的请求
    @Override
    protected BreakerServerMessage doBuildResponseMessage() {
        Message.MessageHead head = requestMessage().getHead();
        return new BreakerServerMessage(MessageCreator.buildBreakerHeader(head
                .getEquipCode(), head.getMessageId(), 4, head.getType()),
                MessageCreator.buildBreakerBody(StatusCode.Success));
    }

    // 省略其他
}
```
平台已经可以接收设备主动上报的数据了，那平台要怎么主动给设备发送数据呢？ServerInitiativeProtocol协议就是用来声明一个协议是平台主动发给客户端的，下面以平台下发给断路器切换开关为例

```
/**
 * 切换断路器的开闭状态
 */
public class SwitchStatusProtocol extends ServerInitiativeProtocol<BreakerServerMessage> {

    private String deviceSn;

    public SwitchStatusProtocol(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    /**
     * 构建要发送给断路器的报文
     */
    @Override
    protected BreakerServerMessage doBuildRequestMessage() throws IOException {
        DefaultMessageHead messageHead = MessageCreator.buildBreakerHeader(Long.valueOf(this.deviceSn), 0, protocolType());
        return new BreakerServerMessage(messageHead);
    }

    /**
     * 处理断路器对此处请求的响应
     */
    @Override
    protected void doBuildResponseMessage(BreakerServerMessage message) {
        /*设备响应是否切换成功的处理*/
    }

    @Override
    public BreakerProtocolType protocolType() {
        return BreakerProtocolType.SwitchStatus;
    }
}
// 然后在业务代码里面调用请求方法：new SwitchStatusProtocol(deviceSn).request(); 这样就可以向指定的设备发起请求了
```

##### 创建报文头对象
看到现在我们已经可以接收到设备的请求和主动发起请求给设备了，但是有一个问题，一般一台设备会包含很多功能(协议)，因为每个功能都会创建一个协议对象那么问题来了，当设备发给平台的时候平台怎么知道需要交给哪个协议处理呢？ 一般来说如果有多个协议那么协议文档一般会有一个字段用来区分的， 比如报文：0x01 11 12 13 05 06 EE FF的第二个字节 0x11作为协议类型字段，比如01代表切换开关，02代表上报温湿度，03代表锁断路器；所以我们需要先解析出协议类型(如果有的话)
我们在看一下报文对象：需要在doBuild方法里面先解析出报文头(MessageHead)，报文头里面需要包含设备编号和协议类型等信息
```
// 先看一下报文头的接口声明，其中type就是用来声明一个客户端的协议类型的
interface MessageHead {

    /**
     * 设备编号
     * @return
     */
    String getEquipCode();

    /**
     * 报文的唯一编号
     * @return
     */
    String getMessageId();

    /**
     * 获取交易类型
     * @return
     */
    <T> T getType();

    byte[] getMessage();

    default int getLength() {
        return getMessage().length;
    }
}
// 然后在报文对象里面解析出报文头
public class BreakerServerMessage extends ServerMessage {
    //省略其他构造函数
    @Override
    protected MessageHead doBuild(byte[] message) {
        this.messageBody = MessageCreator.buildBreakerBody(message);
        return MessageCreator.buildBreakerHeader(message);
    }
}
```
##### 创建协议对象(协议工厂)
上面已经解析出了对应的协议类型了，下一步就需要创建协议对象来解析对应的报文了。上面说过组件对象用来管理整个框架中的各个接口, 首先组件对象实现了协议工厂接口 如下：

```
// 协议工厂接口
public interface IotProtocolFactory<T extends SocketMessage> extends StorageManager<String, Protocol>
// 组件对象实现了协议工厂接口 如下
public abstract class TcpDecoderServerComponent<M extends ServerMessage> extends TcpServerComponent<M> implements IotSocketServer, IotProtocolFactory<M>
```
所以我们可以在组件里面来管理各个协议对象的创建，以断路器组件为例：

```
public class BreakerServerComponent extends LengthFieldBasedFrameDecoderServerComponent<BreakerServerMessage> {
    @Override
    public AbstractProtocol getProtocol(BreakerServerMessage message) {
        // 因为我们已经解析出了协议类型了，所以可以通过协议类型创建对应的协议
        BreakerProtocolType type = message.getHead().getType();

        // 断路器主动推送电压和电流值
        if(type == BreakerProtocolType.PushData) {
             return new DataAcceptProtocol(message);
        } else { // 移除平台主动请求的协议
            return remove(message.getHead().getMessageId());
        }
    }
    // 省略其他方法
}

```
##### 协议处理器(业务处理)
现在我们已经通过协议解析出了客户端给我们的数据了，接下去就是对这些数据进行处理, 比如数据入库，实时批处理，缓存等等操作，iot框架提供了一个专门的接口处理业务，先看协议处理器的声明 如下：

```
public interface ProtocolHandle<T extends Protocol> {

    Method method = ReflectionUtils.findMethod(ProtocolHandle.class, "handle", Protocol.class);

    /**
     * 协议的业务处理
     * @param protocol
     */
    Object handle(T protocol);

}
```
只有一个方法handle，注意泛型参数<T extends Protocol>，此参数用来声明此处理器是用于处理哪个协议的，所以理论上一个协议对象有一个与之对应的处理器(不需要业务处理的协议就不需要创建)，那问题来了，程序是怎么根据协议对象获取对应的处理器的呢？这时候需要将协议处理器对象注入到spring容器，将此对象交由spring容器管理，所以我们需要一个处理器工厂用来可以方便的通过协议对象获取对应的协议处理器对象 声明如下：

```
public abstract class BusinessFactory<T extends ProtocolHandle> implements InitializingBean, BeanFactoryAware {

    private ListableBeanFactory beanFactory;
    private HashMap<Class<? extends Protocol>, T> mapper;

    // 通过协议类型对象获取对应的协议处理器对象
    public T getProtocolHandle(Class<? extends Protocol> protocolClazz){
        return mapper.get(protocolClazz);
    }
    // 省略其他接口
}
```
下面我们看一下断路器的协议处理器对象：

TDengine
涛思 taos 时序数据库

```
// 注入到spring容器
@Component
public class DataAcceptHandle implements ServerProtocolHandle<DataAcceptProtocol> {
    // 比如做数据入库、发送到mqtt网关、缓存到redis、提交到
    @Override
    public Object handle(DataAcceptProtocol protocol) {
        final int i = RandomUtil.randomInt(1, 9);
        if(i % 2 == 0) { // 测试自动创建数据表
            TaosBreakerUsingStable entity = new TaosBreakerUsingStable(protocol.getEquipCode());
            entity.setI(protocol.getI());
            entity.setV(protocol.getV());
            entity.setPy(protocol.getPy());
            entity.setSn(protocol.getEquipCode());
            entity.setPower1(protocol.getPower1());
            entity.setPower2(protocol.getPower2());

            return entity;
        } else { // 测试插入数据表
            TaosBreakerDataTable dataTable = new TaosBreakerDataTable();
            dataTable.setI(protocol.getI());
            dataTable.setV(protocol.getV());
            dataTable.setPy(protocol.getPy());
            dataTable.setTs(new Date());
            dataTable.setSn(protocol.getEquipCode());
            dataTable.setPower1(protocol.getPower1());
            dataTable.setPower2(protocol.getPower2());
            return dataTable;
        }
    }     
}
```
##### 响应客户端请求
如果我们需要确认客户端的请求， 比如断路器要同步平台的时间, 这时候平台接收到请求之后需要将时间按照协议格式发给客户端这时候要怎么将数据给客户端？还是以断路器为例：

```
// 比如当我们收到断路器上报的电压电流值之后需要跟断路器说一声我已经收到你发送的值了，可以在 #doBuildResponseMessage()方法里面根据协议文档的要求构建要发送给断路器的报文, 平台会主动响应给断路器
public class DataAcceptProtocol extends ClientInitiativeProtocol<BreakerServerMessage> {
    // 确认断路器请求
    @Override
    protected BreakerServerMessage doBuildResponseMessage() {
        Message.MessageHead head = requestMessage().getHead();
        return new BreakerServerMessage(MessageCreator.buildBreakerHeader(head
                .getEquipCode(), head.getMessageId(), 4, head.getType()),
                MessageCreator.buildBreakerBody(StatusCode.Success));
    }
    // 省略其他
}
```
到了这里这个iot框架已经实现了闭环了，接收客户端数据 -> 数据解析 -> 业务处理 -> 响应给客户端 或者 平台请求客户端 -> 客户端反馈处理结果 -> 平台接收结果 -> 业务处理。但是还有很多的细节需要处理，比如协议的同步和异步处理，设备的注册等等。接着往下看!
##### 协议异步请求
异步请求指的是不阻塞调用请求的线程(不等待设备响应只管发送)，这里面会碰到的问题：
1. 不知道设备有没有接收到请求： 写出去之后可能因为网络等原因导致设备没收到请求或者执行了请求指令应答的时候出现了问题，平台没有收到应答
解决方案：协议在请求之后保存此协议对象，如果因为网络等原因设备没有应答标记此协议状态为超时，平台可以在超时的时候下发查询指令来查询设备的状态
2. 设备有没有成功执行此请求：设备收到了请求，但是设备执行请求失败了
解决方案：由于保存了协议，这时候再设备应答的时候可以取出对应的请求协议，然后解析响应报文就可以知道设备对请求指令的执行状态了

##### 协议同步请求
我们知道tcp和http不一样并不是基于同步请求响应模式，也就是说平台给设备发送请求完之后请求线程不会阻塞等待设备的响应，当然也不建议阻塞等待因为太耗费性能，这也是物联网一般不会使用http的原因；因为物联网设备太多如果都使用同步会太耗费服务器资源，但是iot框架提供了一个同步机制，原理是调用请求方法时在写出报文之后加锁阻塞此请求然后等设备响应或者响应超时后释放锁。虽然同步会阻塞请求线程，但是在一些需要实时知道请求结果得场景下(比如前端发起请求之后需要知道执行状态)可以简化是流程

##### messageId
不管是同步请求还是异步请求，这里都会有个问题就是设备在应答的时候怎么知道此应答对应哪一条请求指令(协议)，解决的方法也很简单给每条下发指令的报文做一个标记(MessageId)，设备在应答的时候把请求报文的MessageId原封不动的发上来，然后只需要通过此messageId获取对应的请求协议对象(保存协议对象时用map存储, messageId作为key, Protocol作为value)，那我们在什么时候解析此messageId呢？ 如下：

```
// 1. 在报文里面解析报文头得时候一起解析出messageId
public class BreakerServerMessage extends ServerMessage {
    //省略其他构造函数
    @Override
    protected MessageHead doBuild(byte[] message) {
        this.messageBody = MessageCreator.buildBreakerBody(message);
        return MessageCreator.buildBreakerHeader(message);
    }
}
// 2. 在对应得组件里面移除掉对应的协议并且返回
public class BreakerServerComponent extends LengthFieldBasedFrameDecoderServerComponent<BreakerServerMessage> {
    @Override
    public AbstractProtocol getProtocol(BreakerServerMessage message) {
        // 因为我们已经解析出了协议类型了，所以可以通过协议类型创建对应的协议
        BreakerProtocolType type = message.getHead().getType();

        // 断路器主动推送电压和电流值
        if(type == BreakerProtocolType.PushData) {
            return new DataAcceptProtocol(message);
        } else { // 移除平台主动请求的协议
            return remove(message.getHead().getMessageId());
        }
    }
    // 省略其他方法
}
interface MessageHead {
    /**
     * 报文的唯一编号
     * @return
     */
    String getMessageId();
}
```
### DTU使用教程
#### DTU编号注册
dtu设备连网时发送的第一包将作为可以标识此dtu的唯一编号(DTU设备编号)，基本上所有的dtu都是可以配置注册包和心跳包(默认使用字符串格式)
#### DTU心跳包
iot默认的心跳包和dtu编号(DTU设备编号)是一致的， 也就是说dtu配置的心跳包的内容和dtu编号一致的话iot将会作为心跳包(默认使用字符串格式)
#### DTU + modbus协议
对于使用modbus协议且通过RS282 RS485连接dtu的设备可以使用已经写好的服务端组件直接开发，以下是使用教程
1. 首先在自己的spring boot项目的pom.xml文件里面导入jar包

```
<dependency>
    <groupId>com.dream</groupId>
    <version>${iot.version}</version>
    <artifactId>iot-server</artifactId>
</dependency>
<dependency>
    <groupId>com.dream</groupId>
    <version>${iot.version}</version>
    <artifactId>iot-modbus</artifactId>
</dependency>
```
2. 然后需要将用到的modbus对应的组件注入到spring容器

```
// 如果设备使用modbus rtu协议则使用此组件
@Bean
public ModbusRtuForDtuServerComponent modbusRtuForDtuServerComponent() {
    // 监听7058端口
    return new ModbusRtuForDtuServerComponent(new ConnectProperties(7058));
}

// 如果设备使用modbus tcp协议则使用此组件
@Bean
public ModbusTcpForDtuServerComponent modbusTcpForDtuServerComponent() {
    // 监听7068端口
    return new ModbusTcpForDtuServerComponent(new ConnectProperties(7068));
}
```
3. 最后就是使用通用的modbus协议操作设备了， 我们以03读功能为例说明怎么读取设备的数据
注意：在操作设备前需要先确保dtu在线，并且dtu已经连接支持modbus协议的设备

```
// modbus rtu协议操作设备教程
ModbusRtuForDtuCommonProtocol read03Protocol = ModbusRtuForDtuCommonProtocol.buildRead03(...);
read03Protocol.request(protocol -> {
    // 首先判断协议的执行状态
    if(read03Protocol.getExecStatus() == ExecStatus.success) {
        ModbusRtuForDtuMessage responseMessage = read03Protocol.responseMessage();
        ModbusRtuBody body = responseMessage.getBody();
        if(body.isSuccess()) { // 设备执行指令成功
            Payload payload = read03Protocol.getPayload();
            int value = payload.readInt(0);
            // ...
        } else { // 设备执行指令失败
            ModbusErrCode code = body.getErrCode();
            // ...
        }
    }
    return null;
});

// modbus tcp协议操作设备教程
ModbusTcpForDtuCommonProtocol read03Protocol = ModbusTcpForDtuCommonProtocol.buildRead03(...);
read03Protocol.request(protocol -> {
    // 首先判断协议的执行状态
    if(read03Protocol.getExecStatus() == ExecStatus.success) {
        ModbusTcpForDtuMessage responseMessage = read03Protocol.responseMessage();
        ModbusTcpBody body = responseMessage.getBody();
        if(body.isSuccess()) { // 设备执行指令成功
            Payload payload = read03Protocol.getPayload();
            int value = payload.readInt(0);
            // ...
        } else { // 设备执行指令失败
            ModbusErrCode code = body.getErrCode();
            // ...
        }
    }
    return null;
});
```
#### DTU启用AT支持

#### DTU私有指令支持

#### 自定义DTU组件

### 更新日志
#### v2.5.3
1. 新增字段来区分dtu注册包和心跳包的报文格式 
2. 重构dtu报文的释放逻辑
#### v2.5.2
1. 修改modbus for dtu的心跳解析方法没有被委托问题
2. 新增modbus for dtu可以自定义dtu协议
3. 修改dtu本身协议过滤方式
4. 修改某些功能的日志打印级别
5. 需改其余多处小bug
#### v2.5.1
1. 修改modbus tcp for dtu协议为同步协议
2. 客户端编号注册过滤新增关闭连接功能
3. 完善modbus for dtu协议 过滤掉dtu私有的协议
#### v2.5.0
1. 修改设备上线新逻辑, 如果因为连接重复导致的下线不触发下线事件, 只触发最新的上线事件
2. 新增任务调度管理
3. 修改任务调度管理器没有注入配置的问题
4. 修改设备管理器注册设备编号逻辑为只注册编号一次从而提升性能
5. 修改其他bug
#### v2.4.9
1. 修改客户端没有注入任何组件时启动异常问题
2. 新增组件开启时间字段
3. 新增系统信息websocket埋点(信息包括：服务启用多少端口、每个端口有几个连接、注册有几个连接、协议类型)
4. 修改websocket过滤校验问题
#### v2.4.8
1. 新增组件过滤器, 支持连接过滤，黑白名单过滤, 设备编号注册过滤
2. 修改服务端同步调用接口
3. 新增websocket过滤器，支持请求认证
#### v2.4.7
1. 新增支持[ProtocolHandleProxy]异步执行 
2. 修改支持调整多个[ProtocolHandleProxy]执行顺序
3. 新增websocket支持
4. 调整mqtt默认实现
5. 重构udp协议并且新增服务端的udp协议默认实现
6. 调整客户端重连策略, 第一次15秒，然后每次都会增加时间 总共重连5次
#### v2.4.6
1. 调整协议处理器的代理方式(只支持接口代理)
2. 新增ProtocolHandleProxy类，用来声明是一个[ProtocolHandle]的代理接口
2. 新增ProtocolHandleProxy执行过滤
#### v2.4.3
1. 微调系统架构，接口更加统一易读
2. 新增mqtt网关handle， 方便将第三方采集的数据直接推送到mqtt服务器(支持json和自定义byte[])
3. modbus for dtu(tcp/rtu)支持自定义解析设备编号
4. 其他一些bug修改
#### v2.4.2
1. 新增客户端事件的发布和监听
2. 新增事件异步监听机制
3. 修改发布的事件中包含对应的组件
4. 新增读取欧姆龙plc的TIM区
5. 修改其他bug
#### v2.4.1
1. 完成和测试通过modbus tcp客户端协议实现(2021/9/11)
2. 完成和测试通过mqtt协议的客户端实现(2021/9/7)
3. 完成西门子plc的开发和模拟调试(2021/12/17)
4. 完成欧姆龙plc的开发和模拟调试(2021/12/28)
5. 重新构建modbus tcp client以及新增modbus tcp for dtu功能
6. 重新构建modbus以及新增modbus rtu for dtu功能并新增DTU AT协议和自定义协议支持



75039960@qq.com  ,18665802636



