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
 *  业务代码插桩的类，通过实现这个接口，就可以获取插桩拦截的信息
 *  ----> mac下的idea：Show Diagrams 后选中这个接口 按command+option+b 可查看所有的实现类
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
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
