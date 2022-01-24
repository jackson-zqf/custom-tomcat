package com.zqf.custom.tomcat.util;

public class HttpProtocolUtil {

    public static  String  getHttpHeader(int contentLength){
        return  "HTTP/1.1 200 OK \n"+
                "Content-Type: text/html \n"+
                "Content-Length: " +contentLength + "\n" +
                "\r\n";
    }


    /**
     * 404 返回头信息
     *  \n 后面不要有空格，不然浏览器不识别
     * @return
     */
    public static  String  getHttpHeader404(){
        String response = "<h1>404 not found</h1>";
        return  "HTTP/1.1 404 NOT Found \n"+
                "Content-Type: text/html \n"+
                "Content-Length: " +response.getBytes().length + " \n" +
                "\r\n"  + response;
    }
}
