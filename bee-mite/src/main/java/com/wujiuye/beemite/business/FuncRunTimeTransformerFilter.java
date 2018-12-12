package com.wujiuye.beemite.business;

import com.wujiuye.beemite.asmip.AopManager;
import com.wujiuye.beemite.ipevent.InsertPileManager;

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
 *  方法执行耗时统计
 *  ----> 功能：在方法执行之前插座，在方法执行完成之后统计耗时
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class FuncRunTimeTransformerFilter implements TransformerFilter {

    @Override
    public byte[] doTransformer(ClassLoader loader, String className, byte[] classfileBuffer) {
        //System.out.println("FuncRunTimeTransformerFilter====>doTransformer方法");
        byte[] result =  AopManager.newClass(classfileBuffer, InsertPileManager.EventType.RUN_TIME_EVENT);
        FilterChina.sThreadLocal.set(result);
        //System.out.println("返回null，不拦截");
        return null;
    }
}
