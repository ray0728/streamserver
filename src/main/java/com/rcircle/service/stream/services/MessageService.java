package com.rcircle.service.stream.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rcircle.service.stream.clients.RemoteMessageFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private RemoteMessageFeignClient remoteMessageFeignClient;

    @HystrixCommand(fallbackMethod = "buildFallbacksendHLSSplitFinished", threadPoolKey = "MessageThreadPool")
    public boolean sendHLSSplitFinished(int id, String filename, boolean ret) {
        remoteMessageFeignClient.sendHLSSplitFinished(id, filename, ret);
        return true;
    }

    public boolean buildFallbacksendHLSSplitFinished(int id, String filename, boolean ret, Throwable throwable) {
        return false;
    }

}
