package com.wujiuye.beemite.business;


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
 *
 *  实现链接调用
 *  -----> doTransformer方法如果需要拦截，不继续往下处理，那么可以直接将结果返回，否则返回null
 *  -----> mac下的idea：Show Diagrams 后选中这个接口 按command+option+b 可查看所有的实现类
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public interface TransformerFilter {

    /**
     * 如果需要拦截，不继续往下处理，那么可以直接将结果返回，否则返回null
     * @param loader
     * @param className
     * @param classfileBuffer
     * @return
     */
    byte[] doTransformer(ClassLoader loader,
                         String className,
                         byte[] classfileBuffer);

}
