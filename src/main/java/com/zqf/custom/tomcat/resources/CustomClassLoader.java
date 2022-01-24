package com.zqf.custom.tomcat.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义类加载器，加载webapps下的class文件
 */
public class CustomClassLoader extends ClassLoader{

    public static Map<String, String> classMap = new HashMap<>();

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        File file = new File(classMap.get(name));

        try {
            byte[] classBytes = getClassBytes1(file);
            Class<?> c = this.defineClass(name, classBytes, 0, classBytes.length);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }


    private byte[] getClassBytes1(File file) throws Exception {
        // 这里要读入.class的字节，因此要使用字节流
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel wbc = Channels.newChannel(baos);
        ByteBuffer by = ByteBuffer.allocate(1024);

        while (true) {
            int i = fc.read(by);
            if (i == 0 || i == -1) {
                break;
            }
            by.flip();
            wbc.write(by);
            by.clear();
        }

        fis.close();

        return baos.toByteArray();

    }


    private byte[] getClassBytes(File file) throws Exception {
        FileInputStream fileInputStream =new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(2048);

        int read = -1;
        while ((read = channel.read(buffer))!= -1){

        }

        return buffer.array();
    }

    public static void main(String[] args) throws Exception{
        /*String path = "com.zqf.demo1.HelloServlet";
        CustomClassLoader.classMap.put(path, "D:/zqf/webapps/demo1/WEB-INF/classes/com/zqf/demo1/HelloServlet.class");
                                              D:/zqf/webapps/demo1/WEB-INF/classes/com/zqf/demo1/HelloServlet
        CustomClassLoader customClassLoader = new CustomClassLoader();
        Class<?> aClass = Class.forName(path, true, customClassLoader);
        System.out.println(aClass.newInstance());*/
        String  s = "com.zqf.demo1.HelloServlet";
        System.out.println(s.replaceAll("\\.","/"));
    }
}
