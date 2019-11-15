package com.layton.cheeza.common.config;

import com.layton.cheeza.common.remote.NettyServer;

public class ServerConfig {

    private String host = "127.0.0.1";

    private int port;

    /**
     * 服务端对象
     */
    private volatile transient NettyServer server = null;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 获取服务器
     * @return
     */
    public NettyServer getServer() {
        return server;
    }

    protected void start() throws Exception {
        if (server == null) {
            server = new NettyServer(host, port);
        }
    }

}
