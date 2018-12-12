package com.wujiuye.beemite.business;

import com.wujiuye.beemite.asmip.AopManager;
import com.wujiuye.beemite.ipevent.InsertPileManager;

/**
 * 方法执行耗时统计
 * 功能：在方法执行之前插座，在方法执行完成之后统计耗时
 * @author wjy
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
