package com.dream.iot.websocket;

import com.dream.iot.message.DefaultMessageBody;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;

public class WebSocketCloseBody extends DefaultMessageBody {


    private int rsv;
    private String reasonText;
    private WebSocketCloseStatus status;

    public WebSocketCloseBody() {
        this(WebSocketCloseStatus.NORMAL_CLOSURE, "");
    }

    public WebSocketCloseBody(WebSocketCloseStatus status, String reasonText) {
        this.status = status;
        this.reasonText = reasonText;
    }

    public WebSocketCloseBody(WebSocketCloseStatus status, byte[] binaryData) {
        super(binaryData);
        this.status = status;
    }

    public WebSocketCloseBody(int rsv, byte[] binaryData) {
        super(binaryData);
        this.rsv = rsv;
    }

    /**
     * the content of the frame. Must be 2 byte integer followed by optional UTF-8 encoded string.
     */
    @Override
    public byte[] getMessage() {
        return super.getMessage();
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public int getRsv() {
        return rsv;
    }

    public void setRsv(int rsv) {
        this.rsv = rsv;
    }

    public WebSocketCloseStatus getStatus() {
        return status;
    }

    public void setStatus(WebSocketCloseStatus status) {
        this.status = status;
    }
}
