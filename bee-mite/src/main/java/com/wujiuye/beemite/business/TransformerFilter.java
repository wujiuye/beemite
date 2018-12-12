package com.wujiuye.beemite.business;


/**
 * 实现链接调用
 * doTransformer方法如果需要拦截，不继续往下处理，那么可以直接将结果返回，否则返回null
 * @author wjy
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
