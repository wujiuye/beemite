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
package com.wujiuye.beemite.ipevent;

import lombok.*;

import java.io.Serializable;

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
 * @ 类功能描述    | event事件携带的参数，统一封装
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@ToString
public class EventParam implements Serializable {

    //类名
    @Getter
    private String className;
    //方法名
    @Getter
    private String funcName;
    //方法参数，对于方法执行耗时事件这个参数没有值
    @Getter
    private Object[] funcAgrs;
    //方法执行开始时间，对于调用链事件这个参数没有值
    @Getter
    private long startTime;
    //方法执行结束时间，对于调用链事件这个参数没有值
    @Getter
    private long endTime;
    //异常
    @Getter
    private Throwable throwable;
    //session
    @Getter
    private String sessionId;

    private EventParam(String className,String funcName){
        this.className = className;
        this.funcName = funcName;
    }

    public static class Build{
        private String className;
        private String funcName;
        private Object[] funcAgrs;
        private long startTime;
        private long endTime;
        private Throwable throwable;
        private String sessionId;

        public Build(String className,String funcName){
            this.className = className;
            this.funcName = funcName;
        }

        public Build setFuncAgrs(Object[] funcAgrs){
            this.funcAgrs =funcAgrs;
            return this;
        }

        public Build setStartTime(long startTime){
            this.startTime = startTime;
            return this;
        }

        public Build setEndTime(long endTime){
            this.endTime = endTime;
            return this;
        }

        public Build setThrowable(Throwable throwable){
            this.throwable = throwable;
            return this;
        }

        public Build setSessionId(String sessionId){
            this.sessionId = sessionId;
            return this;
        }

        public EventParam build(){
            EventParam eventParam = new EventParam(this.className,this.funcName);
            eventParam.funcAgrs = this.funcAgrs;
            eventParam.startTime=this.startTime;
            eventParam.endTime = this.endTime;
            eventParam.throwable = this.throwable;
            eventParam.sessionId = sessionId;
            return eventParam;
        }
    }
}
