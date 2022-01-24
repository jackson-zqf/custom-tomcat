package com.zqf.custom.tomcat.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticResourceUtil {


    /**
     * 获取资源的绝对路径
     *
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();//classpath的绝对路径。classes绝对路径
        return absolutePath.replaceAll("\\\\", "/") + path;
    }


    /**
     * 从输入流中读取数据，然后输出流输出
     *
     * @param inputStream
     * @param outputStream
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int available = 0;
        while (available == 0) {
            available = inputStream.available();
        }

        //1. 先将响应头信息写入输出流
        outputStream.write(HttpProtocolUtil.getHttpHeader(available).getBytes());

        //2.读取输入流数据
        long written = 0; //已经读取了的长度
        int  readSize = 1024; //每次读取的长度
        byte[]  readBytes = new byte[readSize];
        while (written < available){  //如果还没读完，则继续读
            if(written + readSize > available){ //如果最后一次读取不足一个读取单位1024，则使用真实的长度来读。
                readSize = (int) (available - written);
                readBytes= new byte[readSize];
            }
            inputStream.read(readBytes);
            //3.写入输出流
            outputStream.write(readBytes);
            outputStream.flush();

            written += readSize;
        }
    }

}
