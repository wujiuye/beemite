package com.wujiuye.beemite.boot;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeemiteBoot {

    private static Map<Integer, String> showAllJavaProcess() {
        Map<Integer, String> pidMap = new HashMap<>();
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        int rows = 0;
        System.out.println("找到如下Java进程，请选择：");
        for (VirtualMachineDescriptor vmd : list) {
            if ("com.wujiuye.beemite.boot.BeemiteBoot".equalsIgnoreCase(vmd.displayName())) {
                continue;
            }
            pidMap.put(++rows, vmd.id());
            System.out.println("[" + rows + "] " + vmd.id() + "\t" + vmd.displayName().split(" ")[0]);
        }
        return pidMap;
    }

    private static void attachPid(String pid, String pageckName, String plus) throws Exception {
        VirtualMachine vm;
        vm = VirtualMachine.attach(pid);
        System.out.println("attach pid：" + vm.id());
        try {
            vm.loadAgent("/Users/wjy/MyProjects/beemite/bee-mite/target/bee-mite-1.2.0-jar-with-dependencies.jar", pageckName + "`" + plus);
        } finally {
            vm.detach();
        }
    }

    private static String readString() throws IOException {
        char[] inputBuf = new char[1024];
        int leng = 0;
        while (true) {
            char ch = (char) System.in.read();
            if (ch == '\r') {
                break;
            }
            if (ch == '\n') {
                break;
            }
            inputBuf[leng++] = ch;
        }
        return new String(inputBuf, 0, leng);
    }

    private static String showAllPlus() throws IOException {
        System.out.println("选择插件：");
        Map<String, String> plusMap = new HashMap<>();
        plusMap.put("log", "打印调用链路插件");
        plusMap.put("runtime", "打印方法执行耗时插件");
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, String> entry : plusMap.entrySet()) {
            System.out.println(++index + "\t" + entry.getValue());
        }
        System.out.println("结束请输入：0");
        while (true) {
            index = Integer.parseInt(readString());
            if (index == 0) {
                break;
            }
            switch (index) {
                case 1:
                    builder.append("log+");
                    break;
                case 2:
                    builder.append("runtime+");
                    break;
                default:
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        Map<Integer, String> pidMap = showAllJavaProcess();
        Integer inputId = Integer.parseInt(readString());
        String targetPid = pidMap.get(inputId);
        System.out.println("应用的包名：");
        String packetNsme = readString();
        String plus = showAllPlus();
        attachPid(targetPid, packetNsme, plus);
    }

}
