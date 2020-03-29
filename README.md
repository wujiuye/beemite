>微信公众号：**[javaskill](#jump_10)**
 公众号名：**[Java艺术](#jump_10)**

### 目录

[TOC]

### 关于bee-mite

javaagent+asm实现字节码插桩，在类加载之前对字节码进行修改，插入埋点。
- 实现业务代码调用链插桩，在方法执行之前拦截获取类名、方法名，方法调用的参数，在方法执行异常时，获取到异常信息；
- 为统计方法执行时间插入埋点，在方法执行之前和返回之前获取系统时间。关键技术：javaagent、asm、责任链模式、线程池分派日记消息给监听器处理。 

* 旧版本：`master`分支
* 新版本：`agentmain`分支

#### asmip包

实现字节码插桩，在类加载之前对字节码进行修改，插入埋点。目前已经实现了业务代码调用链插桩，在方法执行之前拦截获取类名、方法名，方法调用的参数，在方法执行异常时，获取到异常信息。还实现了方法执行时间的埋点，在方法执行之前获取系统时间，发送一个日记事件，在方法执行结束之后获取系统时间，发送一个事件。

#### business包

代码插桩过滤器，使用责任连模式，对字节码进行多次插桩。
#### ipevent包

事件的封装，埋点代码抛出事件给线程池，线程池分派事件给监听器进行处理。
#### logs包

提供事件监听器接口，具体实现交由使用者实现，我这里提供了两个默认的实现类，在logimpl包下，默认的实现类只是将日记打印，在控制台打印日记信息。

### 用到的技术

这是用到了asm、javaagent、责任连模式。因为字节码是插入到业务代码中的，当执行业务代码的时候会执行埋点代码，如果处理程序也在业务代码中进行那么这将是个耗时的操作，影响性能，拖慢一次请求的响应速度，所以当埋点代码执行的时候，我是直接抛出一个消息事件，让线程池分派消息给监听器处理事件，这样就可以执行耗时操作，比如将日记存储到数据库进行持久化，也可以使用redis存储，便于后期进行项目代码异常排查。

### bee-mite做了什么

我在bee-mite模块的test包下写了两个测试类，其中UserServiceImpl就是插桩的目标，运行TestAop的main方法，会在项目的targer/classes目录下生成一个叫TargerProxy.class的文件，这个就是对UserServiceImpl插桩后的字节码文件。来看下对比，到底bee-mite都帮我们做了什么。

#### 【源代码】
```java
package com.wujiuye.beemite.test;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl {

    public Map<String, Object> queryMap(String username,Integer age) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("age", age);
//        int n = 1/0;
        return map;
    }

}
```
#### 【bee-mite插桩后】
```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wujiuye.beemite.test;

import com.wujiuye.beemite.event.BusinessCallLinkEvent;
import com.wujiuye.beemite.event.FuncRuntimeEvent;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl {
    private BusinessCallLinkEvent eventObject1;
    private FuncRuntimeEvent eventObject2;

    public UserServiceImpl() {
    }

    public Map<String, Object> queryMap(String var1, Integer var2) {
        if (this.eventObject2 == null) {
            this.eventObject2 = new FuncRuntimeEvent();
        }

        long var3 = System.currentTimeMillis();
        this.eventObject2.sendFuncStartRuntimeEvent("sessionid is null", "com/wujiuye/beemite/test/UserServiceImpl", "queryMap", var3);
        if (this.eventObject1 == null) {
            this.eventObject1 = new BusinessCallLinkEvent();
        }

        try {
            Object[] var8 = new Object[]{var1, var2};
            this.eventObject1.sendBusinessFuncCallEvent("sessionid is null", "com/wujiuye/beemite/test/UserServiceImpl", "queryMap", var8);
            HashMap var9 = new HashMap();
            var9.put("username", var1);
            var9.put("age", var2);
            long var5 = System.currentTimeMillis();
            this.eventObject2.sendFuncEndRuntimeEvent("sessionid is null", "com/wujiuye/beemite/test/UserServiceImpl", "queryMap", var5);
            return var9;
        } catch (Exception var7) {
            this.eventObject1.sendBusinessFuncCallThrowableEvent("sessionid is null", "com/wujiuye/beemite/test/UserServiceImpl", "queryMap", var7);
            throw var7;
        }
    }
}
```
#### 说明

因为使用了责任链模式，会对代码进行两次插桩，目的就是为了后面容易扩展功能，相信看了对比你也能知道bee-mite都帮我们插入了哪些代码，这些代码都是通过asm写字节码指令插入的。当然也不是很难，要说难就是try-catch代码块的插入了，没有文档看还是好难摸索出来的，visitTryCatchBlock方法的三个label的位置，以及catch块处理异常算是个难点，我最终通过在源码类中添加try-catch块然后javap查看字节码发现异常处理表

```
 * Exception table:
 *        from    to  target type
 *            0    27    30   Class java/lang/Exception
```
那么三个label对应的就是from、to、target了。当type为any的时候就只try-finally了。


### 使用方法

   - 先执行maven package进行打包，获取jar包的绝对路径
   - 在测试的web项目下点击锤子->Edit Config....-> VM options ->输入下面内容
   ``` 
   -javaagent:/MyProjects/asm-aop/insert-pile/target/insert-pile-1.0-SNAPSHOT.jar=com.wujiuye 
   ```
   等号后面是参数
  - 如果报如下异常：
       ```
       java.lang.VerifyError: Expecting a stackmap frame at branch target 18
       ```
 jdk1.8可以添加参数：-noverify   最终：
       ``` 
       -noverify -javaagent:/MyProjects/asm-aop/insert-pile/target/insert-pile-1.0-SNAPSHOT.jar=com.wujiuye
       ```
 
   - spring项目中也能用，虽然spring会使用它生成的代理对象，但是最终也会调用原本的对象的方法

### 使用注意和当前存在的缺陷

#### 使用注意

- 由于打jar包不会将依赖的asm的jar包也添加进去，所以在使用的项目中需要添加asm的依赖，也可以修改本项目的amven配置，将asm的jar包打包进去。
- 我为了过滤掉get和set方法，所以方法名以get或者set方法开头的都不会进行插桩
- 进行桩的方法的参数不能含有基本数据类型，如果需要请以Integer等类型
- 为了获取到sessionid，将事件串起来，所以此项目只适合用在web项目

#### 存在的缺陷：

- 因为要获取方法参数，而如果方法参数中有基本数据类型的参数，那么就会异常，所以方法参数只能是Object类型，即引用类型
- 需要同时添加-noverify参数，否则修改后的字节码没问题，但是会验证失败


>微信公众号：Java艺术
 邮箱：419611821@qq.com
 作者：wujiuye


