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
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
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
