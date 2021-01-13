package com.example.demo.nettyDemo;

import com.example.demo.nettyDemo.entity.CatchValueEntity;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestMainClassLoader {
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Integer aa = 40002252;

        byte[] bytes = intToByteArray(aa);

        System.out.println("长度" + bytes.length);


        //这个类class的路径
        String classPathA = "D:/TestTTClassLoader.class";
        String classPathB = "E:/TestTTClassLoader.class";
        classLoaderA(classPathA);

        classLoaderB(classPathB);

        CatchValueEntity p1 = new CatchValueEntity();
        p1.setKey("2");
        p1.setValue("3");

        final BeanCopier copier = BeanCopier.create(CatchValueEntity.class, CatchValueEntity.class, false);

        int count = 10;
        long start = System.currentTimeMillis();
        System.out.println("开始计时：" + start);
        for (int i = 0; i < count; i++) {
            CatchValueEntity p2 = new CatchValueEntity();
            copier.copy(p1, p2, null);
            System.out.println(p2.getKey());
        }
        long end = System.currentTimeMillis();
        System.out.println("结束计时：" + end);
        System.out.println("耗时：" + (end - start));
    }

    private static void classLoaderA(String classPath) {
        try {
            TTClassLoader myClassLoader = new TTClassLoader(classPath);
            //类的全称
            String packageNamePath = "com.example.demo.nettyDemo.TestTTClassLoader";
            //加载Log这个class文件
            Class<?> TT = myClassLoader.loadClass(packageNamePath);
            System.out.println("类加载器是:" + TT.getClassLoader());

            TT.newInstance();
            //利用反射获取main方法
//            Method method = TT.getDeclaredMethod("main", String[].class);
//            Object object = TT.newInstance();
//            String[] arg = {"ad"};
//            method.invoke(object, (Object) arg);
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    private static void classLoaderB(String classPath) {
        try {
            TAClassLoader myClassLoader = new TAClassLoader(classPath);
            //类的全称
            String packageNamePath = "com.example.demo.nettyDemo.TestTTClassLoader";
            //加载Log这个class文件
            Class<?> TT = myClassLoader.loadClass(packageNamePath);
            System.out.println("类加载器是:" + TT.getClassLoader());

            TT.newInstance();
            //利用反射获取main方法
//            Method method = TT.getDeclaredMethod("main", String[].class);
//            Object object = TT.newInstance();
//            String[] arg = {"ad"};
//            method.invoke(object, (Object) arg);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
