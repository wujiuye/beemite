package com.wujiuye.beemite.ipevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

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
 * @ 类功能描述    | event事件携带的参数，统一封装
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
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
