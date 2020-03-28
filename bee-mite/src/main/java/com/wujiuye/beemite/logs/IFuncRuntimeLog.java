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
package com.wujiuye.beemite.logs;

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
 * 监听方法执行时间日记处理接口
 *  ---> mac下的idea：Show Diagrams 后选中这个接口 按command+option+b 可查看所有的实现类
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public interface IFuncRuntimeLog {


    /**
     * 方法执行之前调用
     * @param sessionId 一次请求的sessionId
     * @param className 类名
     * @param funcName  方法名
     * @param startTime 开始执行时间戳
     */
    void savaFuncStartRuntimeLog(
            String sessionId,
            String className,
            String funcName,
            long startTime
    );

    /**
     * 方法结束之前调用
     * @param sessionId  一次请求的sessionId
     * @param className 类名
     * @param funcName  方法名
     * @param endTime   执行结束时间戳
     */
    void savaFuncEndRuntimeLog(
            String sessionId,
            String className,
            String funcName,
            long endTime
    );

}
