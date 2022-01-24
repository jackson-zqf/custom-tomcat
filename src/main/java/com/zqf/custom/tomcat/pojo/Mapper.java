package com.zqf.custom.tomcat.pojo;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.servlet.AbstractServlet;

public class Mapper {

    Host  host;

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public AbstractServlet getServlet(Request request){
      return   host.getServlet(request);
    }
}
