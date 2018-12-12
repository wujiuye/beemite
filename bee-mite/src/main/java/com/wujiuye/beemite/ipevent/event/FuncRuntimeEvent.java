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
 *  插入目标对象目标方法字节码的类，
 *  由插桩调用
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class FuncRuntimeEvent {


    public enum Type {
        Before(0, "前置事件"),
        After(1, "后置事件");

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
                    return After;
            }
            return null;
        }
    }


    /**
     * 方法执行之前调用
     *
     * @param sessionId 一次请求的sessionId
     * @param className 类名
     * @param funcName  方法名
     * @param startTime 开始执行时间戳
     */
    public void sendFuncStartRuntimeEvent(
            String sessionId,
            String className,
            String funcName,
            long startTime
    ) {
        //System.out.println("=========处理事件[sendFuncStartRuntimeEvent]==============");

        EventParam eventParam = new EventParam();
        eventParam.setClassName(className);
        eventParam.setFuncName(funcName);
        eventParam.setStartTime(startTime);

        Event event = new Event(sessionId, InsertPileManager.EventType.RUN_TIME_EVENT,
                Type.Before.code, eventParam);

        InsertPileManager.getInstance().notifyEvent(event);
    }

    /**
     * 方法结束之前调用
     *
     * @param sessionId 一次请求的sessionId
     * @param className 类名
     * @param funcName  方法名
     * @param endTime   执行结束时间戳
     */
    public void sendFuncEndRuntimeEvent(
            String sessionId,
            String className,
            String funcName,
            long endTime
    ) {
        //System.out.println("=========处理事件[sendFuncEndRuntimeEvent]==============");

        EventParam eventParam = new EventParam();
        eventParam.setClassName(className);
        eventParam.setFuncName(funcName);
        eventParam.setEndTime(endTime);

        Event event = new Event(sessionId, InsertPileManager.EventType.RUN_TIME_EVENT,
                Type.After.code, eventParam);

        InsertPileManager.getInstance().notifyEvent(event);
    }

}
