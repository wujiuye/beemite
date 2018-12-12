package com.wujiuye.beemite.business;

import java.util.ArrayList;
import java.util.List;

public class FilterChina{

    //保存中间结果，确保线程安全
    static final ThreadLocal<byte[]> sThreadLocal = new ThreadLocal<>();

    private List<TransformerFilter> transformerFilterList;
    private int index = 0;

    public FilterChina() {
        transformerFilterList = new ArrayList<>();
    }

    /**
     * 添加过滤器
     *
     * @param transformerFilter
     */
    public void addTransformerFilter(TransformerFilter transformerFilter) {
        this.transformerFilterList.add(transformerFilter);
    }

    /**
     * 责任连调用
     *
     * @param loader
     * @param className
     * @param classfileBuffer
     * @return
     */
    public byte[] doFilter(ClassLoader loader, String className, byte[] classfileBuffer) {
        sThreadLocal.set(null);
        do {
            byte[] classByte = sThreadLocal.get();
            byte[] result = transformerFilterList.get(index).doTransformer(
                    loader,
                    className,
                    //如果上一个修改过了就将上一个修改的结果传给下一个
                    classByte==null?classfileBuffer:classByte);
            //某个过滤器要拦截请求就返回非null就行了
            if (result != null) {
                return result;
            }
        } while (++index < transformerFilterList.size());
        return sThreadLocal.get();
    }

}
