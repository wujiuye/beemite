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

import com.wujiuye.beemite.event.BusinessCallLinkEvent;
import com.wujiuye.beemite.util.SessionIdContext;
import com.wujiuye.beemite.util.StringUtils;
import org.objectweb.asm.*;

import java.util.List;

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
 * @ 创建日期      |   Created in 2018年12月11日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   BeeMite
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * 给方法添加try-catch块，包裹方法内的所有代码
 * <p>
 * Exception table:
 * from    to  target type
 * 0    27    30   Class java/lang/Exception
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class BusinessCallLinkMethodAdapter extends MethodAdapter implements Opcodes {

    /**
     * 参数类型签名
     */
    private String[] paramDesc;

    private String thisClassName;
    private String funcName;
    private int localCount = 0;

    private Label from = new Label(), to = new Label(), target = new Label();

    /**
     * 第二个参数：方法签名
     *
     * @param methodVisitor
     * @param desc
     */
    public BusinessCallLinkMethodAdapter(MethodVisitor methodVisitor, String name,
                                         String desc, String thisClassName, boolean isStaticFun) {
        super(methodVisitor);
        if (isStaticFun) {
            localCount -= 1;
        }
        this.thisClassName = thisClassName;
        this.funcName = name;
        // 根据方法签名获取参数个数
        List<String> pds = StringUtils.getParamTypeDesc(desc);
        if (pds == null) {
            paramDesc = null;
            return;
        }
        paramDesc = new String[pds.size()];
        for (int i = 0; i < pds.size(); i++) {
            paramDesc[i] = pds.get(i);
        }
    }

    @Override
    public void visitCode() {
        super.visitCode();
        // 标志：try块开始位置
        visitLabel(from);
        visitTryCatchBlock(from, to, target, "java/lang/Exception");

        // 方法执行之前插入埋点
        // >>>>>>>>>创建数组>>>>>>>>>>
        if (paramDesc != null && paramDesc.length > 0) {
            // 创建数组
            if (paramDesc.length >= 4) {
                // 数组大小
                mv.visitVarInsn(BIPUSH, paramDesc.length);
            } else {
                switch (paramDesc.length) {
                    case 1:
                        mv.visitInsn(ICONST_1);
                        break;
                    case 2:
                        mv.visitInsn(ICONST_2);
                        break;
                    case 3:
                        mv.visitInsn(ICONST_3);
                        break;
                    default:
                        mv.visitInsn(ICONST_0);
                }
            }
            mv.visitTypeInsn(ANEWARRAY, Type.getDescriptor(Object.class));
            // 存储到局部变量表，第二个参数指定在变量表中的下标
            mv.visitVarInsn(ASTORE, paramDesc.length + (++localCount));
            // 为数组赋值
            for (int i = 0; i < paramDesc.length; i++) {
                // 给数组赋值
                // 数组的引用，此时是第（this+参数个数+1）个局部变量
                mv.visitVarInsn(ALOAD, paramDesc.length + localCount);
                if (i > 3) {
                    // 设置下标
                    mv.visitVarInsn(BIPUSH, i);
                } else {
                    switch (i) {
                        case 0:
                            mv.visitInsn(ICONST_0);
                            break;
                        case 1:
                            mv.visitInsn(ICONST_1);
                            break;
                        case 2:
                            mv.visitInsn(ICONST_2);
                            break;
                        case 3:
                            mv.visitInsn(ICONST_3);
                            break;
                        default:
                    }
                }
                // 获取对应的参数
                mv.visitVarInsn(ALOAD, i + 1);
                // 为数组下标索引处赋值
                mv.visitInsn(AASTORE);
            }

            // 设置参数
            visitMethodInsn(INVOKESTATIC, SessionIdContext.class.getName().replace(".", "/"),
                    "getContext", "()Lcom/wujiuye/beemite/util/SessionIdContext;");
            visitMethodInsn(INVOKESPECIAL, SessionIdContext.class.getName().replace(".", "/"),
                    "getSessionId", "()Ljava/lang/String;");
            visitLdcInsn(thisClassName);
            visitLdcInsn(funcName);
            if (paramDesc == null || paramDesc.length == 0) {
                visitInsn(ACONST_NULL);
            } else {
                visitVarInsn(ALOAD, paramDesc.length + localCount);
            }
            // 调用方法
            visitMethodInsn(INVOKESTATIC, BusinessCallLinkEvent.class.getName().replace(".", "/"),
                    "sendBusinessFuncCallEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V");
        }
    }

    /**
     * 标签和指令只能在visitMaxs之前访问，
     * 因为visitMaxs在visitEnd之前访问，所以不能在visitEnd中生成代码，否则将导致错误。
     */
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        // 标志：try块结束
        visitLabel(to);
        // 标志：catch块开始位置
        visitLabel(target);
        // 将异常存储在本地变量表下标[paramDesc.length+(++localCount)]处
        visitVarInsn(ASTORE, paramDesc.length + (++localCount));

        // 在抛出异常之前先插入埋点
        // 设置参数
        // SessionIdContext.getContext().getSessionId()
        visitMethodInsn(INVOKESTATIC, SessionIdContext.class.getName().replace(".", "/"),
                "getContext", "()Lcom/wujiuye/beemite/util/SessionIdContext;");
        visitMethodInsn(INVOKESPECIAL, SessionIdContext.class.getName().replace(".", "/"),
                "getSessionId", "()Ljava/lang/String;");
        visitLdcInsn(thisClassName);
        visitLdcInsn(funcName);
        visitVarInsn(ALOAD, paramDesc.length + localCount);
        // 调用方法
        visitMethodInsn(INVOKESTATIC, BusinessCallLinkEvent.class.getName().replace(".", "/"),
                "sendBusinessFuncCallThrowableEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V");

        // 将本地变量表[paramDesc.length+(++localCount)]处的对象压入操作数栈顶
        visitVarInsn(ALOAD, paramDesc.length + localCount);
        // 向外抛出异常
        visitInsn(ATHROW);
        super.visitMaxs(maxStack, maxLocals);
    }

}
