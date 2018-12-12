package com.wujiuye.beemite.ipevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * event事件携带的参数，统一封装
 * @author wjy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventParam implements Serializable {

    //类名
    private String className;
    //方法名
    private String funcName;
    //方法参数，对于方法执行耗时事件这个参数没有值
    private Object[] funcAgrs;
    //方法执行开始时间，对于调用链事件这个参数没有值
    private long startTime;
    //方法执行结束时间，对于调用链事件这个参数没有值
    private long endTime;
    //异常
    private Throwable throwable;

}
