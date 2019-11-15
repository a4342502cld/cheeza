package com.layton.cheeza.demo.provider.service;

import com.layton.cheeza.demo.api.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public void say() {
        System.out.println("===Hello===");
    }
}
