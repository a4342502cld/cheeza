package com.layton.cheeza.common.config;

import com.layton.cheeza.common.remote.NettyServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProviderConfig {

    /**
     * 服务接口名
     */
    private String interfaceName;

    /**
     * 服务引用
     */
    protected transient Object ref;

    /**
     * 是否已发布服务
     */
    protected volatile boolean exported = false;

    /**
     * 服务配置信息
     */
    private ServerConfig serverConfig;

    public String getInterface() {
        return interfaceName;
    }

    public void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    //导出服务
    protected void export() {
        if (!exported) {
            try {
                serverConfig.start();
                //获取服务器，注册服务
                NettyServer server = serverConfig.getServer();
                server.registerService(this);
            } catch (Exception e) {
                log.info("ProviderConfig Exception, ", e);
            }
            exported = true;
        }
    }
}
