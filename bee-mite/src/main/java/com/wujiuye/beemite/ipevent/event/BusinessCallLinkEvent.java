package com.wujiuye.beemite.ipevent.event;

import com.wujiuye.beemite.ipevent.Event;
import com.wujiuye.beemite.ipevent.EventParam;
import com.wujiuye.beemite.ipevent.InsertPileManager;
import lombok.Getter;

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
 * @ 类功能描述    |
 *      插入目标对象目标方法字节码的类，
 *      由插桩调用
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class BusinessCallLinkEvent {

    public enum Type {
        Before(0, "前置事件"),
        ERROR(1, "异常事件");

        @Getter
        private int code;
        @Getter
        private String name;

        Type(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Type getTypeWithCode(int code) {
            switch (code) {
                case 0:
                    return Before;
                case 1:
                    return ERROR;
                default:
                    return null;
            }
        }
    }


    /**
     * 保存业务代码方法调用日记
     */
    public void sendBusinessFuncCallEvent(
            //通过sessionId最终将一次处理一次请求的调用链串起来
            String sessionId,
            //当前执行的方法的类名
            String className,
            //当前执行的方法名
            String funcName,
            //当前执行的方法的参数
            Object[] funcAgrs) {

        //System.out.println("=========处理事件[sendBusinessFuncCallEvent]==============");

        EventParam eventParam = new EventParam();
        eventParam.setClassName(className);
        eventParam.setFuncName(funcName);
        eventParam.setFuncAgrs(funcAgrs);

        Event event = new Event(sessionId, InsertPileManager.EventType.CALL_LINK_EVENT,
                Type.Before.code, eventParam);

        InsertPileManager.getInstance().notifyEvent(event);
    }

    /**
     * 保存方法执行抛出的异常信息
     */
    public void sendBusinessFuncCallThrowableEvent(
            //通过sessionId最终将一次处理一次请求的调用链串起来
            String sessionId,
            //类名
            String className,
            //方法名称
            String funcName,
            //当前方法抛出的异常
            Throwable throwable
    ) {
        //System.out.println("=========处理事件[sendBusinessFuncCallThrowableEvent]==============");

        EventParam eventParam = new EventParam();
        eventParam.setClassName(className);
        eventParam.setFuncName(funcName);
        eventParam.setThrowable(throwable);

        Event event = new Event(sessionId, InsertPileManager.EventType.CALL_LINK_EVENT,
                Type.ERROR.code, eventParam);

        InsertPileManager.getInstance().notifyEvent(event);
    }


}
