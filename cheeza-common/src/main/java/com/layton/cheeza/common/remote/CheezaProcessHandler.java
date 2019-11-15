package com.layton.cheeza.common.remote;

import com.layton.cheeza.common.remote.server.CheezaRpcRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
@Slf4j
public class CheezaProcessHandler extends SimpleChannelInboundHandler<CheezaRpcRequest> {

    private Map<String, Object> serviceMap;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    public CheezaProcessHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CheezaRpcRequest cheezaRpcRequest) throws Exception {
        executor.execute(new RequestWorker(cheezaRpcRequest, serviceMap));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("RpcProcessHandler exception,", cause);
        ctx.close();
    }
}
