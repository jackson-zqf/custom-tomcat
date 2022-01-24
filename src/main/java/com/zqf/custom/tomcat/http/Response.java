package com.zqf.custom.tomcat.http;


import com.zqf.custom.tomcat.util.HttpProtocolUtil;
import com.zqf.custom.tomcat.util.StaticResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 封装response对象，将静态资源读取并返回。
 */
public class Response {

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public void  output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }


    /**
     *  输出静态资源
     * @param path  url，通过url获取资源的绝对路径，然后拿到绝对路径下的资源，读取资源进行返回。
     */
    public  void  outputResource(String path) throws IOException {
        //获取资源的绝对路径
        String absolutePath = StaticResourceUtil.getAbsolutePath(path);
        File  file = new File(absolutePath);
        if(file.exists() && file.isFile()){
            //读取静态资源，输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        }else{
            //404
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }

}
