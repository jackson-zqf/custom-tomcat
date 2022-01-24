package com.zqf.custom.tomcat.pojo;

import com.zqf.custom.tomcat.http.Request;
import com.zqf.custom.tomcat.servlet.AbstractServlet;
import com.zqf.custom.tomcat.thread.RequestProcessor;
import com.zqf.custom.tomcat.thread.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Host {

    String name;

    String webapps;

    List<Context> contextList = new ArrayList<>();

    Mapper mapper;

    /**
     * 启动服务
     * @param connector
     * @throws IOException
     */
    public void start(Connector connector) throws IOException {
        ServerSocket serverSocket = new ServerSocket(connector.getPort());
        System.out.println("启动消息监听，端口号：" + connector.getPort());

        for (; ; ) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(mapper,socket);
            ThreadPool threadPool = new ThreadPool();
            threadPool.getThreadPoolExecutor().execute(requestProcessor);
        }
    }

    /**
     * 获取请求的servlet
     * @param request
     * @return
     */
    public AbstractServlet getServlet(Request request) {
        //首先根据域名进行筛选
        if (name.equals(request.getHostName())) {
            //然后遍历所有的context，找到对应的应用。
            for (Context context : contextList) {
                //如果是 / 则默认请求tomcat的servlet
                if ("/".equals(request.getUrl())) {
                    return context.getWrapper().getServlet("/");
                }
                //否则去筛选应用的servlet
                else if (request.getUrl().startsWith(context.getContext()) && !"/".equals(context.getContext()) ) {
                    return context.getWrapper().getServlet(request.getUrl().substring(context.getContext().length()));
                }
            }
        }
        return null;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public void setContext(Context context) {
        contextList.add(context);
    }

    public List<Context> getContextList() {
        return contextList;
    }

    public void setContextList(List<Context> contextList) {
        this.contextList = contextList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebapps() {
        return webapps;
    }

    public void setWebapps(String webapps) {
        this.webapps = webapps;
    }
}
