package com.dream.iot.plc;

/**
 * 错误描述
 */
public class PlcErrorConst {

    // Melsec PLC
    public String MelsecPleaseReferToManualDocument (){ return "请查看三菱的通讯手册来查看报警的具体信息"; }
    public String MelsecReadBitInfo (){ return "读取位变量数组只能针对位软元件，如果读取字软元件，请调用Read方法"; }
    public String MelsecCurrentTypeNotSupportedWordOperate (){ return "当前的类型不支持字读写"; }
    public String MelsecCurrentTypeNotSupportedBitOperate (){ return "当前的类型不支持位读写"; }
    public String MelsecFxReceiveZero (){ return "接收的数据长度为0"; }
    public String MelsecFxAckNagative (){ return "PLC反馈的数据无效"; }
    public String MelsecFxAckWrong (){ return "PLC反馈信号错误："; }
    public String MelsecFxCrcCheckFailed (){ return "PLC反馈报文的和校验失败！"; }

    public String MelsecError02      (){ return "“读/写”(入/出)软元件的指定范围不正确。";}
    public String MelsecError51      (){ return "在使用随机访问缓冲存储器的通讯时，由外部设备指定的起始地址设置在 0-6143 的范围之外。解决方法:检查及纠正指定的起始地址。";}
    public String MelsecError52      (){ return "1. 在使用随机访问缓冲存储器的通讯时，由外部设备指定的起始地址+数据字数的计数(读时取决于设置)超出了 0-6143 的范围。\r\n2. 指定字数计数(文本)的数据不能用一个帧发送。(数据长度数值和通讯的文本总数不在允许的范围之内。)";}
    public String MelsecError54      (){ return "当通过 GX Developer 在[操作设置]-[通讯数据代码]中选择“ASCII码通讯”时，则接收来自外部设备的、不能转换为二进制代码的ASCII 码。";}
    public String MelsecError55      (){ return "当不能通过 GX Developer(无检查标记)来设置[操作设置]-[无法在运行时间内写入]时，如 PLCCPU 处于运行状态，则外部设备请求写入数据。 ";}
    public String MelsecError56      (){ return "从外部进行的软元件指定不正确。";}
    public String MelsecError58      (){ return "1. 由外部设备指定的命令起始地址(起始软元件号和起始步号)可设置在指定范围外。\r\n2. 为扩展文件寄存器指定的块号不存在。\r\n3. 不能指定文件寄存器(R)。\r\n4. 为位软元件的命令指定字软元件。\r\n5. 位软元件的起始号由某一个数值指定，此数值不是字软元件命令中16 的倍数。";}
    public String MelsecError59      (){ return "不能指定扩展文件的寄存器。";}
    public String MelsecErrorC04D    (){ return "在以太网模块通过自动开放 UDP端口通讯或无序固定缓冲存储器通讯接收的信息中，应用领域中指定的数据长度不正确。";}
    public String MelsecErrorC050    (){ return "当在以太网模块中进行 ASCII 代码通讯的操作设置时，接收不能转化为二进制代码的 ASCII 代码数据。";}
    public String MelsecErrorC051_54 (){ return "读/写点的数目在允许范围之外。";}
    public String MelsecErrorC055    (){ return "文件数据读/写点的数目在允许范围之外。";}
    public String MelsecErrorC056    (){ return "读/写请求超过了最大地址。";}
    public String MelsecErrorC057    (){ return "请求数据的长度与字符区域(部分文本)的数据计数不匹配。";}
    public String MelsecErrorC058    (){ return "在经过 ASCII 二进制转换后，请求数据的长度与字符区域( 部分文本)的数据计数不相符。";}
    public String MelsecErrorC059    (){ return "命令和子命令的指定不正确。";}
    public String MelsecErrorC05A_B  (){ return "以太网模块不能对指定软元件进行读出和写入";}
    public String MelsecErrorC05C    (){ return "请求内容不正确。 ( 以位为单元请求读 / 写至字软元件。)";}
    public String MelsecErrorC05D    (){ return "不执行监视注册。";}
    public String MelsecErrorC05E    (){ return "以太网模块和 PLC CPU 之间的通讯时问超过了 CPU 监视定时器的时间。";}
    public String MelsecErrorC05F    (){ return "目标 PLC 上不能执行请求。";}
    public String MelsecErrorC060    (){ return "请求内容不正确。 ( 对位软元件等指定了不正确的数据。) ";}
    public String MelsecErrorC061    (){ return "请求数据的长度与字符区域(部分文本)中的数据数目不相符。 ";}
    public String MelsecErrorC062    (){ return "禁止在线更正时，通过 MC 协议远程 I/O 站执行( QnA兼容 3E 帧或4E 帧)写入操作。";}
    public String MelsecErrorC070    (){ return "不能为目标站指定软元件存储器的范围";}
    public String MelsecErrorC072    (){ return "请求内容不正确。 ( 以位为单元请求调写至字软元件。) ";}
    public String MelsecErrorC074    (){ return "目标 PLC 不执行请求。需要纠正网络号和 PC 号。";}

    // 西门子plc
    public String SiemensDBAddressNotAllowedLargerThan255 (){ return "DB块数据无法大于255"; }
    public String SiemensReadLengthMustBeEvenNumber (){ return "读取的数据长度必须为偶数"; }
    public String SiemensWriteError (){ return "写入数据异常，代号为："; }
    public String SiemensReadLengthCannotLargerThan19 (){ return "读取的数组数量不允许大于19"; }
    public String SiemensDataLengthCheckFailed (){ return "数据块长度校验失败，请检查是否开启put/get以及关闭db块优化"; }

    // Omron PLC
    public String OmronAddressMustBeZeroToFifteen(){ return "输入的位地址只能在0-15之间"; }
    public String OmronReceiveDataError (){ return "数据接收异常"; }
    public String OmronStatus0 (){ return "通讯正常"; }
    public String OmronStatus1 (){ return "消息头不是FINS"; }
    public String OmronStatus2 (){ return "数据长度太长"; }
    public String OmronStatus3 (){ return "该命令不支持"; }
    public String OmronStatus20 (){ return "超过连接上限"; }
    public String OmronStatus21 (){ return "指定的节点已经处于连接中"; }
    public String OmronStatus22 (){ return "尝试去连接一个受保护的网络节点，该节点还未配置到PLC中"; }
    public String OmronStatus23 (){ return "当前客户端的网络节点超过正常范围"; }
    public String OmronStatus24 (){ return "当前客户端的网络节点已经被使用"; }
    public String OmronStatus25 (){ return "所有的网络节点已经被使用"; }

    public String UnknownError() {
        return "未知错误";
    }
}
