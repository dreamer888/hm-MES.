package com.dream.iot.plc;

import com.dream.iot.ProtocolException;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.SocketClient;
import com.dream.iot.client.protocol.ClientInitiativeSyncProtocol;
import com.dream.iot.plc.siemens.AddressType;
import com.dream.iot.utils.ByteUtil;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 注：非线程安全 同一个客户端 {@link PlcTcpClient}不能跨线程调用
 * @param <M>
 */
public abstract class PlcClientProtocol<M extends PlcClientMessage> extends ClientInitiativeSyncProtocol<M> implements PlcReadWrite {

    /**
     *  word的长度
     */
    private int wordLength;

    /**
     * 写地址信息
     */
    private WriteAddress writeAddress;

    /**
     * 批量读取的地址列表
     */
    private List<ReadAddress> batchAddress;

    /**
     * 使用默认的客户端
     */
    public PlcClientProtocol(int wordLength) {
        this.wordLength = wordLength;
        this.timeout(2000); // 默认超时时间2秒
    }

    /**
     * 使用指定配置的客户端
     * @param properties
     */
    public PlcClientProtocol(ClientConnectProperties properties, int wordLength) {
        this.setClientKey(properties);
        this.wordLength = wordLength;
        this.timeout(2000); // 默认超时时间2秒
    }

    protected WriteAddress getWriteAddress() {
        return writeAddress;
    }

    protected List<ReadAddress> getBatchAddress() {
        return batchAddress;
    }

    @Override
    public SocketClient getIotClient() {
        //public PlcTcpClient getIotClient() {
        SocketClient cient = super.getIotClient();

        if(  cient instanceof PlcTcpClient )
        return (PlcTcpClient) super.getIotClient();
        else if(  cient instanceof PlcUdpClient )
            return (PlcUdpClient) super.getIotClient();
        else return null;
    }


    //lgl
    public PlcUdpClient getUdpIotClient() {
        return (PlcUdpClient) super.getIotClient();
    }


    @Override
    public abstract PlcProtocolType protocolType();

    @Override
    protected void validateTimeout(long timeout) {
        if(timeout <= 0) {
            throw new ProtocolException("plc使用同步请求timeout必须>0(ms)");
        }

        this.setTimeout(timeout);
    }

    @Override
    public byte[] read(String address, short length) {
        if(address == null || length <= 0) {
            throw new PlcException("参数[address or length]错误");
        }

        this.batchAddress = Arrays.asList(ReadAddress.buildByteRead(address, length));

        // 必须使用同步方式读取
        this.sync(this.getTimeout()).request();

        return doRead(this.batchAddress).get(0);
    }

    protected abstract List<byte[]> doRead(List<ReadAddress> batchAddress);

    @Override
    public List<byte[]> batchRead(List<ReadAddress> batchAddress) {
        if(CollectionUtils.isEmpty(batchAddress)) {
            throw new PlcException("未指定要读取的地址信息[batchAddress]");
        }

        this.batchAddress = batchAddress;

        // 必须使用同步方式读取
        this.sync(this.getTimeout()).request();

        return this.doRead(this.batchAddress);
    }

    @Override
    public void write(String address, byte[] data) {
        if(address == null || data == null) {
            throw new PlcException("参数[address or data]错误");
        }

        this.writeAddress =  new WriteAddress(data, address);

        // 必须使用同步方式读取
        this.sync(this.getTimeout()).request();
    }

    @Override
    public Boolean readBool(String address) {
        if(address == null) {
            throw new PlcException("参数[address]错误");
        }

        this.batchAddress = Arrays.asList(ReadAddress.buildBitRead(address));

        // 必须使用同步方式读取
        this.sync(this.getTimeout()).request();

        byte[] bytes = doRead(this.batchAddress).get(0);
        return bytes[0] == 1;
    }

    @Override
    public Short readInt16(String address) {
        byte[] read = this.read(address, (short) getWordLength());
        if(read!=null)
        return getDataTransfer().toShort(read, 0);
        else return  null;
    }

    @Override
    public short[] readInt16(String address, short length) {
        byte[] read = this.read(address, (short) (getWordLength() * length));

        short[] shorts = new short[length];
        for(int i=0; i<length; i++) {
            shorts[i] = getDataTransfer().toShort(read, i * 2);
        }

        return shorts;
    }

    @Override
    public Integer readUInt16(String address) {
        byte[] read = this.read(address, (short) getWordLength());
        return getDataTransfer().toUShort(read, 0);
    }

    @Override
    public int[] readUInt16(String address, short length) {
        byte[] read = this.read(address, (short) (getWordLength() * length));

        int[] ints = new int[length];
        for(int i=0; i<length; i++) {
            ints[i] = getDataTransfer().toUShort(read, i * 2);
        }

        return ints;
    }

    @Override
    public Integer readInt32(String address) {
        byte[] read = this.read(address, (short) (2 * getWordLength()));
        return getDataTransfer().toInt(read, 0);
    }

    @Override
    public int[] readInt32(String address, short length) {
        byte[] read = this.read(address, (short) ((2 * getWordLength()) * length));

        int[] ints = new int[length];
        for(int i=0; i<length; i++) {
            ints[i] = getDataTransfer().toInt(read, i * 4);
        }

        return ints;
    }

    @Override
    public Long readUInt32(String address) {
        byte[] read = this.read(address, (short) (2 * getWordLength()));
        return getDataTransfer().toUInt(read, 0);
    }

