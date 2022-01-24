package com.zqf.custom.tomcat;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.http.Response;
import com.zqf.custom.tomcat.pojo.Container;
import com.zqf.custom.tomcat.resources.ResourcesLoader;
import com.zqf.custom.tomcat.servlet.AbstractServlet;
import com.zqf.custom.tomcat.thread.RequestProcessor;
import com.zqf.custom.tomcat.thread.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简易版 tomcat
 * 思路：
 * 1.http服务器的服务端，接收浏览器的请求。
 * 2.将请求封装成Request、Response对象，返回静态资源（html）或者动态资源（servlet）。
 */
public class BootStrap {

    final static int port = 8080;

    public static void main(String[] args) throws Exception {
        BootStrap.start();
    }


    /**
     * 1.0
     * 启动socket监听
     * 需求： 浏览器访问localhost:8080，服务端返回字符串。
     */
    /*static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        for(;;){
            Socket socket = serverSocket.accept();

            OutputStream outputStream = socket.getOutputStream();
            String data = "hello,customTomcat! ";
            String result = HttpProtocolUtil.getHttpHeader(data.length()) + data;
            outputStream.write(result.getBytes());
        }
    }*/


    /**
     * 2.0
     * 启动socket监听
     * 完成Request和Response的封装,返回静态html资源。
     *
     * @throws IOException
     */
   /* static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        for (; ; ) {
            Socket socket = serverSocket.accept();

            Request request = new Request(socket.getInputStream());
            Response response = new Response(socket.getOutputStream());

            response.outputResource(request.getUrl());
            socket.close();
        }
    }*/


    /**
     * 3.0
     * 启动socket监听
     * 完成动态资源的加载和处理，即servlet的请求处理
     *
     * @throws IOException
     */
    /*static void start() throws Exception {
        //1.首先要先去加载xml配置文件，将servlet的映射关系缓存到内存中。
        ResourcesLoader resourcesLoader = new ResourcesLoader();
        resourcesLoader.loadDefaultResource();
        //2.启动socket监听
        ServerSocket serverSocket = new ServerSocket(port);
        for (; ; ) {
            Socket socket = serverSocket.accept();
            Request request = new Request(socket.getInputStream());
            AbstractServlet abstractServlet = resourcesLoader.getServletMap().get(request.getUrl());
            Response response = new Response(socket.getOutputStream());
            //动态资源处理
            if(abstractServlet != null){
                abstractServlet.service(request, response);
            }else{
                //静态资源处理
                response.outputResource(request.getUrl());
            }
            socket.close();
        }
    }*/

    /**
     * 多线程改造,这里是简单的创建一个线程去处理，如果高并发就会造成资源浪费，所以最好还是用线程池。
     * 当请求进来的时候，新建一个线程去处理请求
     * @throws Exception
     */
   /* static void start() throws Exception {
        //1.首先要先去加载xml配置文件，将servlet的映射关系缓存到内存中。
        ResourcesLoader resourcesLoader = new ResourcesLoader();
        resourcesLoader.loadDefaultResource();
        //2.启动socket监听
        ServerSocket serverSocket = new ServerSocket(port);
        for (; ; ) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new  RequestProcessor(socket,resourcesLoader.getServletMap());
            requestProcessor.start();
        }
    }*/


    /**
     * 多线程优化，使用线程池来管理线程。
     * @throws Exception
     */
    /*static void start() throws Exception {
        //1.首先要先去加载xml配置文件，将servlet的映射关系缓存到内存中。
        ResourcesLoader resourcesLoader = new ResourcesLoader();
        resourcesLoader.loadDefaultResource();
        //2.启动socket监听
        ServerSocket serverSocket = new ServerSocket(port);
        for (; ; ) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new  RequestProcessor(socket,resourcesLoader.getServletMap());
            ThreadPool threadPool = new ThreadPool();
            threadPool.getThreadPoolExecutor().execute(requestProcessor);
        }
    }*/

    /**
     * 4.0
     * 实现磁盘某路径下webapps目录下的多应用部署，可以根据不同请求路径来映射到不同的应用上。
     * 实现思路：
     *      1.加载自定义tomcat自己的web.xml文件到内存中。
     *      2.加载自定义tomcat自定的server.xml文件到内存中
     *      3.根据server.xml中配置的 appBase 属性拿到webapps路径，根据路径去加载路径下所有应用的web.xml文件，
     *        将应用下的servlet加载到内存中。
     *      4.根据请求的url来一步步定位是哪个应用的哪个servlet，从而处理逻辑返回。
     * @throws Exception
     */
    static void start() throws Exception {
        //1.首先要先去加载xml配置文件，将servlet的映射关系缓存到内存中。
        ResourcesLoader resourcesLoader = new ResourcesLoader();
        resourcesLoader.loadDefaultResource();
        //2.启动socket监听
        Container container = resourcesLoader.getContainer();
        container.start();

    }

}
