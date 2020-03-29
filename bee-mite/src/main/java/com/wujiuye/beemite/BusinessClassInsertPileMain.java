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

import com.wujiuye.beemite.event.InsertPileManager;
import com.wujiuye.beemite.logs.DefaultBusinessCallLinkLog;
import com.wujiuye.beemite.logs.DefaultFuncRuntimeLog;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

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
 * javaagent入口类
 * 像java程序执行入口一样需要定义public static void main方法
 * javaagent也要有入口方法，定义一个public static void premain方法
 * <p>
 * 使用：
 * 先执行maven package进行打包，获取jar包的绝对路径
 * 不建议在当前项目测试
 * 在测试的web项目下点击锤子->Edit Config....-> VM options ->输入下面内容
 * -javaagent:/Users/wjy/MyProjects/beemite/bee-mite/target/bee-mite-1.2.0-jar-with-dependencies.jar=com.wujiuye
 * 等号后面是参数
 * 如果报如下异常：
 * java.lang.VerifyError: Expecting a stackmap frame at branch target 18
 * jdk1.8可以添加参数：-noverify
 * 最终：
 * -noverify -javaagent:/Users/wjy/MyProjects/beemite/bee-mite/target/bee-mite-1.2.0-jar-with-dependencies.jar=com.wujiuye
 * <p>
 * spring项目中也能用，虽然spring会使用它生成的代理对象，但是最终也会调用原本的对象的方法
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class BusinessClassInsertPileMain {

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // 使用注意：
    //      1.我为了过滤掉get和set方法，所以方法名以get或者set方法开头的都不会进行插桩
    //      2.进行插桩的方法的参数不能含有基本数据类型，如果需要请以Integer等类型
    //      3.为了获取到sessionid，将事件串起来，所以此项目只适合用在web项目
    // 缺陷：
    //      1：因为要获取方法参数，而如果方法参数中有基本数据类型的参数，那么就会异常，所以方法参数只能是Object类型，即引用类型
    //      2: 需要同时添加-noverify参数，否则修改后的字节码没问题，但是会验证失败
    //
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * javaagent 的入口方法
     *
     * @param agentOps        java -javaagent:jar文件的位置[=传入premain的参数]
     * @param instrumentation
     */
    public static void premain(String agentOps, Instrumentation instrumentation) {
        System.out.println("=========================== javaagent入口(premain) ===========================");
        // 设置事件日记处理实例
        InsertPileManager.getInstance().addBusinessCallLinkLog(new DefaultBusinessCallLinkLog());
        InsertPileManager.getInstance().addFuncRuntimeLog(new DefaultFuncRuntimeLog());
        // 由参数agentOps控制要拦截的包名
        // 只有是该包下的类或者是其子包下的类，都进行插桩
        System.out.println("参数为：" + agentOps);
        instrumentation.addTransformer(new BusinessClassInstrumentation(agentOps));
        System.out.println("=========================== javaagent 入口执行完毕 ===========================");
    }

    /**
     * java6新特性，main运行之后再修改字节码
     * agent main方式无法向pre main方式那样在命令行指定代理jar，需要借助Attach Tools API，
     * 将编写好的agent jar挂接到目标进程的jvm中执行。
     *
     * @param agentOps
     * @param instrumentation
     * @since 2020/03/28
     */
    public static void agentmain(String agentOps, Instrumentation instrumentation) {
        System.out.println("=========================== javaagent入口(agentmain) ===========================");
        System.out.println("revice ops : " + agentOps);
        String[] ops = agentOps.split("`");
        String[] plus = ops[1].split("\\+");
        for (String plu : plus) {
            switch (plu) {
                case "log":
                    InsertPileManager.getInstance().addBusinessCallLinkLog(new DefaultBusinessCallLinkLog());
                    break;
                case "runtime":
                    InsertPileManager.getInstance().addFuncRuntimeLog(new DefaultFuncRuntimeLog());
                    break;
                default:
            }
        }
        BusinessClassInstrumentation businessClassInstrumentation = new BusinessClassInstrumentation(ops[0]);
        try {
            instrumentation.addTransformer(businessClassInstrumentation, true);
            Class<?>[] classs = instrumentation.getAllLoadedClasses();
            for (Class<?> cla : classs) {
                if (cla.getName().startsWith("com.wujiuye.beemite")) {
                    continue;
                }
                if (cla.getName().startsWith(ops[0])) {
                    try {
                        // attempted to change the schema (add/remove fields) ===> 不允许添加或移除字段
                        instrumentation.retransformClasses(cla);
                    } catch (UnmodifiableClassException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            instrumentation.removeTransformer(businessClassInstrumentation);
        }
        System.out.println("=========================== javaagent入口执行完毕(agentmain) ===========================");
    }

}
