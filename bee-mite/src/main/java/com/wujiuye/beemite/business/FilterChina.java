package com.wujiuye.beemite.business;

import java.util.ArrayList;
import java.util.List;

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
