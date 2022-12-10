package com.dream.iot;

/**
 * Create Date By 2020-09-11
 * @author dream
 * @since 1.7
 */
public interface Message {

    byte[] EMPTY = new byte[]{};

    /**
     * 返回报文长度
     * @return
     */
    int length();

    /**
     * 返回报文字节数
     * @return
     */
    byte[] getMessage();

    /**
     * 返回报文头
     * @return
     */
    MessageHead getHead();

    /**
     * 返回报文体
     * @return
     */
    MessageBody getBody();

    /**
     * 报文头
     */
    interface MessageHead {

        /**
         * 设备编号
         * @return
         */
        String getEquipCode();

        /**
         * 设置设备编号
         * @param equipCode
         */
        void setEquipCode(String equipCode);

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

    /**
     * 报文体
     */
    interface MessageBody {

        byte[] getMessage();

        default int getLength() {
            return getMessage().length;
        }
    }
}
