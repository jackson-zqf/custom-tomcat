package com.zqf.custom.tomcat.servlet;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.http.Response;
import com.zqf.custom.tomcat.util.HttpProtocolUtil;

import java.io.IOException;

public class CustomServlet extends AbstractServlet {

    @Override
    public void doGet(Request request, Response response) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "<h1>hello,customServlet get </h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader(result.getBytes().length)+result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String result = "<h1>hello,customServlet post </h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader(result.getBytes().length)+result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }
}
