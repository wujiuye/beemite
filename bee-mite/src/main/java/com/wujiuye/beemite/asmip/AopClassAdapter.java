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
package com.wujiuye.beemite.asmip;

import com.wujiuye.beemite.ipevent.InsertPileManager;
import com.wujiuye.beemite.ipevent.event.BusinessCallLinkEvent;
import com.wujiuye.beemite.ipevent.event.FuncRuntimeEvent;
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

    private String EVENT_FIELD_NAME;
    //业务代码调用链，还是方法执行事件
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
        this.EVENT_FIELD_NAME = "eventObject" + eventType;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //System.out.println("====AopClassAdapter类的创建=====");
        super.visit(version, access, name, signature, superName, interfaces);
        this.thisClassName = name;
        //过滤掉接口
        if((access&ACC_INTERFACE)==ACC_INTERFACE){
            System.out.println("这是接口=============");
            isInterface = true;
        }else{
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
        //System.out.println("access:"+access+",name:"+name+",desc:"+desc);
        MethodVisitor methodVisitor = super.visitMethod(access,name,desc,signature,exceptions);
        //过滤接口的所有方法
        if(isInterface) {
            return methodVisitor;
        }
        //构造方法、get和set方法、toString、equals不插桩
        if ("<init>".equals(name)
                || name.startsWith("get")
                || name.startsWith("set")
                || name.equals("equals")
                || name.equals("toString")) {
            return methodVisitor;
        }
        //过滤静态方法
        if((access&ACC_STATIC)==ACC_STATIC){
            System.out.println("过滤静态方法："+thisClassName+"."+name);
            return methodVisitor;
        }
        //对其它所有方法都进行插桩
        switch (eventType) {
            case InsertPileManager.EventType.CALL_LINK_EVENT:
                //在return TryCatchMethodAdapter之前插入的代码不会包裹在try-catch内
                //可以在此插入一些代码，原本方法的代码此时还没有添加到方法体内，在return methodVisitor之后由asm调配写入
                insertCallLinkEvnet(methodVisitor);
                return new TryCatchMethodAdapter(methodVisitor,name,desc,thisClassName,EVENT_FIELD_NAME);
            case InsertPileManager.EventType.RUN_TIME_EVENT:
                insertFuncRuntimeEvent(methodVisitor);
                return new FuncRuntimeMethodAdapter(methodVisitor,name,desc,thisClassName,EVENT_FIELD_NAME);
        }
        return methodVisitor;//如果不想要方法原来的代码，那么这里可以直接返回null
    }


    /**
     * 插入业务代码调用链埋点
     */
    private void insertCallLinkEvnet(MethodVisitor methodVisitor) {
        methodVisitor.visitCode();
        newEventObject(methodVisitor);
        methodVisitor.visitEnd();
    }


    /**
     * 插入方法执行时间埋点
     */
    private void insertFuncRuntimeEvent(MethodVisitor methodVisitor) {
        methodVisitor.visitCode();
        newEventObject(methodVisitor);
        methodVisitor.visitEnd();
    }


    /**
     * 属性实例化，不在构造方法，是怕没有无参构造方法
     *
     * @param methodVisitor
     */
    private void newEventObject(MethodVisitor methodVisitor) {
        //获取event对象的类型签名
        String eventObject = null;
        switch (eventType) {
            case InsertPileManager.EventType.CALL_LINK_EVENT:
                eventObject = Type.getDescriptor(BusinessCallLinkEvent.class);
                break;
            case InsertPileManager.EventType.RUN_TIME_EVENT:
                eventObject = Type.getDescriptor(FuncRuntimeEvent.class);
                break;
        }

        Label label = new Label();
        //>>>>>>>>>>>>>>>>>>>>>>>>>> do-while循环 实现方法 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//        //标签放在{}前面，这种实现的是do-while循环
//        methodVisitor.visitLabel(label);
//        {
//            //this.ssss = new xxx();
//            methodVisitor.visitVarInsn(ALOAD, 0);
//            methodVisitor.visitTypeInsn(NEW, eventObject);
//            methodVisitor.visitInsn(DUP);
//            methodVisitor.visitMethodInsn(INVOKESPECIAL, eventObject, "<init>", "()V");
//            methodVisitor.visitFieldInsn(PUTFIELD, thisClassName, EVENT_FIELD_NAME, eventObject);
//        }
//        //判断如果eventObject等于null，则为eventObject创建一个对象
//        methodVisitor.visitVarInsn(ALOAD, 0);
//        methodVisitor.visitFieldInsn(GETFIELD, thisClassName, EVENT_FIELD_NAME, eventObject);
//        //如果为空则跳转到标签处执行{}内的指令
//        methodVisitor.visitJumpInsn(IFNULL, label);
        //>>>>>>>>>>>>>>>>>>>>>>>>>> end >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        //>>>>>>>>>>>>>>>>>>>>>>>>>> if(){} 实现方法 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //判断如果eventObject等于null，则为eventObject创建一个对象
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitFieldInsn(GETFIELD, thisClassName, EVENT_FIELD_NAME, eventObject);
        //如果不为空则跳转到标签处，不执行{}内的指令
        methodVisitor.visitJumpInsn(IFNONNULL, label);
        {
            //this.ssss = new xxx();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, eventObject);
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, eventObject, "<init>", "()V");
            methodVisitor.visitFieldInsn(PUTFIELD, thisClassName, EVENT_FIELD_NAME, eventObject);
        }
        //标签放在{}后面
        methodVisitor.visitLabel(label);
        methodVisitor.visitInsn(NOP);//确包标签后面要有指令
        //>>>>>>>>>>>>>>>>>>>>>>>>>> end >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    }



    /**
     * 插入一个字段
     */
    @Override
    public void visitEnd() {
        //为类添加一个属性
        //参数1；外部访问权限，公有还是私有
        //参数2：属性名
        //参数3：参数类型
        if(isInterface) {
            super.visitEnd();
            return;
        }
        FieldVisitor fieldVisitor;
        switch (eventType) {
            case InsertPileManager.EventType.CALL_LINK_EVENT:
                fieldVisitor = super.cv.visitField(ACC_PRIVATE, EVENT_FIELD_NAME,
                        Type.getDescriptor(BusinessCallLinkEvent.class), null, null);
                fieldVisitor.visitEnd();
                break;
            case InsertPileManager.EventType.RUN_TIME_EVENT:
                fieldVisitor = super.cv.visitField(ACC_PRIVATE, EVENT_FIELD_NAME,
                        Type.getDescriptor(FuncRuntimeEvent.class), null, null);
                fieldVisitor.visitEnd();
                break;
        }
        super.visitEnd();
    }
}
