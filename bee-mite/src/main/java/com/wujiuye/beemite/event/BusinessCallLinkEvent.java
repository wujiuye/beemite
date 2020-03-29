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
package com.wujiuye.beemite.event;

import lombok.Getter;

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
    public static void sendBusinessFuncCallEvent(
            //通过sessionId最终将一次处理一次请求的调用链串起来
            String sessionId,
            //当前执行的方法的类名
            String className,
            //当前执行的方法名
            String funcName,
            //当前执行的方法的参数
            Object[] funcAgrs) {

        EventParam eventParam = new EventParam.Build(className,funcName)
                .setFuncAgrs(funcAgrs)
                .setSessionId(sessionId)
                .build();

        Event event = new Event(sessionId, InsertPileManager.EventType.CALL_LINK_EVENT,
                Type.Before.code, eventParam);

        InsertPileManager.getInstance().notifyEvent(event);
    }

    /**
     * 保存方法执行抛出的异常信息
     */
    public static void sendBusinessFuncCallThrowableEvent(
            //通过sessionId最终将一次处理一次请求的调用链串起来
            String sessionId,
            //类名
            String className,
            //方法名称
            String funcName,
            //当前方法抛出的异常
            Throwable throwable
    ) {
        EventParam eventParam = new EventParam.Build(className,funcName)
                .setThrowable(throwable)
                .setSessionId(sessionId)
                .build();

        Event event = new Event(sessionId, InsertPileManager.EventType.CALL_LINK_EVENT,
                Type.ERROR.code, eventParam);

        InsertPileManager.getInstance().notifyEvent(event);
    }


}
