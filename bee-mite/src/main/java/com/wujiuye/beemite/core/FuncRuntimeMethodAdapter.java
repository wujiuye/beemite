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

import com.wujiuye.beemite.event.FuncRuntimeEvent;
import com.wujiuye.beemite.util.SessionIdContext;
import com.wujiuye.beemite.util.StringUtils;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

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
 * @ 类功能描述    | 插入时间埋点
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class FuncRuntimeMethodAdapter extends MethodAdapter implements Opcodes {

    /**
     * 参数类型签名
     */
    private String[] paramDesc;
    private String thisClassName;
    private String funcName;
    private int localCount = 0;

    /**
     * 第二个参数：方法签名
     *
     * @param methodVisitor
     * @param desc
     */
    public FuncRuntimeMethodAdapter(MethodVisitor methodVisitor, String name, String desc,
                                    String thisClassName, boolean isStaticFun) {
        super(methodVisitor);
        if (isStaticFun) {
            localCount -= 1;
        }
        this.thisClassName = thisClassName;
        this.funcName = name;
        //根据方法签名获取参数个数
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
        // 获取当前系统时间
        // 第二个参数是类名，'.'替换成'/'之后的类名
        visitMethodInsn(INVOKESTATIC, System.class.getName().replace(".", "/"), "currentTimeMillis", "()J");
        visitVarInsn(LSTORE, paramDesc.length + (++localCount));

        // 设置参数
        visitMethodInsn(INVOKESTATIC, SessionIdContext.class.getName().replace(".", "/"),
                "getContext", "()Lcom/wujiuye/beemite/util/SessionIdContext;");
        visitMethodInsn(INVOKESPECIAL, SessionIdContext.class.getName().replace(".", "/"),
                "getSessionId", "()Ljava/lang/String;");
        visitLdcInsn(thisClassName);
        visitLdcInsn(funcName);
        visitVarInsn(LLOAD, paramDesc.length + localCount);

        // 调用方法
        visitMethodInsn(INVOKESTATIC, FuncRuntimeEvent.class.getName().replace(".", "/"),
                "sendFuncStartRuntimeEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V");
    }

    /**
     * 拦截所有visitInsn方法插入指令，在return指令之前搞事情
     *
     * @param i 指令码
     */
    @Override
    public void visitInsn(int i) {
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //  优点：
        //  1：不用添加额外的try-catch代码块
        //  缺陷：
        //  1：当出现异常时就不会执行到埋点（获取方法执行结束时间）
        //
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        if (i == RETURN || i == ARETURN || i == IRETURN || i == LRETURN || i == DRETURN) {
            // 保存当前栈顶元素，这是要返回的值
            switch (i) {
                case RETURN:
                    break;
                case ARETURN:
                    visitVarInsn(ASTORE, paramDesc.length + (++localCount));
                    break;
                case IRETURN:
                    visitVarInsn(ISTORE, paramDesc.length + (++localCount));
                    break;
                case LRETURN:
                    visitVarInsn(LSTORE, paramDesc.length + (++localCount));
                    break;
                case DRETURN:
                    visitVarInsn(DSTORE, paramDesc.length + (++localCount));
                    break;
                default:
            }

            // 获取当前系统时间
            visitVarInsn(ALOAD, 0);
            // 第二个参数是类名，'.'替换成'/'之后的类名
            visitMethodInsn(INVOKESTATIC, System.class.getName().replace(".", "/"), "currentTimeMillis", "()J");
            visitVarInsn(LSTORE, paramDesc.length + (++localCount));

            // 插入埋点
            // 设置参数
            // 将常量压入栈顶
            visitMethodInsn(INVOKESTATIC, SessionIdContext.class.getName().replace(".", "/"),
                    "getContext", "()Lcom/wujiuye/beemite/util/SessionIdContext;");
            visitMethodInsn(INVOKESPECIAL, SessionIdContext.class.getName().replace(".", "/"),
                    "getSessionId", "()Ljava/lang/String;");
            // 将常量压入栈顶
            visitLdcInsn(thisClassName);
            // 将常量压入栈顶
            visitLdcInsn(funcName);
            visitVarInsn(LLOAD, paramDesc.length + localCount);
            // 调用方法
            visitMethodInsn(INVOKESTATIC, FuncRuntimeEvent.class.getName().replace(".", "/"),
                    "sendFuncEndRuntimeEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V");

            // 保存当前栈顶元素，这是要返回的值
            switch (i) {
                case RETURN:
                    break;
                case ARETURN:
                    visitVarInsn(ALOAD, paramDesc.length + localCount - 1);
                    break;
                case IRETURN:
                    visitVarInsn(ILOAD, paramDesc.length + localCount - 1);
                    break;
                case LRETURN:
                    visitVarInsn(LLOAD, paramDesc.length + localCount - 1);
                    break;
                case DRETURN:
                    visitVarInsn(DLOAD, paramDesc.length + localCount - 1);
                    break;
                default:
            }
        }
        super.visitInsn(i);
    }

}
