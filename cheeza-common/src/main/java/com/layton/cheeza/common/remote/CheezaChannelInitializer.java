package com.layton.cheeza.common.remote;

import com.layton.cheeza.common.remote.client.CheezaRpcResponse;
import com.layton.cheeza.common.remote.codec.CheezaDecoder;
import com.layton.cheeza.common.remote.codec.CheezaEncoder;
import com.layton.cheeza.common.remote.server.CheezaRpcRequest;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Map;


public class CheezaChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Map<String, Object> serviceMap;

    public CheezaChannelInitializer(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                .addLast(new CheezaDecoder(CheezaRpcRequest.class))
                .addLast(new CheezaEncoder(CheezaRpcResponse.class))
                .addLast(new CheezaProcessHandler(serviceMap));

    }
}
