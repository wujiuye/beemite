/**
 * Copyright [2019-2020] [wujiuye]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wujiuye.beemite.transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号：Java艺术
 * QQ邮箱：419611821@qq.com
 * 微信号：ye_shao_ismy
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
public class FilterChina {

    private List<ClassTransformer> transformerFilterList;

    public FilterChina() {
        transformerFilterList = new ArrayList<>();
    }

    /**
     * 添加过滤器
     *
     * @param transformerFilter
     */
    public void addTransformerFilter(ClassTransformer transformerFilter) {
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
        try {
            byte[] classByte = classfileBuffer;
            for (ClassTransformer filter : this.transformerFilterList) {
                classByte = filter.doTransformer(loader, className,
                        // 如果上一个修改过了就将上一个修改的结果传给下一个
                        classByte);
            }
            return classByte;
        } catch (Exception e) {
            e.printStackTrace();
            return classfileBuffer;
        }
    }

}
