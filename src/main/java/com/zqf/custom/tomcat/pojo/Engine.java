package com.zqf.custom.tomcat.pojo;

import com.zqf.custom.tomcat.thread.RequestProcessor;
import com.zqf.custom.tomcat.thread.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Engine {

    Host host;

    public Engine(Host host) {
        this.host = host;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void  start(Connector connector) throws IOException {
        host.start(connector);
    }
}
