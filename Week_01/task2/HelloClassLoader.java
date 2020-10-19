package sky.week01;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

    private final String path;

    public HelloClassLoader(String path) {
        this.path = path;
    }

    public static void main(String[] args) {
        try {
            Class<?> hello = new HelloClassLoader("F:\\2020JAVA进阶训练营第0期-P7\\第1课 JVM核心技术-基础知识\\").findClass("Hello");
            Object object = hello.newInstance();
            Method method = hello.getMethod("hello");
            method.invoke(object);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String classFileName) throws ClassNotFoundException {
        byte[] bytes = readFromFile(path + classFileName + ".xlass");
        return defineClass(classFileName, bytes, 0, bytes.length);
    }

    private byte[] readFromFile(String fileFullPath) {
        try {
            System.out.println(fileFullPath);
            FileInputStream in = new FileInputStream(fileFullPath);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int value;
            while (-1 != (value = in.read())) {
                output.write(255 - value);
            }
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
