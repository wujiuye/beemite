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

import com.wujiuye.beemite.transformer.BusinessCallLinkClassTransformer;
import com.wujiuye.beemite.transformer.FilterChina;
import com.wujiuye.beemite.transformer.FuncRunTimeClassTransformer;
import com.wujiuye.beemite.util.ClassSearchUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
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
 * 完成对业务代码的插桩
 * 具体的实现使用责任模式实现
 * <p>
 * 当存在多个转换器时,转换将由 transform 调用链组成。也就是说,
 * 一个 transform 调用返回的 byte 数组将成为下一个调用的输入(通过 classfileBuffer 参数)。
 * 参数:
 * loader - 定义要转换的类加载器;如果是引导加载器,则为 null
 * className - 完全限定类内部形式的类名称和 The Java Virtual Machine Specification 中定义的接口名称。例如,"java/util/List"。
 * classBeingRedefined - 如果是被重定义或重转换触发,则为重定义或重转换的类;如果是类加载,则为 null
 * protectionDomain - 要定义或重定义的类的保护域
 * classfileBuffer - class文件输入字节缓冲区
 * 返回:
 * 一个格式良好的类文件缓冲区(转换的结果),如果未执行转换,则返回 null。
 * 抛出:
 * IllegalClassFormatException
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class BusinessClassInstrumentation implements ClassFileTransformer {

    private String basePackage;
    /**
     * 链式调用,责任分清楚
     */
    private FilterChina filterChina = new FilterChina();

    public BusinessClassInstrumentation() {
        this(null);
    }

    public BusinessClassInstrumentation(String basePackage) {
        this.basePackage = basePackage;
        filterChina.addTransformerFilter(new BusinessCallLinkClassTransformer());
        filterChina.addTransformerFilter(new FuncRunTimeClassTransformer());
    }

    /**
     * 判断进行字节码插桩
     *
     * @param loader
     * @param className
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return 方法返回null表示不替换，还是使用原来的字节码，否则替换原来的类的字节码。
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        // 过滤掉不需要的类
        if (basePackage != null && !className.replace("/", ".").startsWith(basePackage)) {
            return null;
        }
        if (basePackage == null && !ClassSearchUtils.match(className)) {
            return null;
        }
        // 过滤接口
        if (classBeingRedefined.isInterface()) {
            return null;
        }
        // 过滤main方法的类
        Method[] methods = classBeingRedefined.getMethods();
        for (Method method : methods) {
            if ("main".equalsIgnoreCase(method.getName())) {
                return null;
            }
        }
        System.out.println("执行transform方法，className: " + className);
        return filterChina.doFilter(loader, className, classfileBuffer);
    }

}
