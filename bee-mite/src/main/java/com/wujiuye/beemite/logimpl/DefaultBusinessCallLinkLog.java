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
package com.wujiuye.beemite.logimpl;

import com.wujiuye.beemite.logs.IBusinessCallLinkLog;


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
 * @ 创建日期      |   Created in 2018年12月11日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   BeeMite
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class DefaultBusinessCallLinkLog implements IBusinessCallLinkLog {

    @Override
    public void savaBusinessFuncCallLog(String sessionId, String className, String funcName, Object[] funcAgrs) {
        System.out.println(String.format("[接收到事件，打印日记]savaBusinessFuncCallLog[%s,%s,%s]", sessionId, className, funcName));
    }

    @Override
    public void savaBusinessFuncCallErrorLog(String sessionId, String className, String funcName, Throwable throwable) {
        System.out.println(String.format("[接收到事件，打印日记]savaBusinessFuncCallErrorLog[%s,%s,%s,%s]", sessionId, className, funcName, throwable.getMessage()));
    }
}
