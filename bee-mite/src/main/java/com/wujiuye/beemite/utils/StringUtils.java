package com.wujiuye.beemite.utils;

import java.util.ArrayList;
import java.util.List;

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
