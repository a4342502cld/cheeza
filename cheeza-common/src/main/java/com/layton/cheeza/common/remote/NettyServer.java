package com.layton.cheeza.common.remote;

import com.layton.cheeza.common.config.ProviderConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyServer {

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private String host;

    private int port;

    public volatile Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    public NettyServer (String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        startServer();
    }

    private void startServer() throws Exception {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new CheezaChannelInitializer(serviceMap))
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //启动服务
        ChannelFuture future = bootstrap.bind(host, port).sync();
        ChannelFuture channelFuture = future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (future.isSuccess()) {
                    log.info("Server have success bind to {}, port: {}", host, port);
                } else {
                    log.error("Server fail bind to {}, port: {}", host, port);
                    throw new Exception("Server start fail !", future.cause());
                }
            }
        });


        try {
            channelFuture.await(5000, TimeUnit.MILLISECONDS);
            if (channelFuture.isSuccess()) {
                log.info("start easy rpc server success.");
            }

        } catch (InterruptedException e) {
            log.error("start easy rpc occur InterruptedException!", e);
        }
    }

    /**
     * 注册服务引用
     * @param providerConfig
     */
    public void registerService(ProviderConfig providerConfig) {
        serviceMap.put(providerConfig.getInterface(), providerConfig.getRef());
    }
}