    @Override
    public long[] readUInt32(String address, short length) {
        byte[] read = this.read(address, (short) ((2 * getWordLength()) * length));

        long[] ints = new long[length];
        for(int i=0; i<length; i++) {
            ints[i] = getDataTransfer().toUInt(read, i * 4);
        }

        return ints;
    }

    @Override
    public Long readInt64(String address) {
        byte[] read = this.read(address, (short) (4 * getWordLength()));
        return getDataTransfer().toLong(read, 0);
    }

    @Override
    public long[] readInt64(String address, short length) {
        byte[] read = this.read(address, (short) ((4 * getWordLength()) * length));

        long[] longs = new long[length];
        for(int i=0; i<length; i++) {
            longs[i] = getDataTransfer().toLong(read, i * 8);
        }

        return longs;
    }

    @Override
    public Float readFloat(String address) {
        byte[] read = this.read(address, (short) (2 * getWordLength()));
        return getDataTransfer().toFloat(read, 0);
    }

    @Override
    public float[] readFloat(String address, short length) {
        byte[] read = this.read(address, (short) ((2 * getWordLength()) * length));

        float[] floats = new float[length];
        for(int i=0; i<length; i++) {
            floats[i] = getDataTransfer().toFloat(read, i * 4);
        }

        return floats;
    }

    @Override
    public Double readDouble(String address) {
        byte[] read = this.read(address, (short) (4 * getWordLength()));
        return getDataTransfer().toDouble(read, 0);
    }

    @Override
    public double[] readDouble(String address, short length) {
        byte[] read = this.read(address, (short) ((4 * getWordLength()) * length));

        double[] doubles = new double[length];
        for(int i=0; i<length; i++) {
            doubles[i] = getDataTransfer().toDouble(read, i * 8);
        }

        return doubles;
    }

    @Override
    public String readString(String address, short length) {
        return this.readString(address, length, "UTF-8");
    }

    @Override
    public String readString(String address, short length, String encoding) {
        byte[] read = read(address, length);
        return new String(read, Charset.forName(encoding));
    }

    @Override
    public <R> R read(String address, short length, Function<byte[], R> function) {
        return function.apply(read(address, length));
    }

    @Override
    public void write(String address, boolean value) {
        byte[] bytes = value ? new byte[]{1} : new byte[] {0};
        this.writeAddress = new WriteAddress(bytes, address, AddressType.Bit);

        // 必须使用同步方式读取
        this.sync(this.getTimeout()).request();
    }

    @Override
    public void write(String address, boolean[] value) {
        byte[] bytes = ByteUtil.boolArrayToByte(value);
        this.write(address, bytes);
    }

    @Override
    public void write(String address, short value) {
        byte[] bytes = getDataTransfer().fromShort(value);
        this.write(address, bytes);
    }

    @Override
    public void write(String address, short[] values) {
        byte[] bytes = new byte[values.length * 2];
        for (int i=0; i< values.length; i++) {
            byte[] value = getDataTransfer().fromShort(values[i]);
            ByteUtil.addBytes(bytes, value, i * 2);
        }

        this.write(address, bytes);
    }

    @Override
    public void write(String address, int value) {
        byte[] bytes = getDataTransfer().fromInt(value);
        this.write(address, bytes);
    }

    @Override
    public void write(String address, int[] values) {
        byte[] bytes = new byte[values.length * 4];
        for (int i=0; i< values.length; i++) {
            byte[] value = getDataTransfer().fromInt(values[i]);
            ByteUtil.addBytes(bytes, value, i * 4);
        }

        this.write(address, bytes);
    }

    @Override
    public void write(String address, long value) {
        byte[] bytes = getDataTransfer().fromLong(value);
        this.write(address, bytes);
    }

    @Override
    public void write(String address, long[] values) {
        byte[] bytes = new byte[values.length * 8];
        for (int i=0; i< values.length; i++) {
            byte[] value = getDataTransfer().fromLong(values[i]);
            ByteUtil.addBytes(bytes, value, i * 8);
        }

        this.write(address, bytes);
    }

    @Override
    public void write(String address, float value) {
        byte[] bytes = getDataTransfer().fromFloat(value);
        this.write(address, bytes);
    }

    @Override
    public void write(String address, float[] values) {
        byte[] bytes = new byte[values.length * 4];
        for (int i=0; i< values.length; i++) {
            byte[] value = getDataTransfer().fromFloat(values[i]);
            ByteUtil.addBytes(bytes, value, i * 4);
        }

        this.write(address, bytes);
    }

    @Override
    public void write(String address, double value) {
        byte[] bytes = getDataTransfer().fromDouble(value);
        this.write(address, bytes);
    }

    @Override
    public void write(String address, double[] values) {
        byte[] bytes = new byte[values.length * 8];
        for (int i=0; i< values.length; i++) {
            byte[] value = getDataTransfer().fromDouble(values[i]);
            ByteUtil.addBytes(bytes, value, i * 8);
        }

        this.write(address, bytes);
    }

    @Override
    public void write(String address, String value) {
        byte[] bytes = ByteUtil.getBytes(value, "UTF-8");
        this.write(address, bytes);
    }

    @Override
    public void write(String address, String value, String encoding) {
        byte[] bytes = ByteUtil.getBytes(value, encoding);
        this.write(address, bytes);
    }

    public int getWordLength() {
        return wordLength;
    }
}
