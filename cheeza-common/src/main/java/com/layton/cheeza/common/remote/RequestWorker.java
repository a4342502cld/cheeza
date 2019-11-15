package com.layton.cheeza.common.remote;

import com.layton.cheeza.common.remote.client.CheezaRpcResponse;
import com.layton.cheeza.common.remote.server.CheezaRpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class RequestWorker implements Runnable {

    private CheezaRpcRequest request;
    private Map<String, Object> serviceMap;

    public RequestWorker(CheezaRpcRequest request, Map<String, Object> serviceMap) {
        this.request = request;
        this.serviceMap = serviceMap;
    }

    @Override
    public void run() {
        log.info("Receive Request " + request.getRequestId());
        CheezaRpcResponse response = new CheezaRpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handleRequest(request);
            response.setResult(result);
        } catch (Exception e) {
            response.setError(e.getMessage());
            log.error("rpc Server handle request error", e);
        }
    }

    private Object handleRequest(CheezaRpcRequest request) throws Exception {
        String className = request.getClassName();
        Object serviceBean = serviceMap.get(className);

        String methodName = request.getMethodName();
        Class<?> serviceClass = serviceBean.getClass();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        //JDK反射
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }
}
