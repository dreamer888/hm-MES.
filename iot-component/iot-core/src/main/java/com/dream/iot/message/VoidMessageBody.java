package com.dream.iot.message;

/**
 * <p>空的报文体</p>
 * Create Date By 2017-09-12
 * @author dream
 * @since 1.7
 */
public class VoidMessageBody extends DefaultMessageBody {

    private static VoidMessageBody instance = new VoidMessageBody();

    protected VoidMessageBody() { }

    public static VoidMessageBody getInstance() {
        return instance;
    }
}
