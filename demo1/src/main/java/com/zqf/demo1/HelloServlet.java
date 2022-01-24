package com.zqf.demo1;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.http.Response;
import com.zqf.custom.tomcat.servlet.AbstractServlet;
import com.zqf.custom.tomcat.util.HttpProtocolUtil;

import java.io.IOException;

//@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends AbstractServlet {
    private String message = "demo1";

    public void init() {
    }

    public void destroy() {
    }

    @Override
    public void doGet(Request request, Response response) {

        try {
            response.output(HttpProtocolUtil.getHttpHeader(message.getBytes().length) + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        try {
            response.output(HttpProtocolUtil.getHttpHeader(message.getBytes().length) + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}