package com.wujiuye.beemite.logs;

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
 * @ 类功能描述    |
 * 监听方法执行时间日记处理接口
 *  ---> mac下的idea：Show Diagrams 后选中这个接口 按command+option+b 可查看所有的实现类
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
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
