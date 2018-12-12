package com.wujiuye.beemite.ipevent;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {

    private String sessionId;
    private int eventType;//事件类型
    private int type;//对于调用链事件，这是前置通知和异常通知，对于方法运行事件统计，这是前置通知和后置通知
    private EventParam eventParam;
}
