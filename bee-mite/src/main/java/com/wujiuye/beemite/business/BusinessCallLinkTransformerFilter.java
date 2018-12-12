package com.wujiuye.beemite.business;

import com.wujiuye.beemite.asmip.AopManager;
import com.wujiuye.beemite.ipevent.InsertPileManager;

public class BusinessCallLinkTransformerFilter implements TransformerFilter {
    @Override
    public byte[] doTransformer(ClassLoader loader, String className, byte[] classfileBuffer) {
        //System.out.println("BusinessCallLinkTransformerFilter====>doTransformer方法,eventType is : "+InsertPileManager.EventType.CALL_LINK_EVENT);
        byte[] result =  AopManager.newClass(classfileBuffer, InsertPileManager.EventType.CALL_LINK_EVENT);
        FilterChina.sThreadLocal.set(result);
        //System.out.println("返回null，不拦截");
        return null;
    }
}
