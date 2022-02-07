自定义tomcat

应用程序demo1、demo2已经以子工程存在于工程中。

tomat默认请求
http://localhost:8080

访问不同的应用请求：
http://localhost:8080/demo1/hello
http://localhost:8080/demo2/hello

磁盘中的webapps路径如下

![image](https://user-images.githubusercontent.com/57132752/152735080-a556775b-b25d-49ae-8765-669b76b6d8d8.png)


实现思路如下：
 4.0
 
 实现磁盘某路径下webapps目录下的多应用部署，可以根据不同请求路径来映射到不同的应用上。
 
 实现思路：
 
      1.加载自定义tomcat自己的web.xml文件到内存中。
      
      2.加载自定义tomcat自定的server.xml文件到内存中
      
      3.根据server.xml中配置的 appBase 属性拿到webapps路径，根据路径去加载路径下所有应用的web.xml文件，
      将应用下的servlet加载到内存中。
      
      4.根据请求的url来一步步定位是哪个应用的哪个servlet，从而处理逻辑返回。
