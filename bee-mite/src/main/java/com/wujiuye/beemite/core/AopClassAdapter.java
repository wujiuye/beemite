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
package com.wujiuye.beemite.core;

import com.wujiuye.beemite.event.InsertPileManager;
import org.objectweb.asm.*;

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
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class AopClassAdapter extends ClassAdapter implements Opcodes {

    /**
     * 业务代码调用链，还是方法执行事件
     */
    private int eventType;
    private String thisClassName;
    private boolean isInterface;

    /**
     * 构造器
     *
     * @param classVisitor
     */
    public AopClassAdapter(ClassVisitor classVisitor, int eventType) {
        super(classVisitor);
        this.eventType = eventType;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.thisClassName = name;
        // 过滤掉接口
        if ((access & ACC_INTERFACE) == ACC_INTERFACE) {
            isInterface = true;
        } else {
            isInterface = false;
        }
    }

    /**
     * 代码插桩具体实现
     *
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        // 过滤接口的所有方法
        if (isInterface) {
            return methodVisitor;
        }
        // 构造方法、get和set方法、toString、equals不插桩
        if ("<init>".equals(name)
                || name.startsWith("get")
                || name.startsWith("set")
                || "equals".equals(name)
                || "toString".equals(name)) {
            return methodVisitor;
        }
        // 对其它所有方法都进行插桩
        switch (eventType) {
            case InsertPileManager.EventType.RUN_TIME_EVENT:
                return new FuncRuntimeMethodAdapter(methodVisitor, name, desc, thisClassName,(access & ACC_STATIC) == ACC_STATIC);
            case InsertPileManager.EventType.CALL_LINK_EVENT:
                // 在return TryCatchMethodAdapter之前插入的代码不会包裹在try-catch内
                // 可以在此插入一些代码，原本方法的代码此时还没有添加到方法体内，在return methodVisitor之后由asm调配写入
                return new BusinessCallLinkMethodAdapter(methodVisitor, name, desc, thisClassName,(access & ACC_STATIC) == ACC_STATIC);
            default:
                return methodVisitor;
        }
    }

}
