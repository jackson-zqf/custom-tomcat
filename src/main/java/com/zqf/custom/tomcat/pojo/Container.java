package com.zqf.custom.tomcat.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Container {

    List<Service>  serviceList = new ArrayList<>();

    public void  start() throws IOException {

        for (Service service : serviceList) {
            service.start();
        }
    }


    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }
}
