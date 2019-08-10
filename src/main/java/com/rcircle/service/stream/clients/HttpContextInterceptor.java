package com.rcircle.service.stream.clients;

import com.rcircle.service.stream.utils.HttpContext;
import com.rcircle.service.stream.utils.HttpContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class HttpContextInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpContext.AUTH_TOKEN, HttpContextHolder.getContext().getValue(HttpContext.AUTH_TOKEN));
    }
}
