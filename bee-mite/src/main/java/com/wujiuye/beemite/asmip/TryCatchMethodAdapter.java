package com.wujiuye.beemite.asmip;

import com.wujiuye.beemite.ipevent.event.BusinessCallLinkEvent;
import com.wujiuye.beemite.utils.StringUtils;
import org.objectweb.asm.*;

import java.util.List;

/**
 * 给方法添加try-catch块，包裹方法内的所有代码
 *
 * Exception table:
 *        from    to  target type
 *            0    27    30   Class java/lang/Exception
 * @author wjy
 */
public class TryCatchMethodAdapter extends MethodAdapter implements Opcodes {

    //参数类型签名
    private String[] paramDesc;
    //返回类型签名
    private String returnDesc;

    private String thisClassName;
    private String funcName;
    private String eventFieldName;
    private  int localCount = 0;

    /**
     * 第二个参数：方法签名
     * @param methodVisitor
     * @param desc
     */
    public TryCatchMethodAdapter(MethodVisitor methodVisitor,String name,String desc,String thisClassName,String eventFieldName) {
        super(methodVisitor);
        this.eventFieldName = eventFieldName;
        this.thisClassName=thisClassName;
        this.funcName=name;
        //根据方法签名获取参数个数
        List<String> pds = StringUtils.getParamTypeDesc(desc);
        if(pds==null){
            paramDesc=null;
            return;
        }
        paramDesc = new String[pds.size()];
        for (int i=0;i<pds.size();i++){
            paramDesc[i]=pds.get(i);
        }
        returnDesc = StringUtils.getReturnTypeDesc(desc);
    }

    private Label from = new Label(),
            to = new Label(),
            target = new Label();

    @Override
    public void visitCode() {
        super.visitCode();
        //System.out.println("TryCatchMethodAdapter::visitCode");
        //标志：try块开始位置
        visitLabel(from);
        visitTryCatchBlock(from,
                to,
                target,
                "java/lang/Exception");

        //方法执行之前插入埋点
        {
            //>>>>>>>>>创建数组>>>>>>>>>>
            if(paramDesc!=null&&paramDesc.length>0){
                //创建数组
                if (paramDesc.length >= 4) {
                    mv.visitVarInsn(BIPUSH, paramDesc.length);//数组大小
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
                mv.visitVarInsn(ASTORE, paramDesc.length + (++localCount));//存储到局部变量表，第二个参数指定在变量表中的下标
                //为数组赋值
                for (int i = 0; i < paramDesc.length; i++) {
                    //给数组赋值
                    mv.visitVarInsn(ALOAD, paramDesc.length + localCount);//数组的引用，此时是第（this+参数个数+1）个局部变量
                    if(i>3){
                        mv.visitVarInsn(BIPUSH, i);//设置下标
                    }else {
                        switch (i){
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
                        }
                    }
                    mv.visitVarInsn(ALOAD, i + 1);//获取对应的参数
                    mv.visitInsn(AASTORE);//为数组下标索引处赋值
                }
            }
            //>>>>>>>>>>>>>>>>>>>>>>>>>>>
            visitVarInsn(ALOAD,0);
            visitFieldInsn(GETFIELD,thisClassName,this.eventFieldName,Type.getDescriptor(BusinessCallLinkEvent.class));
            //设置参数
            visitLdcInsn("sessionid is null");
            visitLdcInsn(thisClassName);
            visitLdcInsn(funcName);
            if(paramDesc==null||paramDesc.length==0){
                visitInsn(ACONST_NULL);
            }else {
                visitVarInsn(ALOAD,paramDesc.length + localCount);
            }
            //调用方法
            visitMethodInsn(INVOKEVIRTUAL,Type.getDescriptor(BusinessCallLinkEvent.class),
                    "sendBusinessFuncCallEvent","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V");
        }
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

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        //标志：try块结束
        visitLabel(to);
        //标志：catch块开始位置
        visitLabel(target);
        visitVarInsn(ASTORE,paramDesc.length+(++localCount));//将异常存储在本地变量表下标[paramDesc.length+(++localCount)]处
        {

            //在抛出异常之前先插入埋点
            visitVarInsn(ALOAD,0);
            visitFieldInsn(GETFIELD,thisClassName,this.eventFieldName,Type.getDescriptor(BusinessCallLinkEvent.class));
            //设置参数
            visitLdcInsn("sessionid is null");
            visitLdcInsn(thisClassName);
            visitLdcInsn(funcName);
            visitVarInsn(ALOAD,paramDesc.length+localCount);
            //调用方法
            visitMethodInsn(INVOKEVIRTUAL,Type.getDescriptor(BusinessCallLinkEvent.class),
                    "sendBusinessFuncCallThrowableEvent","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V");
        }
        visitVarInsn(ALOAD,paramDesc.length+localCount);//将本地变量表[paramDesc.length+(++localCount)]处的对象压入操作数栈顶
        visitInsn(ATHROW);//向外抛出异常


        //goto tag，那么标签tag后面必须要有代码，否则就是跳转到方法之外了。
        //可以直接插入一条空指令，什么也不做，确保target后面有代码
        //visitInsn(NOP);

//        //方法无返回值
//        if(returnDesc==null||"".equals(returnDesc)){
//            visitInsn(RETURN);
//        }
//        //如果返回值类型为引用，则返回null值
//        else if(returnDesc.startsWith("L")&&returnDesc.endsWith(";")){
//            visitInsn(ACONST_NULL);
//            visitInsn(ARETURN);
//        }else{
//            switch (returnDesc){
//                case "J":
//                    //返回0
//                    visitInsn(LCONST_0);
//                    visitInsn(LRETURN);
//                    break;
//                case "D":
//                    visitInsn(DCONST_0);
//                    visitInsn(DRETURN);
//                    break;
//                default:
//                    //返回0
//                    visitInsn(ICONST_0);
//                    visitInsn(IRETURN);
//                    break;
//            }
//        }
        //System.out.println("TryCatchMethodAdapter::visitMaxs===>"+maxStack+","+maxLocals);
        super.visitMaxs(maxStack, maxLocals);
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
