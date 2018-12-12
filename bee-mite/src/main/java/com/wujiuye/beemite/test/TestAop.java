package com.wujiuye.beemite.test;

import com.wujiuye.beemite.asmip.AopManager;
import com.wujiuye.beemite.ipevent.InsertPileManager;

import java.io.IOException;
import java.util.Map;

/**
 * 测试字节码的插桩
 *
 * @author wjy
 */
public class TestAop {


    public static void main(String[] agrs) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //取消注释，可查看生成的新的class文件,在target目录下
//        Object object = AopManager.newClass(UserServiceImpl.class, InsertPileManager.EventType.RUN_TIME_EVENT);
//        System.out.println(((UserServiceImpl) object).queryMap("wujiuye", 12));

        UserServiceImpl userService = new UserServiceImpl();
        Map<String,Object> map = userService.queryMap("wujiuye",18);
        System.out.println(map.get("username"));

    }

}
