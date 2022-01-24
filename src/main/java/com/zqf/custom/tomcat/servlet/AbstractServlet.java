package com.zqf.custom.tomcat.servlet;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.http.Response;

public abstract  class AbstractServlet implements Servlet {


    @Override
    public void service(Request request, Response response) throws Exception {

        if("GET".equals(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }


    public abstract void  doGet(Request request, Response response);

    public abstract void  doPost(Request request, Response response);
}
