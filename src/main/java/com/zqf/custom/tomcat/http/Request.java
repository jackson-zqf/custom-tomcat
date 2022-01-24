package com.zqf.custom.tomcat.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * 封装浏览器请求参数
 */
public class Request {

    private  String method; //请求方法
    private  String url; //请求路径
    private  String hostName;//请求域名


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    private InputStream inputStream;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        //读取输入流数据
        int available = 0;
        //如果没读到数据，则循环读取。
        while (available ==0){
            available = inputStream.available();
        }
        byte[] data = new byte[available];
        inputStream.read(data);
        System.out.println("-----> 请求信息：" + new String(data));
        System.out.println("-----> 读取长度：" + available);

        //读取到的请求信息字符串
        String inputStr = new String(data);

        //读取第一行数据
        String fistLineStr = inputStr.split("\\n")[0]; //GET /con?from=self?_t=1641806368429 HTTP/1.1

        String secondLineStr = inputStr.split("\\n")[1];  //Host: localhost:8080

        //拆分第一行数据根据空格分隔
        String[] firstLine = fistLineStr.split(" ");
        this.method = firstLine[0];
        this.url = firstLine[1];  //  /demo/query

        //拆分第二行数据
        String[]  secondLine = secondLineStr.split(":");
        this.hostName = secondLine[1].trim();
    }

    public Request() {
    }
}
