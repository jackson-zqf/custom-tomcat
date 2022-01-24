package com.zqf.custom.tomcat.resources;

import com.zqf.custom.tomcat.pojo.*;
import com.zqf.custom.tomcat.servlet.AbstractServlet;
import com.zqf.custom.tomcat.thread.RequestProcessor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源加载器
 */
public class ResourcesLoader {

    Mapper mapper = new Mapper();

    Host host = new Host();

    Container container = new Container();

    List<Service> serviceList = new ArrayList<>();

    CustomClassLoader customClassLoader = new CustomClassLoader();

    /**
     * 加载默认web.xml文件、server.xml、应用的web.xml
     */
    public void loadDefaultResource() {
        //1.加载自身tomcat的web.xml文件
        loadDefaultWebXmlResource();
        //加载自身的server.xml 文件，从而加载webapps目录下项目种的web.xml文件
        loadServerXmlResource();

        mapper.setHost(host);
        host.setMapper(mapper);
    }

    private void loadServerXmlResource() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        try {
            Document document = new SAXReader().read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> elementList = rootElement.selectNodes("//Service");

            //遍历所有的Service标签
            for (Element element : elementList) {
                Element connectorElement = (Element) element.selectSingleNode("Connector");
                String port = connectorElement.attributeValue("port");
                Element engineElement = (Element) element.selectSingleNode("Engine");
                Element hostElement = (Element) engineElement.selectSingleNode("Host");
                String name = hostElement.attributeValue("name");
                String appBase = hostElement.attributeValue("appBase");
                host.setName(name);
                host.setWebapps(appBase);
                serviceList.add(new Service(new Connector(Integer.valueOf(port)),new Engine(host)));
                /**
                 * 去加载webapps目录下的应用。
                 */
                loadWebAppsResource(new File(appBase));
            }
            container.setServiceList(serviceList);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void loadWebAppsResource(File file) {
        if (!file.exists()) {
            return;
        }
        //获取所有webapps下的工程文件
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                loadWebAppsResourceLoop(file1.getName(),file1);
            }
        }
    }

    private void  loadWebAppsResourceLoop(String context,File file){
        //如果是文件夹
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                loadWebAppsResourceLoop(context,file1);
            }
            if ("web.xml".equals(file1.getName())) {
                loadWebXmlResource(context,file1);
            }
        }
    }

    private void loadWebXmlResource(String context,File file) {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        loadWebXmlResource("/"+context,resourceAsStream,file);
    }

    private void loadDefaultWebXmlResource() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        //tomcat自己的servlet默认请求路径为 /
        loadDefaultWebXmlResource("/",resourceAsStream);
    }

    private void loadDefaultWebXmlResource(String contextName,InputStream resourceAsStream) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(resourceAsStream);
            //获取根元素
            Element rootElement = document.getRootElement();
            //获取所有 servlet标签元素
            List<Element> elementList = rootElement.selectNodes("//servlet");
            for (Element element : elementList) {
                //根据每个servlet标签 获取其子标签属性值。
                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();
                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();
                //根据servlet-name属性值找到对应的 servlet-mapping标签
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name= '" + servletName + "']");
                Element urlPatternElement = (Element) servletMapping.selectSingleNode("url-pattern");
                String urlPattern = urlPatternElement.getStringValue();
                Wrapper wrapper = new Wrapper();
                wrapper.setUrl(urlPattern);
                wrapper.setServlet((AbstractServlet) Class.forName(servletClass).newInstance());

                Context context = new Context();
                context.setContext(contextName);
                context.setWrapper(wrapper);

                host.setContext(context);
            }

        } catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法是加载应用下的web.xml文件
     * 使用自定义类加载器去加载应用的servlet
     * @param contextName
     * @param resourceAsStream
     * @param file
     */
    private void loadWebXmlResource(String contextName,InputStream resourceAsStream,File file) {
        SAXReader reader = new SAXReader();
        try {

            Document document = reader.read(resourceAsStream);
            //获取根元素
            Element rootElement = document.getRootElement();
            //获取所有 servlet标签元素
            List<Element> elementList = rootElement.selectNodes("//servlet");

            for (Element element : elementList) {
                //根据每个servlet标签 获取其子标签属性值。
                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();

                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue(); //com.zqf.demo1.HelloServlet

                /**
                 * 将需要加载的servlet全限定名 和 绝对路径 放到需要map中。
                 */
                CustomClassLoader.classMap.put(servletClass,file.getParent().replaceAll("\\\\","/")+"/classes/"+servletClass.replaceAll("\\.","/")+".class");//D:\zqf\webapps\demo1\WEB-INF\web.xml
                //根据servlet-name属性值找到对应的 servlet-mapping标签
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name= '" + servletName + "']");
                Element urlPatternElement = (Element) servletMapping.selectSingleNode("url-pattern");
                String urlPattern = urlPatternElement.getStringValue();
                Wrapper wrapper = new Wrapper();
                wrapper.setUrl(urlPattern);
                /**
                 * 使用自定义的 类加载器去加载servlet
                 */
                wrapper.setServlet((AbstractServlet) Class.forName(servletClass,true,customClassLoader).newInstance());

                Context context = new Context();
                context.setContext(contextName);
                context.setWrapper(wrapper);

                host.setContext(context);
            }

        } catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }
}
