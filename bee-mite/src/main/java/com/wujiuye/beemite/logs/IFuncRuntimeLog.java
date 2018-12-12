package com.wujiuye.beemite.logs;

public interface IFuncRuntimeLog {


    /**
     * 方法执行之前调用
     * @param sessionId 一次请求的sessionId
     * @param className 类名
     * @param funcName  方法名
     * @param startTime 开始执行时间戳
     */
    void savaFuncStartRuntimeLog(
            String sessionId,
            String className,
            String funcName,
            long startTime
    );

    /**
     * 方法结束之前调用
     * @param sessionId  一次请求的sessionId
     * @param className 类名
     * @param funcName  方法名
     * @param endTime   执行结束时间戳
     */
    void savaFuncEndRuntimeLog(
            String sessionId,
            String className,
            String funcName,
            long endTime
    );

}
