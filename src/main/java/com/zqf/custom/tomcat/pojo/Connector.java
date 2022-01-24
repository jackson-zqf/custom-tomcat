package com.zqf.custom.tomcat.pojo;

public class Connector {

    int  port;

    public Connector(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
