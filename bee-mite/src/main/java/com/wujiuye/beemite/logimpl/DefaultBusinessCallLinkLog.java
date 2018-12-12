package com.wujiuye.beemite.logimpl;

import com.wujiuye.beemite.logs.IBusinessCallLinkLog;


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
