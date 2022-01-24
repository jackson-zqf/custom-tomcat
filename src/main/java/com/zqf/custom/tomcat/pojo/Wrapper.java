package com.zqf.custom.tomcat.pojo;

import com.zqf.custom.tomcat.servlet.AbstractServlet;

public class Wrapper {

    String url;

    AbstractServlet servlet;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AbstractServlet getServlet(String url) {
        if (url != null && url.equals(this.url)) {
            return this.servlet;
        }
        return null;
    }

    public void setServlet(AbstractServlet servlet) {
        this.servlet = servlet;
    }
}
