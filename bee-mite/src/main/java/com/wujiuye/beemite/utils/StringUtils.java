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
package com.wujiuye.beemite.utils;

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
 * @ 创建日期      |   Created in 2018年12月11日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   BeeMite
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${1.0-SNAPSHOT}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class StringUtils {

    /**
     * 获取括号内的字符串
     * @param str
     * @return
     */
    private static String getKHIString(String str){
        return str.substring(str.indexOf("(")+1,str.indexOf(")"));
    }

    /**
     * 是否是对象类型，否则基本数据类型
     * @param str
     * @return
     */
    public static boolean isObjectType(String str){
        return str.startsWith("L")&&str.endsWith(";");
    }

    /**
     * 是否是数组类型
     * @param str
     * @return
     */
    public static boolean isObjectArrayType(String str){
        return str.startsWith("[L")&&str.endsWith(";");
    }

    /**
     * 获取返回值类型签名
     * @param str
     * @return
     */
    public static String getReturnTypeDesc(String str){
        return str.substring(str.indexOf(")")+1);
    }


    /**
     * 获取参数类型签名
     * @param str
     * @return
     */
    public static List<String> getParamTypeDesc(String str){
        String paramsStr = getKHIString(str);
        if("".equals(paramsStr))return null;
        StringBuffer sb = new StringBuffer();
        List<String> stringList = new ArrayList<>();
        boolean isObj = false;
        for(int i=0;i<paramsStr.length();i++){
            char ch = paramsStr.charAt(i);
            if(ch=='[') {
                sb.append(ch);
                isObj=true;
            }else if(ch=='L'&&!isObj){
                sb.append(ch);
                isObj = true;
            }else if(ch==';'){
                sb.append(ch);
                stringList.add(sb.toString());
                sb = new StringBuffer();
                isObj=false;
            }else{
                if(isObj) {
                    sb.append(ch);
                    continue;
                }
                stringList.add(ch+"");
            }
        }
        return stringList;
    }

}
