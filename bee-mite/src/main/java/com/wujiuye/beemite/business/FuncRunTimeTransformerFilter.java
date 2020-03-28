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
package com.wujiuye.beemite.business;

import com.wujiuye.beemite.asmip.AopManager;
import com.wujiuye.beemite.ipevent.InsertPileManager;

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
