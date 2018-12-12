package com.wujiuye.beemite.logs;

/**
 * 业务代码插桩的类，通过实现这个接口，就可以获取插桩拦截的信息
 * @author wjy
 */
public interface IBusinessCallLinkLog {

    /**
     * 保存业务代码方法调用日记
     */
    void savaBusinessFuncCallLog(
            //通过sessionId最终将一次处理一次请求的调用链串起来
            String sessionId,
            //当前执行的方法的类名
            String className,
            //当前执行的方法名
            String funcName,
            //当前执行的方法的参数
            Object[] funcAgrs);

    /**
     * 保存方法执行抛出的异常信息
     */
    void savaBusinessFuncCallErrorLog(
            //通过sessionId最终将一次处理一次请求的调用链串起来
            String sessionId,
            //类名
            String className,
            //方法名称
            String funcName,
            //当前方法抛出的异常
            Throwable throwable
    );

}
