package com.zqf.custom.tomcat.pojo;

import java.io.IOException;

public class Service {
    Connector  connector;

    Engine engine;

    public Service(Connector connector) {
        this.connector = connector;
    }

    public Service() {
    }

    public Service(Connector connector, Engine engine) {
        this.connector = connector;
        this.engine = engine;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void  start() throws IOException {
        engine.start(connector);
    }
}
