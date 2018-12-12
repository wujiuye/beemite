package com.wujiuye.beemite.logimpl;

import com.wujiuye.beemite.logs.IBusinessCallLinkLog;


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
public class DefaultBusinessCallLinkLog implements IBusinessCallLinkLog {

    @Override
    public void savaBusinessFuncCallLog(String sessionId, String className, String funcName, Object[] funcAgrs) {
        System.out.println(String.format("[接收到事件，打印日记]savaBusinessFuncCallLog[%s,%s,%s]", sessionId, className, funcName));
    }

    @Override
    public void savaBusinessFuncCallErrorLog(String sessionId, String className, String funcName, Throwable throwable) {
        System.out.println(String.format("[接收到事件，打印日记]savaBusinessFuncCallErrorLog[%s,%s,%s,%s]", sessionId, className, funcName, throwable.getMessage()));
    }
}
