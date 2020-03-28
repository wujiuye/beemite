/**
 * Copyright [2019-2020] [wujiuye]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wujiuye.beemite;

import com.wujiuye.beemite.business.BusinessCallLinkTransformerFilter;
import com.wujiuye.beemite.business.FilterChina;
import com.wujiuye.beemite.business.FuncRunTimeTransformerFilter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号：Java艺术
 * QQ邮箱：419611821@qq.com
 * 微信号：ye_shao_ismy
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月10日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   BeeMite
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    | 
 *    完成对业务代码的插桩
 *    具体的实现使用责任模式实现
 *
 *    当存在多个转换器时,转换将由 transform 调用链组成。也就是说,
 *    一个 transform 调用返回的 byte 数组将成为下一个调用的输入(通过 classfileBuffer 参数)。
 *    参数:
 *         loader - 定义要转换的类加载器;如果是引导加载器,则为 null
 *         className - 完全限定类内部形式的类名称和 The Java Virtual Machine Specification 中定义的接口名称。例如,"java/util/List"。
 *         classBeingRedefined - 如果是被重定义或重转换触发,则为重定义或重转换的类;如果是类加载,则为 null
 *         protectionDomain - 要定义或重定义的类的保护域
 *         classfileBuffer - class文件输入字节缓冲区
 *    返回:
 *         一个格式良好的类文件缓冲区(转换的结果),如果未执行转换,则返回 null。
 *    抛出:
 *         IllegalClassFormatException
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class BusinessClassInstrumentation implements ClassFileTransformer {


    private String basePackage;

    public BusinessClassInstrumentation(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 判断进行字节码插桩
     *
     * @param loader
     * @param className
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return 方法返回null表示不替换，还是使用原来的字节码,否则替换原来的类的字节码。
     *
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        //过滤掉启动类加载器加载的类
        if (loader == null || className == null) {
            return null;
        }
        //过滤掉不需要的类
        if (!className.replace("/", ".").startsWith(basePackage)) {
            return null;
        }
        //过滤掉动态代理类和内部类，只要类名含有$符合都认为是代理类或者内部类
        if(className.indexOf("$")>0){
            //System.out.println("=====>过滤掉代理类和内部类: "+className);
            return null;
        }
        //过滤TestAop
        if(className.contains("test/TestAop")){
            //System.out.println("=====>过滤掉test包下的TestAop: "+className);
            return null;
        }
        System.out.println("BusinessClassInstrumentation::执行transform方法，className===>"+className);
        //链式调用,责任分清楚
        FilterChina filterChina = new FilterChina();
        filterChina.addTransformerFilter(new BusinessCallLinkTransformerFilter());
        filterChina.addTransformerFilter(new FuncRunTimeTransformerFilter());
        return filterChina.doFilter(loader, className, classfileBuffer);
    }
}
