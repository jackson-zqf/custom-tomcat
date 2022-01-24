package com.zqf.custom.tomcat.thread;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.http.Response;
import com.zqf.custom.tomcat.pojo.Mapper;
import com.zqf.custom.tomcat.servlet.AbstractServlet;
import jdk.nashorn.internal.runtime.Scope;

import java.net.Socket;
import java.util.Map;

public class RequestProcessor implements Runnable {

    private Socket socket;

    private Map<String, AbstractServlet> servletMap;

    private Mapper  mapper;

    public RequestProcessor(Socket socket, Map<String, AbstractServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }
    public RequestProcessor(Mapper mapper,Socket socket) {
        this.socket = socket;
        this.mapper = mapper;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
/*@Override
    public void run() {
        try {
            Request request = new Request(socket.getInputStream());
            AbstractServlet abstractServlet = servletMap.get(request.getUrl());
            Response response = new Response(socket.getOutputStream());
            //动态资源处理
            if (abstractServlet != null) {
                abstractServlet.service(request, response);
            } else {
                //静态资源处理
                response.outputResource(request.getUrl());
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    @Override
    public void run() {
        try {
            Request request = new Request(socket.getInputStream());
            AbstractServlet abstractServlet = mapper.getServlet(request);
            Response response = new Response(socket.getOutputStream());
            //动态资源处理
            if (abstractServlet != null) {
                abstractServlet.service(request, response);
            } else {
                //静态资源处理
                response.outputResource(request.getUrl());
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
