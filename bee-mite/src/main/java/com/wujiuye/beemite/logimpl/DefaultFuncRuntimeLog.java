package com.wujiuye.beemite.logimpl;

import com.wujiuye.beemite.logs.IFuncRuntimeLog;

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
