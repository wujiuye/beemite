package com.wujiuye.beemite.asmip;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号id：code_skill
 * QQ邮箱：419611821@qq.com
 * 微信号：www_wujiuye_com
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月10日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   BeeMite
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    | 管理代理实例的创建
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public final class AopManager {

    /**
     * 根据切入点创建一个代理对象
     * @param oldClass 当前类的字节码
     * @param event 是要插桩什么事件，业务代码调用链，还是方法执行事件
     * @return  返回插桩后的字节码
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static byte[] newClass(byte[] oldClass,int event) {
        //System.out.println("==============asm开始对字节码插桩==============");

        /**
         * COMPUTE_FRAMES：从头开始自动计算方法的堆栈映射帧。
         * COMPUTE_MAXS：  自动计算方法的最大堆栈大小和最大本地变量数量。
         * 如果是自己重写了全部方法体内代码，那就需要自己设置大小了
         */
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassReader classReader = new ClassReader(oldClass);

        AopClassAdapter aopClassAdapter = new AopClassAdapter(classWriter,event);
        classReader.accept(aopClassAdapter,ClassReader.SKIP_DEBUG);
        byte[] newClassData = classWriter.toByteArray();


//        //保存一份临时字节码，用于排查错误
//        try {
//            String fileName = Thread.currentThread().getContextClassLoader().getResource("").getPath()
//                    + "TargerProxy".replace(".","/")+".class";
//            File file = new File( fileName);
//            //将新的类字节码保存到文件,替换旧的
//            FileOutputStream fout = new FileOutputStream(file);
//            fout.write(newClassData);
//            fout.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return newClassData;
    }

    /**
     * 根据原本的class进行插桩，获取一个新的class文件替换旧的
     * @param event
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static Object newClass(Class oldClass,int event) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        String fileName = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + oldClass.getName().replace(".","/")+".class";
        File file = new File( fileName);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter classAdapter = new AopClassAdapter(classWriter,event);

        ClassReader classReader = new ClassReader(oldClass.getName());
        classReader.accept(classAdapter,ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();

        //将新的类字节码保存到文件,替换旧的
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(data);
        fout.close();

        //使用当前线程类加载器从刚生成的文件加载类
        Class proxyClass =  Thread.currentThread().getContextClassLoader().loadClass(oldClass.getName());
        return proxyClass.newInstance();
    }

}
