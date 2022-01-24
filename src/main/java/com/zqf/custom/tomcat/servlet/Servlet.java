package com.zqf.custom.tomcat.servlet;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.http.Response;

public interface Servlet {

    void init() throws  Exception;

    void destroy() throws  Exception;

    void service(Request request, Response response) throws  Exception;

}
