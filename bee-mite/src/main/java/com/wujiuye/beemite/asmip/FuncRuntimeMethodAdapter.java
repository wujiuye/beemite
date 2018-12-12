package com.wujiuye.beemite.asmip;

import com.wujiuye.beemite.ipevent.event.FuncRuntimeEvent;
import com.wujiuye.beemite.utils.StringUtils;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

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

    //参数类型签名
    private String[] paramDesc;
    //返回类型签名
    private String returnDesc;

    private String thisClassName;
    private String funcName;
    private String eventFieldName;
    private int localCount = 0;

    /**
     * 第二个参数：方法签名
     *
     * @param methodVisitor
     * @param desc
     */
    public FuncRuntimeMethodAdapter(MethodVisitor methodVisitor, String name, String desc, String thisClassName, String eventFieldName) {
        super(methodVisitor);
        this.eventFieldName = eventFieldName;
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
        returnDesc = StringUtils.getReturnTypeDesc(desc);
    }

//    private Label from = new Label(),
//            to = new Label(),
//            target = new Label();

    @Override
    public void visitCode() {
        super.visitCode();
        //System.out.println("TryCatchMethodAdapter::visitCode");

        //获取当前系统时间
        visitVarInsn(ALOAD, 0);
        //第二个参数是类名，'.'替换成'/'之后的类名
        visitMethodInsn(INVOKESTATIC, System.class.getName().replace(".", "/"), "currentTimeMillis", "()J");
        visitVarInsn(LSTORE, paramDesc.length + (++localCount));

        visitVarInsn(ALOAD, 0);
        visitFieldInsn(GETFIELD, thisClassName, this.eventFieldName, Type.getDescriptor(FuncRuntimeEvent.class));

        //设置参数
        visitLdcInsn("sessionid is null");
        visitLdcInsn(thisClassName);
        visitLdcInsn(funcName);
        visitVarInsn(LLOAD, paramDesc.length + localCount);

        //调用方法
        visitMethodInsn(INVOKEVIRTUAL, FuncRuntimeEvent.class.getName().replace(".", "/"),
                "sendFuncStartRuntimeEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V");

//        //标志：try块开始位置
//        visitLabel(from);
//        visitTryCatchBlock(from,
//                to,
//                target,
//                //null就是any，这样就是finally代码块了，但是异常还是要保存，在方法的最后面抛出
//                null);
    }

//    /**
//     * 拦截所有visitInsn方法插入指令，在return指令之前加入标签
//     * @param i 指令码
//     */
//    @Override
//    public void visitInsn(int i) {
//        //在return指令之前插入try-catch块的标签
//        if(i==RETURN||i==ARETURN||i==IRETURN||i==LRETURN||i==DRETURN){
//            //标志：try块结束
//            visitLabel(to);
//        }
//        super.visitInsn(i);
//    }

//    @Override
//    public void visitMaxs(int maxStack, int maxLocals) {
//        //标志：try块结束
//        visitLabel(to);
//
//        //标志：finally块开始位置
//        visitLabel(target);
//        visitVarInsn(ASTORE,paramDesc.length+(++localCount));//不能少
//
//        //获取当前系统时间
//        visitVarInsn(ALOAD, 0);
//        //第二个参数是类名，'.'替换成'/'之后的类名
//        visitMethodInsn(INVOKESTATIC, System.class.getName().replace(".","/"), "currentTimeMillis", "()J");
//        visitVarInsn(LSTORE,paramDesc.length+(++localCount));
//
//        //插入埋点
//        visitVarInsn(ALOAD, 0);
//        visitFieldInsn(GETFIELD, thisClassName, this.eventFieldName, Type.getDescriptor(FuncRuntimeEvent.class));
//        //设置参数
//        visitLdcInsn("sessionid is null");//将常量压入栈顶
//        visitLdcInsn(thisClassName);//将常量压入栈顶
//        visitLdcInsn(funcName);//将常量压入栈顶
//        visitVarInsn(LLOAD,paramDesc.length+localCount);
//        //调用方法
//        visitMethodInsn(INVOKEVIRTUAL, Type.getDescriptor(FuncRuntimeEvent.class),
//                "sendFuncEndRuntimeEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V");
//
//        //异常处理不能少,虽然是finally，但是有try就得有catch，只是catch没有写因为是any类型的错误，所以没办法指定catch某个异常，只能是finally，所以还是要处理
//        visitVarInsn(ALOAD,paramDesc.length+(localCount-1));//将存储在本地变量表的异常对象压入操作数栈顶
//        visitInsn(ATHROW);//向外抛出异常
//
//        //System.out.println("TryCatchMethodAdapter::visitMaxs===>" + maxStack + "," + maxLocals);
//        super.visitMaxs(maxStack, maxLocals);
//    }


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
        // 微信公众号：code_skill 当前名称为"全栈攻城狮之道"
        // 邮箱：419611821@qq.com
        // 作者：wujiuye
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        if (i == RETURN || i == ARETURN || i == IRETURN || i == LRETURN || i == DRETURN) {
            //保存当前栈顶元素，这是要返回的值
           switch (i){
               case RETURN:
                   break;
               case ARETURN:
                   visitVarInsn(ASTORE,paramDesc.length + (++localCount));
                   break;
               case IRETURN:
                   visitVarInsn(ISTORE,paramDesc.length + (++localCount));
                   break;
               case LRETURN:
                   visitVarInsn(LSTORE,paramDesc.length + (++localCount));
                   break;
               case DRETURN:
                   visitVarInsn(DSTORE,paramDesc.length + (++localCount));
                   break;
           }

            //获取当前系统时间
            visitVarInsn(ALOAD, 0);
            //第二个参数是类名，'.'替换成'/'之后的类名
            visitMethodInsn(INVOKESTATIC, System.class.getName().replace(".", "/"), "currentTimeMillis", "()J");
            visitVarInsn(LSTORE, paramDesc.length + (++localCount));

            //插入埋点
            visitVarInsn(ALOAD, 0);
            visitFieldInsn(GETFIELD, thisClassName, this.eventFieldName, Type.getDescriptor(FuncRuntimeEvent.class));
            //设置参数
            visitLdcInsn("sessionid is null");//将常量压入栈顶
            visitLdcInsn(thisClassName);//将常量压入栈顶
            visitLdcInsn(funcName);//将常量压入栈顶
            visitVarInsn(LLOAD, paramDesc.length + localCount);
            //调用方法
            visitMethodInsn(INVOKEVIRTUAL, Type.getDescriptor(FuncRuntimeEvent.class),
                    "sendFuncEndRuntimeEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V");


            //保存当前栈顶元素，这是要返回的值
            switch (i){
                case RETURN:
                    break;
                case ARETURN:
                    visitVarInsn(ALOAD,paramDesc.length + localCount -1);
                    break;
                case IRETURN:
                    visitVarInsn(ILOAD,paramDesc.length + localCount -1);
                    break;
                case LRETURN:
                    visitVarInsn(LLOAD,paramDesc.length + localCount -1);
                    break;
                case DRETURN:
                    visitVarInsn(DLOAD,paramDesc.length + localCount -1);
                    break;
            }
        }
        super.visitInsn(i);
    }


    /**
     * 这是因为标签和指令只能在visitMaxs之前访问，
     * 而visitMaxs在visitEnd之前访问，
     * 因此在visitEnd中生成代码将导致错误。
     */
    @Override
    public void visitEnd() {
        super.visitEnd();
        //System.out.println("TryCatchMethodAdapter::visitEnd");
    }
}
