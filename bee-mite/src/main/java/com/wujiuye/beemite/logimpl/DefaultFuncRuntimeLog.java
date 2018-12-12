package com.wujiuye.beemite.logimpl;

import com.wujiuye.beemite.logs.IFuncRuntimeLog;


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
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class DefaultFuncRuntimeLog implements IFuncRuntimeLog {

    @Override
    public void savaFuncStartRuntimeLog(String sessionId, String className, String funcName, long startTime) {
        System.out.println(String.format("[接收到事件，打印日记]savaFuncStartRuntimeLog[%s,%s,%s,%d]",sessionId,className,funcName,startTime));
    }

    @Override
    public void savaFuncEndRuntimeLog(String sessionId, String className, String funcName, long endTime) {
        System.out.println(String.format("[接收到事件，打印日记]savaFuncEndRuntimeLog[%s,%s,%s,%d]",sessionId,className,funcName,endTime));
    }
}
