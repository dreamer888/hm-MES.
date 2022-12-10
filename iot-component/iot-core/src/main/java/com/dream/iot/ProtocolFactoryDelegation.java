package com.dream.iot;

public class ProtocolFactoryDelegation<M extends SocketMessage> extends ProtocolFactory<M> {

    private IotProtocolFactory iotProtocolFactory;

    public ProtocolFactoryDelegation(IotProtocolFactory iotProtocolFactory,ProtocolTimeoutStorage delegation) {
        super(delegation);
        this.iotProtocolFactory = iotProtocolFactory;
    }

    @Override
    public AbstractProtocol getProtocol(M message) {
        return iotProtocolFactory.getProtocol(message);
    }
}
